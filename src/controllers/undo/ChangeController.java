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
   *
   */
  private InternalFormatView internalFormatView;
  
  /**
   * 
   */
  private ChangeEdit changeEdit;
  
  /**
   * 
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
