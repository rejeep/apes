package apes.views;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import apes.controllers.PlayerController;
import apes.lib.PlayerHandler;
import apes.models.Config;

/**
 * <p>
 * This is a panel that volume components are placed on.
 * </p>
 * <p>
 * The label can be configured with the option volume_label_format.
 * The value can be anything, but these replacements will be done:
 * </p>
 * <ul>
 * <li>%v - The current volume</li>
 * <li>%m - The minimum volume</li>
 * <li>%M - The maximum volume</li>
 * </ul>
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class VolumePanel extends JPanel
{
  /**
   * The player controller.
   */
  private PlayerController playerController;

  /**
   * The label that the volume percentage is shown on.
   */
  private JLabel label;

  /**
   * The slider that changes the volume.
   */
  private JSlider slider;

  /**
   * The label format.
   */
  private String volumeLabelFormat;

  /**
   * Creates a new <code>VolumePanel</code>.
   * 
   * @param playerController the player controller.
   */
  public VolumePanel(PlayerController playerController)
  {
    // Set the label format.
    Config config = Config.getInstance();
    volumeLabelFormat = config.getOption("volume_label_format");

    // This can be set here since these values never change when the
    // program is running.
    volumeLabelFormat = substituteLabel("m", PlayerHandler.MIN_VALUE);
    volumeLabelFormat = substituteLabel("M", PlayerHandler.MAX_VALUE);

    this.playerController = playerController;

    // Add components.
    setLayout(new GridLayout(1, 2));

    setSlider();
    setLabel();

    add(slider);
    add(label);
  }

  /**
   * Create and set volume slider.
   */
  private void setSlider()
  {
    slider = new VolumeSlider();
    slider.addChangeListener(playerController);
    slider.setName("volume");
  }

  /**
   * Create and set volume label.
   */
  private void setLabel()
  {
    label = new JLabel();

    updateLabelVolume();
  }

  /**
   * Update label to the slider value.
   */
  public void updateLabelVolume()
  {
    label.setText(substituteLabel("v", getVolume()));
  }

  /**
   * Returns the slider volume.
   * 
   * @return the slider volume.
   */
  public int getVolume()
  {
    return slider.getValue();
  }

  /**
   * Returns <code>volumeLabelFormat</code> but with
   * <code>target</code> substituted with <code>replacement</code>.
   * 
   * @param target the target value (%x).
   * @param replacement the replacement.
   * @return <code>volumeLabelFormat</code> substituted.
   */
  private String substituteLabel(String target, int replacement)
  {
    return substituteLabel(target, new Integer(replacement).toString());
  }

  /**
   * Returns <code>volumeLabelFormat</code> but with
   * <code>target</code> substituted with <code>replacement</code>.
   * 
   * @param target the target value (%x).
   * @param replacement the replacement.
   * @return <code>volumeLabelFormat</code> substituted.
   */
  private String substituteLabel(String target, String replacement)
  {
    return volumeLabelFormat.replaceAll("%" + target, replacement);
  }
}
