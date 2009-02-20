package test;

import org.junit.Test;
import apes.plugins.WaveFileFormat;
import apes.interfaces.AudioFormatPlugin;
import apes.models.InternalFormat;

public class TestWaveFileFormat
{

  @Test
  public void testLoadWave()
  {
    AudioFormatPlugin wav = new WaveFileFormat();
    InternalFormat internal;
    try
    {
      internal = wav.importFile( ".", "test.wav" );
    } catch ( Exception e )
    {
      e.printStackTrace();
    }
  }
}
