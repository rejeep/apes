package apes.plugins;

import java.util.Map;
import java.util.HashMap;

import apes.interfaces.TransformPlugin;
import apes.models.Samples;

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
    map.put("sv", "En tystnads effekt för testing.");
    return map;
  }

  public Samples apply( Samples set )
  {
    return set;
  }
}
