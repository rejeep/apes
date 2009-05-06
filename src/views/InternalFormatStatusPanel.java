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
 * TODO: Comment and fix small stuff.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class InternalFormatStatusPanel extends JPanel
{
  /**
   *
   */
  private JTextField beginningTextField;

  /**
   *
   */
  private JTextField endTextField;

  /**
   *
   */
  private JTextField playerTextField;

  /**
   *
   */
  private ApesComboBox beginningUnitList;

  /**
   *
   */
  private ApesComboBox endUnitList;

  /**
   *
   */
  private ApesComboBox playerUnitList;

  /**
   *
   */
  private Map<Mark, JTextField> textFields;

  /**
   *
   */
  private Map<Mark, ApesComboBox> unitLists;

  /**
   * 
   */
  private ChannelController channelController;
  
  /**
   *
   */
  private enum Mark { BEGINNING, END, PLAYER };
  
  /**
   *
   */
  private String[] units = { "channel.unit.milliseconds",
                             "channel.unit.seconds",
                             "channel.unit.samples" };

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

  public JPanel topPanel()
  {
    JPanel panel = new JPanel();

    JLabel header = new ApesLabel( "channel.header" );
    header.setFont( new Font( "verdana", 1, 20 ) );
    panel.add( header );

    return panel;
  }
  
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

  public JPanel bottomPanel()
  {
    JPanel panel = new JPanel();
    
    JButton refresh = new ApesButton( "channel.refresh" );
    refresh.addActionListener( channelController );
    refresh.setName( "refresh" );
    panel.add( refresh );

    return panel;
  }

  public int getBeginningTextFieldValue()
  {
    return getTextFieldValue( Mark.BEGINNING );
  }

  public void setBeginningTextFieldValue( int pixels )
  {
    setTextFieldValue( Mark.BEGINNING, pixels );
  }

  public int getEndTextFieldValue()
  {
    return getTextFieldValue( Mark.END );
  }

  public void setEndTextFieldValue( int pixels )
  {
    setTextFieldValue( Mark.END, pixels );
  }

  public int getPlayerTextFieldValue()
  {
    return getTextFieldValue( Mark.PLAYER );
  }

  public void setPlayerTextFieldValue( int pixels )
  {
    setTextFieldValue( Mark.PLAYER, pixels );
  }

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
      pixels = 6000;
    }
    // Seconds
    else if( unit == 1 )
    {
      pixels = 6;
    }
    // Samples
    else if( unit == 2 )
    {
      pixels = 400;
    }

    return pixels;
  }
  
  private void setTextFieldValue( Mark mark, int pixels )
  {
    JTextField textField = textFields.get( mark );
    ApesComboBox comboBox = unitLists.get( mark );

    int unit = comboBox.getSelectedIndex();
    int result = -1;
    
    // Milliseconds
    if( unit == 0 )
    {
      result = 6000;
    }
    // Seconds
    else if( unit == 1 )
    {
      result = 6;
    }
    // Samples
    else if( unit == 2 )
    {
      result = 400;
    }
    
    textField.setText( "" + result );
  }
}