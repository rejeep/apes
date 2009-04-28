package apes.plugins;

import apes.interfaces.TransformPlugin;
import apes.models.Samples;

// test plugin

public class SilenceTransform implements TransformPlugin
{
  public String getName()
  {
    return( "Silence" );
  }
  
  public String getDescription()
  {
    return( "A silence plugin for testing purposes." );
  }
  
  public Samples apply( Samples set )
  {
    return set;
  }
}
