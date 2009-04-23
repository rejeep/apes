package apes.controllers.undo;

import apes.controllers.ApplicationController;
import apes.views.InternalFormatView;
import apes.models.undo.ChangeEdit;

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
  
  public CopyController( InternalFormatView internalFormatView )
  {
    this.internalFormatView = internalFormatView;
    this.changeEdit = new ChangeEdit();
  }
  
  /**
   *
   */
  public void copy()
  {
    
  }
}
