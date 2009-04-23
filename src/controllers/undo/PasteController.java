package apes.controllers.undo;

import apes.controllers.ApplicationController;
import apes.models.undo.PasteEdit;
import apes.views.InternalFormatView;
import javax.swing.undo.UndoManager;

/**
 * Paste action.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 * @author Johan Åhlander (johan.ahlander@gmail.com)
 */
public class PasteController extends ApplicationController
{
  /**
   * The view over the internal format.
   */
  private InternalFormatView internalFormatView;
  
  /**
   * Paste edit model.
   */
  private PasteEdit pasteEdit;
  
  /**
   * The undo manager that keeps track of all changes.
   */
  private UndoManager undoManager;
  
  public PasteController( UndoManager undoManager, InternalFormatView internalFormatView )
  {
    this.internalFormatView = internalFormatView;
    this.pasteEdit = new PasteEdit();
    this.undoManager = undoManager;
  }
  
  /**
   *
   */
  public void paste()
  {
    
  }
}
