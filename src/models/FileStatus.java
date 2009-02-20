package apes.models;

/**
 * Contians information about the file.
 * @author Simon Holm 
 */
public class FileStatus
{
  /**
   * Contains the path of the current file.
   */
  private String filepath;
  
  /**
   * Contains the name of the current file;
   */
  private String fileName;

  /**
   * Getter for the file path.
   * @return The file path.
   */
  public String getFilepath()
  {
    return filepath;
  }

  /**
   * Setter for the file path.
   * @param filepath The new file path.
   */
  public void setFilepath( String filepath )
  {
    this.filepath = filepath;
  }

  /**
   * Getter for the file name
   * @return The file name.
   */
  public String getFileName()
  {
    return fileName;
  }

  /**
   * Setter for the file name.
   * @param fileName The file name.
   */
  public void setFileName( String fileName )
  {
    this.fileName = fileName;
  }
}
