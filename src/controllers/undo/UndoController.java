package apes.controllers.undo;

import apes.controllers.ApplicationController;
import apes.views.InternalFormatView;
import javax.swing.undo.UndoManager;

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
  
  /**
   * 
   */
  private UndoManager undoManager;
  
  public UndoController( UndoManager undoManager, InternalFormatView internalFormatView )
  {
    this.internalFormatView = internalFormatView;
    this.undoManager = undoManager;
  }

  /**
   *
   */
  public void undo()
  {
    
  }
}
