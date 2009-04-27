package apes.views;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.JLabel.*;
import javax.swing.JFrame;
import apes.models.Tags;
import javax.swing.JButton;
import apes.controllers.TagsController;
import apes.lib.Language;

/**
 *
 */
public class TagsView extends JFrame
{
  /**
   *
   */
  private Tags tags;
  
  /**
   * Tags controller.
   */
  private TagsController tagsController;

  /**
   *
   */
  
  
        // Present current Tags object.
        public JPanel   north,
                        south,
                        center,
                        east,
                        west;

        public JLabel   head,
                        name,
                        track,
                        artist,
                        album,
                        genre,
                        composer,
                        comments,
                        copyright;

        public JButton  okey, close;

        public JTextField n, t, ar, al, g, comp, com, copy;
        public String[] textInFields;

    public TagsView( TagsController tagsController, Tags tags )
    {
        this.tagsController = tagsController;
        this.tags = tags;
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

        name = new JLabel( Language.get( "tags.name" ) );
        track = new JLabel( Language.get( "tags.track" ) );
        artist = new JLabel( Language.get( "tags.artist" ) );
        album = new JLabel( Language.get( "tags.album" ) );
        genre = new JLabel( Language.get( "tags.genre" ) );
        composer = new JLabel( Language.get( "tags.composer" ) );
        comments = new JLabel( Language.get( "tags.comments" ) );
        copyright = new JLabel( Language.get( "tags.copyright" ) );

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
    public String getName()
    {
        return n.getText();

    }
    public String getTrack()
    {
        return t.getText();

    }
    public String getArtist()
    {
        return ar.getText();

    }
    public String getAlbum()
    {
        return al.getText();

    }
    public String getGenre()
    {
        return g.getText();

    }
    public String getComposer()
    {
        return comp.getText();

    }
    public String getComments()
    {
        return com.getText();

    }
    public String getCopyright()
    {
        return copy.getText();

    }
}
