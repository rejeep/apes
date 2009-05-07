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
import apes.views.ApesMenu;
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
  PluginView pluginView;
  
  /**
   * The plugin handler.
   */
  PluginHandler pluginHandler;
  
  /**
   * Menu for effects
   */
  private JMenu effectMenu;
  
  /**
   * 
   */
  private PlayerHandler playerHandler;

  /**
   * Creates a new plugin controller.
   */
  public PluginController(PluginHandler pH, PlayerHandler playerHandler)
  {
    pluginHandler = pH;;
    this.playerHandler = playerHandler;
    pluginView = new PluginView(pluginHandler, this);
    effectMenu = new JMenu("Effects");
    updateEffectMenu();
  }
  
  /**
   * Create and show the frame.
   */
  public void plugin()
  {
    pluginView.create();
  }
  /**
   * TODO
   */
  public void updateEffectMenu()
  {
    effectMenu.removeAll();
    
    for (TransformPlugin p : pluginHandler.getTransforms())
    {
        JMenuItem effect = new JMenuItem( p.getName() );
        effect.setName( "doEffect" );
        effect.addActionListener( this );
        effectMenu.add(effect);
        System.out.println("menu: " + p.getName());
    }
  }
  
  /**
   * TODO
   */
  public JMenu getEffectMenu()
  {
    return effectMenu;
  }
  
  public void doEffect(String name)
  {
    System.out.println("doEffect(): " + name);
    if(playerHandler != null)
    {
    InternalFormat internalFormat = playerHandler.getInternalFormat();
    Player player = playerHandler.getPlayer(internalFormat);
    Point selection = player.getSelection();
    pluginHandler.getTransform(name).apply(internalFormat, selection);
    }
    else
    {
      System.out.println("is null!");
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
      System.out.println("apply() name: " + pluginHandler.isLoaded(name) + "-" + choices.get(name).isSelected());
      
      if( !(pluginHandler.isLoaded(name) == choices.get(name).isSelected()) )
      {
        if(pluginHandler.isLoaded(name))
        {
          System.out.println("unloading " + name);
          pluginHandler.unloadPlugin(name);
        }
        else
        {
          System.out.println("loading " + name);
          pluginHandler.loadPlugin(name);
        }
      }
    }
    
    // update effects menu
    updateEffectMenu();
  }
  
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
    pluginView.setVisible(false);
    pluginView.dispose();
  }
}
