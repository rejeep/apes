package apes.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
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
   * @param fileName The name of the file to be loaded.
   * @return Returns a ByteBuffer with all the data from the file.
   * @throws IOException Throws a IOException if something unexpected happens.
   */
  public static ByteBuffer loadFile( String path, String fileName ) throws IOException
  {
    File file = new File( path + "/" + fileName );
    FileInputStream iStream = new FileInputStream( file );
    byte[] buffer = new byte[(int) file.length()];
    iStream.read( buffer );

    return ByteBuffer.wrap( buffer );
  }

  public static ByteBuffer loadFile( String fileName) throws IOException
  {
    return loadFile(".",fileName);
  }

  public static void saveToFile( String path, String fileName, byte[] data ) throws IOException
  {
    File file = new File( path + "/" + fileName );
    file.createNewFile();
    FileOutputStream oStream = new FileOutputStream( file );
    oStream.write(data);
  }

  public static void saveToFile( String fileName, byte[] data ) throws IOException
  {
    saveToFile(".", fileName, data);
  }
}
