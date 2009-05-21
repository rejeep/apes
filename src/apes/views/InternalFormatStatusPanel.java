package apes.views;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import apes.controllers.ChannelController;
import apes.lib.SampleHelper;
import apes.models.InternalFormat;
import apes.models.Player;

/**
 * Panel with information about the internal format and some controls
 * for it.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class InternalFormatStatusPanel extends JPanel
{
  /**
   * Text field for the start mark.
   */
  private JTextField startTextField;

  /**
   * Text field for the stop mark.
   */
  private JTextField stopTextField;

  /**
   * Text field for the player mark.
   */
  private JTextField playerTextField;

  /**
   * Combo box for the start mark.
   */
  private ApesComboBox startUnitList;

  /**
   * Combo box for the stop mark.
   */
  private ApesComboBox stopUnitList;

  /**
   * Combo box for the player mark.
   */
  private ApesComboBox playerUnitList;

  /**
   * The channel controller.
   */
  private ChannelController channelController;

  /**
   * The player.
   */
  private Player player;

  /**
   * Contains a mark as key and a combo box as value. Used to be able
   * to write more dynamic code.
   */
  private Map<Mark, JComboBox> unitMap;

  /**
   * Contains a mark as key and a text field as value. Used to be able
   * to write more dynamic code.
   */
  private Map<Mark, JTextField> valueMap;

  /**
   * Locale tags to all different units.
   */
  private String[] units = { "channel.unit.samples", "channel.unit.milliseconds", "channel.unit.seconds", "channel.unit.minutes" };

  /**
   * The different marks.
   */
  private enum Mark {
    START, STOP, PLAYER
  };

  /**
   * The sample rate.
   */
  private int sampleRate;

  /**
   * The internal format.
   */
  private InternalFormat internalFormat;

  /**
   * Creates a new <code>InternalFormatStatusPanel</code> instance.
   * 
   * @param channelController The channel controller.
   */
  public InternalFormatStatusPanel(InternalFormat internalFormat, ChannelController channelController, Player player)
  {
    setLayout(new BorderLayout());

    this.channelController = channelController;
    this.player = player;
    this.internalFormat = internalFormat;
    this.sampleRate = internalFormat.getSampleRate();

    unitMap = new HashMap<Mark, JComboBox>();
    valueMap = new HashMap<Mark, JTextField>();

    JPanel topPanel = topPanel();
    add(topPanel, BorderLayout.NORTH);

    JPanel centerPanel = centerPanel();
    add(centerPanel, BorderLayout.CENTER);

    JPanel bottomPanel = bottomPanel();
    add(bottomPanel, BorderLayout.SOUTH);
  }

  /**
   * Returns a top panel with a header and status information on it.
   * 
   * @return The top panel.
   */
  public JPanel topPanel()
  {
    // How many items there are to add.
    int amount = 5;

    JPanel wrapper = new JPanel();
    wrapper.setLayout(new BorderLayout());

    JPanel top = new JPanel();

    JPanel status = new JPanel();
    status.setLayout(new GridLayout(amount, 2));
    status.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

    JLabel header = new JLabel(internalFormat.getFileStatus().getFileName());
    header.setFont(new Font("verdana", 1, 20));
    top.add(header);

    String[] labels = { "sample_rate", "num_channels", "num_samples", "bytes_per_sample", "bits_per_sample" };
    int[] values = { internalFormat.getSampleRate(), internalFormat.getNumChannels(), internalFormat.getSampleAmount(), internalFormat.bytesPerSample, internalFormat.bitsPerSample };

    // Add all labels and values.
    for(int i = 0; i < labels.length; i++)
    {
      JLabel label = new ApesLabel("channel.information." + labels[i]);
      label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

      JLabel value = new JLabel("" + values[i]);

      status.add(label);
      status.add(value);
    }

    wrapper.add(top, BorderLayout.NORTH);
    wrapper.add(status, BorderLayout.CENTER);

    return wrapper;
  }

  /**
   * Returns a panel with fields and boxes.
   * 
   * @return The center panel.
   */
  public JPanel centerPanel()
  {
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(3, 3));

    // Start
    JLabel startLabel = new ApesLabel("channel.start");
    startTextField = new JTextField();
    startUnitList = new ApesComboBox(units);
    unitMap.put(Mark.START, startUnitList);
    valueMap.put(Mark.START, startTextField);

    panel.add(startLabel);
    panel.add(startTextField);
    panel.add(startUnitList);

    // Stop
    JLabel stopLabel = new ApesLabel("channel.stop");
    stopTextField = new JTextField();
    stopUnitList = new ApesComboBox(units);
    unitMap.put(Mark.STOP, stopUnitList);
    valueMap.put(Mark.STOP, stopTextField);

    panel.add(stopLabel);
    panel.add(stopTextField);
    panel.add(stopUnitList);

    // Player
    JLabel playerLabel = new ApesLabel("channel.player");
    playerTextField = new JTextField();
    playerUnitList = new ApesComboBox(units);
    unitMap.put(Mark.PLAYER, playerUnitList);
    valueMap.put(Mark.PLAYER, playerTextField);

    panel.add(playerLabel);
    panel.add(playerTextField);
    panel.add(playerUnitList);

    return panel;
  }

  /**
   * Returns a panel with a refresh button on it.
   * 
   * @return The bottom panel.
   */
  public JPanel bottomPanel()
  {
    JPanel panel = new JPanel();

    JButton refresh = new ApesButton("channel.refresh");
    refresh.addActionListener(channelController);
    refresh.setName("refresh");
    panel.add(refresh);

    return panel;
  }

  /**
   * I called when something has been changed in the player. This then
   * updates the fields.
   */
  public void updatePlayer()
  {
    setStartValue(player.getStart());
    setStopValue(player.getStop());
    setPlayerValue(player.getCurrentSample());
  }

  /**
   * Returns the start value in samples.
   * 
   * @return The start value.
   */
  public int getStartValue()
  {
    return getValue(Mark.START);
  }

  /**
   * Sets the start value.
   * 
   * @param samples The start value.
   */
  public void setStartValue(int samples)
  {
    setValue(Mark.START, samples);
  }

  /**
   * Returns the stop value in samples.
   * 
   * @return The stop value.
   */
  public int getStopValue()
  {
    return getValue(Mark.STOP);
  }

  /**
   * Sets the stop value.
   * 
   * @param samples The stop value.
   */
  public void setStopValue(int samples)
  {
    setValue(Mark.STOP, samples);
  }

  /**
   * Returns the player value in samples.
   * 
   * @return The player value.
   */
  public int getPlayerValue()
  {
    return getValue(Mark.PLAYER);
  }

  /**
   * Sets the player value.
   * 
   * @param samples The player value.
   */
  public void setPlayerValue(int samples)
  {
    setValue(Mark.PLAYER, samples);
  }

  /**
   * Generic helper for getting a value.
   * 
   * @param mark The mark.
   * @return The value for mark in samples.
   */
  private int getValue(Mark mark)
  {
    JTextField textField = valueMap.get(mark);
    int value = getTextFieldValue(textField);

    if(isSamples(mark))
    {
      return value;
    }
    else if(isMilliseconds(mark))
    {
      return SampleHelper.millisecondsToSamples(sampleRate, value);
    }
    else if(isSeconds(mark))
    {
      return SampleHelper.secondsToSamples(sampleRate, value);
    }
    else if(isMinutes(mark))
    {
      return SampleHelper.minutesToSamples(sampleRate, value);
    }

    return value;
  }

  /**
   * Generic helper for setting a value.
   * 
   * @param mark The mark.
   * @param samples The new value for mark.
   */
  private void setValue(Mark mark, int samples)
  {
    JTextField textField = valueMap.get(mark);
    int value = getTextFieldValue(textField);

    if(isSamples(mark))
    {
      value = samples;
    }
    else if(isMilliseconds(mark))
    {
      value = SampleHelper.samplesToMilliseconds(sampleRate, samples);
    }
    else if(isSeconds(mark))
    {
      value = SampleHelper.samplesToSeconds(sampleRate, samples);
    }
    else if(isMinutes(mark))
    {
      value = SampleHelper.samplesToMinutes(sampleRate, samples);
    }

    textField.setText("" + value);
  }

  /**
   * Returns true if <code>mark</code> is in samples. False
   * otherwise.
   * 
   * @param mark The mark.
   * @return True if in samples. False otherwise.
   */
  private boolean isSamples(Mark mark)
  {
    return isUnit(mark, 0);
  }

  /**
   * Returns true if <code>mark</code> is in milliseconds. False
   * otherwise.
   * 
   * @param mark The mark.
   * @return True if in milliseconds. False otherwise.
   */
  private boolean isMilliseconds(Mark mark)
  {
    return isUnit(mark, 1);
  }

  /**
   * Returns true if <code>mark</code> is in seconds. False
   * otherwise.
   * 
   * @param mark The mark.
   * @return True if in seconds. False otherwise.
   */
  private boolean isSeconds(Mark mark)
  {
    return isUnit(mark, 2);
  }

  /**
   * Returns true if <code>mark</code> is in minutes. False
   * otherwise.
   * 
   * @param mark The mark.
   * @return True if in minutes. False otherwise.
   */
  private boolean isMinutes(Mark mark)
  {
    return isUnit(mark, 3);
  }

  /**
   * Generic helper for checking what unit is selected for a mark.
   * 
   * @param mark The mark.
   * @param index The combo box index.
   * @return True if the combo box index is the same as
   *         <code>index</code>. False otherwise.
   */
  private boolean isUnit(Mark mark, int index)
  {
    return unitMap.get(mark).getSelectedIndex() == index;
  }

  /**
   * Fetches the value from <code>textField</code>. If a valid
   * number, that is returned. Otherwise zero is returned.
   * 
   * @param textField The text field.
   * @return The text field value, or zero if not valid.
   */
  private int getTextFieldValue(JTextField textField)
  {
    int value = 0;

    try
    {
      value = Integer.parseInt(textField.getText());
    }
    catch(NumberFormatException e)
    {}

    return value;
  }
}
