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

        System.out.println(tags);
        System.out.println(tags.get("name"));
        
        south.add( close );
        n = new JTextField( "" );
        t = new JTextField( "4" );
        ar = new JTextField( "Hanson" );
        al = new JTextField( "Middle of Nowhere" );
        g = new JTextField( "Pop" );
        comp = new JTextField( "Z. Hanson" );
        com = new JTextField( "small kids" );
        copy = new JTextField( "Hanson" );

        name = new JLabel( "Name" );
        track = new JLabel( "Track" );
        artist = new JLabel( "Artist" );
        album = new JLabel( "Album" );
        genre = new JLabel( "Genre" );
        composer = new JLabel( "Composer" );
        comments = new JLabel( "Comments" );
        copyright = new JLabel( "Copyright" );

        center = new JPanel();
        center.setBackground( Color.GRAY );
        center.setLayout( new GridLayout( 8,2 ) );
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
