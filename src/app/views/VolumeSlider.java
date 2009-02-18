package src.app.views;

import javax.swing.JSlider;

import src.lib.Config;

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
    setMinimum( 0 );
    setMaximum( 100 );

    // Set start value.
    Config config = Config.getInstance();
    setValue( Integer.parseInt( config.getOption( "volume" ) ) );
  }
}