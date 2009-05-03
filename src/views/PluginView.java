package apes.views;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.JLabel.*;
import apes.interfaces.AudioFormatPlugin;
import apes.interfaces.Plugin;
import apes.interfaces.TransformPlugin;
import java.util.ArrayList;
import apes.lib.PluginHandler;
import apes.controllers.PluginController;
import java.util.HashMap;
import java.util.Map;
import apes.lib.Language;

public class PluginView extends JFrame
{
  private PluginController pluginController;
  private PluginHandler pluginHandler;
  private Map<String, JCheckBox> choices;
  private Language language;
  
  public PluginView(PluginHandler pH, PluginController pC)
  {
    pluginController = pC;
    pluginHandler = pH;
    choices = new HashMap<String, JCheckBox>();
    language = Language.getInstance();
  }
  
  public void create()
  {      
    //setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
    setLayout(new BorderLayout());
    add(createPluginPanel(), BorderLayout.NORTH);
    add(createButtonPanel(), BorderLayout.SOUTH);
    pack();
    setVisible(true);
  }
  
  public JPanel createPluginPanel()
  {
    JPanel panel = new JPanel();
    ArrayList<String> names = pluginHandler.getPluginNames();
    panel.setLayout(new GridLayout(names.size(), 2));
    //panel.setLayout(new BorderLayout());
    for(int i=0; i<names.size(); i++)
    {
      JCheckBox pBox = new JCheckBox(names.get(i), pluginHandler.isLoaded(names.get(i)));
      //JLabel pLabel = new JLabel(pluginHandler.getDescription(names.get(i)));
      JTextArea pText = new JTextArea(pluginHandler.getDescription(names.get(i)));
      panel.add(pBox, BorderLayout.WEST);
      //panel.add(pLabel, BorderLayout.EAST);
      panel.add(pText, BorderLayout.EAST);
      choices.put(names.get(i), pBox);
    }
    return panel;
  }
  
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
  
  public Map<String, JCheckBox> getChoices()
  {
    return choices;
  }
}
