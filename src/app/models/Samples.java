package src.app.models;

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
  private static final int BITS_PER_SAMPLE = 32;
  private int size;
  
  /**
   * Contains all the raw data of the samples.
   */
  private byte[] sampleData;
  
  /**
   * Constructs a <code>Samples</code> object from the given data. 
   * @param data sample data using the specified bps.
   */
  public Samples(int bps, byte[] data) throws Exception  
  {
    // Complain if bps is invalid
    if(bps <= 0 || bps%8 != 0 || bps > BITS_PER_SAMPLE)
    {
      throw new Exception();
    }
    
    // Count bytes instead of bits
    bps /= 8;
    size = data.length/bps;
    sampleData = new byte[size];
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
  
  public int getSize()
  {
    return size;
  }
  
  public long getSample( int index )
  {
    long value = 0;
    for(int i = 0; i < BITS_PER_SAMPLE / 8; i++)
    {
      value += sampleData[index] >> (8 * i);
    }
    return value;
  }
  
  public void setSample( int index, long value ) throws Exception
  {
    if( value < 0 )
      throw new Exception();
    for(int i = 0; i < BITS_PER_SAMPLE / 8; i++)
      sampleData[index] = (byte)((value >> i*8) & 0xff);
  }
  
}