package apes.lib;

import java.util.ArrayList;
import java.io.File;
import java.io.FilenameFilter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.MalformedURLException;
import java.lang.ClassLoader;
import java.lang.InstantiationException;

import apes.interfaces.AudioFormatPlugin;
import apes.interfaces.TransformPlugin;

/**
 * Class for loading plugins.
 */
public class PluginHandler extends ClassLoader
{
  /**
   * ArrayList for TransformPlugins.
   */
  private ArrayList<TransformPlugin> transforms;

  /**
   * ArrayList for AudioFormatPlugins.
   */
  private ArrayList<AudioFormatPlugin> formats;

  /**
   * Constructor.
   */
  public PluginHandler ()
  {
    transforms = new ArrayList<TransformPlugin>();
    formats = new ArrayList<AudioFormatPlugin>();
  }

  /**
   * Returns an ArrayList of all the transform classes.
   *
   * @return ArrayList containing TransformPlugins.
   */
  public ArrayList<TransformPlugin> getTransforms()
  {
    return transforms;
  }

  /**
   * Returns an ArrayList of all the format classes.
   *
   * @return ArrayList containing AudioFormatPlugins.
   */
  public ArrayList<AudioFormatPlugin> getFormats()
  {
    return formats;
  }

  /**
   * Given a path pointing to a directory or a file, will try to add
   * all plugins in the directory or the plugin the path was pointing
   * to.
   *
   * @param str A path pointing to either a file or a directory.
   */
  public void addPlugin( String str )
  {
    File file = new File( str );

    if(file.isDirectory())
    {
      loadPluginsInPath( file.getPath() );
    }
    else if( file.isFile() )
    {
      loadFile( file.getPath(), file.getName().substring( 0, file.getName().indexOf( "." ) ) );
    }
  }

  /**
   * TODO: Comment and implement
   */
  public void removePlugin( String name )
  {

  }

  /**
   * Tries to load all plugins in a directory.
   *
   * @param path Directory.
   */
  private void loadPluginsInPath( String path )
  {
    File dir = new File( path );
    String[] files = dir.list();

    if( files.length == 0 )
    {
      // -
    }
    else
    {
      FilenameFilter filter = new FilenameFilter()
      {
        public boolean accept( File d, String n )
        {
          return !n.startsWith( "." );
        }
      };

      files = dir.list( filter );
      for (String f : files)
      {
        loadFile( path + "/" + f, f.substring( 0, f.indexOf( "." ) ) );
      }
    }
  }

  /**
   * Loads a single plugin, given a path and a name.
   *
   * @param path Directory.
   * @param name Name of class to load.
   */
  private void loadFile( String path, String name )
  {
    try
    {
      if( path.endsWith( ".class" ) )
      {
        loadClass( path, "apes.plugins." + name );
      }
      else if( path.endsWith( ".jar" ) )
      {
        loadJAR( path, "apes.plugins." + name );
      }
    }
    catch( ClassNotFoundException e )
    {
      // -
    }
  }

  /**
   * Loads a class file.
   *
   * @param location Directory.
   * @param name Name of class to load.
   * @throws ClassNotFoundException
   */
  private void loadClass( String location, String name ) throws
    ClassNotFoundException
  {
    int BUFFER_SIZE = 4096;
    byte[] classBytes = null;

    // read the class file
    try {

      FileInputStream in = new FileInputStream( location );
      ByteArrayOutputStream buf = new ByteArrayOutputStream();
      int c;
      while ( ( c = in.read() ) != -1 )
      {
        buf.write( c );
      }
      classBytes = buf.toByteArray();

    }
    catch ( IOException e )
    {
      System.out.println( "IOException\n" );
    }

    if( classBytes == null )
    {
      throw new ClassNotFoundException( "Cannot load class" );
    }

    // turn it to a class
    try
    {
      Class<?> cls = defineClass( name, classBytes, 0, classBytes.length );
      resolveClass( cls );
      instancePlugin( cls );
    }
    catch ( ClassFormatError e )
    {
      System.out.println( "ClassFormatError\n" );
    }
  }

  /**
   * Loads a JAR file.
   *
   * @param location Directory.
   * @param name Name of JAR to load.
   * @throws ClassNotFoundException
   */
  private void loadJAR( String location, String name ) throws
    ClassNotFoundException
  {
    try
    {
      File pluginFile = new File( location );
      URL[] locations = new URL[] { pluginFile.toURL() };
      URLClassLoader classloader =
        new URLClassLoader( locations );

      Class<?> cls = classloader.loadClass( name );
      instancePlugin( cls );
    }
    catch ( MalformedURLException e )
    {
      // -
    }
  }

  /**
   * Creates a new instance of the class and adds it to the list
   * of loaded plugins.
   *
   * @param cls Class object.
   */
  private void instancePlugin( Class<?> cls )
  {
    try
    {
      if( TransformPlugin.class.isAssignableFrom( cls ) )
      {
        transforms.add( (TransformPlugin)cls.newInstance() );
      }
      else if( AudioFormatPlugin.class.isAssignableFrom( cls ) )
      {
        formats.add( (AudioFormatPlugin)cls.newInstance() );
      }
    }
    catch( InstantiationException e )
    {
      // -
    }
    catch( IllegalAccessException e )
    {
      // -
    }
  }
}
