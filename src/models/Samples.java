package apes.models;

import java.math.BigInteger;

/**
 * Class for containing a bunch of samples as used by the
 * InternalFormat.
 *
 * @author Daniel Kvick (kvick@student.chalmers.se)
 */
public class Samples
{
  /**
   * Size of a sample in bits.
   */
  public static final int BITS_PER_SAMPLE = 16;

  /**
   * Size of a sample in bytes.
   */
  public static final int BYTES_PER_SAMPLE = BITS_PER_SAMPLE / 8;

  /**
   * Amount of milliseconds in a second.
   */
  public static final int MS_PER_SECOND = 1000;

  /**
   * Amount of samples in this object.
   */
  private int size;

  /**
   * Maximum amplitude of all samples in the Samples object.
   */
  private int maxAmplitude;

  /**
   * The index of the sample due to which the maxAmplitude was last updated.
   */
  private int maxAmplitudeIndex;

  /**
   * Minimum amplitude of all samples in the Samples object.
   */
  private int minAmplitude;

  /**
   * The index of the sample due to which the minAmplitude was last updated.
   */
  private int minAmplitudeIndex;

  /**
   * Contains all the raw data of the samples.
   */
  private int[] sampleData;


  /**
   * Constructs a <code>Samples</code> object with the desired size.
   *
   * @param amount number of samples.
   */
  public Samples( int amount )
  {
    size = amount;
    sampleData = new int[amount];

    minAmplitude = maxAmplitude = 0;
    minAmplitudeIndex = maxAmplitudeIndex = 0;
  }

  /**
   * Constructs a <code>Samples</code> object from the given data.
   *
   * @param data sample data using the specified bps.
   */
  public Samples(int bps, byte[] data) throws Exception
  {
    // Complain if bps is invalid
    if(bps <= 0 || bps%8 != 0 || bps > BITS_PER_SAMPLE)
    {
      throw new Exception("Invalid amount of bits per sample");
    }

    setMinAndMaxDefaultValues();

    // Count bytes instead of bits
    int Bps = bps / 8;
    size = data.length / Bps;
    sampleData = new int[size];
    int BpsDiff = BYTES_PER_SAMPLE - Bps;

    // Transfer data to sampleData array.
    for(int i = 0; i < size; i++)
    {
      // Read value
      byte[] val = new byte[Bps];
      for(int j = 0; j < Bps; j++)
        val[j] = data[(i + 1) * Bps - j -1];
      BigInteger bigAmp = new BigInteger(val);
      int amplitude = bigAmp.intValue() << ( 8 * BpsDiff );

      sampleData[i] = amplitude;
    }
    updateMinAndMaxAmplitude();
  }

  /**
   * Returns amount of samples.
   *
   * @return the amount of samples stored in this samples object.
   */
  public int getSize()
  {
    return size;
  }

  /**
   * Returns an approximation of the average amplitude among all
   * samples in this samples object.
   *
   * @param resolution The function takes every
   * <code>resolution</code>:th sample into account.
   * @return An approximate average amplitude over all samples.
   */
  public int getAverageAmplitude(int resolution)
  {
    int total = 0;
    for( int i = 0; i < size; i += resolution )
      total += sampleData[i];

    return total / ( size / resolution );
  }

  /**
   * Returns the maximum amplitude among all samples in this object.
   *
   * @return The value of the highest amplitude of all samples in this
   * object.
   */
  public int getMaxAmplitude()
  {
    return maxAmplitude;
  }

  /**
   * Returns the minimum amplitude among all samples in this object.
   *
   * @return The value of the highest amplitude of all samples in this
   * object.
   */
  public int getMinAmplitude()
  {
    return minAmplitude;
  }

  /**
   * Returns the sample at given index.
   *
   * @param index The index of the desired sample.
   * @return The amplitude of the requested sample.
   */
  public int getSample( int index )
  {
    return sampleData[index];
  }

  /**
   * Sets amplitude of selected sample.
   *
   * @param index The index of the sample to affect.
   * @param value The desired amplitude.
   */
  public void setSample( int index, int value )
  {
    sampleData[index] = Math.max(Short.MIN_VALUE, Math.min(value, Short.MAX_VALUE));
  }

  /**
   * Calculates the minimum and maximum amplitude, stores it in
   * <code>minAmplitude</code> and <code>maxAmplitude</code> and
   * updates <code>minAmplitudeIndex</code> and
   * <code>maxAmplitudeIndex</code>.
   */
  public void updateMinAndMaxAmplitude()
  {
    int min = Short.MAX_VALUE;
    int minI = -1;

    int max = Short.MIN_VALUE;
    int maxI = -1;

    // Go through all samples
    for( int i = 0; i < size; i++ )
    {
      int amp = getSample( i );

      // Update min as needed.
      if( amp < min )
      {
        min = amp;
        minI = i;
      }

      // Update max as needed.
      if( amp > max )
      {
        max = amp;
        maxI = i;
      }
    }

    minAmplitude = min;
    minAmplitudeIndex = minI;

    maxAmplitude = max;
    maxAmplitudeIndex = maxI;
  }

  /**
   * Returns all data.
   *
   * @return a <code>byte</code> array containing all data.
   */
  public byte[] getData()
  {
    byte[] retArray = new byte[ size * BYTES_PER_SAMPLE ];
    for( int i = 0; i < size; i++ )
    {
      for( int j = 0; j < BYTES_PER_SAMPLE; j++ )
      {
        retArray[i * BYTES_PER_SAMPLE + j] = (byte)((sampleData[i] >> (j * 8)) & 0xff);
      }
    }
    return retArray;
  }

  /**
   * Sets default min and max index and value.
   */
  private void setMinAndMaxDefaultValues()
  {
    minAmplitude = Short.MAX_VALUE;
    minAmplitudeIndex = -1;

    maxAmplitude = Short.MIN_VALUE;
    maxAmplitudeIndex = -1;
  }
}