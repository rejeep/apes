package src.app.views.buttons;

/**
 * This button is for the full screen view.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class FullScreenButton extends ImageButton
{
  @Override
  protected String getImage()
  {
    return "full_screen";
  }
  
  @Override
  protected String getDescription()
  {
    return "button.full_screen.description";
  }
}