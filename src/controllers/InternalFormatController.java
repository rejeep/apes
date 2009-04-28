package apes.controllers;

import java.util.Map;
import javax.swing.*;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import apes.models.InternalFormat;
import apes.models.Player;
import apes.models.undo.ChangeEdit;
import apes.models.undo.CopyEdit;
import apes.models.undo.DeleteEdit;
import apes.models.undo.PasteEdit;
import apes.plugins.WaveFileFormat;
import apes.views.InternalFormatView;
import java.util.HashMap;
import java.io.File;

/**
 * Controller for the internal format.
 */
public class InternalFormatController extends ApplicationController
{
  /**
   * Internal format.
   */
  private InternalFormat internalFormat;

  /**
   * The view over the internal format.
   */
  private InternalFormatView internalFormatView;

  /**
   * The undo manager that keeps track of all changes.
   */
  private UndoManager undoManager;

  /**
   * The main panel tabs.
   */
  private JTabbedPane tabsPane;

  /**
   * Contains all tabs and which internal format is has.
   */
  private Map<Integer, InternalFormat> tabs;


  /**
   * Creates a new <code>InternalFormatController</code> instance.
   *
   * @param undoManager The undo manager.
   * @param internalFormatView The internal format view.
   */
  public InternalFormatController( UndoManager undoManager, JTabbedPane tabsPane )
  {
    this.tabsPane = tabsPane;
    this.undoManager = undoManager;
    this.internalFormatView = new InternalFormatView();
    this.tabs = new HashMap<Integer, InternalFormat>();
  }

  /**
   * A piece was copied from the graph.
   */
  public void copy()
  {
    UndoableEdit edit = new CopyEdit();

    undoManager.addEdit( edit );
  }

  /**
   * A piece was cut from the graph.
   */
  public void cut()
  {
    UndoableEdit edit = new CopyEdit();

    undoManager.addEdit( edit );
  }

  /**
   * A piece was pasted to the graph.
   */
  public void paste()
  {
    UndoableEdit edit = new PasteEdit();

    undoManager.addEdit( edit );
  }

  /**
   * Performs a delete.
   */
  public void delete()
  {
    UndoableEdit edit = new DeleteEdit();

    undoManager.addEdit( edit );
  }

  /**
   * Performs some kind of change.
   */
  public void change()
  {
    UndoableEdit edit = new ChangeEdit();

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
   * Return the internal format.
   */
  public InternalFormatView getInternalFormatView()
  {
    return internalFormatView;
  }

  /**
   * Opens a new file.
   */
  public void open()
  {
    WaveFileFormat wav = new WaveFileFormat();

    try
    {
      // TODO ok to have here?
      final JFileChooser fc = new JFileChooser();
      fc.setCurrentDirectory( new File(".") );
      fc.showOpenDialog(new JPanel());
      File file = fc.getSelectedFile();
            
      internalFormat = wav.importFile( file.getParent(), file.getName() );

      Player.getInstance().setInternalFormat( internalFormat );

      internalFormatView.setInternalFormat( internalFormat );

      addInternalFormatToTab();
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
  }

  /**
   * Adds the internal format in a new tab.
   */
  private void addInternalFormatToTab()
  {
    String tabName = internalFormat.getFileStatus().getFileName();
    int index = tabs.keySet().size();
    
    tabs.put( index, internalFormat );
    
    tabsPane.addTab( tabName, internalFormatView );
    // TODO: Do not change tab.
    tabsPane.setEnabledAt( index, true );
  }
}