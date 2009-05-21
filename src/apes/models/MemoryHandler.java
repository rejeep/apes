package apes.models;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This is not a class.
 * 
 * @author Daniel Kvick (kvick@student.chalmers.se)
 */
public class MemoryHandler implements Serializable
{
  private final int PAGE_SIZE = 100000; // Magic number.

  private final int FRAME_NUM = 100;

  private long usedMemory = 0;

  private Frame[] frameTable;

  private List<Page> pageTable;

  public MemoryHandler()
  {
    frameTable = new Frame[FRAME_NUM];
    pageTable = new LinkedList<Page>();
    for(int i = 0; i < FRAME_NUM; i++)
      frameTable[i] = new Frame();
  }

  public long getUsedMemory()
  {
    return usedMemory;
  }

  public boolean free(long index, long bytes) throws IOException
  {
    if(index < 0 || index + bytes > usedMemory || bytes <= 0)
      return false;

    int frameF = find(index);
    int frameL = find(index + bytes - 1);

    // Find new size
    Frame fstFrame = ( frameF == -1 ? swap(index) : frameTable[frameF] );
    Frame lstFrame = ( frameL == -1 ? swap(index + bytes - 1) : frameTable[frameL] );

    long firstIndex = fstFrame.page.index;
    int fstSize = (int) ( index - firstIndex );
    int lstSize = (int) ( ( lstFrame.data.length + lstFrame.page.index ) - ( index + bytes ) );

    int size = fstSize + lstSize;

    // Destroy pages
    List<Page> doomed = new ArrayList<Page>();
    for(Page p : pageTable)
      if(p.index >= firstIndex && p.index < index + bytes)
        doomed.add(p);

    for(Page p : doomed)
    {
      destroyPage(p);
    }

    doomed.clear();

    if(size != 0)
    {
      // Create pages
      createPages(firstIndex, size);
      byte[] data = new byte[size];
      // Copy arrays
      System.arraycopy(fstFrame.data, 0, data, 0, fstSize);
      System.arraycopy(lstFrame.data, lstFrame.data.length - lstSize, data, fstSize, lstSize);

      // Copy data
      write(firstIndex, data);
    }
    return true;
  }

  /**
   * Allocates new memory and effectively inserts it at the given
   * index. Inserted data is to be considered uninitialized.
   * 
   * @param index Position in memory where to add new memory.
   * @param bytes Amount of data to allocate, in INTEGER_BYTES.
   * @return Returns true if allocation succeeded, otherwise false.
   * @throws IOException IOException
   */
  // TODO: Fix small files.
  public boolean malloc(long index, long bytes) throws IOException
  {
    if(index < 0 || index > usedMemory || bytes <= 0)
      return false;

    int frameI = find(index);
    Frame frame;
    // Not in memory
    if(frameI == -1)
    {
      // Doesn't exist
      if( ( frame = swap(index) ) == null)
      {
        createPages(index, bytes);
        return true;
      }
    }
    else
      frame = frameTable[frameI];

    long firstIndex = frame.page.index;
    long index1 = index + bytes;

    if(frame.page.index == index)
    {
      createPages(index, bytes);
      return true;
    }
    // Destroy old page
    destroyPage(frame.page);

    // Create pages
    createPages(firstIndex, bytes + frame.data.length);

    byte[] fst = new byte[(int) ( index - firstIndex + 1 )];
    byte[] lst = new byte[frame.data.length - fst.length];

    System.arraycopy(frame.data, 0, fst, 0, fst.length);
    System.arraycopy(frame.data, fst.length, lst, 0, lst.length);

    write(firstIndex, fst);
    write(index1, lst);

    return true;
  }

  private Page[] createPages(long index, long amount) throws IOException
  {
    if(amount < 1 || index < 0)
      return null;

    // Update indexes of pages after insertion point.
    for(Page page : pageTable)
    {
      if(page.index >= index)
      {
        page.index += amount;
      }
    }

    if(amount <= PAGE_SIZE)
    {
      Page[] pages = new Page[1];

      Page page = new Page(amount);
      page.index = index;

      pages[0] = page;
      pageTable.add(page);

      usedMemory += amount;

      return pages;
    }

    // Create and add pages
    int numPages = (int) ( amount / PAGE_SIZE );
    Page[] pages = new Page[numPages];
    for(int i = 0; i < pages.length - 1; i++)
    {
      Page page = new Page(PAGE_SIZE);
      // Set index
      page.index = index + i * PAGE_SIZE;

      // Add pages
      pages[i] = page;
      pageTable.add(page);
    }

    Page page = new Page(PAGE_SIZE + ( amount % PAGE_SIZE ));
    page.index = index + ( pages.length - 1 ) * PAGE_SIZE;

    pageTable.add(page);
    pages[pages.length - 1] = page;

    usedMemory += amount;

    return pages;
  }

