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
   * Maximum amplitude of all samples in the Samples object.
   */
  private long maxAmplitude;
  
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
    size = amount;
    sampleData = new byte[size*bps*8];  //TODO: size = 0 ??
    maxAmplitude = 0;
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
    
    maxAmplitude = 0;
    
    // Count bytes instead of bits
    int Bps = bps / 8;
    size = data.length/Bps;
    sampleData = new byte[size*(BITS_PER_SAMPLE/8)];
    int BpsDiff = BITS_PER_SAMPLE/8 - Bps;

    System.out.println("BpsDiff: " + BpsDiff);
    System.out.println("Bytes per sample: " + BITS_PER_SAMPLE/8); 
    System.out.println("size: " + size);
    // Transfer data to sampleData array.
    for(int i = 0; i < size; i++)
    {
      int j;
      
      // Pad as needed.
      for( j = 0; j < BpsDiff; j++ )
      {
        sampleData[i * Bps + j] = 0; // dont make sense, sampledata are array of bytes but pad one byte for every bit diff
        //System.out.println("Padding");
      }
      
      // Handle supplied bytes.
      for(; j < (BITS_PER_SAMPLE / 8) - BpsDiff; j++)
      {
        sampleData[i * Bps + j] = data[i * Bps + j];
        //System.out.println("data");
      }
      
      long samp;
      if( ( samp = getSample( i ) ) > maxAmplitude )
        maxAmplitude = samp; 
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
   * Returns an approximation of the average amplitude among all samples in this samples object.
   * @return An approximate average amplitude over all samples.
   */
  public long getAverageAmplitude()
  {
    long total = 0;
    for( int i = 0; i < size; i += 10 )
      total += getSample( i );
    return total / size;
  }
  
  /**
   * Returns the maximum amplitude among all samples in this object.
   * @return The value of the highest amplitude of all samples in this object.
   */
  public long getMaxAmplitude()
  {
    return maxAmplitude;
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
    if( value > maxAmplitude )
      maxAmplitude = value;
  }
  
}