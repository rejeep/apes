package apes.controllers;

import java.awt.Point;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.UndoManager;

import apes.exceptions.UnidentifiedLanguageException;
import apes.lib.ApesFile;
import apes.lib.PlayerHandler;
import apes.models.InternalFormat;
import apes.models.Player;
import apes.models.Samples;
import apes.models.undo.CutEdit;
import apes.models.undo.PasteEdit;
import apes.views.ApesError;
import apes.views.InternalFormatView;


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
   * An internal format view object used by the zoom methods.
   */
  private InternalFormatView internalFormatView;

  /**
   * New zoom.
   */
  private int zoom;

  /**
   * New center position in zoom.
   */
  private int center;

  /**
   * Holds an undoable edit.
   */
  private AbstractUndoableEdit edit;

  /**
   * An internal format.
   */
  private InternalFormat internalFormat;

  /**
   * A player.
   */
  private Player player;

  /**
   * The marked selection.
   */
  private Point selection;

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

  public void beforeFilter() throws Exception
  {
    if( name.matches( "^zoom.*" ) )
    {
      internalFormatView = tabsController.getCurrentInternalFormatView();

      if( internalFormatView == null )
      {
        throw new Exception();
      }
    }
    else
    {
      internalFormat = playerHandler.getInternalFormat();

      if( internalFormat == null )
      {
        throw new Exception();
      }

      player = playerHandler.getCurrentPlayer();
      selection = player.getSelection();

      if( selection.x == selection.y )
      {
        throw new Exception();
      }
    }
  }

  public void afterFilter()
  {
    if( name.matches( "^zoom.*" ) )
    {
      if( internalFormatView != null )
      {
        internalFormatView.setCenter( center );
        internalFormatView.setZoom( zoom );
        internalFormatView.updateAll();
      }
    }
    else
    {
      undoManager.addEdit( edit );
    }
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
   * Copies the selected region.
   */
  public void copy()
  {
    clipboard = internalFormat.copySamples( selection.x, selection.y );
  }

  /**
   * Cuts the selected region.
   */
  public void cut()
  {
    edit = new CutEdit( internalFormat, selection );
    clipboard = ((CutEdit)edit).getCutout();
  }

  /**
   * Pastes the top of the stack at mark.
   */
  public void paste()
  {
    edit = new PasteEdit( internalFormat, selection, clipboard );
  }

  /**
   * Deletes the selected region.
   */
  public void delete()
  {
    edit = new CutEdit( internalFormat, selection );
  }

  /**
   * Zoom in.
   */
  public void zoomIn()
  {
    int currentZoom = internalFormatView.getZoom();
    int newZoom = currentZoom / InternalFormatView.ZOOM;

    zoom = newZoom < InternalFormatView.MAX_ZOOM ? InternalFormatView.MAX_ZOOM : newZoom;
  }

  /**
   * Zoom out.
   */
  public void zoomOut()
  {
    Player player = playerHandler.getCurrentPlayer();

    int currentZoom = internalFormatView.getZoom();
    int newZoom = currentZoom * InternalFormatView.ZOOM;
    int stop = player.getSampleAmount();

    zoom = newZoom > stop ? stop : newZoom;
  }

  /**
   * Zoom to mark.
   */
  public void zoomSelection()
  {
    Player player = playerHandler.getCurrentPlayer();

    zoom = player.getStop() - player.getStart();
    center = zoom / 2;
  }

  /**
   * Reset zoom.
   */
  public void zoomReset()
  {
    Player player = playerHandler.getCurrentPlayer();

    zoom = player.getSampleAmount();
    center = zoom / 2;
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