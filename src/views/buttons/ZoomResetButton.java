package apes.views.buttons;

/**
 * This button is for resetting the zoom.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ZoomResetButton extends ImageButton
{
  @Override
  protected String getImage()
  {
    return "zoom_reset";
  }

  @Override
  protected String getDescription()
  {
    return "button.zoom_reset.description";
  }
}