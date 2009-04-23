package apes.controllers.undo;

import javax.swing.undo.UndoManager;

import apes.controllers.ApplicationController;
import apes.views.InternalFormatView;
import apes.models.undo.CopyEdit;

/**
 * Copy action.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 * @author Johan Åhlander (johan.ahlander@gmail.com)
 */
public class CopyController extends ApplicationController
{
  /**
   * The view over the internal format.
   */
  private InternalFormatView internalFormatView;
  
  /**
   * The undo manager that keeps track of all changes.
   */
  private UndoManager undoManager;
  
  public CopyController( UndoManager undoManager, InternalFormatView internalFormatView )
  {
    this.internalFormatView = internalFormatView;
    this.undoManager = undoManager;
  }
  
  /**
   *
   */
  public void copy()
  {
    
  }
}
