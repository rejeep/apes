package apes.views;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import apes.controllers.ChannelController;
import apes.models.Player;
import apes.lib.SampleHelper;


/**
 * TODO: Comment
 *
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
  private String[] units = { "channel.unit.samples",
                             "channel.unit.milliseconds",
                             "channel.unit.seconds",
                             "channel.unit.minutes"};

  /**
   * The different marks.
   */
  private enum Mark { START, STOP, PLAYER };

  /**
   * The sample rate.
   */
  private int sampleRate;

  /**
   * Creates a new <code>InternalFormatStatusPanel</code> instance.
   *
   * @param channelController The channel controller.
   */
  public InternalFormatStatusPanel( int sampleRate, ChannelController channelController, Player player )
  {
    setLayout( new BorderLayout() );

    this.channelController = channelController;
    this.player = player;
    this.sampleRate = sampleRate;
    
    unitMap = new HashMap<Mark, JComboBox>();
    valueMap = new HashMap<Mark, JTextField>();

    JPanel topPanel = topPanel();
    add( topPanel, BorderLayout.NORTH );

    JPanel centerPanel = centerPanel();
    add( centerPanel, BorderLayout.CENTER );

    JPanel bottomPanel = bottomPanel();
    add( bottomPanel, BorderLayout.SOUTH );
  }

  /**
   * Returns a top panel with a header on it.
   *
   * @return The top panel.
   */
  public JPanel topPanel()
  {
    JPanel panel = new JPanel();

    JLabel header = new ApesLabel( "channel.header" );
    header.setFont( new Font( "verdana", 1, 20 ) );
    panel.add( header );

    return panel;
  }

  /**
   * Returns a panel with fields and boxes.
   *
   * @return The center panel.
   */
  public JPanel centerPanel()
  {
    JPanel panel = new JPanel();
    panel.setLayout( new GridLayout( 3, 3 ) );

    // Start
    JLabel startLabel = new ApesLabel( "channel.start" );
    startTextField = new JTextField();
    startUnitList = new ApesComboBox( units );
    unitMap.put( Mark.START, startUnitList );
    valueMap.put( Mark.START, startTextField );

    panel.add( startLabel );
    panel.add( startTextField );
    panel.add( startUnitList );

    // Stop
    JLabel stopLabel = new ApesLabel( "channel.stop" );
    stopTextField = new JTextField();
    stopUnitList = new ApesComboBox( units );
    unitMap.put( Mark.STOP, stopUnitList );
    valueMap.put( Mark.STOP, stopTextField );

    panel.add( stopLabel );
    panel.add( stopTextField );
    panel.add( stopUnitList );

    // Player
    JLabel playerLabel = new ApesLabel( "channel.player" );
    playerTextField = new JTextField();
    playerUnitList = new ApesComboBox( units );
    unitMap.put( Mark.PLAYER, playerUnitList );
    valueMap.put( Mark.PLAYER, playerTextField );

    panel.add( playerLabel );
    panel.add( playerTextField );
    panel.add( playerUnitList );

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

    JButton refresh = new ApesButton( "channel.refresh" );
    refresh.addActionListener( channelController );
    refresh.setName( "refresh" );
    panel.add( refresh );

    return panel;
  }

  public void updatePlayer()
  {
    setStartValue( player.getStart() );
    setStopValue( player.getStop() );
    setPlayerValue( player.getCurrentSample() );
  }

  public int getStartValue()
  {
    return getValue( Mark.START );
  }

  public int getStopValue()
  {
    return getValue( Mark.STOP );
  }

  public int getPlayerValue()
  {
    return getValue( Mark.PLAYER );
  }

  public void setStartValue( int samples )
  {
    setValue( Mark.START, samples );
  }

  public void setStopValue( int samples )
  {
    setValue( Mark.STOP, samples );
  }

  public void setPlayerValue( int samples )
  {
    setValue( Mark.PLAYER, samples );
  }
  
  private int getValue( Mark mark )
  {
    JComboBox comboBox = unitMap.get( mark );
    JTextField textField = valueMap.get( mark );

    int value = Integer.parseInt( textField.getText() );
    
    // Samples
    if( comboBox.getSelectedIndex() == 0 )
    {
      return value;
    }
    // Milliseconds
    else if( comboBox.getSelectedIndex() == 1 )
    {
      return SampleHelper.millisecondsToSamples( sampleRate, value );
    }
    // Seconds
    else if( comboBox.getSelectedIndex() == 2 )
    {
      return SampleHelper.secondsToSamples( sampleRate, value );
    }
    // Minutes
    else if( comboBox.getSelectedIndex() == 3 )
    {
      return SampleHelper.minutesToSamples( sampleRate, value );
    }
    
    return value;
  }

  private void setValue( Mark mark, int samples )
  {
    JComboBox comboBox = unitMap.get( mark );
    JTextField textField = valueMap.get( mark );
    
    int value = Integer.parseInt( textField.getText() );
    
    // Samples
    if( comboBox.getSelectedIndex() == 0 )
    {
      value = samples;
    }
    // Milliseconds
    else if( comboBox.getSelectedIndex() == 1 )
    {
      value = SampleHelper.samplesToMilliseconds( sampleRate, samples );
    }
    // Seconds
    else if( comboBox.getSelectedIndex() == 2 )
    {
      value = SampleHelper.samplesToseconds( sampleRate, samples );
    }
    // Minutes
    else if( comboBox.getSelectedIndex() == 3 )
    {
      value = SampleHelper.samplesToMinutes( sampleRate, samples );
    }
  }
}