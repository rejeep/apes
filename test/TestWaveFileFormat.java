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
      long t = System.currentTimeMillis();
      System.out.println("Importing file...");      
      internal = wav.importFile( ".", "test.wav" );
      System.out.println("Time to import test.wav: " + (System.currentTimeMillis()-t)/1000.0 + " s.");
      System.out.println("File imported.");
      System.out.println("Exporting file....");
      t = System.currentTimeMillis();
      wav.exportFile(internal, ".", "test2.wav" );
      System.out.println("Time to export test2.wav: " + (System.currentTimeMillis()-t)/1000.0 + " s.");
      System.out.println("File exported.");
    } catch ( Exception e )
    {
      e.printStackTrace();
    }
  }
}
