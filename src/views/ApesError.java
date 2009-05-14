package apes.views;

import javax.swing.JOptionPane;
import apes.lib.Language;
import apes.lib.Config;

/**
 * Apes error messages.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ApesError
{
  /**
   * Unknown error.
   */
  public static void unknownErrorOccurred()
  {
    ApesError.showError("unknown.title", "unknown.message");
  }

  /**
   * Shows an error message saying that the format is not supported.
   */
  public static void unsupportedFormat()
  {
    ApesError.showError("format.unsupported.title", "format.unsupported.message");
  }

  /**
   * Saving the internal format failed.
   */
  public static void saveFailure()
  {
    ApesError.showError("if.save.title", "if.save.message");
  }

  /**
   * Opening an internal format failed.
   */
  public static void couldNotOpenFileError()
  {
    ApesError.showError("if.open.title", "if.open.message");
  }

  /**
   * Show a dialog error box or prints to STDERR.
   * 
   * @param titleTag The title locale tag except "error.".
   * @param messageTag The message locale tag except "error.".
   */
  private static void showError(String titleTag, String messageTag)
  {
    Language language = Language.getInstance();
    Config config = Config.getInstance();

    String title = language.get("error." + titleTag);
    String message = language.get("error." + messageTag);

    if(config.getBooleanOption("gui_error_messages"))
    {
      JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }
    else
    {
      System.err.println(title + ":");
      System.err.println(message);
    }
  }
}
