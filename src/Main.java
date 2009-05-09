package apes;

import javax.swing.JFrame;
import javax.swing.undo.UndoManager;

import apes.controllers.ConfigController;
import apes.controllers.HelpController;
import apes.controllers.InternalFormatController;
import apes.controllers.LanguageController;
import apes.controllers.PlayerController;
import apes.controllers.PluginController;
import apes.controllers.TabsController;
import apes.controllers.TagsController;
import apes.exceptions.UnidentifiedLanguageException;
import apes.lib.ApesFile;
import apes.lib.Config;
import apes.lib.Language;
import apes.lib.PlayerHandler;
import apes.views.ApesError;
import apes.views.ApplicationView;

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
   *
   * @param args Files to loaded at startup.
   */
  public Main( String[] args )
  {
    // Parse the configuration file and set default values.
    Config config = Config.getInstance();
    config.parse();

    // Initialize a player handler.
    PlayerHandler playerHandler = new PlayerHandler();

    // Set up controllers.
    ConfigController configController = new ConfigController();
    HelpController helpController = new HelpController();
    PlayerController playerController = new PlayerController( playerHandler );
    TagsController tagsController = new TagsController( playerHandler );
    TabsController tabsController = new TabsController( playerHandler );
    LanguageController languageController = new LanguageController();
    PluginController pluginController = new PluginController();

    // Fix language.
    Language language = Language.getInstance();
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

    // Create the undo manager.
    UndoManager undoManager = new UndoManager();
    undoManager.setLimit( config.getIntOption( "undo" ) );
    
    // Controller for the internal format.
    InternalFormatController internalFormatController = new InternalFormatController( undoManager, tabsController, playerHandler );

    // Create the application view.
    new ApplicationView( internalFormatController,
                         tagsController,
                         languageController,
                         configController,
                         pluginController,
                         helpController,
                         playerController,
                         tabsController );
  }

  public static void main( String[] args )
  {
    new Main( args );
  }
}
