package apes.models;

import java.nio.ByteBuffer;

/**
 * Class for containing a bunch of samples as used by the InternalFormat.
 * @author Daniel Kvick (kvick@student.chalmers.se)
 *
 */
public class Samples
{
  /**
   * Size of a sample in bits.
   */
  public static final int BITS_PER_SAMPLE = 32;
  
  /**
   * Amount of samples in this object.
   */
  private int size;
  
  /**
   * Contains all the raw data of the samples.
   */
  private byte[] sampleData;
  
  /**
   * Constructs a <code>Samples</code> object with the desired size.
   * @param bps bits per sample.
   * @param amount number of samples.
   */
  public Samples(int bps, int amount)
  {
    sampleData = new byte[size*bps];
    size = amount;
  }
  
  /**
   * Constructs a <code>Samples</code> object from the given data. 
   * @param data sample data using the specified bps.
   */
  public Samples(int bps, byte[] data) throws Exception  
  {
    // Complain if bps is invalid
    if(bps <= 0 || bps%8 != 0 || bps > BITS_PER_SAMPLE)
    {
      throw new Exception("Invalid amount of bits per sample");
    }
    
    // Count bytes instead of bits
    bps /= 8;
    size = data.length/bps;
    sampleData = new byte[data.length];
    int bpsDiff = BITS_PER_SAMPLE - bps;
    
    // Transfer data to sampleData array.
    for(int i = 0; i < data.length / bps; i++)
    {
      int j;
      
      // Pad as needed.
      for(j = 0; j < bpsDiff; i++)
      {
        sampleData[i * bps + j] = 0;
      }
      
      // Handle supplied bytes.
      for(; j < BITS_PER_SAMPLE/8; j++)
      {
        sampleData[i * bps + j] = data[i * bps + j];
      }
    }
  }
  
  /**
   * Returns amount of samples.
   * @return the amount of samples stored in this samples object.
   */
  public int getSize()
  {
    return size;
  }
  
  /**
   * Returns the sample at given index.
   * @param index The index of the desired sample. 
   * @return The amplitude of the requested sample.
   */
  public long getSample( int index )
  {
    long value = 0;
    for(int i = 0; i < BITS_PER_SAMPLE / 8; i++)
    {
      value += sampleData[index] >> (8 * i);
    }
    return value;
  }
  
  /**
   * Sets amplitude of selected sample.
   * @param index The index of the sample to affect.
   * @param value The desired amplitude.
   * @throws Exception Throws an exception of the amplitude has a negative value.
   */
  public void setSample( int index, long value ) throws Exception
  {
    if( value < 0 )
      throw new Exception("Amplitude must be non-negative!");
    for(int i = 0; i < BITS_PER_SAMPLE / 8; i++)
      sampleData[index] = (byte)((value >> i*8) & 0xff);
  }
  
}