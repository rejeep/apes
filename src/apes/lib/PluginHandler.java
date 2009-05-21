package apes.lib;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apes.interfaces.AudioFormatPlugin;
import apes.interfaces.TransformPlugin;

/**
 * Class for loading/unloading plugins.
 * 
 * @author Johan Åhlander (johan.ahlander@gmail.com)
 */
public class PluginHandler
{
  /**
   * Arraylist of plugins.
   */
  private ArrayList<PluginInfo> plugins;

  /**
   * Custom classloader.
   */
  private PluginLoader cl;

  /**
   * Constructor.
   * 
   * @param path The path to load plugins from.
   */
  public PluginHandler(String path)
  {
    plugins = new ArrayList<PluginInfo>();
    cl = new PluginLoader();
    addPluginsInPath(path);

  }

  /**
   * Returns an ArrayList of the plugin names.
   * 
   * @return The plugin names.
   */
  public ArrayList<String> getPluginNames()
  {
    ArrayList<String> list = new ArrayList<String>();

    for(PluginInfo p : plugins)
    {
      list.add(p.getName());
    }

    return list;
  }

  /**
   * Returns the description of a plugin.
   * 
   * @param name Name of the plugin.
   * @return The description.
   */

  public String getDescription(String name, String language)
  {
    for(PluginInfo p : plugins)
    {
      if(p.getName().equals(name))
      {
        return p.getDescription(language);
      }
    }

    return null;
  }

  /**
   * Returns true if the plugin is loaded.
   * 
   * @param name The name of the plugin.
   */
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
   * @return ArrayList of TransformPlugins.
   */
  public ArrayList<TransformPlugin> getTransforms()
  {
    ArrayList<TransformPlugin> list = new ArrayList<TransformPlugin>();

    for(PluginInfo p : plugins)
    {
      if(p.getType().equals("transform") && p.isLoaded())
      {
        list.add(p.getTransformObject());
      }
    }

    return list;
  }

