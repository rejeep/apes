package test;

import apes.models.InternalFormat;
import apes.models.MemoryHandler;
import apes.plugins.WaveFileFormat;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestMemoryHandler
{
  private MemoryHandler mH1;
  private MemoryHandler mH2;
  
  @Before public void setup()
  {
    mH1 = new MemoryHandler();
    mH2 = new MemoryHandler();
  }

  @Test public void testTransfer()
  {
    try
    {
      int mhLength = 10000000;
      int start    =  2500005;
      int stop     =  7500005;
      int amount = stop - start + 1;
      
      mH1.malloc(0, mhLength);
      assertEquals(mhLength, mH1.getUsedMemory());

      byte[] indexes = new byte[mhLength];
      for(int i = 0; i < indexes.length; i++)
      {
        indexes[i] = (byte)(i % 256);
      }

      mH1.write(0, indexes);
      assertEquals(mhLength, mH1.getUsedMemory());
      assertArrayEquals(indexes, mH1.read(0, mhLength));
      
      mH2.transfer(mH1, start, stop, 0);
      assertEquals(amount, mH2.getUsedMemory());
      assertEquals(mhLength, mH1.getUsedMemory());
      assertArrayEquals(mH1.read(start, amount), mH2.read(0, (int)mH2.getUsedMemory()));


      // Något ganska annat...
      int size = 103000;
      int pos = 107000;
      byte[] zeros = new byte[size];
      
      mH1.dispose();
      assertEquals(0, mH1.getUsedMemory());
      
      mH1.malloc(0, size);
      assertEquals(size, mH1.getUsedMemory());
      
      mH1.write(0, zeros);
      assertEquals(size, mH1.getUsedMemory());
      assertArrayEquals(zeros, mH1.read(0, size));
      
      mH2.transfer(mH1, 0, size - 1, pos);
      assertEquals(amount + size, mH2.getUsedMemory());

      int i = 0;
      byte[] temp;

      temp = mH2.read(0, pos);
      for(; i < pos; i++)
      {
        System.out.println(i);
        assertEquals(indexes[i + start], temp[i]);
      }
      
      temp = mH2.read(pos, size);
      for(int c = 0; i < pos + size; i++, c++)
      {
        assertEquals(0, temp[c]);
      }
      
      temp = mH2.read(pos + size, amount - ( pos + size ));
      for(int c = 0; i < amount; i++, c++)
      {
        assertEquals(indexes[i + start], temp[c]);
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  @Test public void testCutAndPaste()
  {
    try
    {
      InternalFormat iF = new WaveFileFormat().importFile( "/home/rejeep/dev/apes/", "audio_simon.wav" );
      
      long start =  10000;
      long stop  = 100000;
      int amount = iF.getSampleAmount();
      byte[] source = null;
      int length = (int)( amount - ( stop - start + 1 ) );
      
      // Before cut and paste
      source = iF.getChunk( 0, amount );
      byte[] before = new byte[source.length];
      System.arraycopy( source, 0, before, 0, source.length );
      assertArrayEquals("Before cut and paste", source, before);
      
      MemoryHandler mH = new MemoryHandler();
      iF.cutSamples(start, stop, mH);
      
      // After cut
      source = iF.getChunk( 0, length );
      byte[] afterCut = new byte[source.length];
      System.arraycopy( source, 0, afterCut, 0, source.length );
      assertArrayEquals("After cut", source, afterCut);
      
      iF.pasteSamples(start, mH);
      
      // After paste
      source = iF.getChunk( 0, length );
      byte[] after = new byte[source.length];
      System.arraycopy( source, 0, after, 0, source.length );
      assertArrayEquals("'Source' after paste", source, after);
      assertArrayEquals("'Before' after paste", before, after);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
}