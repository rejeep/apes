package apes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.undo.UndoManager;

import apes.controllers.ConfigController;
import apes.controllers.HelpController;
import apes.controllers.InternalFormatController;
import apes.controllers.LanguageController;
import apes.controllers.PlayerController;
import apes.controllers.PluginController;
import apes.controllers.EffectController;
import apes.controllers.TabsController;
import apes.controllers.TagsController;
import apes.exceptions.UnidentifiedLanguageException;
import apes.lib.ApesFile;
import apes.lib.Config;
import apes.lib.Language;
import apes.lib.PlayerHandler;
import apes.lib.PluginHandler;
import apes.views.ApesError;
import apes.views.ApesMenu;
import apes.views.ApesMenuItem;
import apes.views.VolumePanel;
import apes.views.buttons.BackwardButton;
import apes.views.buttons.CopyButton;
import apes.views.buttons.CutButton;
import apes.views.buttons.DeleteButton;
import apes.views.buttons.ForwardButton;
import apes.views.buttons.ImageButton;
import apes.views.buttons.OpenButton;
import apes.views.buttons.PasteButton;
import apes.views.buttons.PauseButton;
import apes.views.buttons.PlayButton;
import apes.views.buttons.RedoButton;
import apes.views.buttons.SaveButton;
import apes.views.buttons.StopButton;
import apes.views.buttons.UndoButton;
import apes.views.buttons.ZoomInButton;
import apes.views.buttons.ZoomOutButton;
import apes.views.buttons.ZoomResetButton;
import javax.swing.UIDefaults;

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
   * Tabs controller.
   */
  private TabsController tabsController;

  /**
   * Config controller.
   */
  private ConfigController configController;

  /**
   * Language controller.
   */
  private LanguageController languageController;

  /**
   * Change controller.
   */
  private InternalFormatController internalFormatController;

  /**
   * Plugin controller.
   */
  private PluginController pluginController;

  /**
   * Effect controller.
   */
  private EffectController effectController;
  
  /**
   * Undo manager (keeps history list).
   */
  private UndoManager undoManager;

  /**
   * Config object.
   */
  private Config config;

  /**
   * Config object.
   */
  private Language language;

  /**
   * Plugin handler, takes care of the plugin business.
   */
  private PluginHandler pluginHandler;
  
  /**
   * Starts the program.
   */
  public Main( String[] args )
  {
    // These should by default be white.
    String[] whites = { "Panel", "Label", "Slider", "Frame", "CheckBox", "TextField", "TextArea", "MenuBar", "Menu", "MenuItem" };
    for( int i = 0; i < whites.length; i++)
    {
      UIManager.put( whites[i] + ".background", Color.WHITE );
    }

    // Parse the configuration file and set default values.
    config = Config.getInstance();
    config.parse();

    // Initialize a player handler.
    PlayerHandler playerHandler = new PlayerHandler();
    
    // Create the plugin handler
    pluginHandler = new PluginHandler("build/apes/plugins");

    // Set some controllers.
    configController = new ConfigController();
    helpController = new HelpController();
    playerController = new PlayerController( playerHandler );
    tagsController = new TagsController( playerHandler );
    tabsController = new TabsController( playerHandler );
    languageController = new LanguageController();
    pluginController = new PluginController(pluginHandler, playerHandler);

    // Fix language
    language = Language.getInstance();

    try
    {
      language.setLanguage( config.getOption( "language" ) );
      language.load();
    }
    catch ( Exception e )
    {
      e.printStackTrace();
    }

    // Open all files passed in as arguments.
    for( int i = 0; i < args.length; i++)
    {
      try
      {
        ApesFile apesFile = new ApesFile( args[i] );
        tabsController.add( apesFile.getInternalFormat(), apesFile.getName() );
      }
      catch( UnidentifiedLanguageException e )
      {
        ApesError.unsupportedFormat();
      }
      catch( Exception e )
      {
        e.printStackTrace();
      }
    }

    // Undomanager
    undoManager = new UndoManager();
    undoManager.setLimit( Config.getInstance().getIntOption( "undo" ) );

    // Frame options.
    setTitle( language.get( "help.about.name" ) );
    setDefaultCloseOperation( EXIT_ON_CLOSE );
    setLayout( new BorderLayout() );

    // Add tabpanel.
    add( tabsController.getTabsView(), BorderLayout.CENTER );

    // Controller for the internal format.
    internalFormatController = new InternalFormatController( undoManager, tabsController, playerHandler );

    // Create and add menu.
    createMenu();

    // Create and add top panel.
    JPanel topPanel = topPanel();
    add( topPanel, BorderLayout.NORTH );

    // Create and bottom top panel.
    JPanel bottomPanel = bottomPanel();
    add( bottomPanel, BorderLayout.SOUTH );

    // Set window dimensions.
    setWindowDimensions();

    // Set a title.
    setTitle( language.get( "help.about.name" ) );

    // Set icon.
    setIconImage( Toolkit.getDefaultToolkit().createImage("images/apes.png") );

    // Start in center on screen.
    setLocationRelativeTo( null );

    // Make frame visible.
    setVisible( true );

    // Do something before close
    addWindowListener( new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        // Do before exit
      }
    } );
  }

  /**
   * Sets the dimensions for the window depending on the
   * configuration file.
   */
  private void setWindowDimensions()
  {
    pack();

    boolean maximized = config.getBooleanOption( "maximized" );

    try
    {
      int width = config.getIntOption( "frame_width" );
      int height = config.getIntOption( "frame_height" );

      if( width > 0 && height > 0 )
      {
        // Best do both JIC.
        setPreferredSize( new Dimension( width, height ) );
        setSize( width, height );
      }
    }
    catch( NumberFormatException e )
    {
      e.printStackTrace();
    }

    if( maximized )
    {
      setExtendedState( getExtendedState() | MAXIMIZED_BOTH );
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
    open.addActionListener( internalFormatController );
    open.setName( "open" );
    file.add( open );

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
    undo.addActionListener( internalFormatController );
    undo.setName( "undo" );
    edit.add( undo );

    JMenuItem redo = new ApesMenuItem( "menu.edit.redo" );
    redo.addActionListener( internalFormatController );
    redo.setName( "redo" );
    edit.add( redo );

    JMenuItem cut = new ApesMenuItem( "menu.edit.cut" );
    cut.addActionListener( internalFormatController );
    cut.setName( "cut" );
    edit.add( cut );

    JMenuItem copy = new ApesMenuItem( "menu.edit.copy" );
    copy.addActionListener( internalFormatController );
    copy.setName( "copy" );
    edit.add( copy );

    JMenuItem paste = new ApesMenuItem( "menu.edit.paste" );
    paste.addActionListener( internalFormatController );
    paste.setName( "paste" );
    edit.add( paste );

    JMenuItem delete = new ApesMenuItem( "menu.edit.delete" );
    delete.addActionListener( internalFormatController );
    delete.setName( "delete" );
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
    zoomIn.addActionListener( internalFormatController );
    zoomIn.setName( "zoomIn" );
    zoom.add( zoomIn );

    JMenuItem zoomOut = new ApesMenuItem( "menu.view.zoom.out" );
    zoomOut.addActionListener( internalFormatController );
    zoomOut.setName( "zoomOut" );
    zoom.add( zoomOut );

    JMenuItem zoomReset = new ApesMenuItem( "menu.view.zoom.reset" );
    zoomReset.addActionListener( internalFormatController );
    zoomReset.setName( "zoomReset" );
    zoom.add( zoomReset );

    JMenu languages = new ApesMenu( "menu.view.languages" );
    view.add( languages );

    for( String lang : language.getLanguages() )
    {
      Locale locale = new Locale( lang );

      JMenuItem menuItem = new JMenuItem( locale.getDisplayName() );
      menuItem.addActionListener( languageController );
      menuItem.setName( lang );
      languages.add( menuItem );
    }
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
    // Player END
    
    // Effects START
    JMenu effects = pluginController.getEffectMenu();
    menuBar.add( effects );
    // Effects END

    // Tools START
    JMenu tools = new ApesMenu( "menu.head.tools" );
    menuBar.add( tools );

    JMenuItem properties = new ApesMenuItem( "menu.tools.properties" );
    properties.addActionListener( configController );
    properties.setName( "show" );
    tools.add( properties );

    JMenuItem plugins = new ApesMenuItem( "menu.tools.plugins" );
    plugins.addActionListener( pluginController );
    plugins.setName( "plugin" );
    tools.add( plugins);
    // Tools END

    // Help START
    JMenu help = new ApesMenu( "menu.head.help" );
    menuBar.add( help );

    JMenuItem manual = new ApesMenuItem( "menu.help.manual" );
    help.add( manual );

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
    topPanel.setBorder( new LineBorder( Color.GRAY, 1, true ) );

    ImageButton open = new OpenButton();
    // TODO: Maybe this is too much of a hack...
    open.addActionListener(
      new ActionListener()
      {
        public void actionPerformed( ActionEvent e )
        {
          internalFormatController.open();
        }
      }
      );
    topPanel.add( open );

    ImageButton save = new SaveButton();
    topPanel.add( save );

    ImageButton undo = new UndoButton();
    undo.addActionListener( internalFormatController );
    undo.setName( "undo" );
    topPanel.add( undo );

    ImageButton redo = new RedoButton();
    redo.addActionListener( internalFormatController );
    redo.setName( "redo" );
    topPanel.add( redo );

    ImageButton copy = new CopyButton();
    copy.addActionListener( internalFormatController );
    copy.setName( "copy" );
    topPanel.add( copy );

    ImageButton cut = new CutButton();
    cut.addActionListener( internalFormatController );
    cut.setName( "cut" );
    topPanel.add( cut );

    ImageButton paste = new PasteButton();
    paste.addActionListener( internalFormatController );
    paste.setName( "paste" );
    topPanel.add( paste );

    ImageButton delete = new DeleteButton();
    delete.addActionListener( internalFormatController );
    delete.setName( "delete" );
    topPanel.add( delete );

    ImageButton zoomIn = new ZoomInButton();
    zoomIn.addActionListener( internalFormatController );
    zoomIn.setName( "zoomIn" );
    topPanel.add( zoomIn );

    ImageButton zoomOut = new ZoomOutButton();
    zoomOut.addActionListener( internalFormatController );
    zoomOut.setName( "zoomOut" );
    topPanel.add( zoomOut );

    ImageButton zoomReset = new ZoomResetButton();
    zoomReset.addActionListener( internalFormatController );
    zoomReset.setName( "zoomReset" );
    topPanel.add( zoomReset );

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
    bottomPanel.setBorder( new LineBorder( Color.GRAY, 1, true ) );

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

    JPanel volumePanel = new VolumePanel( playerController );
    bottomPanel.add( volumePanel );

    return bottomPanel;
  }

  public static void main( String[] args )
  {
    new Main( args );
  }
}
