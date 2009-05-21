package apes.models;

import java.util.NoSuchElementException;

public class SampleIterator
{
  /**
   * The internal format the iterator is bound to.
   */
  InternalFormat intForm;

  /**
   * Current index.
   */
  private long samplesIndex;

  /**
   * Consists a SampleIterator iterating over all samples in the given
   * <code>Channel</code>.
   * 
   * @param c The <code>Channel</code> to iterate over.
   */
  public SampleIterator(InternalFormat iF, int c)
  {
    intForm = iF;

    if(c < iF.getNumChannels())
      samplesIndex = c;
    else
      samplesIndex = 0;
  }

  /**
   * Creates a SampleIterator iterating over all samples in the given
   * channel starting from the given sample if it exists, otherwise
   * from the beginning of the channel.
   * 
   * @param iF Internal format to iterate over.
   * @param c Channel of iteration.
   * @param i Index to start at.
   */
  public SampleIterator(InternalFormat iF, int c, int i)
  {
    intForm = iF;

    if(c < iF.getNumChannels())
      samplesIndex = c;
    else
      samplesIndex = 0;

    if(i < iF.getSampleAmount())
      samplesIndex *= i;
  }

  /**
   * Returns false if there are no more samples in the Iterator.
   * Otherwise, returns true.
   */
  public boolean hasNext()
  {
    return samplesIndex < intForm.getSampleAmount();
  }

  /**
   * Returns the amplitude of the next sample.
   */
  public int next()
  {
    if(hasNext())
    {
      byte[] bytes = intForm.getChunk((int)samplesIndex, 1);
      samplesIndex += intForm.getNumChannels();
      int amplitude = 0;
      for(int i = 0; i < intForm.bytesPerSample; i++)
        amplitude += bytes[i] << ( i * 8 );
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
}
