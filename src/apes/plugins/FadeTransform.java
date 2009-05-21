package apes.plugins;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import apes.interfaces.TransformPlugin;
import apes.models.InternalFormat;

/**
 * Effect plugin that fades in or out.
 */
public class FadeTransform implements TransformPlugin, ActionListener
{
  /**
   * Window frame.
   */
  JFrame frame;

  /**
   * The internal format.
   */
  InternalFormat internalFormat;

  /**
   * Selected region.
   */
  Point selection;

  /**
   * The interval, how many samples per fade step.
   */
  int interval;

  /**
   * Return the name of the plugin.
   * 
   * @return The name
   */
  public String getName()
  {
    return "Fade";
  }

  /**
   * Returns the description map.
   * 
   * @return Description map.
   */
  public Map<String, String> getDescriptions()
  {
    HashMap<String, String> map = new HashMap<String, String>();

    map.put("en", "Fades the volume of a marked area either in or out.");
    map.put("sv", "Höjer volymen stegvis från noll, alternativt sänker den till noll.");

    return map;
  }

  /**
   * Creates and shows the frame.
   */
  public void showFrame()
  {
    frame = new JFrame();
    JPanel bPanel = new JPanel();

    bPanel.setLayout(new BorderLayout(5, 5));
    bPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JButton inButton = new JButton("Fade in");
    inButton.addActionListener(this);
    bPanel.add(inButton, BorderLayout.NORTH);

    JButton outButton = new JButton("Fade out");
    outButton.addActionListener(this);
    bPanel.add(outButton, BorderLayout.SOUTH);

    frame.add(bPanel);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }

  /**
   * Runs the effect on selected region.
   * 
   * @param internalFormat The internal format.
   * @param selection The selected region.
   */
  public void apply(InternalFormat internalFormat, Point selection)
  {
    this.internalFormat = internalFormat;
    this.selection = selection;
    interval = 10000;
    showFrame();
  }

  /**
   * Performs the fade on the internal format.
   * 
   * @param flag Fade in if true.
   */
  public void doFade(Boolean flag)
  {

    int curr = 0;
    float scale = 1.0f;

    int diff = selection.y - selection.x;
    int steps = diff / interval;
    int spill = diff % interval;

    for(int j = 0; j < steps; j++)
    {
      if(flag)
      {
        scale = (float)j / steps;
      }
      else
      {
        scale = (float)j / steps;
        scale = (float) ( 1.0f - scale );
      }
      curr = selection.x + ( j * interval );
      internalFormat.scaleSamples(curr, curr + interval - 1, scale);
    }

    curr = selection.x + steps * interval;
    internalFormat.scaleSamples(curr, curr + spill, scale);
    internalFormat.updated();
  }

  /**
   * Removes the frame.
   */
  public void closeFrame()
  {
    frame.setVisible(false);
    frame.dispose();
  }

  /**
   * Listen for events from the buttons.
   * 
   * @param ae ActionEvent.
   */
  public void actionPerformed(ActionEvent ae)
  {
    String action = ae.getActionCommand();

    if(action.equals("Fade in"))
    {
      doFade(true);
      closeFrame();
    }
    else if(action.equals("Fade out"))
    {
      doFade(false);
      closeFrame();
    }
  }
}
