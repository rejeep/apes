package test;

import org.junit.Test;
import apes.plugins.WaveFileFormat;
import apes.interfaces.AudioFormatPlugin;
import apes.models.InternalFormat;
import apes.lib.FileHandler;

import java.nio.ByteBuffer;

public class TestWaveFileFormat
{

  @Test
  public void testLoadWave()
  {
    AudioFormatPlugin wav = new WaveFileFormat();
    InternalFormat internal;
    try
    {
      System.out.println("Importing file...");      
      internal = wav.importFile( ".", "test.wav" );
      System.out.println("File imported.");
      System.out.println("Exporting file....");
      wav.exportFile(internal, ".", "test2.wav" );
      System.out.println("File exported.");
    } catch ( Exception e )
    {
      e.printStackTrace();
    }
  }
}
