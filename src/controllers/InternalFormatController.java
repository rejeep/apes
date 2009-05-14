package apes.controllers;

import java.awt.Point;
import java.io.IOException;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.UndoManager;

import apes.lib.ApesFile;
import apes.lib.PlayerHandler;
import apes.models.InternalFormat;
import apes.models.Player;
import apes.models.Tabs;
import apes.models.undo.CutEdit;
import apes.models.undo.PasteEdit;
import apes.views.InternalFormatView;
import apes.views.ApesError;

/**
 * Controller for the internal format.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class InternalFormatController extends ApplicationController
{
  /**
   * Values copied or cut.
   */
  private byte[] clipboard;

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
   * A player handler.
   */
  private PlayerHandler playerHandler;

  /**
   * The marked selection.
   */
  private Point selection;

  /**
   * The tabs model.
   */
  private Tabs tabs;

  /**
   * The undo manager that keeps track of all changes.
   */
  private UndoManager undoManager;

  /**
   * Creates a new <code>InternalFormatController</code>.
   * 
   * @param tabs The tabs model.
   * @param undoManager The undo manager.
   */
  public InternalFormatController(Tabs tabs, UndoManager undoManager)
  {
    this.tabs = tabs;
    this.undoManager = undoManager;
    this.playerHandler = PlayerHandler.getInstance();
  }

  public void beforeFilter() throws Exception
  {
    if(name.matches("^zoom.*"))
    {
      // This may throw an exception if there's no tab. But thats what
      // we want.
      Tabs.Tab tab = tabs.getSelectedTab();
      internalFormatView = tab.getInternalFormatView();

      if(internalFormatView == null)
      {
        throw new Exception();
      }
    }
    else if(name.matches("copy|cut|paste|delete"))
    {
      internalFormat = playerHandler.getInternalFormat();

      if(internalFormat == null)
      {
        throw new Exception();
      }

      player = playerHandler.getCurrentPlayer();
      selection = player.getSelection();

      if(selection.x == selection.y)
      {
        throw new Exception();
      }
    }
  }

  public void afterFilter()
  {
    if(name.matches("^zoom.*"))
    {
      if(internalFormatView != null)
      {
        internalFormatView.setCenter(center);
        internalFormatView.setZoom(zoom);
        internalFormatView.updateAll();
      }
    }
    else
    {
      undoManager.addEdit(edit);
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
    clipboard = internalFormat.getSamples(selection.x, selection.y);
  }

  /**
   * Cuts the selected region.
   */
  public void cut()
  {
    edit = new CutEdit(internalFormat, selection);
    clipboard = ( (CutEdit)edit ).getCutout();
  }

  /**
   * Pastes the top of the stack at mark.
   */
  public void paste()
  {
    edit = new PasteEdit(internalFormat, selection, clipboard);
  }

  /**
   * Deletes the selected region.
   */
  public void delete()
  {
    edit = new CutEdit(internalFormat, selection);
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
    player = playerHandler.getCurrentPlayer();

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
    player = playerHandler.getCurrentPlayer();

    zoom = player.getStop() - player.getStart();
    center = internalFormatView.getCenter() / 2;
  }

  /**
   * Reset zoom.
   */
  public void zoomReset()
  {
    Player player = playerHandler.getCurrentPlayer();

    zoom = player.getSampleAmount();
    center = internalFormatView.getCenter() / 2;
  }

  /**
   * Opens a new file.
   */
  public void open()
  {
    ApesFile apesFile = ApesFile.open();

    if(apesFile != null)
    {
      try
      {
        InternalFormatView internalFormatView = new InternalFormatView(apesFile.getInternalFormat());

        tabs.add(internalFormatView);
      }
      catch(Exception e)
      {
        ApesError.couldNotOpenFileError();

        e.printStackTrace();
      }
    }
  }

  /**
   * Save the current internal format.
   */
  public void save()
  {
    internalFormat = playerHandler.getInternalFormat();

    try
    {
      internalFormat.save();
    }
    catch(IOException e)
    {
      ApesError.saveFailure();

      e.printStackTrace();
    }
  }

  /**
   * Save the current internal format as.
   */
  public void saveAs()
  {
    internalFormat = playerHandler.getInternalFormat();
    ApesFile apesFile = ApesFile.open();

    try
    {
      internalFormat.saveAs(apesFile.getParent(), apesFile.getName());
    }
    catch(IOException e)
    {
      ApesError.saveFailure();

      e.printStackTrace();
    }
  }
}