  /**
   * Returns an ArrayList of all the format classes.
   * 
   * @return ArrayList of AudioFormatPlugins.
   */
  public ArrayList<AudioFormatPlugin> getFormats()
  {
    ArrayList<AudioFormatPlugin> list = new ArrayList<AudioFormatPlugin>();

    for(PluginInfo p : plugins)
    {
      if(p.getType().equals("format") && p.isLoaded())
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
  public void addPlugin(String str)
  {
    File file = new File(str);

    if(file.isDirectory())
    {
      addPluginsInPath(file.getPath());
    }
    else if(file.isFile())
    {
      PluginInfo pi = new PluginInfo();
      loadFile(file.getPath(), file.getName().substring(0, file.getName().indexOf(".")), pi);
    }
  }

  /**
   * Returns a TransformPlugin object.
   * 
   * @param name The name of the plugin.
   * @return The TransformPlugin object.
   */
  public TransformPlugin getTransform(String name)
  {
    for(int i = 0; i < plugins.size(); i++)
    {
      if(plugins.get(i).getName().equals(name) && plugins.get(i).getType().equals("transform"))
      {
        return plugins.get(i).getTransformObject();
      }
    }
    return null;
  }

  /**
   * Returns a AudioFormatPlugin object.
   * 
   * @param name The name of the plugin.
   * @return The AudioFormatPlugin object.
   */
  public AudioFormatPlugin getAudioFormat(String name)
  {
    for(int i = 0; i < plugins.size(); i++)
    {
      if(plugins.get(i).getName().equals(name) && plugins.get(i).getType().equals("format"))
      {
        return plugins.get(i).getAudioFormatObject();
      }
    }
    return null;
  }

  /**
   * Unloads a plugin.
   * 
   * @param name The name of the plugin.
   */
  public void unloadPlugin(String name)
  {
    for(int i = 0; i < plugins.size(); i++)
    {
      if(plugins.get(i).getName().equals(name))
      {
        if(plugins.get(i).isLoaded())
        {
          plugins.get(i).unLoad();
          /*
           * discard the old classloader to remove any references to
           * the class. Read somewhere that two gc() should do the
           * trick :-)
           */
          cl = new PluginLoader();
          System.gc();
          System.gc();
          return;
        }
      }
    }
  }

  /**
   * Loads an unloaded plugin.
   * 
   * @param name The name of the plugin.
   */
  public void loadPlugin(String name)
  {
    for(PluginInfo p : plugins)
    {
      if(p.getName().equals(name))
      {
        File file = new File(p.getPath());
        loadFile(file.getPath(), file.getName().substring(0, file.getName().indexOf(".")), p);
      }
    }
  }

  /**
   * Tries to load all plugins in a directory.
   * 
   * @param path Directory.
   */
  private void addPluginsInPath(String path)
  {
    File dir = new File(path);
    String[] files = dir.list();

    if(files.length == 0)
    {
      return;
    }
    else
    {
      FilenameFilter filter = new FilenameFilter()
      {
        public boolean accept(File d, String n)
        {
          return !n.startsWith(".");
        }
      };

      files = dir.list(filter);
      for(String f : files)
      {
        loadFile(path + "/" + f, f.substring(0, f.indexOf(".")), new PluginInfo());
      }
    }
  }

  /**
   * Loads a single plugin, given a path and a name.
   * 
   * @param path Directory.
   * @param name Name of class to load.
   * @param pi PluginInfo object.
   */
  private void loadFile(String path, String name, PluginInfo pi)
  {
    Class<?> cls;
    
    try
    {
      if(!plugins.contains(pi))
      {
        pi.setPath(path);
      }

      if(path.endsWith(".class"))
      {
        cls = cl.loadClass(path, "apes.plugins." + name);
        instancePlugin(cls, pi);
      }
      else if(path.endsWith(".jar"))
      {
        cls = cl.loadClass(path, "apes.plugins." + name);
        instancePlugin(cls, pi);
      }
    }
    catch(ClassNotFoundException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Creates a new instance of the class and adds it to the list of
   * loaded plugins.
   * 
   * @param cls Class object.
   * @param pi The PluginInfo object to assign it to.
   */
  private void instancePlugin(Class<?> cls, PluginInfo pi)
  {
    try
    {
      if(TransformPlugin.class.isAssignableFrom(cls))
      {
        TransformPlugin tp = (TransformPlugin)cls.newInstance();
        pi.setTransformObject(tp);

        if(!plugins.contains(pi))
        {
          pi.setName(tp.getName());
          pi.setDescription(tp.getDescriptions());
          pi.setType("transform");
          pi.setTransformObject(tp);
          plugins.add(pi);
        }
      }
      else if(AudioFormatPlugin.class.isAssignableFrom(cls))
      {
        AudioFormatPlugin afp = (AudioFormatPlugin)cls.newInstance();
        pi.setAudioFormatObject(afp);

        if(!plugins.contains(pi))
        {
          pi.setName(afp.getName());
          pi.setDescription(afp.getDescriptions());
          pi.setType("format");
          pi.setAudioFormatObject(afp);
          plugins.add(pi);
        }
      }
    }
    catch(InstantiationException e)
    {
      e.printStackTrace();
    }
    catch(IllegalAccessException e)
    {
      e.printStackTrace();
    }
  }
}


/**
 * Custom loader to avoid loading with the system ClassLoader.
 */
class PluginLoader extends ClassLoader
{
  /**
   * Loads the class.
   * 
   * @param location The path.
   * @param name The name.
   * @return The class.
   */
  public Class<?> loadClass(String location, String name) throws ClassNotFoundException
  {
    byte[] classBytes = null;

    // read the class file
    try
    {

      FileInputStream in = new FileInputStream(location);
      ByteArrayOutputStream buf = new ByteArrayOutputStream();
      int c;
      while( ( c = in.read() ) != -1)
      {
        buf.write(c);
      }
      classBytes = buf.toByteArray();

    }
    catch(IOException e)
    {
      e.printStackTrace();
    }

    if(classBytes == null)
    {
      throw new ClassNotFoundException("Cannot load class");
    }

    // turn it to a class
    try
    {
      Class<?> cls = defineClass(name, classBytes, 0, classBytes.length);
      resolveClass(cls);
      return cls;
    }
    catch(ClassFormatError e)
    {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Loads a JAR file. Untested :-)
   * 
   * @param location Directory.
   * @param name Name of JAR to load.
   * @throws ClassNotFoundException
   */
  @SuppressWarnings("deprecation")
  public Class<?> loadJAR(String location, String name) throws ClassNotFoundException
  {
    try
    {
      File pluginFile = new File(location);
      URL[] locations = new URL[] { pluginFile.toURL() };
      URLClassLoader classloader = new URLClassLoader(locations);

      Class<?> cls = classloader.loadClass(name);
      return cls;
    }
    catch(MalformedURLException e)
    {
      e.printStackTrace();
    }

    return null;
  }
}


/**
 * Class to hold plugin information.
 */
class PluginInfo
{
  /**
   * The path to the plugin file.
   */
  private String path;

  /**
   * The name of the plugin.
   */
  private String name;

  /**
   * The description of the plugin.
   */
  private Map<String, String> desc;

  /**
   * The type of the plugin, "format" or "transform".
   */
  private String type;

  /**
   * The state of the plugin.
   */
  private Boolean loaded;

  /**
   * Holder for a AudioFormatPlugin object.
   */
  private AudioFormatPlugin fObject;

  /**
   * Holder for a TransformPlugin object.
   */
  private TransformPlugin tObject;

  /**
   * Constructor.
   */
  public PluginInfo()
  {
    desc = new HashMap<String, String>();
  }

  /**
   * Returns path to plugin.
   * 
   * @return The path.
   */
  public String getPath()
  {
    return path;
  }

  /**
   * Sets the path to the plugin.
   * 
   * @param str The path.
   */
  public void setPath(String str)
  {
    path = str;
  }

  /**
   * Returns the name of the plugin.
   * 
   * @return The plugin name.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Sets the name of the plugin.
   * 
   * @param str The name.
   */
  public void setName(String str)
  {
    name = str;
  }

  /**
   * Returns the plugin description.
   * 
   * @param language The chosen language.
   * @return The description.
   */
  public String getDescription(String language)
  {
    if(desc.containsKey(language))
    {
      return desc.get(language);
    }
    else
    {
      return desc.get("en");
    }
  }

  /**
   * Sets the plugin description.
   * 
   * @param map The description map.
   */
  public void setDescription(Map<String, String> map)
  {
    desc = map;
  }

  /**
   * Returns the type.
   * 
   * @return The type.
   */
  public String getType()
  {
    return type;
  }

  /**
   * Sets the plugin type.
   * 
   * @param str The type.
   */
  public void setType(String str)
  {
    type = str;
  }

  /**
   * Returns true if the plugin is loaded.
   * 
   * @return
   */
  public Boolean isLoaded()
  {
    return loaded;
  }

  /**
   * Returns the AudioFormatPlugin object.
   * 
   * @return Plugin object.
   */
  public AudioFormatPlugin getAudioFormatObject()
  {
    return fObject;
  }

  /**
   * Sets the AudioFormatPlugin object.
   * 
   * @param obj The object.
   */
  public void setAudioFormatObject(AudioFormatPlugin obj)
  {
    fObject = obj;
    loaded = true;
  }

  /**
   * Returns the TransformPlugin object.
   * 
   * @return Plugin object.
   */
  public TransformPlugin getTransformObject()
  {
    return tObject;
  }

  /**
   * Sets the TransformPlugin object.
   * 
   * @param obj The object.
   */
  public void setTransformObject(TransformPlugin obj)
  {
    tObject = obj;
    loaded = true;
  }

  /**
   * Unloads the plugin object.
   */
  public void unLoad()
  {
    tObject = null;
    fObject = null;
    loaded = false;
  }
}
