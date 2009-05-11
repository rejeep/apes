package apes.views.buttons;

/**
 * This button is zooming the selection.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ZoomSelectionButton extends ImageButton
{
  @Override
  protected String getImage()
  {
    return "zoom_selection";
  }

  @Override
  protected String getDescription()
  {
    return "button.zoom_selection.description";
  }
}