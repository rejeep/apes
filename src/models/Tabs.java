package apes.models;

import java.util.HashSet;
import java.util.Observable;
import java.util.Set;
import javax.swing.SingleSelectionModel;
import apes.lib.PlayerHandler;

import apes.views.InternalFormatView;

/**
 * Keeps track of tabs. All adding and removing of tabs should be done
 * through this model. The view should then listen to this and update
 * itself when something happens.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class Tabs extends Observable
{
  /**
   * A set with all tabs.
   */
  private Set<Tab> tabs;
  
  /**
   * The default model.
   */
  private SingleSelectionModel model;
  
  /**
   * Creates a new <code>Tabs</code> instance.
   */
  public Tabs()
  {
    tabs = new HashSet<Tab>();
  }
  
  /**
   * Adds a new tab.
   * 
   * @param internalFormatView The internal format view to add to the
   *          tab panel.
   */
  public void add( InternalFormatView internalFormatView )
  {
    Tab tab = new Tab( tabs.size(), internalFormatView );
    tabs.add( tab );
      
    setChanged();
    notifyObservers( tab );
  }
  
  /**
   * Adds a new tab.
   * 
   * @param internalFormat The internal format to add to the tab
   *          panel.
   */
  public void add( InternalFormat internalFormat )
  {
    add( new InternalFormatView( internalFormat ) );
  }

  /**
   * Sets the default model.
   * 
   * @param model The default model.
   */
  public void setModel( SingleSelectionModel model )
  {
    this.model = model;
  }

  /**
   * Returns the <code>Tab</code> that is selected.
   * 
   * @return The selected tab.
   */
  public Tab getSelectedTab()
  {
    return findTabByIndex( model.getSelectedIndex() );
  }
  
  /**
   * Removes <code>tab</code> with index <code>index</code>.
   * 
   * @param index The index of the tab that should be removed.
   */
  public void remove( int index )
  {
    Tab tab = findTabByIndex( index );
    
    tabs.remove( tab );
    
    // If a tab is removed, the indexes are changed. So we must update
    // all indexes.
    for( Tab tabX : tabs )
    {
      if( tabX.getIndex() > tab.getIndex() )
      {
        tabX.decIndex();
      }
    }
    
    setChanged();
    notifyObservers( tab );
    
    // Remove the internal format from the player handler.
    PlayerHandler.getInstance().remove(tab.getInternalFormat());
  }
  
  /**
   * Returns true if <code>tab</code> is in the list of tabs. False
   * otherwise.
   * 
   * @param tab The tab to look for.
   * @return True if <code>tab</code> is in the list of tabs. False
   *         otherwise.
   */
  public boolean contains( Tab tab )
  {
    return tabs.contains( tab );
  }
  
  /**
   * Returns the tab with index <code>index</code>.
   * 
   * @param index The index.
   * @return The tab with index index, or null if there's no such tab.
   */
  public Tab get( int index )
  {
    return findTabByIndex( index );
  }
  
  /**
   * See {@link Tabs#get get}
   * 
   * @param index -
   * @return -
   */
  private Tab findTabByIndex( int index )
  {
    for( Tab tab : tabs )
    {
      if( tab.getIndex() == index )
      {
        return tab;
      }
    }

    return null;
  }
  
  /**
   * This class is a tab that holds information about the index and
   * what internal format view.
   */
  public class Tab
  {
    /**
     * The tab index.
     */
    private int index;
    
    /**
     * The internal format view.
     */
    private InternalFormatView internalFormatView;

    /**
     * Creates a new <code>Tab</code> instance.
     * 
     * @param index The tab index.
     * @param internalFormatView The view to place on the tab panel.
     */
    public Tab( int index, InternalFormatView internalFormatView )
    {
      this.index = index;
      this.internalFormatView = internalFormatView;
    }
    
    /**
     * Return this tab index.
     * 
     * @return The index.
     */
    public int getIndex()
    {
      return index;
    }
    
    /**
     * Return the tab title, which is the same as the file name.
     * 
     * @return The tab title.
     */
    public String getTitle()
    {
      return getInternalFormat().getFileStatus().getFileName();
    }

    /**
     * Returns the internal format view that is on this panel.
     * 
     * @return The internal format view.
     */
    public InternalFormatView getInternalFormatView()
    {
      return internalFormatView;
    }
    
    /**
     * Returns the internal format for this tab.
     * 
     * @return The internal format.
     */
    public InternalFormat getInternalFormat()
    {
      return internalFormatView.getInternalFormat();
    }
    
    /**
     * Decreases this tab index (by one).
     */
    public void decIndex()
    {
      index--;
    }
  }
}