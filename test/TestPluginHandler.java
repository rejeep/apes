package test;

import org.junit.Before;
import org.junit.Test;
import apes.lib.PluginHandler;
import apes.interfaces.AudioFormatPlugin;

import static org.junit.Assert.*;

public class TestPluginHandler
{
  private PluginHandler pluginobj;
  
  /**
   * Some before stuff.
   */
  @Before public void setupPluginHandler()
  {
    pluginobj = new PluginHandler();
    pluginobj.addPlugin("test/WaveFileFormat.class");
  }
  
  /**
   * Check if it can load the class.
   */
  @Test public void testDoesAddPluginAddAClass()
  {
    assertEquals( "Transform plugins should not be loaded", 0, pluginobj.getTransforms().size());
    assertEquals( "Format plugin should be loaded", 1, pluginobj.getFormats().size());
  }
  
  /**
   * Can we use it?
   */
  @Test public void testDoesThePluginWork()
  {
    AudioFormatPlugin plugin = pluginobj.getFormats().get(0);
    assertEquals( "Plugin should return 'wav'", "wav", plugin.getExtension() );
  }
}

