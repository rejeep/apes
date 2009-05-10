package apes.controllers;

import java.awt.Point;
import java.io.File;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import apes.lib.ApesFile;
import apes.lib.PlayerHandler;
import apes.models.Player;
import apes.models.InternalFormat;
import apes.models.Samples;
import apes.models.undo.CutEdit;
import apes.models.undo.PasteEdit;
import apes.views.InternalFormatView;
import apes.views.ApesError;
import apes.exceptions.UnidentifiedLanguageException;


/**
 * Controller for the internal format.
 */
public class InternalFormatController extends ApplicationController
{
  /**
   * The undo manager that keeps track of all changes.
   */
  private UndoManager undoManager;

  /**
   * The main panel tabs.
   */
  private TabsController tabsController;
  
  /**
   * The player handler.
   */
  private PlayerHandler playerHandler;

  /**
   * Values copied or cut.
   */
  private Samples[][] clipboard;

  /**
   * Creates a new <code>InternalFormatController</code>.
   *
   * @param undoManager The undo manager.
   * @param tabsController The tabs controller.
   */
  public InternalFormatController( UndoManager undoManager, TabsController tabsController, PlayerHandler playerHandler )
  {
    this.tabsController = tabsController;
    this.undoManager = undoManager;
    this.playerHandler = playerHandler;
  }

  /**
   * A piece was copied from the graph.
   */
  public void copy()
  {
     InternalFormat iF = playerHandler.getInternalFormat();
     Player player = playerHandler.getPlayer(iF);
     Point selection = player.getSelection();

     if( selection.x == selection.y )
       return;

     clipboard = iF.copySamples( selection.x, selection.y );
  }

  /**
   * A piece was cut from the graph.
   */
  public void cut()
  {
    InternalFormat iF = playerHandler.getInternalFormat();
    Player player = playerHandler.getPlayer(iF);
    Point selection = player.getSelection();

    if( selection.x == selection.y )
      return;

     CutEdit edit = new CutEdit( iF, selection );
     undoManager.addEdit( edit );
    
     clipboard = edit.getCutout();
  }

  /**
   * A piece was pasted to the graph.
   */
  public void paste()
  {
    InternalFormat iF = playerHandler.getInternalFormat();
    Player player = playerHandler.getPlayer(iF);
    Point selection = player.getSelection();

    if( selection.x == selection.y )
      return;

     UndoableEdit edit = new PasteEdit( iF, selection, clipboard );
     undoManager.addEdit( edit );
  }

  /**
   * Performs a delete.
   */
  public void delete()
  {
    InternalFormat iF = playerHandler.getInternalFormat();
    Player player = playerHandler.getPlayer(iF);
    Point selection = player.getSelection();

    if( selection.x == selection.y )
      return;

     CutEdit edit = new CutEdit( iF, selection );
     undoManager.addEdit( edit );
  }

  /**
   * Performs an undo.
   */
  public void undo()
  {
     undoManager.undo();
  }

  /**
   * Performs a redo.
   */
  public void redo()
  {
     undoManager.redo();
  }

  /**
   * Zoom in.
   */
  public void zoomIn()
  {
//     System.out.println( "in" );
//     for(int i = 0; i < getCurrentInternalFormatView().getChannelViews().size(); ++i)
//       getCurrentInternalFormatView().getChannelViews().get(i).setZoom(100);
//     getCurrentInternalFormatView().updateView();
  }

  /**
   * Zoom out.
   */
  public void zoomOut()
  {
    System.out.println("out");
  }

  /**
   * Reset zoom to normal size.
   */
  public void zoomReset()
  {
    System.out.println( "reset" );
  }

  /**
   * Opens a new file.
   */
  public void open()
  {
    try
    {
      // Chose file.
      final JFileChooser fc = new JFileChooser();
      fc.setCurrentDirectory( new File( "." ) );
      int returnVal = fc.showOpenDialog( null );
      
      if( returnVal == JFileChooser.APPROVE_OPTION )
      {
        File file = fc.getSelectedFile();
        
        ApesFile apesFile = new ApesFile( file );
        tabsController.add( apesFile.getInternalFormat(), apesFile.getName() );
      }
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
}