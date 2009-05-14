package apes.views.buttons;

/**
 * This button is for open a new music file in to the program.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class OpenButton extends ImageButton
{
  @Override
  protected String getImage()
  {
    return "open";
  }

  @Override
  protected String getDescription()
  {
    return "button.open.description";
  }
}