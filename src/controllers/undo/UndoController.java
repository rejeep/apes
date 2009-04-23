package apes.controllers.undo;

import apes.controllers.ApplicationController;
import apes.views.InternalFormatView;

/**
 * Performs an undo action in the undoManager.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 * @author Johan Åhlander (johan.ahlander@gmail.com)
 */
public class UndoController extends ApplicationController
{
  /**
   *
   */
  private InternalFormatView internalFormatView;
  
  public UndoController( InternalFormatView internalFormatView )
  {
    this.internalFormatView = internalFormatView;
  }

  /**
   *
   */
  public void undo()
  {
    
  }
}
