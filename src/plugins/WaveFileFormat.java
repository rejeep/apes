package apes.plugins;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import apes.interfaces.AudioFormatPlugin;
import apes.lib.FileHandler;
import apes.models.*;

/**
 * Module used for converting .wav-files to the internal format
 * and converting the internal format to .wav-files.
 *
 * @author Simon Holm
 */
public class WaveFileFormat implements AudioFormatPlugin
{

  /**
   * Returns the extension of the file format
   * @return extension
   */
  public String getExtension()
  {
    return "wav";
  }
  
  /**
   * Converts a file from the internal file format to .wav and stores it on the disk
   *
   * @param internalFormat The file to be converted.
   * @param path           The path to the folder were the file should be saved.
   * @param fileName       The name of the file to be saved.
   * @throws Exception
   */
  public void exportFile( InternalFormat internalFormat, String path, String fileName ) throws Exception
  {
    ByteBuffer data; // contians data to be exported

    //TODO; Add better support for different headers

    byte[] chunkID       = {'R','I','F','F'};
    int    chunkSize;
    byte[] format        = {'W','A','V','E'};
    int    subchunk1ID   = 0x666d7420; // fmt
    int    subchunk1Size = 16;
    short  audioFormat   = 1;
    short  numChannels   = (short)internalFormat.getNumChannels();
    int    sampleRate    = internalFormat.getSampleRate();
    int    byteRate      = sampleRate * numChannels * (Samples.BITS_PER_SAMPLE/8);
    short  blockAlign    = (short)(numChannels * (Samples.BITS_PER_SAMPLE/8));
    short  bitsPerSample = Samples.BITS_PER_SAMPLE;
    byte[] subchunk2ID   = {'d','a','t','a'};
    int    subchunk2Size;

    int numSamples = 0; 

    SampleIterator[] iterators = new SampleIterator[numChannels];

    for(int i = 0; i < numChannels; ++i)
      iterators[i] = internalFormat.getChannel( i ).getIterator();

    for( SampleIterator iterator : iterators)
    {
      while(iterator.hasNext())
      {
        iterator.next();
        ++numSamples;        
      }
    }
    
    subchunk2Size = numSamples * numChannels * (Samples.BITS_PER_SAMPLE/8);
    chunkSize = 4+(8+subchunk1Size)+(8+subchunk2Size);

    data = ByteBuffer.wrap( new byte[8 + chunkSize] );

    // Start copy data

    data.order( ByteOrder.BIG_ENDIAN );
    data.put( chunkID );
    data.order( ByteOrder.LITTLE_ENDIAN );
    data.putInt( chunkSize );
    data.order( ByteOrder.BIG_ENDIAN );
    data.put( format );
    data.putInt( subchunk1ID );
    data.order( ByteOrder.LITTLE_ENDIAN );
    data.putInt( subchunk1Size );
    data.putShort( audioFormat );
    data.putShort( numChannels );
    data.putInt( sampleRate );
    data.putInt( byteRate );
    data.putShort( blockAlign );
    data.putShort( bitsPerSample );
    data.order( ByteOrder.BIG_ENDIAN );
    data.put( subchunk2ID );
    data.order( ByteOrder.LITTLE_ENDIAN );
    data.putInt( subchunk2Size );

    iterators = new SampleIterator[numChannels];

    for(int i = 0; i < numChannels; ++i)
      iterators[i] = internalFormat.getChannel( i ).getIterator();

    while(iterators[0].hasNext())
      for( SampleIterator iterator : iterators)
        data.putInt( iterator.next() );
    
    FileHandler.saveToFile( path, fileName, data.array() );
  }

  //TODO: Create a more detailed description of exception
  /**
   * Imports a wave file, converts it to the internal format and returns it.
   *
   * @param path     The path to the folder with the file to be loaded.
   * @param filename The name of the file to be imported.
   * @return Returns the file converted to the internal format
   * @throws Exception Will throw an exception if something bad happens
   */
  public InternalFormat importFile( String path, String filename ) throws Exception
  {
    //ByteBuffer buffer = FileHandler.loadFile( path, filename );
    ByteBuffer buffer = FileHandler.loadFileGraphical( );
    // Wave do not contain any tags
    Tags tag = null;

    // 4 big
    buffer.order( ByteOrder.BIG_ENDIAN );
    buffer.getInt();
    // 4 little
    buffer.order( ByteOrder.LITTLE_ENDIAN );
    buffer.getInt();
    // 4 big
    buffer.order( ByteOrder.BIG_ENDIAN );
    buffer.getInt();
    // 4 big
    buffer.order( ByteOrder.BIG_ENDIAN );
    buffer.getInt();
    // 4 little
    buffer.order( ByteOrder.LITTLE_ENDIAN );
    buffer.getInt();
    // 2 little
    buffer.getShort();
    // 2 little
    int numChannels = buffer.getShort();
    // 4 little
    int sampleRate = buffer.getInt();
    // 4 little
    buffer.getInt();
    // 2 little
    buffer.getShort();
    // 2 little
    int bitsPerSample = buffer.getShort();
    // 4 big
    buffer.order( ByteOrder.BIG_ENDIAN );
    buffer.getInt();
    // 4 little
    buffer.order( ByteOrder.LITTLE_ENDIAN );
    int subChunk2Size = buffer.getInt();
    // little

    List<Channel> channels = new ArrayList<Channel>();

    byte[][] samplesPerChannel = new byte[numChannels][subChunk2Size/numChannels];

    int channel = 0;
    int bytesPerSample = bitsPerSample/8;

    for( int i = 0; i < subChunk2Size/bytesPerSample; ++i )
    {
      buffer.get( samplesPerChannel[channel], i*bytesPerSample, bytesPerSample );
      channel = (channel+1) % numChannels;
    }

    for ( int i = 0; i < numChannels; ++i )
      channels.add( new Channel( new Samples( bitsPerSample, samplesPerChannel[i] ) ) );

    InternalFormat internalFormat = new InternalFormat( tag, sampleRate, channels );
    internalFormat.setFileStatus( new FileStatus( path, filename ) );

    return internalFormat;
  }
}
