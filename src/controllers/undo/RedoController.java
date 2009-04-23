package apes.controllers.undo;

import apes.controllers.ApplicationController;
import apes.views.InternalFormatView;
import javax.swing.undo.UndoManager;

/**
 * Performs a redo action in the undoManager.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 * @author Johan Åhlander (johan.ahlander@gmail.com)
 */
public class RedoController extends ApplicationController
{
  /**
   * The view over the internal format.
   */
  private InternalFormatView internalFormatView;
  
  /**
   * The undo manager that keeps track of all changes.
   */
  private UndoManager undoManager;
  
  public RedoController( UndoManager undoManager, InternalFormatView internalFormatView )
  {
    this.internalFormatView = internalFormatView;
    this.undoManager = undoManager;
  }

  /**
   * Performs a redo.
   */
  public void redo()
  {
    undoManager.redo();
  }
}
