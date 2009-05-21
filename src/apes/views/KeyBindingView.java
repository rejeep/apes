package apes.views;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import apes.controllers.KeyBindingController;
import apes.models.KeyBinding;


/**
 * View where binding can be changed.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class KeyBindingView extends JFrame
{
  /**
   * Key bindings model.
   */
  private KeyBinding keyBinding;

  /**
   * The locale prefix (same for all tags in this class).
   */
  private String localePrefix;

  /**
   * Key bindings controller.
   */
  private KeyBindingController keyBindingController;

  /**
   * A set of all key binding labels that has been added.
   */
  private Set<BindingsPanel.KeyBindingLabel> keyBindingLabels;

  /**
   * Creates a new key binding view.
   * 
   * @param keyBindingController The key binding controller.
   * @param keyBinding The key binding model.
   */
  public KeyBindingView(KeyBindingController keyBindingController, KeyBinding keyBinding)
  {
    this.keyBinding = keyBinding;
    this.keyBindingController = keyBindingController;
    this.localePrefix = "binding.";
    this.keyBindingLabels = new HashSet<BindingsPanel.KeyBindingLabel>();
  }

  /**
   * Creates the frame.
   */
  public void create()
  {
    setTitle(localePrefix + "header");
    setLayout(new BorderLayout());

    // Add panels.
    add(new TopPanel(), BorderLayout.NORTH);
    add(new BindingsPanel(), BorderLayout.CENTER);
    add(new ButtonPanel(), BorderLayout.SOUTH);

    pack();
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  /**
   * Returns a map with all bindings. Keys are options keys and values are the
   * bindings.
   * 
   * @return All bindings.
   */
  public Map<String, String> getOptions()
  {
    Map<String, String> options = new HashMap<String, String>();

    for(BindingsPanel.KeyBindingLabel label : keyBindingLabels)
    {
      options.put(label.getKey(), label.getValue());
    }

    return options;
  }

  /**
   * The top panel.
   */
  private class TopPanel extends JPanel
  {
    /**
     * Creates a new top panel.
     */
    public TopPanel()
    {
      JLabel header = new ApesLabel(localePrefix + "header");
      header.setFont(new Font("verdana", 1, 20));
      add(header);
    }
  }

  /**
   * The panel with all bindings.
   */
  private class BindingsPanel extends JPanel
  {
    /**
     * Creates a new bindings panel.
     */
    public BindingsPanel()
    {
      setLayout(new GridLayout(0, 1, 0, 3));

      Map<String, String> options = keyBinding.getOptions();
      for(String key : options.keySet())
      {
        KeyBindingLabel keyBindingLabel = new KeyBindingLabel(key, options.get(key));

        add(keyBindingLabel);
        keyBindingLabels.add(keyBindingLabel);
      }

      setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    /**
     * Creates a new key binding label that contains a label of the key binding
     * name and a field with the key binding.
     */
    private class KeyBindingLabel extends JPanel
    {
      /**
       * The text field with the binding.
       */
      private JTextField binding;

      /**
       * The binding key.
       */
      private String key;

      /**
       * Creates a new key binding label.
       * 
       * @param key The binding key.
       * @param value The binding.
       */
      public KeyBindingLabel(String key, String value)
      {
        this.key = key;

        setLayout(new GridLayout(1, 2));

        JLabel label = new ApesLabel(localePrefix + key);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        binding = new JTextField(value);

        add(label);
        add(binding);
      }

      /**
       * Returns the key.
       * 
       * @return The key.
       */
      public String getKey()
      {
        return key;
      }

      /**
       * Returns the value, which is the binding.
       * 
       * @return The binding.
       */
      public String getValue()
      {
        return binding.getText();
      }
    }
  }

  /**
   * The bottom panel with all buttons.
   */
  private class ButtonPanel extends JPanel
  {
    /**
     * Creates a new button panel.
     */
    public ButtonPanel()
    {
      JButton applyButton = new ApesButton(localePrefix + "apply");
      applyButton.addActionListener(keyBindingController);
      applyButton.setName("apply");
      add(applyButton);

      JButton closeButton = new ApesButton(localePrefix + "close");
      closeButton.addActionListener(keyBindingController);
      closeButton.setName("close");
      add(closeButton);

      setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
  }
}
