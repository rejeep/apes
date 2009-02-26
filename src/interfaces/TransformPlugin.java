package apes.interfaces;

import apes.models.Samples;

public interface TransformPlugin extends Plugin
{
  Samples apply ( Samples set );
}
