package apes.controllers;

import java.util.Map;
import java.util.ArrayList;
import javax.swing.JCheckBox;

import apes.views.PluginView;
import apes.lib.PluginHandler;

/**
 * Plugin controller.
 *
 * @author Johan Åhlander (johan.ahlander@gmail.com)
 */
public class PluginController extends ApplicationController
{
  /**
   * The plugin view.
   */
  PluginView pluginView;
  
  /**
   * The plugin handler.
   */
  PluginHandler pluginHandler;

  /**
   * Creates a new plugin controller.
   */
  public PluginController()
  {
    //config option probably
    pluginHandler = new PluginHandler("build/apes/plugins");
    pluginView = new PluginView(pluginHandler, this);
  }
  
  /**
   * Create and show the frame.
   */
  public void plugin()
  {
    pluginView.create();
  }
  
  /**
   * Apply any changes made to selected plugins.
   */
  public void apply()
  {
    Map<String, JCheckBox> choices = pluginView.getChoices();
    ArrayList<String> names = pluginHandler.getPluginNames();
    
    for(String name : names)
    {
      if( !(pluginHandler.isLoaded(name) == choices.get(name).isSelected()) )
      {
        if(pluginHandler.isLoaded(name))
        {
          pluginHandler.unloadPlugin(name);
        }
        else
        {
          pluginHandler.loadPlugin(name);
        }
      }
    }
  }
  
  /**
   * Closes the frame.
   */
  public void close()
  {
    pluginView.setVisible(false);
    pluginView.dispose();
  }
}
