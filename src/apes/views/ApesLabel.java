package apes.views;

import java.util.Observable;

import javax.swing.JLabel;

import apes.interfaces.LanguageObserver;
import apes.lib.Language;

/**
 * Like JLabel except that it takes locale tag as argument instead of
 * the text.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ApesLabel extends JLabel implements LanguageObserver
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
   * Creates a new <code>ApesPanel</code> instance.
   * 
   * @param tag The Language tag.
   */
  public ApesLabel(String tag)
  {
    this.language = Language.getInstance();
    this.tag = tag;

    setText(language.get(tag));
    language.addObserver(this);
  }

  public void update(Observable o, Object arg)
  {
    setText(language.get(tag));

    this.updateUI();
  }
}
