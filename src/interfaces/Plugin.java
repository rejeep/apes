package apes.interfaces;

import java.util.Map;

/**
 * Interface for the plugins.
 * 
 * @author Johan Åhlander (johan.ahlander@gmail.com)
 */
public interface Plugin
{
  /**
   * Returns the given name of the plugin.
   */
  public String getName();

  /**
   * Returns the description key of the plugin.
   */
  public Map<String, String> getDescriptions();
}
