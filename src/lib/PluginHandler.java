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
	 * 
	 */
	private ArrayList<PluginInfo> plugins;

  /**
   * Constructor.
   */
  public PluginHandler ()
  {
		plugins = new ArrayList<PluginInfo>();
		addPluginsInPath("/home/jfa/apes/build/apes/plugins");
  }
	
	public ArrayList<String> getPluginNames()
	{
		ArrayList list = new ArrayList<String>();
		
		for(PluginInfo p : plugins)
		{
			list.add(p.getName());
		}
		
		return list;
	}
	
	public String getDescription(String name)
	{
		for(PluginInfo p : plugins)
		{
			if(p.getName().equals(name))
			{
				return p.getDescription();
			}
		}
		
		return null;
	}
	
	public Boolean isLoaded(String name)
	{
		for(PluginInfo p : plugins)
		{
			if(p.getName().equals(name))
			{
				return p.isLoaded();
			}
		}
		
		return false;
	}

  /**
   * Returns an ArrayList of all the transform classes.
   *
   * @return ArrayList containing TransformPlugins.
   */
  public ArrayList<TransformPlugin> getTransforms()
  {		
		ArrayList list = new ArrayList<TransformPlugin>();
		
		for(PluginInfo p : plugins)
		{
			if(p.getType().equals("transform"))
			{
				list.add(p.getTransformObject());
			}
		}
		
		return list;
  }

  /**
   * Returns an ArrayList of all the format classes.
   *
   * @return ArrayList containing AudioFormatPlugins.
   */
  public ArrayList<AudioFormatPlugin> getFormats()
  {		
		ArrayList list = new ArrayList<AudioFormatPlugin>();
		
		for(PluginInfo p : plugins)
		{
			if(p.getType().equals("format"))
			{
				list.add(p.getAudioFormatObject());
			}
		}
		
		return list;
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
      addPluginsInPath( file.getPath() );
    }
    else if( file.isFile() )
    {
			PluginInfo pi = new PluginInfo();
      loadFile( file.getPath(), file.getName().substring( 0, file.getName().indexOf( "." ) ),
				pi );
    }
  }
	
	public TransformPlugin getTransform(String name)
	{
		for(int i=0; i<plugins.size(); i++)
		{
			if(plugins.get(i).getName().equals(name) &&
				plugins.get(i).getType().equals("transform"))
				{
					return plugins.get(i).getTransformObject();
				}
		}
		
		return null;
	}
	
	public AudioFormatPlugin getAudioFormat(String name)
	{
		for(int i=0; i<plugins.size(); i++)
		{
			if(plugins.get(i).getName().equals(name) &&
				plugins.get(i).getType().equals("format"))
				{
					return plugins.get(i).getAudioFormatObject();
				}
		}
		
		return null;
	}

  /**
   * TODO: Comment and implement
   */
  public void unloadPlugin( String name )
  {
		for(int i=0; i<plugins.size(); i++)
		{
			if(plugins.get(i).getName().equals(name))
			{
				if(plugins.get(i).isLoaded())
				{
					plugins.get(i).unLoad();
					return;
				}
			}
		}
  }
	
	public void loadPlugin( String name )
	{
		for(PluginInfo p : plugins)
		{
			if(p.getName().equals(name))
			{
				loadFile(p.getPath(), name, p);
			}
		}
	}

  /**
   * Tries to load all plugins in a directory.
   *
   * @param path Directory.
   */
  private void addPluginsInPath( String path )
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
        loadFile( path + "/" + f, f.substring( 0, f.indexOf( "." ) ), 
					new PluginInfo() );
      }
    }
  }

  /**
   * Loads a single plugin, given a path and a name.
   *
   * @param path Directory.
   * @param name Name of class to load.
   */
  private void loadFile( String path, String name, PluginInfo pi )
  {
    try
    {
			if(!plugins.contains(pi))
			{
				pi.setPath(path);
			}
			
      if( path.endsWith( ".class" ) )
      {
        loadClass( path, "apes.plugins." + name, pi );
      }
      else if( path.endsWith( ".jar" ) )
      {
        loadJAR( path, "apes.plugins." + name, pi );
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
  private void loadClass( String location, String name, PluginInfo pi ) throws
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
      instancePlugin( cls, pi );
    }
    catch ( ClassFormatError e )
    {
      System.out.println( "ClassFormatError\n" );
    }
  }

  /**
   * Loads a JAR file. Untested :-)
   *
   * @param location Directory.
   * @param name Name of JAR to load.
   * @throws ClassNotFoundException
   */
  private void loadJAR( String location, String name, PluginInfo pi ) throws
    ClassNotFoundException
  {
    try
    {
      File pluginFile = new File( location );
      URL[] locations = new URL[] { pluginFile.toURL() };
      URLClassLoader classloader =
        new URLClassLoader( locations );

      Class<?> cls = classloader.loadClass( name );
      instancePlugin( cls, pi );
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
  private void instancePlugin( Class<?> cls, PluginInfo pi )
  {
    try
    {
      if( TransformPlugin.class.isAssignableFrom( cls ) )
      {
        //transforms.add( (TransformPlugin)cls.newInstance() );
				TransformPlugin tp = (TransformPlugin)cls.newInstance();
				pi.setTransformObject(tp);
				
				if(!plugins.contains(pi))
				{
					pi.setName(tp.getName());
					pi.setDescription(tp.getDescription());
					pi.setType("transform");
					pi.setTransformObject(tp);
					plugins.add(pi);
				}
      }
      else if( AudioFormatPlugin.class.isAssignableFrom( cls ) )
      {
        //formats.add( (AudioFormatPlugin)cls.newInstance() );
				AudioFormatPlugin afp = (AudioFormatPlugin)cls.newInstance();
				pi.setAudioFormatObject(afp);
				
				if(!plugins.contains(pi))
				{
					pi.setName(afp.getName());
					pi.setDescription(afp.getDescription());
					pi.setType("format");
					pi.setAudioFormatObject(afp);
					plugins.add(pi);
				}
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

class PluginInfo
{
	private String path;
	private String name;
	private String desc;
	private String type;
	private Boolean loaded;
	private AudioFormatPlugin fObject;
	private TransformPlugin tObject;
	
	public String getPath()
	{
		return path;
	}
	
	public void setPath(String str)
	{
		path = str;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String str)
	{
		name = str;
	}
	
	public String getDescription()
	{
		return desc;
	}
	
	public void setDescription(String str)
	{
		desc = str;
	}
	
	public String getType()
	{
		return type;
	}
	
	public void setType(String str)
	{
		type = str;
	}
	
	public Boolean isLoaded()
	{
		return loaded;
	}
	
	public AudioFormatPlugin getAudioFormatObject()
	{
		return fObject;
	}
	
	public void setAudioFormatObject(AudioFormatPlugin obj)
	{
		fObject = obj;
		loaded = true;
	}
	
	public TransformPlugin getTransformObject()
	{
		return tObject;
	}
	
	public void setTransformObject(TransformPlugin obj)
	{
		tObject = obj;
		loaded = true;
	}
	
	public void unLoad()
	{
		tObject = null;
		fObject = null;
		loaded = false;
	}
}
