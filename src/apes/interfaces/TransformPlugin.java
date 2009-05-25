package apes.interfaces;

import java.awt.Point;

import apes.models.InternalFormat;


/**
 * Interface for effect plugins.
 */
public interface TransformPlugin extends Plugin
{
  /**
   * Applies the effect.
   * 
   * @param internalFormat A internal format.
   * @param selection A Point selection.
   */
  void apply(InternalFormat internalFormat, Point selection);
}
