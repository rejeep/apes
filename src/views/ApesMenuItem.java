package apes.views;

import java.util.Observable;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

import apes.interfaces.LanguageObserver;
import apes.lib.Language;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.File;

/**
 * This extends {@link javax.swing.JMenuItem JMenuItem} with locale
 * functionality.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ApesMenuItem extends JMenuItem implements LanguageObserver
{
  /**
   * The locale tag.
   */
  private String tag;

  /**
   * A language object.
   */
  private Language language;

  /**
   * Creates a new <code>ApesMenuItem</code> instance.
   *
   * @param tag The Language tag.
   */
  public ApesMenuItem( String tag )
  {
    this.language = Language.getInstance();
    this.tag = tag;

    setText( language.get( tag ) );
    language.addObserver( this );

    // Set icon.
    String[] split = tag.split( "\\." );
    String button = split[split.length - 1];
    File file = new File( "images/menu/" + button + ".png" );
    if( file.exists() )
    {
      ImageIcon icon = new ImageIcon( file.getAbsolutePath() );
      setIcon( icon );
    }
  }

  public void update( Observable o, Object arg )
  {
    setText( language.get( tag ) );

    this.updateUI();
  }
}