package apes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.undo.UndoManager;

import apes.controllers.HelpController;
import apes.controllers.PlayerController;
import apes.controllers.TagsController;
import apes.controllers.undo.ChangeController;
import apes.controllers.undo.CopyController;
import apes.controllers.undo.CutController;
import apes.controllers.undo.PasteController;
import apes.controllers.undo.RedoController;
import apes.controllers.undo.UndoController;
import apes.lib.Config;
import apes.lib.Language;
import apes.models.InternalFormat;
import apes.models.Player;
import apes.plugins.WaveFileFormat;
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
import apes.views.ApesMenu;
import apes.views.ApesMenuItem;
import apes.views.InternalFormatView;
import apes.views.SamplesView;
import apes.views.VolumePanel;

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
   * Tags controller.
   */
  private TagsController tagsController;

  /**
   * Change controller.
   */
  private ChangeController changeController;

  /**
   * Copy controller.
   */
  private CopyController copyController;

  /**
   * Cut controller.
   */
  private CutController cutController;

  /**
   * Redo controller.
   */
  private RedoController redoController;

  /**
   * Undo controller.
   */
  private UndoController undoController;

  /**
   * Paste controller.
   */
  private PasteController pasteController;

  /**
   * Undo manager (keeps history list)
   */
  private UndoManager undoManager;

  /**
   * Config object.
   */
  private Config config;

  /**
   * The internal format representation.
   */
  private InternalFormat internal = null;

  /**
   * The view of the samples.
   */
  private SamplesView internalFormatView;

  /**
   * Starts the program.
   */
  public Main()
  {
    // Parse the configuration file and set default values.
    config = Config.getInstance();
    config.parse();

    Player player = Player.getInstance();

    // Set some instance variables.
    helpController = new HelpController();
    playerController = new PlayerController();
    tagsController = new TagsController();

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

    if(internal == null)
      internalFormatView = new SamplesView(Player.getInstance(), null, this.getWidth(),300);
    else
      internalFormatView = new SamplesView(Player.getInstance(), internal.getChannel( 0 ), this.getWidth(),300);
    
    // TODO: Temp because holm created a SamplesView???
    InternalFormatView internalFormatViews = new InternalFormatView();

    // Undomanager
    undoManager = new UndoManager();
    undoManager.setLimit( Config.getInstance().getIntOption( "undo" ) );
    
    // "Undo" controllers.
    changeController = new ChangeController( undoManager, internalFormatViews );
    copyController   = new CopyController( undoManager, internalFormatViews );
    cutController    = new CutController( undoManager, internalFormatViews );
    redoController   = new RedoController( undoManager, internalFormatViews );
    undoController   = new UndoController( undoManager, internalFormatViews );
    pasteController  = new PasteController( undoManager, internalFormatViews );

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

    tabs.addTab( "Some file.wav", internalFormatView );

    // Create and bottom top panel.
    JPanel bottomPanel = bottomPanel();
    add( bottomPanel, BorderLayout.SOUTH );

    // Set window dimensions.
    setWindowDimensions();

    setVisible( true );

    // Do something before close
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        // Do before exit
      }
    });
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
    open.addActionListener( new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        WaveFileFormat wav = new WaveFileFormat();
        try
        {
          internal = wav.importFile( ".", "test.wav" );
          Player.getInstance().setInternalFormat( internal );
          internalFormatView.setChannel( internal.getChannel(0));
        } catch ( Exception exception ) { exception.printStackTrace(); };
        internalFormatView.updateView();
      }
    } );
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
    undo.addActionListener( undoController );
    undo.setName( "undo" );
    edit.add( undo );

    JMenuItem redo = new ApesMenuItem( "menu.edit.redo" );
    redo.addActionListener( redoController );
    redo.setName( "redo" );
    edit.add( redo );

    JMenuItem cut = new ApesMenuItem( "menu.edit.cut" );
    cut.addActionListener( cutController );
    cut.setName( "cut" );
    edit.add( cut );

    JMenuItem copy = new ApesMenuItem( "menu.edit.copy" );
    copy.addActionListener( copyController );
    copy.setName( "copy" );
    edit.add( copy );

    JMenuItem paste = new ApesMenuItem( "menu.edit.paste" );
    paste.addActionListener( pasteController );
    paste.setName( "paste" );
    edit.add( paste );

    JMenuItem delete = new ApesMenuItem( "menu.edit.delete" );
    edit.add( delete );

    JMenuItem tags = new ApesMenuItem( "menu.edit.tags" );
    tags.addActionListener( tagsController );
    tags.setName( "edit" );
    edit.add( tags );
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
    undo.addActionListener( undoController );
    undo.setName( "undo" );
    topPanel.add( undo );

    ImageButton redo = new RedoButton();
    redo.addActionListener( redoController );
    redo.setName( "redo" );
    topPanel.add( redo );

    ImageButton copy = new CopyButton();
    copy.addActionListener( copyController );
    copy.setName( "copy" );
    topPanel.add( copy );

    ImageButton cut = new CutButton();
    cut.addActionListener( cutController );
    cut.setName( "cut" );
    topPanel.add( cut );

    ImageButton paste = new PasteButton();
    paste.addActionListener( pasteController );
    paste.setName( "paste" );
    topPanel.add( paste );

    ImageButton delete = new DeleteButton();
    topPanel.add( delete );

    ImageButton zoomIn = new ZoomInButton();
    zoomIn.addActionListener( new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        System.out.println(internalFormatView.getZoom());
        internalFormatView.setZoom( internalFormatView.getZoom()-100000 );
        internalFormatView.updateView();
      }
    } );
    topPanel.add( zoomIn );

    ImageButton zoomOut = new ZoomOutButton();
    zoomOut.addActionListener( new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        internalFormatView.setZoom( internalFormatView.getZoom()+100000 );
        internalFormatView.updateView();
      }
    } );
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

    JPanel volumePanel = new VolumePanel( playerController );
    bottomPanel.add( volumePanel );

    return bottomPanel;
  }

  public static void main( String[] args )
  {
    new Main();
  }
}
