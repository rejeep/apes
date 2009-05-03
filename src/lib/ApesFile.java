package apes.lib;

import java.io.File;
import apes.interfaces.AudioFormatPlugin;
import apes.models.InternalFormat;

/**
 * Create a new object of this class with a file on the system. Then
 * you can get all kinds of information and other objects.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ApesFile
{
  /**
   * The file.
   */
  private File file;
  
  
  /**
   * Creates a new <code>ApesFile</code> instance.
   *
   * @param fileName The filename of the file.
   */
  public ApesFile( String fileName )
  {
    this( new File( "./" + fileName ) );
  }
  
  /**
   * Creates a new <code>ApesFile</code> instance.
   *
   * @param file The file.
   */
  public ApesFile( File file )
  {
    this.file = file;
  }

  /**
   * Returns the name of the file.
   *
   * @return The file name.
   */
  public String getName()
  {
    return file.getName();
  }
  
  /**
   * Creates new internal format from the file.
   *
   * @return A new internal format.
   * @exception Exception Is something went wrong in creating the
   * internal format.
   */
  public InternalFormat getInternalFormat() throws Exception
  {
    AudioFormatPlugin audioFile = new ApesFormat( file ).getAudioFile();
    
    return audioFile.importFile( file.getParent(), getName() );
  }
}