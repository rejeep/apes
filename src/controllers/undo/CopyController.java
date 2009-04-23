package apes.controllers.undo;

import apes.controllers.ApplicationController;
import apes.models.undo.ChangeEdit;
import apes.views.InternalFormatView;
import javax.swing.undo.UndoManager;

/**
 * Copy action.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 * @author Johan Åhlander (johan.ahlander@gmail.com)
 */
public class CopyController extends ApplicationController
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
  
  public CopyController( UndoManager undoManager, InternalFormatView internalFormatView )
  {
    this.internalFormatView = internalFormatView;
    this.changeEdit = new ChangeEdit();
    this.undoManager = undoManager;
  }
  
  /**
   *
   */
  public void copy()
  {
    
  }
}
