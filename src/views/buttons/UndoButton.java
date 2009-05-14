package apes.views.buttons;

/**
 * This button is for undoing a change.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class UndoButton extends ImageButton
{
  @Override
  protected String getImage()
  {
    return "undo";
  }

  @Override
  protected String getDescription()
  {
    return "button.undo.description";
  }
}