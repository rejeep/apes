package apes.controllers;

import apes.models.KeyBinding;
import apes.views.KeyBindingView;

/**
 * Controller for the key binding view.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class KeyBindingController extends ApplicationController
{
  /**
   * Key binding model.
   */
  private KeyBinding keyBinding;

  /**
   * Key binding view. 
   */
  private KeyBindingView keyBindingView;

  /**
   * Creates a new key binding controller.
   */
  public KeyBindingController()
  {
    this.keyBinding = KeyBinding.getInstance();
    this.keyBinding.parse();
    this.keyBindingView = new KeyBindingView(this, keyBinding);
  }

  /**
   * Pops up the key binding view.
   */
  public void show()
  {
    keyBindingView.create();
  }

  /**
   * Saves the key bindings.
   */
  public void apply()
  {
    keyBinding.setOptions(keyBindingView.getOptions());
    keyBinding.save();
  }

  /**
   * Closes the key binding view.
   */
  public void close()
  {
    keyBindingView.setVisible(false);
    keyBindingView.dispose();
  }
}