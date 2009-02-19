package test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import apes.lib.FileHandler;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Test class for FileHandler
 * @author Simon Holm
 */
public class TestFileHandler
{
  /**
   * Tests that the file that you write to the disk is identical when you read the same file.
   */
  @Test
  public void testWriteAndReadShouldBeTheSame()
  {
    byte[] data = {1,5};

    try
    {
      FileHandler.saveToFile("fileTest", data);
      ByteBuffer buffer = FileHandler.loadFile("fileTest");

      byte[] data2 = buffer.array();

      assertEquals( "Should be of the same length", data.length, data2.length );
      for(int i = 0; i < data.length; ++i)
      {
        assertEquals( "Data should not differ", data[i], data2[i]);
      }

    } catch ( IOException e )
    {
      e.printStackTrace();
    }

  }


}
