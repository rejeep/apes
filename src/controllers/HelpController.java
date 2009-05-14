package apes.controllers;

import apes.views.HelpView;

/**
 * This controller handles all help related events.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class HelpController extends ApplicationController
{
  /**
   * An instance of the help view.
   */
  HelpView helpView;

  /**
   * Creates a new <code>HelpController</code> instance.
   */
  public HelpController()
  {
    helpView = new HelpView();
  }

  /**
   * About apes.
   */
  public void about()
  {
    helpView.about();
  }
}
