package apes.views.buttons;

/**
 * This button is for zooming in.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ZoomInButton extends ImageButton
{
  @Override
  protected String getImage()
  {
    return "zoom_in";
  }

  @Override
  protected String getDescription()
  {
    return "button.zoom_in.description";
  }
}