package apes.lib;

/**
 * Helper methods to convert from and to time units from and to
 * samples.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public class SampleHelper
{
  /**
   * Calculates how many samples <code>milliseconds</code> are with
   * <code>sampleRate</code>.
   * 
   * @param sampleRate The sample rate.
   * @param milliseconds The number of milliseconds.
   * @return Milliseconds in samples.
   */
  public static int millisecondsToSamples(int sampleRate, int milliseconds)
  {
    return Math.round( ( milliseconds / 1000.0f ) * sampleRate);
  }

  /**
   * Calculates how many samples <code>seconds</code> are with
   * <code>sampleRate</code>.
   * 
   * @param sampleRate The sample rate.
   * @param seconds The number of seconds.
   * @return Seconds in samples.
   */
  public static int secondsToSamples(int sampleRate, int seconds)
  {
    return seconds * sampleRate;
  }

  /**
   * Calculates how many samples <code>minutes</code> are with
   * <code>sampleRate</code>.
   * 
   * @param sampleRate The sample rate.
   * @param minutes The number of minutes.
   * @return Minutes in samples.
   */
  public static int minutesToSamples(int sampleRate, int minutes)
  {
    return ( minutes * 60 ) * sampleRate;
  }

  /**
   * Calculates how many milliseconds <code>samples</code> are with
   * <code>sampleRate</code>.
   * 
   * @param sampleRate The sample rate.
   * @param samples The number of samples.
   * @return Samples in milliseconds.
   */
  public static int samplesToMilliseconds(int sampleRate, int samples)
  {
    return Math.round((float)samples / sampleRate * 1000);
  }

  /**
   * Calculates how many seconds <code>samples</code> are with
   * <code>sampleRate</code>.
   * 
   * @param sampleRate The sample rate.
   * @param samples The number of samples.
   * @return Samples in seconds.
   */
  public static int samplesToSeconds(int sampleRate, int samples)
  {
    return Math.round((float)samples / sampleRate);
  }

  /**
   * Calculates how many minutes <code>samples</code> are with
   * <code>sampleRate</code>.
   * 
   * @param sampleRate The sample rate.
   * @param samples The number of samples.
   * @return Samples in minutes.
   */
  public static int samplesToMinutes(int sampleRate, int samples)
  {
    return Math.round( ( (float)samples / sampleRate ) / 60.0f);
  }
}
