/**
 *
 * @author kores
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.JLabel.*;

public class TagsGUI extends JFrame
{

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

//    public String [] newInput;
    public JButton  okey, close;

    public JTextField n, t, ar, al, g, comp, com, copy;
    public String[] textInFields;
    public TagsGUI()
    {
        JFrame f = new JFrame( "J'aime l'information!" );
        f.setSize(300, 250);
        Container content = f.getContentPane();
        content.setBackground( Color.ORANGE );
        content.setLayout( new BorderLayout() );
        /*NORTH*/
        north = new JPanel();
        north.setLayout( new FlowLayout() );
        head = new JLabel( "This is the information" );
        north.add( head );
        /*SOUTH*/
        south = new JPanel();
        south.setLayout( new FlowLayout() );
        okey = new JButton( "Okey" );
        close = new JButton( "Close" );
        close.addActionListener( new ExitHandler() );
        south.add( okey );
        south.add( close );
//add actionlistener for SAVE/OKAY
        n = new JTextField( "Mmmbop" );
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
        content.add( south, BorderLayout.SOUTH );
        content.add( new JPanel(), BorderLayout.WEST );
        content.add( center, BorderLayout.CENTER );
        content.add( new JPanel(), BorderLayout.EAST );
        content.add( north, BorderLayout.NORTH );
        f.setDefaultCloseOperation( EXIT_ON_CLOSE );
        f.setVisible( true );
        pack();

    }
    
    class ExitHandler implements ActionListener
    {
        public void actionPerformed( ActionEvent e ) {
            System.exit( 0 );
        }
    }

}

