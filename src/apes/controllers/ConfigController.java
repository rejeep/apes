package apes.controllers;

import java.awt.Component;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JTextField;

import apes.lib.Config;
import apes.views.ConfigView;

/**
 * Controller for the configuration file.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ConfigController extends ApplicationController
{
  /**
   * The view.
   */
  private ConfigView configView;

  /**
   * The model.
   */
  private Config config;

  /**
   * Creates a new controller.
   */
  public ConfigController()
  {
    config = Config.getInstance();
    configView = new ConfigView(config, this);
  }

  /**
   * Show the config frame.
   */
  public void show()
  {
    configView.create();
  }

  /**
   * Save the new settings.
   */
  public void save()
  {
    Map<String, Component> newOptions = configView.getNewOptions();

    for(String key : newOptions.keySet())
    {
      Config.Type type = config.getType(key);
      String value = null;

      if(type == Config.Type.INTEGER || type == Config.Type.STRING)
      {
        value = ( (JTextField)newOptions.get(key) ).getText();
      }
      else if(type == Config.Type.BOOLEAN)
      {
        JCheckBox checkBox = (JCheckBox)newOptions.get(key);

        value = checkBox.isSelected() ? "true" : "false";
      }

      config.addOption(key, value, type);
    }

    config.save();
  }

  /**
   * Closes the config frame.
   */
  public void close()
  {
    configView.setVisible(false);
    configView.dispose();
  }
}
