wpackage apes.views.buttons;

/**
 * This button is for deleting the selected region.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class DeleteButton extends ImageButton
{
  @Override
  protected String getImage()
  {
    return "delete";
  }

  @Override
  protected String getDescription()
  {
    return "button.delete.description";
  }
}
