package apes.models;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class SampleIterator
{
  
  /**
   * Consts a SampleIterator iterating over all samples in the given <code>Channel</code>.
   * @param c The <code>Channel</code> to iterate over.
   */
  public SampleIterator( Channel c )
  {
    channel = c;
    samplesObject = 0;
    samplesIndex = 0;
  }
  
  /**
   * Returns false if there are no more samples in the Iterator. Otherwise, returns true.
   */
  public boolean hasNext()
  {
    return samplesObject < channel.getSamplesSize();
  }
  
  /**
   * Returns the amplitude of the next sample.
   */
  public int next()
  {
    int amplitude;
    if( hasNext() )
    {
      amplitude = channel.getSamples(samplesObject).getSample(samplesIndex);
      if(++samplesIndex >= channel.getSamples(samplesObject).getSize())
      {
        samplesObject++;
        samplesIndex = 0;
      }
      return amplitude;
    }
    throw new NoSuchElementException();
  }
  
  /**
   * Not implemented.
   */
  public void remove()
  {
    throw new UnsupportedOperationException();
  }
  
  /**
   * The channel to iterate over.
   */
  private Channel channel;
  
  /**
   * Which Samples object is currently being iterated over.
   */
  private int samplesObject;
  
  /**
   * Current index within the current samples object. 
   */
  private int samplesIndex;
}
