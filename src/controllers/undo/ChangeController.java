package apes.controllers.undo;

import apes.controllers.ApplicationController;
import apes.views.InternalFormatView;

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
  
  public ChangeController(InternalFormatView internalFormatView)
  {
    this.internalFormatView = internalFormatView;
  }
  
  /**
   *
   */
  public void change()
  {
    
  }
}
