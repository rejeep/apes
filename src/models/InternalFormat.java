package apes.models;

import apes.models.MemoryHandler;
import java.awt.Point;
import java.io.IOException;
import java.util.List;

import apes.lib.FileHandler;
import java.util.Observable;

/**
 * Describes the audio in a format suitable for internal
 * representation in the program.
 *
 * @author Daniel Kvick (kvick@student.chalmers.se)
 */
public class InternalFormat extends Observable
{
  /**
   * Information about where the file were saved to or loaded from.
   */
  private FileStatus fileStatus;

  /**
   * Information of all sound file tags.
   */
  private Tags tags;

  /**
   * Amount of samples per second.
   */
  private int sampleRate;

  /**
   * Amount of channels in file
   */
  private int channels;
  
  /**
   * Memory handler taking care of swapping
   */
  private MemoryHandler memoryHandler;

  public final static int BITS_PER_SAMPLE = 16;
  public final static int BYTES_PER_SAMPLE = BITS_PER_SAMPLE / 8;

  /**
   * Length of each channel in samples.
   */
  private int sampleAmount;

  /**
   * Constructor setting up the Internal Format according to the
   * supplied data.
   *
   * @param tags        Tag information of the audio file.
   * @param samplerate  Amount of samples per second.
   * @param channelList Channels containing all audio data.
   */
  public InternalFormat( Tags tags, int samplerate, int numChannels )
  {
    if(tags == null)
    {
      this.tags = new Tags();
    }
    else
    {
      this.tags = tags;
    }
    sampleRate = samplerate;
    channels = numChannels;
    memoryHandler = new MemoryHandler();
    sampleAmount = 5; // TODO: OBSERVE
  }

  /**
   * The tag object describing all the tags of the audio file.
   *
   * @return All tags of the audio file as a Tag object.
   */
  public Tags getTags()
  {
    return tags;
  }

  /**
   * Sets the tags object for this IF.
   *
   * @param tags The tags object.
   */
  public void setTags( Tags tags )
  {
    this.tags = tags;
  }

  /**
   * Returns the number of channels.
   *
   * @return The number of channels.
   */
  public int getNumChannels()
  {
    return channels;
  }
  
  /**
   * Returns the number of samples in each channel.
   * @return <code>sampleAmount</code>.
   */
  public int getSampleAmount()
  {
    return sampleAmount;
  }

  /**
   * Returns the sample rate, that is, how many samples make up one
   * second.
   *
   * @return Returns the sample rate.
   */
  public int getSampleRate()
  {
    return sampleRate;
  }

  /**
   * Returns a chunk of data in PCM format. The chunk contains <code>amount</code> samples from each channel starting from absolute index <code>index</code>. 
   * @param index The first sample to include in the chunk, as an absolute index.
   * @param amount Amount of samples in the chunk. If there are fewer than <code>amount</code> samples after <code>index</code>, all remaining samples are included.
   * @return A byte array containing the requested data.
   */
  public byte[] getChunk( int index, int amount )
  {
    if( index + amount > getSampleAmount() || index < 0 || amount < 1 )
      return null;
    
    return memoryHandler.read( channels * index * BYTES_PER_SAMPLE, amount * index * BYTES_PER_SAMPLE );
  }
  
  /**
   * Returns an approximate average of the specified interval.
   * @param channel What channel to perform average on.
   * @param start First sample to consider.
   * @param length Amount of samples to consider.
   * @return
   */
  public int getAverageAmplitude( int channel, int start, int length )
  {
    int step = length <= 1000 ? 1 : length / 1000;
    int i = 0;
    int average = 0;
    for( ; i < length; i += step )
      average += getSample( channel, start + i );
    
    return average / i;
  }
  
  /**
   * Save file as
   * TODO: add error handling, or some sort of response
   */
  /**
   * Saves the internal format to the specifed location with the
   * specified name.
   *
   * @param filePath The location the file should be saved to.
   * @param fileName The name of the file to be stored.
   */
  public void saveAs(String filePath, String fileName) throws IOException
  {
    FileHandler.saveObjectToFile( filePath, fileName, this );
  }

  /**
   * Save file
   * TODO: add error handling or some sort of response
   */
  public void save() throws IOException
  {
    FileHandler.saveObjectToFile( fileStatus.getFilepath(), fileStatus.getFileName(), this);
  }

