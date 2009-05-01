package apes.views.buttons;

/**
 * This button is for zooming out.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ZoomOutButton extends ImageButton
{
  @Override
  protected String getImage()
  {
    return "zoom_out";
  }

  @Override
  protected String getDescription()
  {
    return "button.zoom_out.description";
  }
}