  /**
   * Removes a page from the system.
   * 
   * @param page Page to remove.
   * @return The frame which contained the specified page. If no such
   *         frame exists, returns null.
   * @throws IOException IOException
   */
  private Frame destroyPage(Page page) throws IOException
  {
    // Update indexes that comes after the one that was removed.
    for(Page pageX : pageTable)
    {
      if(pageX.index > page.index)
      {
        pageX.index -= page.file.length();
      }
    }

    for(Frame f : frameTable)
    {
      if(f.page == page)
      {
        usedMemory -= f.page.file.length();
        page.file.close();
        f.page = null;
        f.timeStamp = Long.MIN_VALUE;

        page.tempFile.delete();
        pageTable.remove(page);

        return f;
      }
    }

    return null;
  }

  public byte[] read(long index, int amount) throws IOException
  {
    Frame frame = null;
    int frameI;
    byte[] buf = new byte[amount];
    int prevEndPos = 0;

    while(amount > 0)
    {
      frameI = find(index);

      frame = ( frameI == -1 ? swap(index) : frameTable[frameI] );

      int offset = (int) ( index - frame.page.index );
      int length = frame.data.length - offset;

      if(length > amount)
        length = amount;
      System.arraycopy(frame.data, offset, buf, prevEndPos, length);

      index += length;
      prevEndPos += length;
      amount -= length;
    }
    frame.timeStamp = System.currentTimeMillis();
    return buf;
  }

  public void write(long index, byte[] data) throws IOException
  {

    if(index < 0 || index + data.length > usedMemory || data.length < 1)
      return;
    Frame frame = null;
    int frameI;
    int offset = 0;
    int targetPos;

    while(offset < data.length)
    {
      frameI = find(index);
      frame = ( frameI == -1 ? swap(index) : frameTable[frameI] );
      targetPos = (int) ( index - frame.page.index );

      int length = Math.min(frame.data.length - targetPos, data.length - offset);

      System.arraycopy(data, offset, frame.data, targetPos, length);

      index += length;
      offset += length;
    }
    frame.timeStamp = System.currentTimeMillis();
  }

  public void dispose()
  {
    try
    {
      free(0, usedMemory);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  /**
   * @param source
   * @param start
   * @param stop
   * @param putAt
   */
  public void transfer(MemoryHandler source, long start, long stop, long putAt)
  {
    if(start < 0 || start > stop || source.usedMemory < stop || putAt > usedMemory)
      return;

    // Allocate memory
    try
    {
      long amount = stop - start + 2;
      malloc(putAt, amount);

      // Copy memory
      for(long i = putAt, index = start; index <= stop; i += PAGE_SIZE, index += PAGE_SIZE)
      {
        int chunkSize = amount > PAGE_SIZE ? PAGE_SIZE : (int)amount;
        byte[] chunk = source.read(index, chunkSize);
        write(i, chunk);
        amount -= chunkSize;
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  private Frame swap(long index) throws IOException
  {
    long timeStamp = Long.MAX_VALUE;
    Frame evicted = null;
    for(Frame frame : frameTable)
    {
      if(frame.page == null)
      {
        evicted = frame;
        timeStamp = frame.timeStamp;
        break;
      }

      if(frame.timeStamp < timeStamp)
      {
        evicted = frame;
        timeStamp = frame.timeStamp;
      }
    }
    for(Page page : pageTable)
    {
      if(page.index <= index && ( page.index + page.file.length() ) > index)
      {
        evicted.writeAll();
        evicted.load(page);
        evicted.timeStamp = System.currentTimeMillis();
        return evicted;
      }
    }

    return null;
  }

  private int find(long index) throws IOException
  {
    for(int i = 0; i < frameTable.length; i++)
    {
      Frame f = frameTable[i];
      if(f.page != null && ( f.page.index <= index ) && ( f.page.index + f.page.file.length() > index ))
      {
        return i;
      }
    }

    return -1;
  }

  private class Frame
  {
    private Page page;

    byte[] data;

    private long timeStamp;

    private void writeAll() throws IOException
    {
      if(page != null)
        page.write(data);
    }

    public void load(Page page) throws IOException
    {
      this.page = page;
      data = new byte[(int)page.file.length()];
      page.read(data);
    }

  }

  private class Page
  {
    private RandomAccessFile file;

    private File tempFile;

    private long index;

    public Page(long l) throws IOException
    {
      tempFile = File.createTempFile("apes", "page");
      tempFile.deleteOnExit();
      file = new RandomAccessFile(tempFile, "rw");
      file.setLength(l);
      setIndex(pageTable.size());
    }

    public void read(byte[] data) throws IOException
    {
      file.seek(0);
      file.read(data);
    }

    public void write(byte[] data) throws IOException
    {
      file.seek(0);
      file.write(data);
    }

    public void setIndex(long index)
    {
      this.index = index;
    }

    public long getIndex()
    {
      return index;
    }
  }
}
