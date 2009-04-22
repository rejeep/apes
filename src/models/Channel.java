package apes.models;

import java.awt.Point;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

/**
 * Describes a single channel of audio information.
 *
 * @author Daniel Kvick (kvick@student.chalmers.se)
 */
public class Channel
{
  /**
   * Amount of samples in each Samples object in the samplesList.
   */
  public static final int SAMPLES_SIZE = 1000;

  /**
   * A list containing all Samples structure, and thus all audio data of the channel.
   */
  private List<Samples> samplesList;

  /**
   * Constructor which adds a list of samples to the Channel.
   *
   * @param samples All audio data to be added to the Channel.
   */
  public Channel( Samples samples )
  {
    // Divide samples into chunks of SAMPLES_SIZE.
    samplesList = splitSamples( samples );
  }
  
  public SampleIterator getIterator()
  {
    return new SampleIterator(this);
  }
  
  /**
   * Returns a Samples object containing all samples in the Channel.
   * @return A Samples object containing all samples in the Samples objects of the Channel, in the same order.
   */
  public Samples getAllSamples()
  {
    int amount = 0;
    
    // Count samples
    for( Samples s : samplesList )
      amount += s.getSize();
    
    // No samples in channel
    if( amount == 0 )
      return null;
    
    // Create Samples object of correct size.
    Samples retSamp = new Samples( amount );
    
    int sampCount = 0;
    // For each Samples object
    for( Samples s : samplesList )
    {
      // Add to return object.
      for( int i = 0; i < s.getSize(); i++ )
      {
        //TODO: Quick fix for the exception
        try
        {
          retSamp.setSample( sampCount++, s.getSample( i ) );
        } catch ( Exception e )
        {
          e.printStackTrace();
        }
      }
    }
    
    return retSamp;
    
  }

  /**
   * Returns a Samples object containing all samples in the specified interval, inclusively.<br>
   * If the <code>stop</code> index is beyond the length of the channel, all samples from <code>start</code> to the last sample of the channel are returned. 
   *
   * @param start Index of the first sample to return.
   * @param stop  Index of the last sample to return.
   * @return A Samples object with all samples from sample start until sample stop.

   */
  public Samples getSamples( int start, int stop ) throws Exception
  {
    if( start < 0 || stop < 0 || start > stop )
      throw new Exception("Invalid interval");
    
    // Create object for returning.
    Samples retObj = new Samples( stop - start + 1 );
    
    // What samples object are we looking at?
    int samplesCounter;
    
    // What absolute sample index are we at?
    int curSample;
    
    // Find start
    for( samplesCounter = 0, curSample = 0; samplesCounter < samplesList.size(); samplesCounter++ )
    {
      Samples sampObj = samplesList.get(samplesCounter);
      int nextSample = curSample + sampObj.getSize();
      
      // start is within this Samples object
      if( nextSample > start )
      {
        // Find index of start
        int startIndex;
        for(startIndex = 0; curSample + startIndex < start; startIndex++)
          ;
        
        // Copy samples
        for( int i = startIndex; i < sampObj.getSize() && nextSample <= stop; i++, nextSample++ )
          retObj.setSample( i - startIndex , sampObj.getSample(i) );
        break;
      }
      
      // fill in remaining samples
      for( samplesCounter++; samplesCounter < samplesList.size(); samplesCounter++ )
      {
        Samples SampObj = samplesList.get( samplesCounter ); 
        for( int i = 0; i < sampObj.getSize(); i++, nextSample++ )
        {
          retObj.setSample( nextSample - start, sampObj.getSample(i) );
          if( nextSample == stop )
            return retObj;
        }
      }
      curSample = nextSample;
    }
    
    return retObj;
    
  }

  /**
   * Inserts samples at the selected position.
   *
   * @param start   Position of insertion as sample index.
   * @param samples Sample data to insert into channel.
   */
  public void setSamples( int start, Samples samples ) throws IndexOutOfBoundsException
  {
    if( start < 0 )
      throw new IndexOutOfBoundsException("Sample index cannot be negative");
    
    // Find start indices.
    Point p = findAbsoluteIndex(start);
    int absIndex = 0;
    int relIndex = p.y;
    int objIndex = p.x;
    
    
    Samples s = samplesList.get( objIndex );
    
    // Save part to be overwritten
    int[] overwritten = new int[s.getSize() - relIndex];
    for( int i = relIndex; i < s.getSize(); i++ )
      overwritten[i - relIndex] = s.getSample( i ); 
    
    // Overwrite end of first Samples object. WARNING WHAT IF LESS THAN ONE SAMPLES OBJECT?
    absIndex = 0;
    for( int i = relIndex; i < s.getSize(); i++, absIndex++ )
      s.setSampleNoUpdate( i, samples.getSample(absIndex) );
    
    s.updateMinAndMaxAmplitude();
    
    // Create new Samples objects.
    while( absIndex + SAMPLES_SIZE < samples.getSize())
    {
      Samples newSamples = new Samples( SAMPLES_SIZE );
      for( int i = 0; i < SAMPLES_SIZE; i++, absIndex++ )
      {
        newSamples.setSampleNoUpdate( i, samples.getSample( absIndex ) );
      }
      newSamples.updateMinAndMaxAmplitude();
      samplesList.add( ++objIndex, newSamples );
    }
    
    // Put back overwritten data.
    
    
  }

  /**
   * Replaces selected interval with the specified samples.
   *
   * @param start   Index of the first sample to replace.
   * @param stop    Index of the last sample to replace.
   * @param samples An object containing all sample data to replace selection with.

   */
  public void setSamples( int start, int stop, Samples samples ) throws IndexOutOfBoundsException
  { 
    if( start < 0 || stop < 0 || start > stop )
      throw new IndexOutOfBoundsException( "Invalid interval" );
    
    // Find start indices.
    Point startPoint = findAbsoluteIndex(start);
    Point stopPoint = findAbsoluteIndex(stop);
    
    // Overwrite beginning
    
    // Find end
    
    // Overwrite end
    
    // Remove old and add new
    
    
  }
  
