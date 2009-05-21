package apes.views;

import java.io.File;
import java.util.Observable;

import javax.swing.ImageIcon;
import javax.swing.JMenu;

import apes.interfaces.LanguageObserver;
import apes.lib.Language;


/**
 * This extends {@link javax.swing.JMenuItem JMenu} with locale functionality.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ApesMenu extends JMenu implements LanguageObserver
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
   * Creates a new <code>ApesMenu</code> instance.
   * 
   * @param tag The Language tag.
   */
  public ApesMenu(String tag)
  {
    this.tag = tag;
    this.language = Language.getInstance();

    setText(language.get(tag));
    language.addObserver(this);

    // What should have been Arrays#join
    String[] split = tag.split("\\.");
    String button = split[split.length - 1];

    // Set icon
    File file = new File("images/menu/" + button + ".png");
    if(file.exists())
    {
      ImageIcon icon = new ImageIcon(file.getAbsolutePath());
      setIcon(icon);
    }
  }

  public void update(Observable o, Object arg)
  {
    setText(language.get(tag));
    this.updateUI();
  }
}
