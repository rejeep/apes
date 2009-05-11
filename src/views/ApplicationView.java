package apes.views;

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

import apes.controllers.ConfigController;
import apes.controllers.HelpController;
import apes.controllers.InternalFormatController;
import apes.controllers.LanguageController;
import apes.controllers.PlayerController;
import apes.controllers.PluginController;
import apes.controllers.TabsController;
import apes.controllers.TagsController;
import apes.lib.Config;
import apes.lib.Language;
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
import apes.views.buttons.ZoomSelectionButton;
import apes.views.buttons.ZoomResetButton;

/**
 *
 *
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class ApplicationView extends JFrame
{
  /**
   * The configuration.
   */
  private Config config;

  /**
   * The language.
   */
  private Language language;

  // Controllers
  private InternalFormatController internalFormatController;
  private TagsController tagsController;
  private LanguageController languageController;
  private ConfigController configController;
  private PluginController pluginController;
  private HelpController helpController;
  private PlayerController playerController;
  private TabsController tabsController;

  /**
   * Creates a new <code>ApplicationView</code> instance.
   *
   * @param internalFormatController The internal format controller.
   * @param tagsController The tags controller.
   * @param languageController The language controller.
   * @param configController The configuration controller.
   * @param pluginController The plugin controller.
   * @param helpController The help controller.
   * @param playerController The player controller.
   * @param tabsController The tabs controller.
   */
  public ApplicationView( InternalFormatController internalFormatController,
                          TagsController tagsController,
                          LanguageController languageController,
                          ConfigController configController,
                          PluginController pluginController,
                          HelpController helpController,
                          PlayerController playerController,
                          TabsController tabsController )
  {
    this.config = Config.getInstance();
    this.language = Language.getInstance();

    // Set controllers.
    this.internalFormatController = internalFormatController;
    this.tagsController = tagsController;
    this.languageController = languageController;
    this.configController = configController;
    this.pluginController = pluginController;
    this.helpController = helpController;
    this.playerController = playerController;
    this.tabsController = tabsController;

    // These should by default be white.
    String[] whites = { "Panel",
                        "Label",
                        "Slider",
                        "Frame",
                        "CheckBox",
                        "TextField",
                        "TextArea",
                        "MenuBar",
                        "Menu",
                        "MenuItem" };

    for( int i = 0; i < whites.length; i++)
    {
      UIManager.put( whites[i] + ".background", Color.WHITE );
    }

    // Set layout.
    setLayout( new BorderLayout() );

    // Add tab panel.
    add( tabsController.getTabsView(), BorderLayout.CENTER );

    // Set the menu.
    setJMenuBar( this.new Menu() );

    // Add top panel.
    add( this.new TopPanel(), BorderLayout.NORTH );

    // Add bottom panel.
    add( this.new BottomPanel(), BorderLayout.SOUTH );

    // Set window dimensions.
    setWindowDimensions();

    // Set a title.
    setTitle( language.get( "help.about.name" ) );

    // Exit on close.
    setDefaultCloseOperation( EXIT_ON_CLOSE );

    // Set icon.
    setIconImage( Toolkit.getDefaultToolkit().createImage( "images/apes.png" ) );

    // Start in center on screen.
    setLocationRelativeTo( null );

    // Make frame visible.
    setVisible( true );

    // Do something before close
    addWindowListener( new WindowAdapter() {
      public void windowClosing( WindowEvent e ) {
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

    // Do this even if maximized is set to true. This is because then
    // when the window gets restored, the preferred size is set.
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
   * The program menu.
   */
  private class Menu extends JMenuBar
  {

    /**
     * Creates a new <code>Menu</code> instance.
     */
    public Menu()
    {
      JMenu file = new ApesMenu( "menu.head.file" );
      add( file );

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
      add( edit );

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
      add( view );

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

      JMenuItem zoomSelection = new ApesMenuItem( "menu.view.zoom.selection" );
      zoomSelection.addActionListener( internalFormatController );
      zoomSelection.setName( "zoomSelection" );
      zoom.add( zoomSelection );
      
      JMenuItem zoomReset = new ApesMenuItem( "menu.view.zoom.reset" );
      zoomReset.addActionListener( internalFormatController );
      zoomReset.setName( "zoomReset" );
      zoom.add( zoomReset );

      JMenu languages = new ApesMenu( "menu.view.languages" );
      view.add( languages );

      for( String lang : language.getLanguages() )
      {
        Locale locale = new Locale( lang );

        String tempName = locale.getDisplayName().replaceFirst( "reg", "" );
        char[] chars = tempName.toCharArray();
        chars[0] = Character.toUpperCase( chars[0] );
        String name = new String( chars );
        JMenuItem menuItem = new JMenuItem( name );
        menuItem.addActionListener( languageController );
        menuItem.setName( lang );
        languages.add( menuItem );
      }
      // View END

      // Player START
      JMenu player = new ApesMenu( "menu.head.player" );
      add( player );

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
      JMenuItem effects = pluginController.getEffectMenu();
      add(effects);
      // Effects END

      // Tools START
      JMenu tools = new ApesMenu( "menu.head.tools" );
      add( tools );

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
      add( help );

      JMenuItem manual = new ApesMenuItem( "menu.help.manual" );
      help.add( manual );

      JMenuItem about = new ApesMenuItem( "menu.help.about" );
      about.addActionListener( helpController );
      about.setName( "about" );
      help.add( about );
      // Help END
    }
  }

  /**
   * The program top panel.
   */
  private class TopPanel extends JPanel
  {
    /**
     * Creates a new <code>TopPanel</code> instance.
     */
    public TopPanel()
    {
      setBorder( new LineBorder( Color.GRAY, 1, true ) );

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
      add( open );

      ImageButton save = new SaveButton();
      add( save );

      ImageButton undo = new UndoButton();
      undo.addActionListener( internalFormatController );
      undo.setName( "undo" );
      add( undo );

      ImageButton redo = new RedoButton();
      redo.addActionListener( internalFormatController );
      redo.setName( "redo" );
      add( redo );

      ImageButton copy = new CopyButton();
      copy.addActionListener( internalFormatController );
      copy.setName( "copy" );
      add( copy );

      ImageButton cut = new CutButton();
      cut.addActionListener( internalFormatController );
      cut.setName( "cut" );
      add( cut );

      ImageButton paste = new PasteButton();
      paste.addActionListener( internalFormatController );
      paste.setName( "paste" );
      add( paste );

      ImageButton delete = new DeleteButton();
      delete.addActionListener( internalFormatController );
      delete.setName( "delete" );
      add( delete );

      ImageButton zoomIn = new ZoomInButton();
      zoomIn.addActionListener( internalFormatController );
      zoomIn.setName( "zoomIn" );
      add( zoomIn );

      ImageButton zoomOut = new ZoomOutButton();
      zoomOut.addActionListener( internalFormatController );
      zoomOut.setName( "zoomOut" );
      add( zoomOut );

      ImageButton zoomSelection = new ZoomSelectionButton();
      zoomSelection.addActionListener( internalFormatController );
      zoomSelection.setName( "zoomSelection" );
      add( zoomSelection );
      
      ImageButton zoomReset = new ZoomResetButton();
      zoomReset.addActionListener( internalFormatController );
      zoomReset.setName( "zoomReset" );
      add( zoomReset );
    }
  }

  /**
   * The program bottom panel.
   */
  private class BottomPanel extends JPanel
  {
    /**
     * Creates a new <code>BottomPanel</code> instance.
     */
    public BottomPanel()
    {
      setBorder( new LineBorder( Color.GRAY, 1, true ) );

      ImageButton backward = new BackwardButton();
      backward.addActionListener( playerController );
      backward.setName( "backward" );
      add( backward );

      ImageButton pause = new PauseButton();
      pause.addActionListener( playerController );
      pause.setName( "pause" );
      add( pause );

      ImageButton play = new PlayButton();
      play.addActionListener( playerController );
      play.setName( "play" );
      add( play );

      ImageButton stop = new StopButton();
      stop.addActionListener( playerController );
      stop.setName( "stop" );
      add( stop );

      ImageButton forward = new ForwardButton();
      forward.addActionListener( playerController );
      forward.setName( "forward" );
      add( forward );

      JPanel volumePanel = new VolumePanel( playerController );
      add( volumePanel );
    }
  }
}
