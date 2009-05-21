package apes.views;

import javax.swing.JSlider;

import apes.lib.Config;
import apes.lib.PlayerHandler;

/**
 * Slider that changes the volume.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class VolumeSlider extends JSlider
{
  /**
   * Creates a new <code>VolumeSlider</code> instance.
   */
  public VolumeSlider()
  {
    // Min and max values for volume.
    setMinimum(PlayerHandler.MIN_VALUE);
    setMaximum(PlayerHandler.MAX_VALUE);

    // Set start value.
    Config config = Config.getInstance();
    setValue(config.getIntOption("volume"));
  }
}
