package apes.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JFrame;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apes.lib.Language;
import apes.lib.PluginHandler;
import apes.views.ApesMenu;
import apes.interfaces.TransformPlugin;
import apes.controllers.PluginController;

/**
 * Graphical view for the plugins.
 * 
 * @author Johan Åhlander (johan.ahlander@gmail.com)
 */
public class PluginView extends JFrame
{
  /**
   * The plugin controller
   */
  private PluginController pluginController;

  /**
   * The plugin handler
   */
  private PluginHandler pluginHandler;

  /**
   * Mapping to fetch selected values from checkboxes.
   */
  private Map<String, JCheckBox> choices;

  /**
   * The language object.
   */
  private Language language;

  /**
   * True if view has been created.
   */
  private boolean viewCreated = false;
  
  /**
   * The effect menu.
   */
  private JMenu effectMenu;

  /**
   * Creates a new <code>PluginView/code> instance.
   *
   * @param pH plugin handler.
   * @param Pc plugin controller
   */
  public PluginView(PluginHandler pH, PluginController pC)
  {
    pluginController = pC;
    pluginHandler = pH;
    choices = new HashMap<String, JCheckBox>();
    language = Language.getInstance();
    effectMenu = new ApesMenu( "menu.head.effects" );
  }

  /**
   * Creates the frame.
   */
  public void create()
  {
    if( !viewCreated )
    {
      setLayout(new BorderLayout());
      add(createPluginPanel(), BorderLayout.NORTH);
      add(createButtonPanel(), BorderLayout.SOUTH);
      setDefaultCloseOperation( DISPOSE_ON_CLOSE );
      pack();
      setLocationRelativeTo( null );
      viewCreated = true;
    }

    setVisible(true);
  }

  /**
   * Returns the top panel with selectable plugins.
   * 
   * @return The top panel.
   */
  public JPanel createPluginPanel()
  {
    JPanel panel = new JPanel();
    panel.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
    panel.setLayout( new BorderLayout() );
    
    JPanel top = new JPanel();
    JLabel header = new ApesLabel( "plugins.header" );
    header.setFont( new Font( "verdana", 1, 20 ) );
    top.add(header);
    panel.add(top, BorderLayout.NORTH);
    
    JPanel bottom = new JPanel();
    GridBagConstraints c = new GridBagConstraints();
    GridBagLayout gridbag = new GridBagLayout();
    
    bottom.setLayout(gridbag);
    bottom.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
    
    ArrayList<String> names = pluginHandler.getPluginNames();
    
    for(int i=0; i<names.size(); i++)
    {
      JCheckBox pBox = new JCheckBox(names.get(i), pluginHandler.isLoaded(names.get(i)));
      JTextArea pText = new JTextArea(pluginHandler.getDescription(names.get(i), "en"));
      
      c.gridx = 0;
      c.gridy = i+1;
      c.gridwidth = 1;;
      c.ipadx = 10;
      c.fill = GridBagConstraints.HORIZONTAL; 
      gridbag.setConstraints(pBox, c);
      bottom.add(pBox);
      c.gridx = 1;
      gridbag.setConstraints(pText, c);
      bottom.add(pText);
      choices.put(names.get(i), pBox);
    }
    
    panel.add(bottom, BorderLayout.SOUTH);
    
    return panel;
  }
  
  /**
   * Returns the bottom panel with buttons.
   * 
   * @return The bottom panel.
   */
  public JPanel createButtonPanel()
  {
    JPanel panel = new JPanel();
    
    JButton applyButton = new JButton( language.get( "plugins.apply" ) );
    applyButton.addActionListener(pluginController);
    applyButton.setName("apply");
    panel.add(applyButton);
    
    JButton closeButton = new JButton( language.get( "plugins.close" ) );
    closeButton.addActionListener(pluginController);
    closeButton.setName("close");
    panel.add(closeButton);
    
    return panel;
  }
  
  /**
   * Updates the effects menu.
   */
  public void updateEffectMenu()
  {
    effectMenu.removeAll();
    
    for (TransformPlugin p : pluginHandler.getTransforms())
    {
        JMenuItem effect = new JMenuItem( p.getName() );
        effect.setName( "doEffect" );
        effect.addActionListener( pluginController );
        effectMenu.add(effect);
    }
  }
  
  /**
   * Returns the effects menu.
   * 
   * @return Effects menu.
   */
  public JMenu getEffectMenu()
  {
    return effectMenu;
  }
    
  /**
   * Returns mapping with plugin names and checkboxes.
   * 
   * @return Selected options.
   */
  public Map<String, JCheckBox> getChoices()
  {
    return choices;
  }
}
