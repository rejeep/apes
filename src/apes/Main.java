package apes;

import javax.swing.JFrame;
import javax.swing.undo.UndoManager;

import apes.controllers.ConfigController;
import apes.controllers.HelpController;
import apes.controllers.InternalFormatController;
import apes.controllers.KeyBindingController;
import apes.controllers.LanguageController;
import apes.controllers.PlayerController;
import apes.controllers.PluginController;
import apes.controllers.TabsController;
import apes.controllers.TagsController;
import apes.exceptions.UnidentifiedLanguageException;
import apes.lib.ApesFile;
import apes.lib.Language;
import apes.lib.PluginHandler;
import apes.models.Config;
import apes.models.Tabs;
import apes.views.ApesError;
import apes.views.ApplicationView;


/**
 * This is where it all starts. This creates a basic GUI with a layout and on
 * it a few components are placed.
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
  public Main(String[] args) throws Exception
  {
    // Parse the configuration file and set default values.
    Config config = Config.getInstance();
    config.parse();

    // Create the plugin handler
    PluginHandler pluginHandler = new PluginHandler(config.getOption("plugin_path"));

    // Fix language.
    Language language = Language.getInstance();

    try
    {
      language.setLanguage(config.getOption("language"));
      language.load();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

    // Create the undo manager.
    UndoManager undoManager = new UndoManager();
    undoManager.setLimit(config.getIntOption("undo"));

    // Init tabs.
    Tabs tabs = new Tabs();
    TabsController tabsController = new TabsController(tabs);

    // Set up some controllers.
    ConfigController configController = new ConfigController();
    HelpController helpController = new HelpController();
    PlayerController playerController = new PlayerController();
    TagsController tagsController = new TagsController();
    LanguageController languageController = new LanguageController();
    PluginController pluginController = new PluginController(pluginHandler);
    InternalFormatController internalFormatController = new InternalFormatController(tabs, undoManager);
    KeyBindingController keyBindingController = new KeyBindingController();

    // Create the application view.
    new ApplicationView(internalFormatController, tagsController, languageController, configController, pluginController, helpController, playerController, tabsController, keyBindingController);

    // Open all files passed in as arguments.
    for(int i = 0; i < args.length; i++)
    {
      try
      {
        ApesFile apesFile = new ApesFile(args[i]);
        tabs.add(apesFile.getInternalFormat());
      }
      catch(UnidentifiedLanguageException e)
      {
        ApesError.unsupportedFormat();
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args)
  {
    try
    {
      new Main(args);
    }
    catch(Exception e)
    {
      ApesError.unknownErrorOccurred();
      System.out.println();
      e.printStackTrace();
    }
  }
}
