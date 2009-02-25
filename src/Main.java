package apes;

import java.awt.BorderLayout;
import java.awt.Dimension;
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

import apes.controllers.HelpController;
import apes.controllers.PlayerController;
import apes.interfaces.AudioFormatPlugin;
import apes.lib.Config;
import apes.lib.Language;
import apes.models.InternalFormat;
import apes.models.Player;
import apes.plugins.WaveFileFormat;
import apes.views.ApesMenu;
import apes.views.ApesMenuItem;
import apes.views.InternalFormatView;
import apes.views.VolumeSlider;
import apes.views.buttons.BackwardButton;
import apes.views.buttons.CopyButton;
import apes.views.buttons.CutButton;
import apes.views.buttons.DeleteButton;
import apes.views.buttons.ForwardButton;
import apes.views.buttons.FullScreenButton;
import apes.views.buttons.ImageButton;
import apes.views.buttons.OpenButton;
import apes.views.buttons.PasteButton;
import apes.views.buttons.PauseButton;
import apes.views.buttons.PlayButton;
import apes.views.buttons.RecordButton;
import apes.views.buttons.RedoButton;
import apes.views.buttons.SaveButton;
import apes.views.buttons.StopButton;
import apes.views.buttons.UndoButton;
import apes.views.buttons.ZoomInButton;
import apes.views.buttons.ZoomOutButton;
import apes.views.buttons.ZoomResetButton;

/**
 * This is where it all starts. This creates a basic GUI with a layout
 * and on it a few components are placed.
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class Main extends JFrame
{
  /**
   * Help controller.
   */
  private HelpController helpController;

  /**
   * Player controller.
   */
  private PlayerController playerController;

  /**
   * Config object.
   */
  private Config config;

  /**
   * Starts the program.
   */
  public Main()
  {
    // Parse the configuration file and set default values.
    config = Config.getInstance();
    config.parse();

    // Set some instance variables.
    helpController = new HelpController();
    playerController = new PlayerController();

    // Initiate the language with default and then load the
    // dictionary.
    Language.initLanguage();
    try
    {
      Language.load();
    }
    catch ( Exception e )
    {
      e.printStackTrace();
    }

    // Frame options.
    setTitle( Language.get( "help.about.name" ) );
    setDefaultCloseOperation( EXIT_ON_CLOSE );
    setLayout( new BorderLayout() );

    // Create and add menu.
    createMenu();

    // Create and add top panel.
    JPanel topPanel = topPanel();
    add( topPanel, BorderLayout.NORTH );

    JTabbedPane tabs = new JTabbedPane();
    JPanel defaultPanel = new JPanel();
    tabs.addTab( "*Default*", defaultPanel );
    add( tabs, BorderLayout.CENTER );

    InternalFormatView internalFormatView = new InternalFormatView();
    tabs.addTab( "Some file.wav", internalFormatView );

    // Create and bottom top panel.
    JPanel bottomPanel = bottomPanel();
    add( bottomPanel, BorderLayout.SOUTH );

    // Set window dimensions.
    setWindowDimensions();

    setVisible( true );
  }

  /**
   * Sets the dimensions for the window depending on the
   * configuration file.
   */
  private void setWindowDimensions()
  {
    pack();

    boolean maximized = config.getBooleanOption( "maximized" );

    if( maximized )
    {
      setExtendedState( getExtendedState() | MAXIMIZED_BOTH );
    }
    else
    {
      try
      {
        int width = config.getIntOption( "width" );
        int height = config.getIntOption( "height" );

        if( width > 0 && height > 0 )
        {
          setPreferredSize( new Dimension( width, height ) );
        }
      }
      catch( NumberFormatException e ) {}
    }
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
   * Creates a panel and adds some buttons such as save, zoom, undo
   * and redo to it.
   *
   * @return The panel that all components are placed on.
   */
  private JPanel topPanel()
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
   * Creates a panel and adds some components such as a progress bar,
   * buttons: play, pause, stop, etc.. and a volume control.
   *
   * @return The panel that all components are placed on.
   */
  private JPanel bottomPanel()
  {
    JPanel bottomPanel = new JPanel();

    JProgressBar progressBar = new JProgressBar();
    bottomPanel.add( progressBar );

    ImageButton backward = new BackwardButton();
    backward.addActionListener( playerController );
    backward.setName( "backward" );
    bottomPanel.add( backward );

    ImageButton pause = new PauseButton();
    pause.addActionListener( playerController );
    pause.setName( "pause" );
    bottomPanel.add( pause );

    ImageButton play = new PlayButton();
    play.addActionListener( playerController );
    play.setName( "play" );
    bottomPanel.add( play );

    ImageButton stop = new StopButton();
    stop.addActionListener( playerController );
    stop.setName( "stop" );
    bottomPanel.add( stop );

    ImageButton forward = new ForwardButton();
    forward.addActionListener( playerController );
    forward.setName( "forward" );
    bottomPanel.add( forward );

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
