package apes.models;

import java.io.File;

// TODO: Add functionality to choose between save and save as...

/**
 * Contians information about the file.
 *
 * @author Simon Holm
 */
public class FileStatus
{
  /**
   * Contains the path of the current file.
   */
  private String filePath;

  /**
   * Contains the name of the current file;
   */
  private String fileName;


  /**
   * Creates a new <code>FileStatus</code> instance.
   */
  public FileStatus() {}

  /**
   * Creates a new <code>FileStatus</code> instance and sets
   * <code>filePath</code> and <code>fileName</code>.
   */
  public FileStatus( String filePath, String fileName )
  {
    setFilePath( filePath );
    setFileName( fileName );
  }

  /**
   * Getter for the file path.
   *
   * @return The file path.
   */
  public String getFilepath()
  {
    return filePath;
  }

  /**
   * Setter for the file path.
   *
   * @param filePath The new file path.
   */
  public void setFilePath( String filePath )
  {
    this.filePath = filePath;
  }

  /**
   * Getter for the file name
   *
   * @return The file name.
   */
  public String getFileName()
  {
    return fileName;
  }

  /**
   * Setter for the file name.
   *
   * @param fileName The file name.
   */
  public void setFileName( String fileName )
  {
    this.fileName = fileName;
  }

  /**
   * Returns the full path.
   *
   * @return The full path.
   */
  public String getFullPath()
  {
    return new File( this.filePath + File.separator + this.fileName ).getAbsolutePath();
  }
}
