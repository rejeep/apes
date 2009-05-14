package apes.views.buttons;

/**
 * This button is for saving the current file.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class SaveButton extends ImageButton
{
  @Override
  protected String getImage()
  {
    return "save";
  }

  @Override
  protected String getDescription()
  {
    return "button.save.description";
  }
}