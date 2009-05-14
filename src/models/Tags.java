package apes.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper class enabling tag usage within the program.
 * 
 * @author sophie kores
 */
public class Tags
{
  /**
   * Contains all tags. Example: { "artist" => "Madonna", "track" =>
   * "Tell Me" }
   */
  Map<String, String> tags;

  /**
   * Creates a new <code>Tags</code> instance.
   */
  public Tags()
  {
    tags = new HashMap<String, String>();
  }

  /**
   * Associates a value to a specific key and puts them in a map.
   * 
   * @param key The tag key
   * @param value The tag value
   */
  public void put( String key, String value )
  {
    tags.put( key, value );
  }
  /**
   * Gets the value given a specific key.
   * 
   * @param key The tag key
   * @return The tag value. Or null if it does not exist.
   */
  public String get( String key )
  {
    return (String)tags.get( key );
  }
}