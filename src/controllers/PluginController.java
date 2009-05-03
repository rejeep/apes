package apes.controllers;

import apes.views.PluginView;
import apes.lib.PluginHandler;
import java.util.Map;
import java.util.ArrayList;
import javax.swing.*;

public class PluginController extends ApplicationController
{
  PluginView pluginView;
  PluginHandler pluginHandler;

  public PluginController()
  {
    pluginHandler = new PluginHandler();
    pluginView = new PluginView(pluginHandler, this);
  }
  
  public void plugin()
  {
    pluginView.create();
  }
  
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
  
  public void close()
  {
    pluginView.setVisible(false);
    pluginView.dispose();
  }
}
