package apes.views;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import apes.controllers.ConfigController;
import apes.lib.Config;
import apes.lib.Language;

/**
 * A graphical view for the configuration file.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ConfigView extends JFrame
{
  /**
   * The model.
   */
  private Config config;

  /**
   * The controller.
   */
  private ConfigController configController;

  /**
   * A language object.
   */
  private Language language;

  /**
   * Contains all new option names as key. And the component as value.
   * This is so that the new value can be fetched from the component.
   */
  private Map<String, Component> newOptions;

  /**
   * Creates a new <code>ConfigView</code> instance.
   * 
   * @param config The model.
   * @param configController The controller.
   */
  public ConfigView(Config config, ConfigController configController)
  {
    this.config = config;
    this.configController = configController;
    this.newOptions = new HashMap<String, Component>();
    this.language = Language.getInstance();
  }

  /**
   * Creates the config frame.
   */
  public void create()
  {
    setLayout(new BorderLayout());

    setTitle(language.get("config.header"));

    // Create top and bottom panel.
    JPanel topPanel = topPanel();
    add(topPanel, BorderLayout.NORTH);

    JPanel bottomPanel = bottomPanel();
    add(bottomPanel, BorderLayout.SOUTH);

    pack();

    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    // Start in center on screen.
    setLocationRelativeTo(null);

    setVisible(true);
  }

  /**
   * Returns the top panel with all configuration options.
   * 
   * @return The top panel.
   */
  private JPanel topPanel()
  {
    Map<String, String> options = config.getOptions();
    Set<String> keys = options.keySet();

    JPanel panel = new JPanel();
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    panel.setLayout(new BorderLayout());

    JPanel top = new JPanel(new FlowLayout());
    panel.add(top, BorderLayout.NORTH);

    JLabel header = new ApesLabel("config.header");
    header.setFont(new Font("verdana", 1, 20));
    top.add(header);

    JPanel bottom = new JPanel(new GridLayout(options.size(), 2));
    panel.add(bottom, BorderLayout.SOUTH);

    for(String key : keys)
    {
      JLabel label = new ApesLabel("config." + key);
      label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
      bottom.add(label);

      String value = options.get(key);

      Config.Type type = config.getType(key);

      if(type == Config.Type.INTEGER || type == Config.Type.STRING)
      {
        JTextField textField = new JTextField(value);

        bottom.add(textField);

        newOptions.put(key, textField);
      }
      else if(type == Config.Type.BOOLEAN)
      {
        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(config.getBooleanOption(key));

        bottom.add(checkBox);

        newOptions.put(key, checkBox);
      }
    }

    return panel;
  }

  /**
   * Returns the bottom panel with a save and a close button.
   * 
   * @return The bottom panel.
   */
  private JPanel bottomPanel()
  {
    JPanel panel = new JPanel();

    JButton close = new ApesButton("config.properties.close");
    close.addActionListener(configController);
    close.setName("close");
    panel.add(close);

    JButton save = new ApesButton("config.properties.save");
    save.addActionListener(configController);
    save.setName("save");
    panel.add(save);

    return panel;
  }

  /**
   * Returns the new options map.
   * 
   * @return The new options.
   */
  public Map<String, Component> getNewOptions()
  {
    return this.newOptions;
  }
}
