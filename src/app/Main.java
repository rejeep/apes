package src.app;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;

import src.app.controllers.HelpController;
import src.app.views.ApesMenu;
import src.app.views.ApesMenuItem;
import src.app.views.VolumeSlider;
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
import src.lib.Config;
import src.lib.Locale;

/**
 * This is where it all starts. This creates a basic GUI with a layout
 * and on it a few components are placed.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class Main extends JFrame
{
  private HelpController helpController;

  /**
   * Starts the program.
   */
  public Main()
  {
    // Parse the configuration file and set default values.
    Config config = Config.getInstance();
    config.parse();

    // Set some instance variables.
    helpController = new HelpController();

    // Frame options.
    setTitle( "apes - Audio Program for Editing Sound" );
    setDefaultCloseOperation( EXIT_ON_CLOSE );
    setLayout( new BorderLayout() );

    // Set default locale to en.
    Locale locale = Locale.getInstance();
    locale.setLocale( "en" );
    locale.load();

    // Create and add menu.
    createMenu();

    // Create and add top panel.
    JPanel topPanel = createTopPanel();
    add( topPanel, BorderLayout.NORTH );

    JTabbedPane tabs = new JTabbedPane();
    JPanel defaultPanel = new JPanel();
    tabs.addTab( "*Default*", defaultPanel );
    add( tabs, BorderLayout.CENTER );

    // Create and bottom top panel.
    JPanel bottomPanel = createBottomPanel();
    add( bottomPanel, BorderLayout.SOUTH );

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
    about.addActionListener( helpController );
    about.setName( "about" );
    help.add( about );
    // Help END

    setJMenuBar( menuBar );
  }

  /**
   * Creates and adds a panel on the top of the frame. The panel
   * contains buttons such as save, zoom, undo and redo.
   *
   * @return The panel that all components are placed on.
   */
  private JPanel createTopPanel()
  {
    JPanel topPanel = new JPanel();

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

    return topPanel;
  }

  /**
   * Creates and adds a panel on the bottom of the frame. The panel
   * contains a progress bar, buttons such as play, pause, stop,
   * etc.. and a volume control.
   *
   * @return The panel that all components are placed on.
   */
  private JPanel createBottomPanel()
  {
    JPanel bottomPanel = new JPanel();

    JProgressBar progressBar = new JProgressBar();
    bottomPanel.add( progressBar );

    ImageButton play = new PlayButton();
    bottomPanel.add( play );

    ImageButton pause = new PauseButton();
    bottomPanel.add( pause );

    ImageButton stop = new StopButton();
    bottomPanel.add( stop );

    ImageButton forward = new ForwardButton();
    bottomPanel.add( forward );

    ImageButton backward = new BackwardButton();
    bottomPanel.add( backward );

    ImageButton record = new RecordButton();
    bottomPanel.add( record );

    JSlider volume = new VolumeSlider();;
    bottomPanel.add( volume );

    return bottomPanel;
  }

  public static void main( String[] args )
  {
    new Main();
  }
}
