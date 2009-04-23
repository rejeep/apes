package apes.controllers.undo;

import apes.controllers.ApplicationController;
import apes.views.InternalFormatView;

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
  
  public PasteController(InternalFormatView internalFormatView)
  {
    this.internalFormatView = internalFormatView;
  }
  
  /**
   *
   */
  public void paste()
  {
    
  }
}
