package apes.controllers.undo;

import apes.controllers.ApplicationController;
import apes.views.InternalFormatView;

/**
 * Performs a redo action in the undoManager.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 * @author Johan Åhlander (johan.ahlander@gmail.com)
 */
public class RedoController extends ApplicationController
{
  /**
   *
   */
  private InternalFormatView internalFormatView;
  
  public RedoController(InternalFormatView internalFormatView)
  {
    this.internalFormatView = internalFormatView;
  }

  /**
   *
   */
  public void redo()
  {
    
  }
}
