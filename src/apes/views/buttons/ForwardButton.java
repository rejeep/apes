package apes.views.buttons;

/**
 * This button is for going forward in the music file.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ForwardButton extends ImageButton
{
  @Override
  protected String getImage()
  {
    return "forward";
  }

  @Override
  protected String getDescription()
  {
    return "button.forward.description";
  }
}
