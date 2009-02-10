package apes;

import javax.swing.JFrame;

/**
 * This is where it all starts. This creates a basic GUI with a layout
 * and on it a few components are placed.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class Main extends JFrame
{
  public static void main( String[] args )
  {
    new Main();
  }

  public Main()
  {
    setDefaultCloseOperation( EXIT_ON_CLOSE );

    // To come

    pack();
    setVisible( true );
  }
}