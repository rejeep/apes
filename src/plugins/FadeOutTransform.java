package apes.plugins;

import java.awt.Point;
import java.util.Map;
import java.util.HashMap;

import apes.interfaces.TransformPlugin;
import apes.models.Samples;
import apes.models.InternalFormat;

/**
 * TODO: Comment
 */
public class FadeOutTransform implements TransformPlugin
{
  public String getName()
  {
    return "Fade out";
  }

  public Map<String, String> getDescriptions()
  {
    HashMap map = new HashMap<String, String>();
    map.put("en", "Fade out");
    map.put("sv", "-");
    return map;
  }

  public void apply( InternalFormat internalFormat, Point selection )
  {
    int interval = 100;
    int diff = selection.y - selection.x;
    float scale;
    
    for(int i=0; i<internalFormat.getNumChannels(); i++)
    {
      scale = 0.05f;
      System.out.println("Setting for channel " + i+1);
      internalFormat.getChannel(i).scaleSamples(selection.x, selection.y, scale);
    }
  }
}
