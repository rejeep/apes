package apes.models;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * This is not a class.
 * @author Daniel Kvick (kvick@student.chalmers.se)
 */
public class MemoryHandler
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
    for( int i = 0; i < FRAME_NUM; i++ )
      frameTable[i] = new Frame();
  }
  
  public boolean free(long index, long bytes) throws IOException
  {
    if( index < 0 || index + bytes > usedMemory || bytes <= 0 )
      return false;
    
    int frameF = find( index );
    int frameL = find( index + bytes - 1 );
    
    // Find new size
    Frame fstFrame = (frameF  == -1 ? swap(index)             : frameTable[frameF]);
    Frame lstFrame = (frameL  == -1 ? swap(index + bytes - 1)  : frameTable[frameL]);

    long firstIndex = fstFrame.page.index;
    int fstSize = (int)(index - firstIndex);
    int lstSize = (int)((lstFrame.data.length + lstFrame.page.index) - (index + bytes));

    int size = fstSize + lstSize;
    
    Page[] newPages = null;
    int nPages = 0;
    if( size != 0 )
    { 
      // Create pages
      nPages = size > PAGE_SIZE ? 2 : 1; 
      newPages = new Page[nPages];
      for(int i = 0; i < newPages.length; ++i)
        newPages[i] = new Page(size / nPages);
       
      // Copy first data
      newPages[0].file.write(fstFrame.data, 0, fstSize );
      
      // Copy last data
      newPages[nPages - 1].file.write(lstFrame.data, (int)(index+bytes - lstFrame.page.index), lstSize);
    }  
    // Destroy pages
    System.out.println(pageTable.size());
    for( int i = 0; i < pageTable.size(); i++ )
    {
      Page p = pageTable.get(i);
      if( p.index >= firstIndex && p.index < index + bytes)
      {
        destroyPage(p);
        i--;
      }
    }
    
    for(Page p : pageTable)
      if(p.index > index + bytes)
        p.index -= bytes;
    
    // Add pages, set indices
    if(size != 0)
    {
      pageTable.add( newPages[0] );
      newPages[0].index = firstIndex;
      if( nPages == 2 )
      {
        pageTable.add( newPages[1] );
        newPages[1].index = firstIndex + fstSize;
      }
    }
    
    usedMemory -= bytes;
    return true;
  }
  
  /**
   * Allocates new memory and effectively inserts it at the given index. Inserted data is to be considered uninitialized.
   * @param index Position in memory where to add new memory.
   * @param integers Amount of data to allocate, in INTEGER_BYTES.
   * @return Returns true if allocation succeeded, otherwise false.
   */
  // TODO: Fix small files.
  public boolean malloc(long index, long bytes) throws IOException
  {
    if( index < 0 || index > usedMemory || bytes <= 0 )
      return false;
    
    int frameI = find( index );
    // Not in memory
    if( frameI == -1 )
    {
      // Doesn't exist
      if( swap(index) == null )
      {
        Page[] pages = createPages(index, bytes);
        for( Page p : pages )
          pageTable.add( p );
        usedMemory += bytes;
        return true;
      }
    }
    
    
    Frame frame = frameTable[frameI];
    long firstIndex = index - frame.page.index;
    long frontSize = index - firstIndex;
    long backSize = frame.page.file.length() - frontSize;
    long newSizeB = frame.page.file.length() + bytes;
    long newSizeI = newSizeB;
    
    // Create pages
    Page[] pages = createPages( firstIndex, newSizeI );
    
    // Copy front data
    Page front0 = pages[0], front1 = null;
    long front0CopySize = frontSize > PAGE_SIZE ? PAGE_SIZE : frontSize;
    if( frontSize > PAGE_SIZE )
      front1 = pages[1]; 
    front0.file.write(frame.data, 0, (int)front0CopySize);
    if( front1 != null )
    {
      front1.file.write(frame.data, (int)front0CopySize, (int)(frontSize - PAGE_SIZE));
    }
    
    // Copy back data
    Page back0 = pages[pages.length-1], back1 = null;
    long back0CopySize = backSize > PAGE_SIZE ? PAGE_SIZE : backSize;
    if( backSize > PAGE_SIZE )
      back1 = pages[pages.length-2]; 
    back0.file.write(frame.data, (int)(PAGE_SIZE - back0CopySize), (int)back0CopySize);
    if( back1 != null )
    {
      back1.file.write(frame.data, (int)(PAGE_SIZE - (backSize - back0CopySize)), (int)(backSize - back0CopySize));
    }
    
    // Remove old page from disk
    destroyPage( frame.page );
    
    // Put front0 in frame
    frame.load(front0);
    frame.timeStamp = System.currentTimeMillis();
    
    // Update indexes
    for( Page p : pageTable )
    {
      if( p.index > firstIndex )
        p.index += bytes;
    }
    
    // Add new pages
    for( Page p : pages )
      pageTable.add( p );
        
    usedMemory += bytes;
    
    return true;
  }
  
  private Page[] createPages( long index, long amount) throws IOException
  {
    if( amount < 1 || index < 0 )
      return null;
    
    if( amount <= PAGE_SIZE)
    {
      Page[] page = new Page[1];
      page[0] = new Page(amount);
      page[0].index = index;
      return page;
    }
      
    int numPages = (int)(amount / PAGE_SIZE);
    Page[] pages = new Page[numPages];
    for( int i = 0; i < pages.length - 1; i++ )
    {
      pages[i] = new Page(PAGE_SIZE);
      pages[i].index = index + i*PAGE_SIZE;
    }
    pages[pages.length - 1] = new Page( PAGE_SIZE + amount % PAGE_SIZE );
    pages[pages.length - 1].index = index + (pages.length-1)*PAGE_SIZE;
    return pages;
  }
  
  /**
   * Removes a page from the system.
   * @param page Page to remove.
   * @return The frame which contained the specified page. If no such frame exists, returns null.
   * @throws IOException 
   */
  private Frame destroyPage( Page page ) throws IOException
  {
    page.file.close();
    page.tempFile.delete();
    pageTable.remove(page);
    for( Frame f : frameTable )
    {
      if(f.page == page)
      {
        f.page = null;
        f.timeStamp = Long.MAX_VALUE;
        return f;
      }
    }
    return null;
  }

  // TODO: Well.. Yeah, implement
  public byte[] read( long index, int amount ) throws IOException
  {
    Frame frame;
    int frameI;
    byte[] buf = new byte[amount];
    while( amount > 0 )
    {     
      frameI = find(index);
      frame = (frameI  == -1 ? swap(index) : frameTable[frameI]);
      
      int offset = (int)(index - frame.page.index);
      int length = frame.data.length - offset;
      System.arraycopy(frame.data, offset, buf, 0, length);
      
      index  += length;
      amount -= length;
    }
    
    return buf;
  }
  
  public void write( long index, byte[] data ) throws IOException
  { 
    Frame frame;
    int frameI;
    int offset = 0;
    while( offset < data.length )
    {
      frameI = find( index );
      frame = (frameI  == -1 ? swap( index ) : frameTable[frameI]);
      
      int length = Math.min(frame.data.length, data.length - offset);
      System.arraycopy(data, offset, frame.data, 0, length);
      index  += length;
      offset += length;
    } 
  } 
  
  private Frame swap( long index ) throws IOException
  {
    long timeStamp = Long.MAX_VALUE;
    Frame evicted = null; 
    for( Frame frame : frameTable )
    {
      if( frame.page == null )
      {
        evicted = frame;
        timeStamp = frame.timeStamp;
        break;
      }
      
      if( frame.timeStamp < timeStamp  )
      {
        evicted = frame;
        timeStamp = frame.timeStamp;
      }
    }
    
    for(Page page : pageTable)
    {
      if( page.index <= index && page.index + page.file.length() > index)
      {
        evicted.writeAll();
        evicted.load( page );
        evicted.timeStamp = System.currentTimeMillis();
        return evicted;
      }
    }
    return null;
  }
  
  private int find( long index ) throws IOException
  {
    for( int i = 0; i < frameTable.length; i++ )
    {
      Frame f = frameTable[i];
      if( f.page != null && (f.page.index <= index) && 
          (f.page.index + f.page.file.length() > index) )
        return i;
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
        page.write( data );
    }

    public void load( Page page ) throws IOException
    {
      this.page = page;
      data = new byte[(int)page.file.length()];
      page.read( data );
    }
    
  }
  private static short count = 0;
  
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
      file.setLength( l * Integer.SIZE / 8  );
      setIndex(pageTable.size());
    }
    
    public void read( byte[] data ) throws IOException
    {
      file.seek( 0 );
      file.read( data );
    }

    public void write( byte[] data ) throws IOException
    {
      file.seek(0);
      file.write(data);
    }

    public void setIndex( long index )
    {
      this.index = index;
    }

    public long getIndex()
    {
      return index;
    }
  }
}
