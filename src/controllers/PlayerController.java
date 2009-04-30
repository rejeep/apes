package apes.controllers;

import javax.swing.JSlider;

import apes.lib.PlayerHandler;
import apes.views.VolumePanel;

/**
 * Controller for the player.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class PlayerController extends ApplicationController
{
  /**
   * The player handler.
   */
  private PlayerHandler playerHandler;

  /**
   * Creates a new Player controller.
   */
  public PlayerController( PlayerHandler playerHandler )
  {
    this.playerHandler = playerHandler;
  }

  /**
   * Go backward.
   */
  public void backward()
  {
    
  }
  
  /**
   * Forward playing.
   */
  public void forward()
  {
    
  }

  /**
   * Pause playing.
   */
  public void pause()
  {
    playerHandler.pause();
  }

  /**
   * Play.
   */
  public void play()
  {
    playerHandler.play();
  }

  /**
   * Stop playing.
   */
  public void stop()
  {
    playerHandler.stop();
  }
  
  /**
   * Change volume and update label that shows volume percentage.
   */
  public void volume()
  {
    VolumePanel panel = (VolumePanel) ( (JSlider) event.getSource() ).getParent();
    
    playerHandler.setVolume( panel.getVolume() );
    
    panel.updateLabelVolume();
  }
}