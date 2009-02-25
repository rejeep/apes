package apes.controllers;

import apes.models.Player;
import javax.swing.JSlider;

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
    long pausePosition = player.getPausePosition();
    
    if( pausePosition == 0L )
    {
      player.play();
    }
    else
    {
      player.play( pausePosition );
    }
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
   * Change volume.
   */
  public void volume()
  {
    JSlider slider = (JSlider) event.getSource();
    
    player.setVolume( slider.getValue() );
  }
}