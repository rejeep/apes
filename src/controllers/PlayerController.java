package apes.controllers;

import javax.swing.JSlider;

import apes.models.Player;
import apes.views.VolumePanel;

/**
 * Controller for the player.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class PlayerController extends ApplicationController
{
  /**
   * Player model.
   */
  private Player player;

  /**
   * Creates a new Player controller.
   */
  public PlayerController()
  {
    this.player = Player.getInstance();
  }

  /**
   * Go backward.
   */
  public void backward()
  {
    player.backward();
  }

  /**
   * Pause playing.
   */
  public void pause()
  {
    player.pause();
  }

  /**
   * Play.
   */
  public void play()
  {
    player.play( player.getPausePosition() );
  }

  /**
   * Stop playing.
   */
  public void stop()
  {
    player.stop();
  }

  /**
   * Forward playing.
   */
  public void forward()
  {
    player.forward();
  }
  
  /**
   * Change volume and update label that shows volume percentage.
   */
  public void volume()
  {
    VolumePanel panel = (VolumePanel) ( (JSlider) event.getSource() ).getParent();
    
    player.setVolume( panel.getVolume() );
    
    panel.updateLabelVolume();
  }
}