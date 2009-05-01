package apes.views.buttons;

/**
 * This button is for stop playing the music file.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class StopButton extends ImageButton
{
  @Override
  protected String getImage()
  {
    return "stop";
  }

  @Override
  protected String getDescription()
  {
    return "button.stop.description";
  }
}