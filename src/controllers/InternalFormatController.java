package apes.controllers;

import java.awt.Point;
import java.io.IOException;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.UndoManager;

import apes.lib.ApesFile;
import apes.lib.ApesFormat;
import apes.lib.PlayerHandler;
import apes.models.InternalFormat;
import apes.models.Player;
import apes.models.Tabs;
import apes.models.undo.CutEdit;
import apes.models.undo.PasteEdit;
import apes.plugins.WaveFileFormat;
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
    player = playerHandler.getCurrentPlayer();

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
        // Make sure that the center sample is at a correct and valid
        // location.
        int left = center - ( zoom / 2 );
        int right = center + ( zoom / 2 );
        int stop = player.getSampleAmount();

        if(left < 0)
        {
          center = zoom / 2;
        }
        else if(right > stop)
        {
          center = stop - ( zoom / 2 );
        }

        // Set zoom options.
        internalFormatView.setCenter(center);
        internalFormatView.setZoom(zoom);
        internalFormatView.updateAll();
      }
    }
    else if(name.matches("copy|cut|paste|delete"))
    {
      if(name.matches("cut|delete"))
      {
        player.setStart(0);
        player.setStop(0);
      }
      
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

    int currentCenter = internalFormatView.getCenter();
    int currentSample = player.getCurrentSample();

    center = currentSample == 0 ? currentCenter : currentSample;
    zoom = newZoom < InternalFormatView.MAX_ZOOM ? InternalFormatView.MAX_ZOOM : newZoom;
  }

  /**
   * Zoom out.
   */
  public void zoomOut()
  {
    int currentZoom = internalFormatView.getZoom();
    int newZoom = currentZoom * InternalFormatView.ZOOM;

    int currentCenter = internalFormatView.getCenter();
    int stop = player.getSampleAmount();

    zoom = newZoom > stop ? stop : newZoom;
    center = currentCenter;
  }

  /**
   * Zoom to mark.
   */
  public void zoomSelection()
  {
    int start = player.getStart();
    int stop = player.getStop();

    if(start != stop)
    {
      zoom = stop - start;
      center = start + ( zoom / 2 );

      player.setStart( 0 );
      player.setStop( 0 );
    }
  }

  /**
   * Reset zoom.
   */
  public void zoomReset()
  {
    zoom = player.getSampleAmount();
    center = zoom / 2;
  }

  /**
   * Opens a new file.
   */
  public void open()
  {
    ApesFile apesFile = ApesFile.open();
    ApesFormat format = new ApesFormat(apesFile.getFile());
    
    

    if(apesFile != null)
    {
      try
      {
        InternalFormatView internalFormatView; 
        if(format.isWave())
        {
          internalFormatView = new InternalFormatView(apesFile.getInternalFormat());
          tabs.add(internalFormatView);
        }
        else if(format.isApes())
        {
          internalFormatView = new InternalFormatView(InternalFormat.load(apesFile.getParent(), apesFile.getName()));
          tabs.add(internalFormatView); 
        }
        
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
    internalFormat = player.getInternalFormat();
    
    try
    {
      if(!internalFormat.getFileStatus().openedByInternal())
        saveAs();
      else
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
    internalFormat = player.getInternalFormat();
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
  
  public void export()
  {
    ApesFile apesFile = ApesFile.open();
    ApesFormat format = new ApesFormat(apesFile.getFile());
    InternalFormat internalFormat = player.getInternalFormat();
    
    if(format.isWave())
    {
      WaveFileFormat wav = new WaveFileFormat();
      try{
        if(player.getStop() != 0 && player.getStart() != player.getStop())
          wav.exportFile( internalFormat, apesFile.getFile(), player.getStart(), player.getStop());
        else
          wav.exportFile( internalFormat, apesFile.getFile());     
      }
      catch(Exception e)
      {
        e.printStackTrace();
        System.exit(1);     
      }
     }
  }
}
