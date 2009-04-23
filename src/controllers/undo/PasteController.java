package apes.controllers.undo;

import apes.controllers.ApplicationController;
import apes.views.InternalFormatView;
import apes.models.undo.PasteEdit;

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
  
  public PasteController(InternalFormatView internalFormatView)
  {
    this.internalFormatView = internalFormatView;
    this.pasteEdit = new PasteEdit();
  }
  
  /**
   *
   */
  public void paste()
  {
    
  }
}
