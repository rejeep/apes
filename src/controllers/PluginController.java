package apes.controllers;

import java.util.Map;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.Point;

import apes.views.PluginView;
import apes.lib.PluginHandler;
import apes.interfaces.TransformPlugin;
import apes.views.ApesMenuItem;
import apes.lib.PlayerHandler;
import apes.models.InternalFormat;
import apes.models.Player;

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
  private PluginView pluginView;
  
  /**
   * The plugin handler.
   */
  private PluginHandler pluginHandler;
  
  /**
   * The player handler.
   */
  private PlayerHandler playerHandler;

  /**
   * Creates a new plugin controller.
   * 
   * @param pH Plugin handler.
   */
  public PluginController(PluginHandler pH)
  {
    pluginHandler = pH;;
    this.playerHandler = PlayerHandler.getInstance();
    pluginView = new PluginView(pluginHandler, this);
    //effectMenu = new JMenu( "menu.head.effects" );
    pluginView.updateEffectMenu();
  }
  
  /**
   * Create and show the frame.
   */
  public void plugin()
  {
    pluginView.create();
  }
  
  public JMenu getEffectMenu()
  {
    return pluginView.getEffectMenu();
  }
  
  /**
   * Given a plugin name, runs the effect.
   * 
   * @param name The name of the plugin.
   */
  public void doEffect(String name)
  {
    if(playerHandler != null)
    {
      InternalFormat internalFormat = playerHandler.getInternalFormat();
      if(internalFormat == null)
      {
        return;
      }
      else
      {
      Player player = playerHandler.getPlayer(internalFormat);
      Point selection = player.getSelection();
      pluginHandler.getTransform(name).apply(internalFormat, selection);
      }
    }
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
    
    // update effects menu
    pluginView.updateEffectMenu();
  }
  
  /**
   * Override actionPerformed in ActionController for effect events.
   * 
   * @param ae ActionEvent.
   */
  public void actionPerformed(ActionEvent ae)
  {
    String action = ae.getActionCommand();
    if(action.equals("Apply") || action.equals("Close") || action.equals("Plugins"))
    {
      super.actionPerformed(ae);
    }
    else
    {
      doEffect(action);
    }
  }
  
  /**
   * Closes the frame.
   */
  public void close()
  {
    Map<String, JCheckBox> choices = pluginView.getChoices();
    ArrayList<String> names = pluginHandler.getPluginNames();
    
    for(String name : names)
    {
      choices.get(name).setSelected(pluginHandler.isLoaded(name));
    }
    pluginView.setVisible(false);
    pluginView.dispose();
  }
}