  /*
    public void savePart()
    {
    //TODO: IMPLEMENT
    }
  */

  /**
   * Get the <code>FileStatus</code>.
   *
   * @return The <code>FileStatus</code>.
   */
  public FileStatus getFileStatus()
  {
    return fileStatus;
  }

  /**
   * Set a <code>FileStatus</code>.
   *
   * @param fileStatus The new FileStatus.
   */
  public void setFileStatus( FileStatus fileStatus )
  {
    this.fileStatus = fileStatus;
  }

  /**
   * Load file
   *
   * @param filePath Where the file is located.
   * @param fileName The name of the file.
   *
   * @return Returns an internal format.
   */
  public static InternalFormat load(String filePath, String fileName) throws IOException, ClassNotFoundException
  {
    // TODO: add error handling or some sort of response
    return (InternalFormat) FileHandler.loadObjectFile( filePath, fileName );
  }
  
  public int getSample( int channel, int index )
  {
    int amplitude = 0;
    index = index * channels + channel;
    byte[] b = getSamples( index, index );
    for( int i = 0; i < InternalFormat.BYTES_PER_SAMPLE; i++ )
      amplitude += b[i] << (i * 8);
    return amplitude;
  }
  
  /**
   * Returns an array of Samples[] containing the data copied from each channel.
   * @param start The first index to copy.
   * @param stop The last index to copy.
   * @return All data copied.
   */
  // FIXME: How large chunks can we get?
  public byte[] getSamples( int start, int stop )
  {
    if( start < 0 || start > stop || stop >= sampleAmount )
      return null;
    
    return memoryHandler.read( start * channels * BYTES_PER_SAMPLE, (stop - start) * channels * BYTES_PER_SAMPLE );
  }
  
  /**
   * Removes all samples 
   * @param start
   * @param stop
   */
  public void removeSamples( int start, int stop )
  {
    if( start < 0 || start > stop || stop >= sampleAmount )
      return;
    
    int length = (stop - start);
    if(memoryHandler.free( start * channels * BYTES_PER_SAMPLE, length * channels * BYTES_PER_SAMPLE ) )
      sampleAmount -= length;
    updated();
  }
  
  /**
   * Removes the samples in the specified interval from all channel and returns them in a Samples[][] containing a Samples[] for each channel.
   * @param start First sample to cut away.
   * @param stop Last sample to cut away.
   * @return An array containing arrays of Samples objects of the data removed from the channels.
   */
  public byte[] cutSamples( int start, int stop )
  {
    if( start < 0 || start > stop || stop >= sampleAmount )
      return null;
    
    byte[] retVal = getSamples( start, stop );
    removeSamples( start, stop );
    return retVal;
  }
  
  /**
   * Sets all samples in the selected range to data taken from <code>values</code>
   * @param start First index to set
   * @param values An array of byte values to use for setting.
   */
  public void setSamples( int start, byte[] values )
  {
    if( start < 0 || start + values.length >= sampleAmount )
      return;
    
    memoryHandler.write( start * channels * BYTES_PER_SAMPLE , values );
    updated();
  }
  
  /**
   * Inserts the provided samples at the specified index.
   * @param start Index to insert at.
   * @param samples Samples to insert at start.
   * @return Index of the first sample after the inserted samples.
   */
  public int pasteSamples( int start, byte[] samples )
  {
    if( samples == null || samples.length < sampleAmount )
      return -1;
    
    boolean alloc = memoryHandler.malloc(start * BYTES_PER_SAMPLE, samples.length);
    if(!alloc)
      return -1;
    
    setSamples( start, samples );
    
    sampleAmount += samples.length;
    
    updated();
    
    return start + samples.length;
  }
  
  /**
   * Scales all samples in the internal format.
   *
   * @param start The start sample.
   * @param stop The end sample.
   * @param alpha The alpha value.
   */
  public void scaleSamples( int start, int stop, float alpha )
  {
    byte[] samples = getSamples( start, stop );
    for( byte b : samples )
      b *= alpha;
    setSamples( start, samples );
  }
  
  public void updated()
  {
    setChanged();
    notifyObservers();
  }
}
