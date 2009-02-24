package apes.models;

import java.util.HashMap;
/**
 *
 * @author sophie kores
 */
public class Tags extends HashMap<String, String>
{
    /**
     *
     * @param k
     * @param v
     * @return
     */
      public String put( String k, String v )
      {
            return super.put( k, v );
      }
      /**
       *
       * @param k
       * @return the value
       */
      public String get( String k )
      {
            return super.get( k );
      }


}