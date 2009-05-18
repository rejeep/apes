package apes.models;

import apes.models.MemoryHandler;
import apes.plugins.WaveFileFormat;

import java.io.IOException;

import java.util.Observable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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

  public final int bitsPerSample;

  public final int bytesPerSample;

  /**
   * Length of each channel in samples.
   */
  private int sampleAmount;

  /**
   * Constructor setting up the Internal Format according to the
   * supplied data.
   * 
   * @param tags Tag information of the audio file.
   * @param samplerate Amount of samples per second.
   * @param numChannels Number of channels.
   */
  public InternalFormat(Tags tags, int samplerate, int numChannels, int bitsPerSample)
  {
    if(tags == null)
    {
      this.tags = new Tags();
    }
    else
    {
      this.tags = tags;
    }
    
    this.bitsPerSample = bitsPerSample;
    this.bytesPerSample = bitsPerSample / 8;
    this.sampleRate = samplerate;
    channels = numChannels;
    memoryHandler = new MemoryHandler();
    sampleAmount = 0;
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
  public void setTags(Tags tags)
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
   * 
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
   * Returns a chunk of data in PCM format. The chunk contains
   * <code>amount</code> samples from each channel starting from
   * absolute index <code>index</code>.
   * 
   * @param indexS The first sample to include in the chunk, as an
   *          absolute index.
   * @param amountS Amount of samples in the chunk. If there are fewer
   *          than <code>amount</code> samples after
   *          <code>index</code>, all remaining samples are
   *          included.
   * @return A byte array containing the requested data.
   */
  public byte[] getChunk(long indexS, int amountS)
  {
    if(indexS + amountS > sampleAmount || indexS < 0 || amountS < 1)
      return null;

    try
    {
      return memoryHandler.read( samplesToBytes(indexS), (int)samplesToBytes(amountS) );
    }
    catch ( IOException e )
    {
      e.printStackTrace();
      
      System.exit(1);
    }
    
    return null;
  }

  /**
   * Returns an approximate average of the specified interval.
   * 
   * @param channel What channel to perform average on.
   * @param startS First sample to consider.
   * @param lengthS Amount of samples to consider.
   * @return
   */
  public int getAverageAmplitude(int channel, int startS, int lengthS)
  {
    if( startS < 0 || channel >= channels || lengthS < 1 || startS + lengthS > sampleAmount )
      return 0;
    
    int c = 0;
    int total = 0;
    int step = lengthS <= 50 ? 1 : Math.round(lengthS * 0.1f);

    final int IO_SIZE = 100000; // Amount in samples
    
    ByteBuffer buffer;
    int nToRead = IO_SIZE;

    for(int iS = startS; iS < startS+lengthS; iS++)
    {
      if(iS+nToRead > startS+lengthS)
        nToRead = startS+lengthS - iS;

      buffer = ByteBuffer.wrap( getSamples(iS, iS+nToRead) );
      for(int i = 0; i < nToRead; i+=step, c++)
      {
        switch(bytesPerSample)
        {
          case 2:
            total += buffer.getShort((int)(samplesToBytes(i) + channel*bytesPerSample));
            break;
          case 4:
            total += buffer.getInt((int)(samplesToBytes(i) + channel*bytesPerSample));
            break;
          default:
            System.out.println("BAD BYTES PER SAMPLE IN INTERNAL FORMAT WHILE AVERAGING");
            System.exit( 1 );
        }
      }
      iS += nToRead;
    }

    return Math.round((float)total/c);
  }

  /**
   * Save file as TODO: add error handling, or some sort of response
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
    WaveFileFormat wav = new WaveFileFormat();
    wav.exportFile( this, filePath, fileName); 
    fileStatus.setFileName(fileName);
    fileStatus.setFilePath(filePath);
    fileStatus.setOpenedByInternal();
  }

  /**
   * Save file TODO: add error handling or some sort of response
   */
  public void save() throws IOException
  {
    WaveFileFormat wav = new WaveFileFormat();
    wav.exportFile( this, fileStatus.getFilepath(), fileStatus.getFileName());
    fileStatus.setOpenedByInternal();
  }
  
  /**
   * Closes all streams and cleans up the <code>InternalFormat</code>
   */
  public void close()
  {
    memoryHandler.dispose();
    this.channels = 0;
  }
  
  /**
   * load an internal format
   * @param filePath
   * @param fileName
   * @throws IOException
   */
  public static InternalFormat load(String filePath, String fileName) throws IOException
  {
    WaveFileFormat wav = new WaveFileFormat();
    InternalFormat internalFormat = wav.importFile( filePath, fileName); 
    internalFormat.fileStatus.setOpenedByInternal();  
    return internalFormat;
  }

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
  public void setFileStatus(FileStatus fileStatus)
  {
    this.fileStatus = fileStatus;
  }

  /**
   * TODO: Comment
   *
   * @param channel 
   * @param indexS 
   * @return 
   */
  public int getSample(int channel, int indexS)
  {
    if( channel >= channels || indexS >= sampleAmount || indexS < 0)
      return 0;
    
    int amplitude = 0;
    byte[] b = null;
      
    try
    {
      b = memoryHandler.read( samplesToBytes(indexS) + channel * bytesPerSample, bytesPerSample );
    }
    catch ( IOException e )
    {
      e.printStackTrace();
    }
    
    for(int i = 0; i < bytesPerSample; i++)
      amplitude += b[i] << ( i * 8 );
    
    return amplitude;
  }

  /**
   * Returns an array of Samples[] containing the data copied from
   * each channel.
   * 
   * @param startS The first index to copy.
   * @param stopS The last index to copy.
   * @return All data copied.
   */
  // FIXME: How large chunks can we get?
  public byte[] getSamples(long startS, long stopS)
  {
    if(startS < 0 || startS > stopS || stopS >= sampleAmount)
      return null;

    try
    {
      return memoryHandler.read( samplesToBytes(startS), (int)samplesToBytes(stopS - startS + 1) );
    } catch ( IOException e )
    {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Removes all samples
   * 
   * @param startS
   * @param stopS
   */
  public void removeSamples(long startS, long stopS)
  {
    if(startS < 0 || startS > stopS || stopS >= sampleAmount)
      return;

    long lengthS = ( stopS - startS + 1);
    try
    {
      if(memoryHandler.free(samplesToBytes(startS), samplesToBytes(lengthS)))
      {
        sampleAmount -= lengthS;
      }
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
    updated();
  }

  public void copy(int startS, int stopS, MemoryHandler mH)
  {
    mH.transfer(memoryHandler, samplesToBytes(startS), samplesToBytes(stopS), 0);
  }
  
  /**
   * Removes the samples in the specified interval from all channel
   * and returns them in a Samples[][] containing a Samples[] for each
   * channel.
   * 
   * @param startS First sample to cut away.
   * @param stopS Last sample to cut away.
   * @return An array containing arrays of Samples objects of the data
   *         removed from the channels.
   */
  public void cutSamples(long startS, long stopS, MemoryHandler mH)
  {
    if(startS < 0 || startS > stopS || stopS >= sampleAmount)
      return;
    
    mH.transfer(memoryHandler, samplesToBytes(startS), samplesToBytes(stopS), 0L);
    removeSamples(startS, stopS);
  }

  /**
   * Sets all samples in the selected range to data taken from
   * <code>values</code>
   * 
   * @param startB First index to set as bytes.
   * @param values An array of byte values to use for setting.
   */
  // INDEX OK  !!!
  private void setSamples(long startB, byte[] values)
  {
    if(startB < 0 || startB + values.length > samplesToBytes(sampleAmount))
      return;
    
    try
    {
      memoryHandler.write(startB, values);
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Inserts the provided samples at the specified index.
   * @param startB Index to insert at in bytes.
   * @param samplesB Samples to insert at start.
   * @return Index of the first sample after the inserted samples.
   */
  public int insertSamples(int startB, byte[] samplesB)
  {
    if( samplesB == null || startB > samplesToBytes(sampleAmount) )
      return -1;
    
    boolean alloc = false;
    try
    {
      alloc = memoryHandler.malloc(startB, samplesB.length);
    } catch ( IOException e )
    {
      e.printStackTrace();
    }
    if(!alloc)
      return -1;
    
    sampleAmount += bytesToSamples(samplesB.length);
    
    setSamples( startB, samplesB );
    updated();
    
    return startB + samplesB.length;
  }

  /**
   * TODO: Comment
   *
   * @param startS
   * @param m
   * @return 
   */
  public void pasteSamples(long startS, MemoryHandler m)
  {
    memoryHandler.transfer(m, 0, (int)(m.getUsedMemory()-1), (long)samplesToBytes(startS));
    sampleAmount += bytesToSamples(m.getUsedMemory());
    updated();
  }

  /**
   * Scales all samples in the internal format.
   * 
   * @param startS The start sample.
   * @param stopS The end sample.
   * @param alpha The alpha value.
   */
  public void scaleSamples( long startS, long stopS, float alpha )
  {
    final int IO_SIZE = 100000; // Amount in samples
    
    ByteBuffer toWrite = null;

    int nToWriteS = IO_SIZE;

    if(alpha == 0)
    {
      toWrite = ByteBuffer.wrap(new byte[(int)samplesToBytes(nToWriteS)]);
      toWrite.order(ByteOrder.LITTLE_ENDIAN);
    }

    for(long iS = startS; iS < stopS; iS++)
    {
      if((stopS - iS + 1) < IO_SIZE)
      {
        nToWriteS = (int)(stopS - iS + 1);
        if(alpha == 0)
        {
          toWrite = ByteBuffer.wrap(new byte[(int)samplesToBytes(nToWriteS)]);
          toWrite.order(ByteOrder.LITTLE_ENDIAN);
        }
      }

      if(alpha != 0) 
      {
        toWrite = ByteBuffer.wrap(getSamples(iS, iS+nToWriteS));
        toWrite.order(ByteOrder.LITTLE_ENDIAN);

        for(int index = 0; index < nToWriteS; ++index)
        {
          switch(bytesPerSample)
          {
            case 2:
              toWrite.putShort( index*2, (short)Math.round(toWrite.getShort() * alpha));
              break;
            case 4:
              toWrite.putInt( index*4, Math.round(toWrite.getInt() * alpha));
              break;
            default:
              System.out.println("BAD BYTES PER SAMPLE IN INTERNAL FORMAT WHILE SCALING");
              System.exit( 1 );
          }
        }
      }
      setSamples(samplesToBytes(iS), toWrite.array());
      iS += nToWriteS;
    }
  }

  /**
   * TODO: Comment
   */
  public void updated()
  {
    setChanged();
    notifyObservers();
  }
  
  /**
   * Returns <code>samples</code> in bytes.
   *
   * @param samples Number of samples.
   * @return Number of bytes.
   */
  public long samplesToBytes( long samples )
  {
    return samples * bytesPerSample * channels;
  }
  
  /**
   * Returns <code>bytes</code> in samples.
   *
   * @param bytes Number of byes.
   * @return Number of samples.
   */  
  public long bytesToSamples( long bytes )
  {
    return bytes / ( bytesPerSample * channels );
  }
}