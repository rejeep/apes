package apes.interfaces;
import java.awt.Point;

import apes.models.InternalFormat;

/**
 * Interface for transform plugins.
 *
 * TODO: Some plugins will want get/set some value(s).
 */
public interface TransformPlugin extends Plugin
{
  void apply ( InternalFormat internalFormat, Point selection );
}
