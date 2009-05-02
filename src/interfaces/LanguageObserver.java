package apes.interfaces;

/**
 * Observer used for observing Language
 *
 * @author Simon Holm
 */
public interface LanguageObserver
{
  /**
   * Called when the Language changes language.
   */
  public void update();
}
