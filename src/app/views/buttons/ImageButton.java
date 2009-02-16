package src.app.views.buttons;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import src.lib.Locale;

/**
 * <p>Creates a button with an icon on it. The button is stripped down so
 * that all that is visible is the icon. All buttons should have it's
 * own button class and extend this class which does the base job.</p>
 *
 * <p>The reason all buttons are in a separate class and that this
 * class does not handle all cases (because it would have been easy),
 * is because there may come additional features for these
 * buttons. And then doing it this way make it more extensible.</p>
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public abstract class ImageButton extends JButton
{
  /**
   * Path to folder where buttons images are.
   */
  private String buttonPath;

  /**
   * Name of default image. This is used if an image is missing.
   */
  private static final String DEFAULT_ICON = "default";

  /**
   * Default extension for image button icons.
   */
  private static final String DEFAULT_EXTENSION = "png";

  /**
   * Default operations for a button with an image on it.
   */
  public ImageButton()
  {
    // Locale object.
    Locale locale = Locale.getInstance();

    setToolTipText( locale.get( getDescription() ) );

    // Set default button path.
    setButtonPath( "images/buttons" );

    // Default icon.
    ImageIcon icon = getButtonDefaultIcon();
    setIcon( icon );

    // Size of button should be same as size of image.
    int width = icon.getIconWidth();
    int height = icon.getIconHeight();

    setPreferredSize( new Dimension( width, height ) );

    // Remove border.
    setBorderPainted(false);

    // Remove filling.
    setContentAreaFilled(false);

    // Background color.
    setBackground(Color.GRAY);

    // Icon to use for rollover.
    setRolloverIcon( getButtonHoverIcon() );

    // Icon to use for clicks.
    setPressedIcon( getButtonClickIcon() );
  }

  /**
   * Returns the default button icon.
   *
   * @return The default button icon.
   */
  private ImageIcon getButtonDefaultIcon()
  {
    return getButtonIcon( null );
  }

  /**
   * Returns the hover button icon. By standard should this image be
   * named same as the regular image but with "_hover" added to the
   * end.
   *
   * @return The hover button icon.
   */
  private ImageIcon getButtonHoverIcon()
  {
    return getButtonIcon( "_hover" );
  }

  /**
   * Returns the click button icon. By standard should this image be
   * named same as the regular image but with "_click" added to the
   * end.
   *
   * @return The click button icon.
   */
  private ImageIcon getButtonClickIcon()
  {
    return getButtonIcon( "_click" );
  }

  /**
   * Returns an ImageIcon object for an image.
   *
   * @return An ImageIcon object for an image if it exist. If not,
   * a default icon is returned.
   */
  private ImageIcon getButtonIcon( String postfix )
  {
    // File object for the image.
    File file = new File( getButtonIconPath( getImage() + postfix ) );

    // If there's no such file. Try with base file.
    if( !file.isFile() )
    {
      file = new File( getButtonIconPath( getImage() ) );
    }

    // Return base image if it exists.
    if( file.isFile() )
    {
      return new ImageIcon( file.getPath() );
    }

    return new ImageIcon( getButtonIconPath( DEFAULT_ICON ) );
  }

  /**
   * Returns the path to the image given by name.
   *
   * @param name The name of the image.
   * @return The path to the image.
   */
  private String getButtonIconPath( String name )
  {
    return getButtonPath() + "/" + name + "." + DEFAULT_EXTENSION;
  }

  /**
   * Returns the default buttons path.
   *
   * @return Default buttons path.
   */
  public String getButtonPath()
  {
    return buttonPath;
  }

  /**
   * Sets the default buttons path to <code>buttonPath</code>.
   *
   * @param buttonPath Path to the folder.
   */
  private void setButtonPath( String buttonPath )
  {
    this.buttonPath = buttonPath;
  }

  /**
   * Returns the name and name only of the image. If the image name is
   * <code>person_with_hat.png</code>, then this method should return
   * <code>person_with_hat</code>.
   *
   * @return The name of the image without extension.
   */
  protected abstract String getImage();

  /**
   * Returns the locale tag that gives a description of what this
   * button does.
   *
   * @return Locale tag for this button.
   */
  protected abstract String getDescription();
}