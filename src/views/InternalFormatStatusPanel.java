package apes.views;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import apes.controllers.ChannelController;


/**
 * Panel with information about the internal format and some controls
 * for it.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class InternalFormatStatusPanel extends JPanel
{
  /**
   * Text field for the beginning mark.
   */
  private JTextField beginningTextField;

  /**
   * Text field for the end mark.
   */
  private JTextField endTextField;

  /**
   * Text field for the player mark.
   */
  private JTextField playerTextField;

  /**
   * Combo box for the beginning mark.
   */
  private ApesComboBox beginningUnitList;

  /**
   * Combo box for the end mark.
   */
  private ApesComboBox endUnitList;

  /**
   * Combo box for the player mark.
   */
  private ApesComboBox playerUnitList;

  /**
   * Contains as key a {@link InternalFormatStatusPanel#Mark
   * Mark}. And as value a text field.
   *
   * Example: { Mark.END => JTextField }
   *
   * Used to make code more dynamic.
   */
  private Map<Mark, JTextField> textFields;

  /**
   * Contains as key a {@link InternalFormatStatusPanel#Mark
   * Mark}. And as value a combo box.
   *
   * Example: { Mark.END => ApesComboBox }
   *
   * Used to make code more dynamic.
   */
  private Map<Mark, ApesComboBox> unitLists;

  /**
   * The channel controller.
   */
  private ChannelController channelController;
  
  /**
   * Enum with the different marks.
   */
  private enum Mark { BEGINNING, END, PLAYER };
  
  /**
   * Locale tags to all different units.
   */
  private String[] units = { "channel.unit.milliseconds",
                             "channel.unit.seconds",
                             "channel.unit.samples" };

  /**
   * Creates a new <code>InternalFormatStatusPanel</code> instance.
   *
   * @param channelController The channel controller.
   */
  public InternalFormatStatusPanel( ChannelController channelController )
  {
    setLayout( new BorderLayout() );

    textFields = new HashMap<Mark, JTextField>();
    unitLists = new HashMap<Mark, ApesComboBox>();
    
    this.channelController = channelController;

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

    // Beginning
    JLabel beginningLabel = new ApesLabel( "channel.beginning" );
    beginningTextField = new JTextField();
    beginningUnitList = new ApesComboBox( units );

    panel.add( beginningLabel );
    panel.add( beginningTextField );
    panel.add( beginningUnitList );
    textFields.put( Mark.BEGINNING, beginningTextField );
    unitLists.put( Mark.BEGINNING, beginningUnitList );

    // End
    JLabel endLabel = new ApesLabel( "channel.end" );
    endTextField = new JTextField();
    endUnitList = new ApesComboBox( units );

    panel.add( endLabel );
    panel.add( endTextField );
    panel.add( endUnitList );
    textFields.put( Mark.END, endTextField );
    unitLists.put( Mark.END, endUnitList );

    // Player
    JLabel playerLabel = new ApesLabel( "channel.player" );
    playerTextField = new JTextField();
    playerUnitList = new ApesComboBox( units );

    panel.add( playerLabel );
    panel.add( playerTextField );
    panel.add( playerUnitList );
    textFields.put( Mark.PLAYER, playerTextField );
    unitLists.put( Mark.PLAYER, playerUnitList );

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

  /**
   * Returns the value of the beginning mark in pixels.
   *
   * @return The beginning mark in pixels.
   */
  public int getBeginningTextFieldValue()
  {
    return getTextFieldValue( Mark.BEGINNING );
  }

  /**
   * Set the value of the beginning mark.
   *
   * @param pixels The number of pixels on the x-axis.
   */
  public void setBeginningTextFieldValue( int pixels )
  {
    setTextFieldValue( Mark.BEGINNING, pixels );
  }

  /**
   * Returns the value of the end mark in pixels.
   *
   * @return The end mark in pixels.
   */
  public int getEndTextFieldValue()
  {
    return getTextFieldValue( Mark.END );
  }

  /**
   * Set the value of the end mark.
   *
   * @param pixels The number of pixels on the x-axis.
   */
  public void setEndTextFieldValue( int pixels )
  {
    setTextFieldValue( Mark.END, pixels );
  }

  /**
   * Returns the value of the player mark in pixels.
   *
   * @return The player mark in pixels.
   */
  public int getPlayerTextFieldValue()
  {
    return getTextFieldValue( Mark.PLAYER );
  }

  /**
   * Set the value of the player mark.
   *
   * @param pixels The number of pixels on the x-axis.
   */
  public void setPlayerTextFieldValue( int pixels )
  {
    setTextFieldValue( Mark.PLAYER, pixels );
  }
  
  /**
   * Returns the value of <code>mark</code> mark in pixels.
   *
   * TODO: Set pixels to a correct value.
   *
   * @param mark The mark type.
   * @return The <code>mark</code> in pixels.
   */
  private int getTextFieldValue( Mark mark )
  {
    JTextField textField = textFields.get( mark );
    ApesComboBox comboBox = unitLists.get( mark );

    int unit = comboBox.getSelectedIndex();
    int value = -1;
    try
    {
      value = Integer.parseInt( textField.getText() );
    }
    catch( NumberFormatException e ) {}
    
    int pixels = -1;
    
    // Milliseconds
    if( unit == 0 )
    {
      pixels = value;
    }
    // Seconds
    else if( unit == 1 )
    {
      pixels = value;
    }
    // Samples
    else if( unit == 2 )
    {
      pixels = value;
    }

    return pixels;
  }
  
  /**
   * Set the mark for the type <code>mark</code>.
   *
   * @param mark The mark type.
   * @param pixels The number of pixels on the x-axis.
   */
  private void setTextFieldValue( Mark mark, int pixels )
  {
    JTextField textField = textFields.get( mark );
    ApesComboBox comboBox = unitLists.get( mark );

    int unit = comboBox.getSelectedIndex();
    int result = -1;
    
    // Milliseconds
    if( unit == 0 )
    {
      result = pixels;
    }
    // Seconds
    else if( unit == 1 )
    {
      result = pixels;
    }
    // Samples
    else if( unit == 2 )
    {
      result = pixels;
    }
    
    textField.setText( "" + result );
  }

  public void updatePlayer()
  {
    
  }
}