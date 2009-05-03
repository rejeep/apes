package apes.views;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
   * Contains all new option names as key. And the component as
   * value. This is so that the new value can be fetched from the
   * component.
   */
  private Map<String, Component> newOptions;

  /**
   * A language object.
   */
  private Language language;


  /**
   * Creates a new <code>ConfigView</code> instance.
   *
   * @param config The model.
   * @param configController The controller.
   */
  public ConfigView( Config config, ConfigController configController )
  {
    this.language = Language.getInstance();
    this.config = config;
    this.configController = configController;
    this.newOptions = new HashMap<String, Component>();
  }

  /**
   * Creates the config frame.
   */
  public void create()
  {
    setLayout( new BorderLayout() );

    // Create top and bottom panel.
    JPanel topPanel = topPanel();
    add( topPanel, BorderLayout.NORTH );

    JPanel bottomPanel = bottomPanel();
    add( bottomPanel, BorderLayout.SOUTH );

    pack();
    
    // Start in center on screen.
    setLocationRelativeTo( null );
    
    setVisible( true );
  }

  /**
   * Returns the top panel with all configuration options.
   *
   * @return The top panel.
   */
  private JPanel topPanel()
  {
    Map<String, String> options = config.getOptions();

    JPanel panel = new JPanel();
    panel.setLayout( new GridLayout( options.size(), 2 ) );

    Set<String> keys = options.keySet();

    for( String key : keys )
    {
      String name = language.get( "config." + key );
      Config.Type type = config.getType( key );

      panel.add( new JLabel( name ) );

      if( type == Config.Type.INTEGER || type == Config.Type.STRING )
      {
        JTextField textField = new JTextField( options.get( key ) );

        panel.add( textField );

        newOptions.put( key, textField );
      }
      else if( type == Config.Type.BOOLEAN )
      {
        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected( config.getBooleanOption( key ) );

        panel.add( checkBox );

        newOptions.put( key, checkBox );
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

    JButton close = new JButton( language.get( "config.properties.close" ) );
    close.addActionListener( configController );
    close.setName( "close" );
    panel.add( close );

    JButton save = new JButton( language.get( "config.properties.save" ) );
    save.addActionListener( configController );
    save.setName( "save" );
    panel.add( save );

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