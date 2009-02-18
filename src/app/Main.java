package src.app;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;

import src.app.views.ApesMenu;
import src.app.views.ApesMenuItem;
import src.app.views.buttons.BackwardButton;
import src.app.views.buttons.CopyButton;
import src.app.views.buttons.CutButton;
import src.app.views.buttons.DeleteButton;
import src.app.views.buttons.ForwardButton;
import src.app.views.buttons.FullScreenButton;
import src.app.views.buttons.ImageButton;
import src.app.views.buttons.OpenButton;
import src.app.views.buttons.PasteButton;
import src.app.views.buttons.PauseButton;
import src.app.views.buttons.PlayButton;
import src.app.views.buttons.RecordButton;
import src.app.views.buttons.RedoButton;
import src.app.views.buttons.SaveButton;
import src.app.views.buttons.StopButton;
import src.app.views.buttons.UndoButton;
import src.app.views.buttons.ZoomInButton;
import src.app.views.buttons.ZoomOutButton;
import src.app.views.buttons.ZoomResetButton;
import src.lib.Locale;


/**
 * This is where it all starts. This creates a basic GUI with a layout
 * and on it a few components are placed.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class Main extends JFrame
{
  /**
   * Starts the program.
   */
  public Main()
  {
    setTitle( "apes - Audio Program for Editing Sound" );
    setDefaultCloseOperation( EXIT_ON_CLOSE );
    setLayout( new FlowLayout() );

    // Set default locale to en.
    Locale locale = Locale.getInstance();
    locale.setLocale( "en" );
    locale.load();

    // Create and add menu.
    createMenu();

    // Create and add top panel.
    createTopPanel();

    pack();
    setVisible( true );
  }

  /**
   * Creates a menu and adds it to the frame.
   */
  private void createMenu()
  {
    JMenuBar menuBar = new JMenuBar();

    // File START
    JMenu file = new ApesMenu( "menu.head.file" );
    menuBar.add( file );

    JMenuItem open = new ApesMenuItem( "menu.file.open" );
    file.add( open );

    JMenuItem newTab = new ApesMenuItem( "menu.file.new_tab" );
    file.add( newTab );

    JMenuItem closeTab = new ApesMenuItem( "menu.file.close_tab" );
    file.add( closeTab );

    JMenuItem save = new ApesMenuItem( "menu.file.save" );
    file.add( save );

    JMenuItem saveAs = new ApesMenuItem( "menu.file.save_as" );
    file.add( saveAs );

    JMenuItem export = new ApesMenuItem( "menu.file.export" );
    file.add( export );

    JMenuItem quit = new ApesMenuItem( "menu.file.quit" );
    // Exit program is this is clicked.
    quit.addActionListener( new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        System.exit( 0 );
      }
    } );

    file.add( quit );

    // File END

    // Edit START
    JMenu edit = new ApesMenu( "menu.head.edit" );
    menuBar.add( edit );

    JMenuItem undo = new ApesMenuItem( "menu.edit.undo" );
    edit.add( undo );

    JMenuItem redo = new ApesMenuItem( "menu.edit.redo" );
    edit.add( redo );

    JMenuItem cut = new ApesMenuItem( "menu.edit.cut" );
    edit.add( cut );

    JMenuItem copy = new ApesMenuItem( "menu.edit.copy" );
    edit.add( copy );

    JMenuItem paste = new ApesMenuItem( "menu.edit.paste" );
    edit.add( paste );

    JMenuItem delete = new ApesMenuItem( "menu.edit.delete" );
    edit.add( delete );

    // Edit END

    // View START
    JMenu view = new ApesMenu( "menu.head.view" );
    menuBar.add( view );

    JMenu zoom = new ApesMenu( "menu.head.zoom" );
    view.add( zoom );

    JMenuItem zoomIn = new ApesMenuItem( "menu.view.zoom.in" );
    zoom.add( zoomIn );

    JMenuItem zoomOut = new ApesMenuItem( "menu.view.zoom.out" );
    zoom.add( zoomOut );

    JMenuItem zoomReset = new ApesMenuItem( "menu.view.zoom.reset" );
    zoom.add( zoomReset );

    JMenuItem fullScreen = new ApesMenuItem( "menu.view.full_screen" );
    view.add( fullScreen );

    // View END

    // Player START
    JMenu player = new ApesMenu( "menu.head.player" );
    menuBar.add( player );

    JMenuItem play = new ApesMenuItem( "menu.player.play" );
    player.add( play );

    JMenuItem pause = new ApesMenuItem( "menu.player.pause" );
    player.add( pause );

    JMenuItem stop = new ApesMenuItem( "menu.player.stop" );
    player.add( stop );

    JMenuItem forward = new ApesMenuItem( "menu.player.forward" );
    player.add( forward );

    JMenuItem backward = new ApesMenuItem( "menu.player.backward" );
    player.add( backward );

    JMenuItem record = new ApesMenuItem( "menu.player.record" );
    player.add( record );

    // Player END

    // Tools START
    JMenu tools = new ApesMenu( "menu.head.tools" );
    menuBar.add( tools );

    JMenuItem properties = new ApesMenuItem( "menu.tools.properties" );
    tools.add( properties );

    // Tools END

    // Help START
    JMenu help = new ApesMenu( "menu.head.help" );
    menuBar.add( help );

    JMenuItem manual = new ApesMenuItem( "menu.help.manual" );
    help.add( manual );

    JMenuItem bug = new ApesMenuItem( "menu.help.bug" );
    help.add( bug );

    JMenuItem about = new ApesMenuItem( "menu.help.about" );
    help.add( about );
    // Help End
    setJMenuBar( menuBar );
  }

  /**
   * Creates and adds a panel on the top of the frame. The panel
   * contains buttons such as save, zoom, undo and redo.
   */
  private void createTopPanel()
  {
    JPanel topPanel = new JPanel();
    add( topPanel );

    ImageButton open = new OpenButton();
    topPanel.add( open );

    ImageButton save = new SaveButton();
    topPanel.add( save );

    ImageButton undo = new UndoButton();
    topPanel.add( undo );

    ImageButton redo = new RedoButton();
    topPanel.add( redo );

    ImageButton copy = new CopyButton();
    topPanel.add( copy );

    ImageButton cut = new CutButton();
    topPanel.add( cut );

    ImageButton paste = new PasteButton();
    topPanel.add( paste );

    ImageButton delete = new DeleteButton();
    topPanel.add( delete );

    ImageButton zoomIn = new ZoomInButton();
    topPanel.add( zoomIn );

    ImageButton zoomOut = new ZoomOutButton();
    topPanel.add( zoomOut );

    ImageButton zoomReset = new ZoomResetButton();
    topPanel.add( zoomReset );

    ImageButton fullScreen = new FullScreenButton();
    topPanel.add( fullScreen );
  }

  public static void main( String[] args )
  {
    new Main();
  }
}
