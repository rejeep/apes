package apes.controllers.undo;

import apes.controllers.ApplicationController;
import apes.views.InternalFormatView;

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
  
  public CopyController(InternalFormatView internalFormatView)
  {
    this.internalFormatView = internalFormatView;
  }
  
  /**
   *
   */
  public void copy()
  {
    
  }
}
