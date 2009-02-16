package src.app;

import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import src.app.views.ApesMenu;
import src.app.views.ApesMenuItem;
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
    menuBar.add(file);

    JMenuItem open = new ApesMenuItem( "menu.file.open" );
    file.add(open);

    JMenuItem newTab = new ApesMenuItem( "menu.file.new_tab" );
    file.add(newTab);

    JMenuItem closeTab = new ApesMenuItem( "menu.file.close_tab" );
    file.add(closeTab);

    JMenuItem save = new ApesMenuItem( "menu.file.save" );
    file.add(save);

    JMenuItem saveAs = new ApesMenuItem( "menu.file.save_as" );
    file.add(saveAs);

    JMenuItem export = new ApesMenuItem( "menu.file.export" );
    file.add(export);

    JMenuItem quit = new ApesMenuItem( "menu.file.quit" );
    file.add(quit);
    // File END

    // Edit START
    JMenu edit = new ApesMenu( "menu.head.edit" );
    menuBar.add(edit);

    JMenuItem undo = new ApesMenuItem( "menu.edit.undo" );
    edit.add(undo);

    JMenuItem redo = new ApesMenuItem( "menu.edit.redo" );
    edit.add(redo);

    JMenuItem cut = new ApesMenuItem( "menu.edit.cut" );
    edit.add(cut);

    JMenuItem copy = new ApesMenuItem( "menu.edit.copy" );
    edit.add(copy);

    JMenuItem paste = new ApesMenuItem( "menu.edit.paste" );
    edit.add(paste);

    JMenuItem delete = new ApesMenuItem( "menu.edit.delete" );
    edit.add(delete);
    // Edit END

    // View START
    JMenu view = new ApesMenu( "menu.head.view" );
    menuBar.add(view);

    JMenu zoom = new ApesMenu( "menu.head.zoom" );
    view.add(zoom);

    JMenuItem zoomIn = new ApesMenuItem( "menu.view.zoom.in" );
    zoom.add(zoomIn);

    JMenuItem zoomOut = new ApesMenuItem( "menu.view.zoom.out" );
    zoom.add(zoomOut);

    JMenuItem zoomReset = new ApesMenuItem( "menu.view.zoom.reset" );
    zoom.add(zoomReset);

    JMenuItem fullScreen = new ApesMenuItem( "menu.view.full_screen" );
    view.add(fullScreen);
    // View END

    // Tools START
    JMenu tools = new ApesMenu( "menu.head.tools" );
    menuBar.add(tools);

    JMenuItem properties = new ApesMenuItem( "menu.tools.properties" );
    tools.add(properties);
    // Tools END

    // Help START
    JMenu help = new ApesMenu( "menu.head.help" );
    menuBar.add(help);

    JMenuItem manual = new ApesMenuItem( "menu.help.manual" );
    help.add(manual);

    JMenuItem bug = new ApesMenuItem( "menu.help.bug" );
    help.add(bug);

    JMenuItem about = new ApesMenuItem( "menu.help.about" );
    help.add(about);
    // Help End

    setJMenuBar(menuBar);
  }

  public static void main( String[] args )
  {
    new Main();
  }
}