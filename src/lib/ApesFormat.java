package apes.lib;

import java.io.File;
import apes.interfaces.AudioFormatPlugin;
import apes.plugins.WaveFileFormat;
import apes.exceptions.UnidentifiedLanguageException;

/**
 * This class is to get information about what format a file is in.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ApesFormat
{
  /**
   * The file.
   */
  private File file;

  /**
   * Creates a new <code>ApesFormat</code> instance.
   * 
   * @param file The file.
   */
  public ApesFormat(File file)
  {
    this.file = file;
  }

  /**
   * Returns correct <code>AudioFormatPlugin</code> from file.
   * 
   * @return A new <code>AudioFormatPlugin</code> for file.
   * @exception UnidentifiedLanguageException If the format is not
   *              supported.
   */
  public AudioFormatPlugin getAudioFile() throws UnidentifiedLanguageException
  {
    if(isWave())
    {
      return new WaveFileFormat();
    }

    throw new UnidentifiedLanguageException();
  }

  /**
   * Returns true if file is a wave file. False otherwise.
   * 
   * @return True if wave. False otherwise.
   */
  public boolean isWave()
  {
    String name = file.getName().toLowerCase();

    return name.matches(".*wave?$");
  }
  
  /**
   * Returns true if file is an apes file. False otherwise.
   * 
   * @return True if apes. False otherwise.
   */
  public boolean isApes()
  {
    String name = file.getName().toLowerCase();

    return name.matches(".*ape?$");
  }
}
