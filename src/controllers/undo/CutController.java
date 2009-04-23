package apes.controllers.undo;

import apes.controllers.ApplicationController;
import apes.views.InternalFormatView;
import apes.models.undo.CutEdit;

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
  
  /**
   * 
   */
  private CutEdit cutEdit;

  public CutController(InternalFormatView internalFormatView)
  {
    this.internalFormatView = internalFormatView;
    this.cutEdit = new CutEdit();
  }
  
  /**
   *
   */
  public void cut()
  {
    
  }
}