  /**
   * Sets all samples within the specified range to <code>value</code> 
   * @param start The absolute index of the first value to change.
   * @param stop The absolute index of the last value to change.
   * @param value The value to set selected samples to.
   */
  public void setSamples( int start, int stop, int value )
  {
    Point startPoint = findAbsoluteIndex( start );
    int curIndex = start - startPoint.y;
    for( int objI = startPoint.x; objI < samplesList.size(); objI++ )
    {
      Samples s = samplesList.get(objI);
      for( int i = 0; i < s.getSize(); i++, curIndex++ )
      {
        if(curIndex <= stop )
        {
          if(curIndex >= start)
            s.setSampleNoUpdate(i, value);
        }
        else
        {
          s.updateMinAndMaxAmplitude();
          return;
        }
      }
      s.updateMinAndMaxAmplitude();
    }
      
  }
  
  /**
   * Changes the values of all samples within a specified range by adding <code>delta</code>.
   * @param start The index of the first sample to affect.
   * @param stop The index of the last sample to affect.
   * @param delta Value added to each sample in the selected range. May be negative.
   */
  public void alterSamples( int start, int stop, int delta )
  {
    Point startPoint = findAbsoluteIndex( start );
    int curIndex = start - startPoint.y;
    for( int objI = startPoint.x; objI < samplesList.size(); objI++ )
    {
      Samples s = samplesList.get(objI);
      for( int i = 0; i < s.getSize(); i++, curIndex++ )
      {
        if( curIndex <= stop )
        {
          if(curIndex >= start)
            s.setSampleNoUpdate(i, s.getSample(i) + delta);
        }
        else
        {
          s.updateMinAndMaxAmplitude();
          return;
        }
      }
      s.updateMinAndMaxAmplitude();
    }
  }
  
  /**
   * Finds the sample with the given absolute index.
   * @param absIndex The absolute index to find.
   * @return A point with x being the SamplesObject of the requested sample 
   */
  public Point findAbsoluteIndex( int absIndex )
  {
    int sampObj = 0;
    int curIndex = 0;
    for(; sampObj < samplesList.size(); sampObj++ )
    {
      Samples s = samplesList.get(sampObj);
      int newIndex = curIndex + s.getSize();
      if( newIndex == absIndex )
      {
        return new Point(sampObj+1, 0);
      }
      
      if( newIndex < absIndex )
      {
        return new Point(sampObj, absIndex - curIndex);
      }
      
      curIndex = newIndex;
    }
    
    return new Point(-1, -1);
  }
  
  /**
   * Splits a <code>Samples</code> object into chunks of size <code>SAMPLES_SIZE</code>.
   * @param samples The samples object to split.
   * @return A samples object containing all samples in <code>samples</code> divided into Samples objects of size <code>SAMPLES_SIZE</code>
   */
  private List<Samples> splitSamples( Samples samples )
  {
    // Amount of full chunks
    int fullChunks = samples.getSize() / SAMPLES_SIZE;
    // Remaining samples
    int remainder = samples.getSize() % SAMPLES_SIZE;
    
    List<Samples> split = new ArrayList<Samples>();

    // Make full chunks
    for( int i = 0; i < fullChunks; i++ )
    {
      Samples sampObj = new Samples( SAMPLES_SIZE );
      for( int j = 0; j < SAMPLES_SIZE; j++ )
      {
        sampObj.setSampleNoUpdate( j, samples.getSample( i * SAMPLES_SIZE + j ) );
      }
      sampObj.updateMinAndMaxAmplitude();
      split.add( sampObj );
    }
    
    
    // Add remainder in smaller chunk
    Samples sampObj = new Samples( remainder );
    for( int i = 0; i < remainder; i++ )
    {
      //TODO: Had to add the try/catch, handle this in a better way maybe??
      try
      {
        sampObj.setSample( i, samples.getSample( fullChunks * SAMPLES_SIZE + i ) );
      } catch ( Exception e )
      {
        e.printStackTrace();
      }
    }
    split.add(sampObj);
    
    // Return chunks.
    return split;
  }
  
  /**
   * Get sample chunk at point <code>index</code>.
   *
   * @param index the index value
   * @return a chunk of <code>Samples</code> for <code>index</code>.
   */
  public Samples getSamples( int index )
  {
    return samplesList.get( index );
  }

  /**
   * Describe <code>getSamplesSize</code> method here.
   *
   * @return an <code>int</code> value
   */
  public int getSamplesSize()
  {
    return samplesList.size();
  }
  
  /**
   * Returns that smallest amplitude for this channel.
   *
   * @return The smallest amplitude.
   */
  public int getMinAmplitude()
  {
    int min = Integer.MAX_VALUE;
    int value;
    for( Samples s : samplesList )
      if( (value = s.getMinAmplitude()) < min )
        min = value;
    return min;
  }
  
  /**
   * Returns that largest amplitude for this channel.
   *
   * @return The largest amplitude.
   */
  public int getMaxAmplitude()
  {
    int max = Integer.MAX_VALUE;
    int value;
    for( Samples s : samplesList )
      if( (value = s.getMaxAmplitude()) > max )
        max = value;
    return max;
  }
  
  /**
   * Returns the diff between the largest and smallest amplitude.
   *
   * @return Diff between amplitues.
   */
  public long getDiffAmplitude()
  {
    return (long)getMaxAmplitude() - getMinAmplitude();
  }
}