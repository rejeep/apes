package apes.plugins;

import java.util.Map;
import java.util.HashMap;
import java.awt.Point;

import apes.interfaces.TransformPlugin;
import apes.models.Samples;
import apes.models.InternalFormat;
import apes.models.Channel;

/**
 * A simple silence effect.
 */
public class SilenceTransform implements TransformPlugin
{
  /**
   * Returns the name
   * 
   * @return Name
   */
  public String getName()
  {
    return "Silence";
  }

  /**
   * Returns mapping with descriptions.
   * 
   * @return Description map.
   */
  public Map<String, String> getDescriptions()
  {
    HashMap map = new HashMap<String, String>();
    map.put("en", "A silence effect for testing purposes.");
    map.put("sv", "En tystnadseffekt för testning.");
    return map;
  }

  /**
   * Apples effect on internal format.
   * 
   * @param internalFormat The internal format.
   * @param selection The selected region.
   */
  public void apply( InternalFormat internalFormat, Point selection )
  {
    for(int i=0; i<internalFormat.getNumChannels(); i++)
    {
      internalFormat.getChannel(i).setSamples(selection.x, selection.y, 0);
    }
  }
}
