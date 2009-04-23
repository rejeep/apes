package apes.controllers.undo;

import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import apes.controllers.ApplicationController;
import apes.views.InternalFormatView;
import apes.models.undo.ChangeEdit;

/**
 * Change controller for sample modifications/effects.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 * @author Johan Åhlander (johan.ahlander@gmail.com)
 */
public class ChangeController extends ApplicationController
{
  /**
   * The view over the internal format.
   */
  private InternalFormatView internalFormatView;
  
  /**
   * The undo manager that keeps track of all changes.
   */
  private UndoManager undoManager;
  
  public ChangeController( UndoManager undoManager, InternalFormatView internalFormatView )
  {
    this.internalFormatView = internalFormatView;
    this.undoManager = undoManager;
  }
  
  /**
   *
   */
  public void change()
  {
    UndoableEdit edit = new ChangeEdit();
    
    undoManager.addEdit( edit );
  }
}
