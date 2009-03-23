package apes.lib;

import javax.swing.*;
import java.io.*;
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
   * @throws IOException Throws an IOException if something unexpected happens.
   */
  public static ByteBuffer loadFile( String path, String fileName ) throws IOException
  {
    File file = new File( path + "/" + fileName );
    FileInputStream iStream = new FileInputStream( file );
    byte[] buffer = new byte[(int) file.length()];
    iStream.read( buffer );

    return ByteBuffer.wrap( buffer );
  }

  /**
   * Loads a file at the root path from the disk and stores the data into a ByteBuffer
   *
   * @param fileName The name of the file to be loaded.
   * @return Returns a ByteBuffer with all the data from the file
   * @throws IOException Throws an IOException if something unexpected happens.
   */
  public static ByteBuffer loadFile( String fileName) throws IOException
  {
    return loadFile(".",fileName);
  }

  /**
   * Saves data to a file.
   * @param path The path to the folder were the file will be save.
   * @param fileName The name of the file to be saved.
   * @param data The data to be saved.
   * @throws IOException Throws an IOException if something unexpected happens.
   */
  public static void saveToFile( String path, String fileName, byte[] data ) throws IOException
  {
    File file = new File( path + "/" + fileName );
    file.createNewFile();
    FileOutputStream oStream = new FileOutputStream( file );
    oStream.write(data);
  }

  /**
   * Saves data to a file in the folder ".".
   * @param fileName The name of the file.
   * @param data The data to be saved.
   * @throws IOException Throws an IOException if something unexpected happens.
   */
  public static void saveToFile( String fileName, byte[] data ) throws IOException
  {
    saveToFile( ".", fileName, data );
  }

  /**
   * Saves an object to a file.
   * @param path The path to were the file will be saved.
   * @param fileName The name of the file.
   * @param data The object to be saved.
   * @throws IOException Throws an IOException if something unexpected happens.
   */
  public static void saveObjectToFile( String path, String fileName, Object data ) throws IOException
  {
    File file = new File( path + "/" + fileName );
    file.createNewFile();
    FileOutputStream oStream = new FileOutputStream( file );

    ObjectOutputStream obj_out = new ObjectOutputStream( oStream );
    obj_out.writeObject( data );
  }

  /**
   * Saves an object to a file in the folder ".".
   * @param fileName The name of the file.
   * @param data The object to be saved.
   * @throws IOException Throws an IOException if something unexpected happens.
   */
  public static void saveObjectToFile( String fileName, Object data ) throws IOException
  {
    saveObjectToFile( ".", fileName, data );
  }


  /**
   * Loads an object from a file.
   * @param path The path to the folder were to load the file.
   * @param fileName The name of the file to be loaded.
   * @return Returns the loaded object.
   * @throws IOException Throws an IOException if something unexpected happens.
   * @throws ClassNotFoundException Throws an ClassNotFoundException if something unexpected happens.
   */
  public static Object loadObjectFile( String path, String fileName ) throws IOException, ClassNotFoundException
  {
    File file = new File( path + "/" + fileName );
    FileInputStream iStream = new FileInputStream( file );

    ObjectInputStream obj_in = new ObjectInputStream (iStream);

    return obj_in.readObject();
  }

  /**
   * Loads an object form the folder ".".
   * @param fileName The name of the file to be loaded.
   * @return Returns the loaded object.
   * @throws IOException Throws an IOException if something unexpected happens.
   * @throws ClassNotFoundException Throws an ClassNotFoundException if something unexpected happens.
   */
  public static Object loadObjectFile( String fileName ) throws IOException, ClassNotFoundException
  {
    return loadObjectFile(".", fileName);
  }

  ///////////////////////////////////////////////////////

  /**
   * Loads a file from the disk and stores the data into a ByteBuffer with a dialogbox.
   *
   * @return Returns a ByteBuffer with all the data from the file.
   * @throws IOException Throws an IOException if something unexpected happens.
   */
  public static ByteBuffer loadFileGraphical() throws IOException
  {
    final JFileChooser fc = new JFileChooser();
    fc.showOpenDialog(new JPanel());
    File file = fc.getSelectedFile();
    FileInputStream iStream = new FileInputStream( file );
    byte[] buffer = new byte[(int) file.length()];
    iStream.read( buffer );

    return ByteBuffer.wrap( buffer );
  }

  /**
   * Saves data to a file with a dialogbox.
   * @param path The path to the folder were the file will be save.
   * @param fileName The name of the file to be saved.
   * @param data The data to be saved.
   * @throws IOException Throws an IOException if something unexpected happens.
   */
  public static void saveToFileGraphical( String path, String fileName, byte[] data ) throws IOException
  {
    File file = new File( path + "/" + fileName );
    file.createNewFile();
    FileOutputStream oStream = new FileOutputStream( file );
    oStream.write(data);
  }

  /**
   * Saves an object to a file with a dialogbox.
   * @param path The path to were the file will be saved.
   * @param fileName The name of the file.
   * @param data The object to be saved.
   * @throws IOException Throws an IOException if something unexpected happens.
   */
  public static void saveObjectToFileGraphical( String path, String fileName, Object data ) throws IOException
  {
    File file = new File( path + "/" + fileName );
    file.createNewFile();
    FileOutputStream oStream = new FileOutputStream( file );

    ObjectOutputStream obj_out = new ObjectOutputStream( oStream );
    obj_out.writeObject( data );
  }

  /**
   * Loads an object from a file with a dialogbox.
   * @param path The path to the folder were to load the file.
   * @param fileName The name of the file to be loaded.
   * @return Returns the loaded object.
   * @throws IOException Throws an IOException if something unexpected happens.
   * @throws ClassNotFoundException Throws an ClassNotFoundException if something unexpected happens.
   */
  public static Object loadObjectFileGraphical( String path, String fileName ) throws IOException, ClassNotFoundException
  {
    File file = new File( path + "/" + fileName );
    FileInputStream iStream = new FileInputStream( file );

    ObjectInputStream obj_in = new ObjectInputStream (iStream);

    return obj_in.readObject();
  }
}
