package apes.views.buttons;

/**
 * This button is for pausing the music file.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class PauseButton extends ImageButton
{
  @Override
  protected String getImage()
  {
    return "pause";
  }

  @Override
  protected String getDescription()
  {
    return "button.pause.description";
  }
}