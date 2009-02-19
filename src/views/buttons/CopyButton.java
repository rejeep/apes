package apes.views.buttons;

/**
 * This button is for coping a region.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class CopyButton extends ImageButton
{
  @Override
  protected String getImage()
  {
    return "copy";
  }
  
  @Override
  protected String getDescription()
  {
    return "button.copy.description";
  }
}