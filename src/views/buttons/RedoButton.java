package apes.views.buttons;

/**
 * This button is for redoing a change.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class RedoButton extends ImageButton
{
  @Override
  protected String getImage()
  {
    return "redo";
  }

  @Override
  protected String getDescription()
  {
    return "button.redo.description";
  }
}