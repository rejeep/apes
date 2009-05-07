package apes.plugins;

import java.util.Map;
import java.util.HashMap;
import java.awt.Point;

import apes.interfaces.TransformPlugin;
import apes.models.Samples;
import apes.models.InternalFormat;
import apes.models.Channel;

/**
 * TODO: Comment
 */
public class SilenceTransform implements TransformPlugin
{
  public String getName()
  {
    return "Silence";
  }

  public Map<String, String> getDescriptions()
  {
    HashMap map = new HashMap<String, String>();
    map.put("en", "A silence effect for testing purposes.");
    map.put("sv", "En tystnadseffekt för testning.");
    return map;
  }

  public void apply( InternalFormat internalFormat, Point selection )
  {
    for(int i=0; i<internalFormat.getNumChannels(); i++)
    {
      internalFormat.getChannel(i).setSamples(selection.x, selection.y, 0);
    }
  }
}
