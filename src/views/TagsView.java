package apes.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import apes.controllers.TagsController;
import apes.lib.Language;
import apes.models.Tags;

/**
 * TODO: Comment this
 */
public class TagsView extends JFrame
{
  /**
   *
   */
  private Language language;

  /**
   *
   */
  private JPanel north;

  /**
   *
   */
  private JPanel south;

  /**
   *
   */
  private JPanel center;

  /**
   *
   */
  private JLabel head;

  /**
   *
   */
  private JLabel name;

  /**
   *
   */
  private JLabel track;

  /**
   *
   */
  private JLabel artist;

  /**
   *
   */
  private JLabel album;

  /**
   *
   */
  private JLabel genre;

  /**
   *
   */
  private JLabel composer;

  /**
   *
   */
  private JLabel comments;

  /**
   *
   */
  private JLabel copyright;

  /**
   *
   */
  private JButton close;

  /**
   *
   */
  private JTextField n;

  /**
   *
   */
  private JTextField t;

  /**
   *
   */
  private JTextField ar;

  /**
   *
   */
  private JTextField al;

  /**
   *
   */
  private JTextField g;

  /**
   *
   */
  private JTextField comp;

  /**
   *
   */
  private JTextField com;

  /**
   *
   */
  private JTextField copy;

  /**
   *
   */
  public TagsView( TagsController tagsController, Tags tags )
  {
    this.language = Language.getInstance();
    
    setLayout( new BorderLayout() );
    /*NORTH*/
    north = new JPanel();
    north.setLayout( new FlowLayout() );
    head = new JLabel( "This is the information" );
    north.add( head );
    /*SOUTH*/
    south = new JPanel();
    south.setLayout( new FlowLayout() );
    JButton save = new JButton( "Save" );
    save.addActionListener( tagsController );
    save.setName( "save" );
    south.add(save);
    close = new JButton( "Close" );
    close.addActionListener( tagsController );
    close.setName( "close" );
    south.add( close );

    n = new JTextField( tags.get( "name" ) );
    t = new JTextField( tags.get( "track" ) );
    ar = new JTextField( tags.get( "artist" ) );
    al = new JTextField( tags.get( "album" ) );
    g = new JTextField( tags.get( "genre" ) );
    comp = new JTextField( tags.get( "composer" ) );
    com = new JTextField( tags.get( "comments" ) );
    copy = new JTextField( tags.get( "copyright" ) );

    name = new JLabel( language.get( "tags.name" ) );
    track = new JLabel( language.get( "tags.track" ) );
    artist = new JLabel( language.get( "tags.artist" ) );
    album = new JLabel( language.get( "tags.album" ) );
    genre = new JLabel( language.get( "tags.genre" ) );
    composer = new JLabel( language.get( "tags.composer" ) );
    comments = new JLabel( language.get( "tags.comments" ) );
    copyright = new JLabel( language.get( "tags.copyright" ) );

    center = new JPanel();
    center.setBackground( Color.GRAY );
    center.setLayout( new GridLayout( 8,2 ) );
    center.setPreferredSize( new Dimension( 200, 170 ) );
    center.add( name );
    center.add( n );
    center.add( track );
    center.add( t );
    center.add( artist );
    center.add( ar );
    center.add( album );
    center.add( al );
    center.add( genre );
    center.add( g );
    center.add( composer );
    center.add( comp );
    center.add( comments );
    center.add( com );
    center.add( copyright );
    center.add( copy );
    /*PLACING*/
    add( south, BorderLayout.SOUTH );
    add( new JPanel(), BorderLayout.WEST );
    add( center, BorderLayout.CENTER );
    add( new JPanel(), BorderLayout.EAST );
    add( north, BorderLayout.NORTH );

    setDefaultCloseOperation( EXIT_ON_CLOSE );
    setVisible( true );
    pack();
  }

  /**
   * Describe <code>getName</code> method here.
   *
   * @return a <code>String</code> value
   */
  public String getName()
  {
    return n.getText();

  }

  /**
   * Describe <code>getTrack</code> method here.
   *
   * @return a <code>String</code> value
   */
  public String getTrack()
  {
    return t.getText();

  }

  /**
   * Describe <code>getArtist</code> method here.
   *
   * @return a <code>String</code> value
   */
  public String getArtist()
  {
    return ar.getText();

  }

  /**
   * Describe <code>getAlbum</code> method here.
   *
   * @return a <code>String</code> value
   */
  public String getAlbum()
  {
    return al.getText();

  }

  /**
   * Describe <code>getGenre</code> method here.
   *
   * @return a <code>String</code> value
   */
  public String getGenre()
  {
    return g.getText();

  }

  /**
   * Describe <code>getComposer</code> method here.
   *
   * @return a <code>String</code> value
   */
  public String getComposer()
  {
    return comp.getText();

  }

  /**
   * Describe <code>getComments</code> method here.
   *
   * @return a <code>String</code> value
   */
  public String getComments()
  {
    return com.getText();

  }

  /**
   * Describe <code>getCopyright</code> method here.
   *
   * @return a <code>String</code> value
   */
  public String getCopyright()
  {
    return copy.getText();

  }
}
