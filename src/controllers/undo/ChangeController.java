package apes.controllers.undo;

import apes.controllers.ApplicationController;
import apes.models.undo.ChangeEdit;
import apes.views.InternalFormatView;
import javax.swing.undo.UndoManager;

/**
 * Change action for sample modifications/effects.
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
   * Change edit model.
   */
  private ChangeEdit changeEdit;
  
  /**
   * The undo manager that keeps track of all changes.
   */
  private UndoManager undoManager;
  
  public ChangeController( UndoManager undoManager, InternalFormatView internalFormatView )
  {
    this.internalFormatView = internalFormatView;
    this.changeEdit = new ChangeEdit();
    this.undoManager = undoManager;
  }
  
  /**
   *
   */
  public void change()
  {
    
  }
}
