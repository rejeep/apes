package apes.views.buttons;

/**
 * This button is for pasting a region.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class PasteButton extends ImageButton
{
  @Override
  protected String getImage()
  {
    return "paste";
  }
  
  @Override
  protected String getDescription()
  {
    return "button.paste.description";
  }
}