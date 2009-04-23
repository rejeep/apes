package apes.controllers.undo;

import apes.controllers.ApplicationController;
import apes.views.InternalFormatView;

/**
 * Cut action.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 * @author Johan Åhlander (johan.ahlander@gmail.com)
 */
public class CutController extends ApplicationController
{
 /**
  *
  */
  private InternalFormatView internalFormatView;

  public CutController(InternalFormatView internalFormatView)
  {
    this.internalFormatView = internalFormatView;
  }
  
  /**
   *
   */
  public void cut()
  {
    
  }
}
