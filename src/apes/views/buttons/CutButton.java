package apes.views.buttons;

/**
 * This button is for cutting a region.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class CutButton extends ImageButton
{
  @Override
  protected String getImage()
  {
    return "cut";
  }

  @Override
  protected String getDescription()
  {
    return "button.cut.description";
  }
}
