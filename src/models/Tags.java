package apes.models;

import java.util.HashMap;
/**
 * Wraper class enabeling tag usage within the program.
 * @author sophie kores
 */
public class Tags extends HashMap<String, String>
{
    /**
     * Associates a value to a specific key and puts them in a map.
     * @param k
     * @param v
     * @return
     */
      public String put( String k, String v )
      {
            return super.put( k, v );
      }
      /**
       * Gets the value given a specific key.
       * @param k
       * @return the value
       */
      public String get( String k )
      {
            return super.get( k );
      }


}