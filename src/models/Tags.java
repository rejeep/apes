package apes.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Wraper class enabeling tag usage within the program.
 * @author sophie kores
 */
public class Tags
{
    /**
     * 
     */
    Map<String, String> tags;
    
    public Tags()
    {
      tags = new HashMap<String, String>();
    }

    /**
     * Associates a value to a specific key and puts them in a map.
     * @param key -
     * @param value - 
     * @return
     */
      public void put( String key, String value )
      {
        tags.put( key, value );
      }
      /**
       * Gets the value given a specific key.
       * @param key
       * @return the value
       */
      public String get( String key )
      {
        return (String)tags.get( key );
      }
}