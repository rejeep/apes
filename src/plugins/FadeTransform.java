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
import apes.models.Samples;
import apes.models.InternalFormat;

public class FadeTransform implements TransformPlugin, ActionListener
{
  JFrame frame;
  InternalFormat internalFormat;
  Point selection;
  
  public String getName()
  {
    return "Fade";
  }

  public Map<String, String> getDescriptions()
  {
    HashMap map = new HashMap<String, String>();
    map.put("en", "Fade");
    map.put("sv", "-");
    return map;
  }
  
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

  public void apply( InternalFormat internalFormat, Point selection )
  {
    showFrame();
    int interval = 100;
    int diff = selection.y - selection.x;
    float scale;
    
    // just scale it for testing
    for(int i=0; i<internalFormat.getNumChannels(); i++)
    {
      scale = 0.05f;
      System.out.println("Setting for channel " + i+1);
      internalFormat.getChannel(i).scaleSamples(selection.x, selection.y, scale);
    }
  }
  
  public void actionPerformed(ActionEvent ae)
  {
    String action = ae.getActionCommand();
    System.out.println(action);
    if(action.equals("Fade in"))
    {
    }
    else if(action.equals("Fade out"))
    {
    }
  }
}
