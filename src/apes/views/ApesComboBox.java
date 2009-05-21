package apes.views;

import java.util.Observable;

import javax.swing.JComboBox;

import apes.interfaces.LanguageObserver;
import apes.lib.Language;

/**
 * Like JComboBox except that it takes an array of locale tags as
 * argument instead of the text.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ApesComboBox extends JComboBox implements LanguageObserver
{
  /**
   * A language object.
   */
  private Language language;

  /**
   * A list of all items (locale tags).
   */
  private Object[] items;

  /**
   * Creates a new <code>ApesPanel</code> instance.
   * 
   * @param items The list of locale tags.
   */
  public ApesComboBox(Object[] items)
  {
    this.language = Language.getInstance();
    this.items = items;

    addLanguageItems();

    language.addObserver(this);
  }

  public void update(Observable o, Object arg)
  {
    removeAllItems();
    addLanguageItems();

    this.updateUI();
  }

  /**
   * Adds all items fetched from the current locale.
   */
  private void addLanguageItems()
  {
    for(int i = 0; i < items.length; i++)
    {
      addItem(language.get((String)items[i]));
    }
  }
}
