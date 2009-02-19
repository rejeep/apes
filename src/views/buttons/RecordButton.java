package apes.views.buttons;

/**
 * This button is for recording.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class RecordButton extends ImageButton
{
  @Override
  protected String getImage()
  {
    return "record";
  }
  
  @Override
  protected String getDescription()
  {
    return "button.record.description";
  }
}