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
   *
   */
  private InternalFormatView internalFormatView;
  
  /**
   * 
   */
  private PasteEdit pasteEdit;
  
  /**
   * 
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
