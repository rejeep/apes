package apes.plugins;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.util.Map;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JButton;

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
    HashMap map = new HashMap<String, String>();
    map.put("en", "Fade");
    map.put("sv", "-");
    return map;
  }
  
  /**
   * Creates and shows the frame.
   */
  public void showFrame()
  {
    frame = new JFrame();
    frame.setLayout( new BorderLayout() );
    
    JButton inButton = new JButton("Fade in");
    inButton.addActionListener(this);
    frame.add(inButton, BorderLayout.NORTH);
    JButton outButton = new JButton("Fade out");
    outButton.addActionListener(this);
    frame.add(outButton, BorderLayout.SOUTH);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation( frame.DISPOSE_ON_CLOSE );
    frame.pack();
    frame.setVisible(true);
  }

  /**
   * Runs the effect on selected region.
   *
   * @param internalFormat The internal format.
   * @param selection The selected region.
   */
  public void apply( InternalFormat internalFormat, Point selection )
  {
    this.internalFormat = internalFormat;
    this.selection = selection;
    interval = 100;
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
  
 
    for(int j=0; j<steps; j++)
    {
      if(flag)
      {
        scale = (float) j / steps;
      }
      else
      {
        scale = (float) j / steps;
        scale = (float) (1.0f - scale);
      }
      curr = selection.x + (j*interval);
      internalFormat.scaleSamples(curr, curr+interval-1, scale);
    }
    
    curr = selection.x + steps*interval;
    internalFormat.scaleSamples(curr, curr+spill, scale);
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
