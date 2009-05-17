package apes.views;

import java.io.File;
import java.util.Observable;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import apes.interfaces.LanguageObserver;
import apes.lib.Language;
import java.awt.event.InputEvent;

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
  public ApesMenuItem(String tag)
  {
    this.language = Language.getInstance();
    this.tag = tag;

    setText(language.get(tag));
    language.addObserver(this);

    // What should have been Arrays#join
    String[] split = tag.split("\\.");
    StringBuffer button = new StringBuffer();
    int start = 2;
    for(int i = start; i < split.length; i++)
    {
      if(i != start)
      {
        button.append("_");
      }

      button.append(split[i]);
    }

    // Set icon
    File file = new File("images/menu/" + button + ".png");
    if(file.exists())
    {
      ImageIcon icon = new ImageIcon(file.getAbsolutePath());
      setIcon(icon);
    }
  }
  
  /**
   * Creates a new <code>ApesMenuItem</code> instance. The menu will
   * then also be able to reach with key, that is assumed to start
   * with <code>control</code>.
   * 
   * @param tag The Language tag.
   * @param key The key to connect to this menu item.
   */
  public ApesMenuItem(String tag, int key)
  {
    this(tag);
    
    // Set keybinding.
    setAccelerator(KeyStroke.getKeyStroke(key, InputEvent.CTRL_DOWN_MASK));
  }

  /**
   * Creates a new <code>ApesMenuItem</code> instance. The menu will
   * then also be able to reach with key.
   * 
   * @param tag The Language tag.
   * @param keyStroke The keystroke as a string.
   */
  public ApesMenuItem(String tag, String keyStroke)
  {
    this(tag);
    
    // Set keybinding.
    setAccelerator(KeyStroke.getKeyStroke(keyStroke));
  }
  
  public void update(Observable o, Object arg)
  {
    setText(language.get(tag));

    this.updateUI();
  }
}
