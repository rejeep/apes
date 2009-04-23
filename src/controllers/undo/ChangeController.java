package apes.controllers.undo;

import apes.controllers.ApplicationController;
import apes.views.InternalFormatView;
import apes.models.undo.ChangeEdit;

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
  
  public ChangeController(InternalFormatView internalFormatView)
  {
    this.internalFormatView = internalFormatView;
    this.changeEdit = new ChangeEdit();
  }
  
  /**
   *
   */
  public void change()
  {
    
  }
}
