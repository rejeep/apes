package apes.views.buttons;

/**
 * This button is for playing a music file or a region of it.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class PlayButton extends ImageButton
{
  @Override
  protected String getImage()
  {
    return "play";
  }

  @Override
  protected String getDescription()
  {
    return "button.play.description";
  }
}
