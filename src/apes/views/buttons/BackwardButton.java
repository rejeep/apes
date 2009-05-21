package apes.views.buttons;

/**
 * This button is for going backward in the music file.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class BackwardButton extends ImageButton
{
  @Override
  protected String getImage()
  {
    return "backward";
  }

  @Override
  protected String getDescription()
  {
    return "button.backward.description";
  }
}
