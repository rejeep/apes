package apes.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * A helper class to load and save files from and to the disk
 *
 * @author Simon Holm
 */
public class FileHandler
{

  /**
   * Loads a file from the disk and stores the data into a ByteBuffer
   *
   * @param path     The path to the folder with the file to be loaded.
   * @param filename The name of the file to be loaded.
   * @return Returns a ByteBuffer with all the data from the file.
   * @throws IOException Throws a IOException if something unexpected happens.
   */
  public static ByteBuffer loadFile( String path, String filename ) throws IOException
  {
    File file = new File( path + "/" + filename );
    FileInputStream iStream = new FileInputStream( file );
    byte[] buffer = new byte[(int) file.length()];
    iStream.read( buffer );

    return ByteBuffer.wrap( buffer );
  }
}
