package apes.interfaces;

import apes.models.Samples;

/**
 * Interface for transform plugins.
 */
public interface TransformPlugin extends Plugin
{
  /**
   * Applies transformation to a Samples object.
   * 
   * @param set Samples object containing samples to be altered.
   * @return Samples object containing the changes.
   */
  Samples apply ( Samples set );
}
