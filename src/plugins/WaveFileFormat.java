package apes.plugins;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

import apes.interfaces.AudioFormatPlugin;
import apes.lib.FileHandler;
import apes.models.*;

/**
 * Module used for converting .wav-files to the internal format and
 * converting the internal format to .wav-files.
 *
 * @author Simon Holm
 */
public class WaveFileFormat implements AudioFormatPlugin
{
  
  /**
   * Magic number of samples to read and write.
   */
  private final static int IO_CHUNK_SIZE = 100000; 
  /**
   * TODO: Comment
   */
  public String getName()
  {
    return "Wave";
  }

  /**
   * TODO: Comment
   */
  public Map<String, String> getDescriptions()
  {
    HashMap map = new HashMap<String, String>();
    map.put("en", "Support for .wav files.");
    map.put("sv", "Stöd för .wav filer.");
    return map;
  }

  /**
   * Returns the extension of the file format
   *
   * @return extension
   */
  public String getExtension()
  {
    return "wav";
  }

  /**
   * Converts a file from the internal file format to .wav and stores
   * it on the disk
   *
   * @param internalFormat The file to be converted.
   * @param path The path to the folder were the file should be saved.
   * @param fileName The name of the file to be saved.
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
    int    byteRate      = sampleRate * numChannels * (InternalFormat.BYTES_PER_SAMPLE);
    short  blockAlign    = (short)(numChannels * (InternalFormat.BYTES_PER_SAMPLE));
    short  bitsPerSample = InternalFormat.BITS_PER_SAMPLE;
    byte[] subchunk2ID   = {'d','a','t','a'};
    int    subchunk2Size;

    int numSamples = internalFormat.getSampleAmount();

    subchunk2Size = numSamples * numChannels * InternalFormat.BYTES_PER_SAMPLE;
    chunkSize = 4+(8+subchunk1Size)+(8+subchunk2Size);
    
    data = ByteBuffer.wrap( new byte[chunkSize - subchunk2Size] );

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

    File file = new File( path + fileName );
    FileOutputStream fStream = new FileOutputStream( file );
    
    fStream.write( data.array() );
    int written = 0;
     
    while( written < numSamples )
    {
      byte[] bytes = internalFormat.getChunk( written, IO_CHUNK_SIZE );
      if( bytes == null )
        bytes = internalFormat.getChunk( written, numSamples - written );
      written += IO_CHUNK_SIZE;
      fStream.write(bytes);
    }
    
    fStream.close();
  }

  //TODO: Create a more detailed description of exception
  /**
   * Imports a wave file, converts it to the internal format and
   * returns it.
   *
   * @param path The path to the folder with the file to be loaded.
   * @param filename The name of the file to be imported.
   * @return Returns the file converted to the internal format
   * @throws Exception Will throw an exception if something bad
   * happens
   */
  // TODO: Rewrite
  public InternalFormat importFile( String path, String filename ) throws Exception
  {
    //ByteBuffer buffer = FileHandler.loadFile( path, filename );
    //buffer.order( ByteOrder.LITTLE_ENDIAN );

    File file = new File( path, filename );
    FileInputStream fStream = new FileInputStream( file );
    DataInputStream dStream = new DataInputStream( fStream );

    // Wave do not contain any tags
    Tags tag = null;
    dStream.skip( 22 );
    
    // 2 little
    int numChannels = bigToLittleEndian(dStream.readShort());
    
    // 4 little
    int sampleRate = bigToLittleEndian(dStream.readInt());
    
    dStream.skip(6);
    
    // 2 little
    // TODO: Dangerous => Should be used!
    int bitsPerSample = bigToLittleEndian(dStream.readShort());

    dStream.skip( 4 );
    
    // 4 little
    int subChunk2Size = bigToLittleEndian(dStream.readInt());

    InternalFormat internalFormat = new InternalFormat( tag, sampleRate, numChannels );
    internalFormat.setFileStatus( new FileStatus( path, filename ) );
    
    int written = 0;
    byte b[] = new byte[IO_CHUNK_SIZE];
    
    while( written < subChunk2Size )
    {
      int read = dStream.read(b);
      if( read < IO_CHUNK_SIZE )
      {
        byte[] bTemp = new byte[read];
        System.arraycopy( b, 0, bTemp, 0, read );
        b = bTemp;
      }
        
      internalFormat.insertSamples( written, b );
      written += b.length;
    }

    return internalFormat;
  }

  private static int bigToLittleEndian(int bigendian) {  
    ByteBuffer buf = ByteBuffer.allocate(4);

    buf.order(ByteOrder.BIG_ENDIAN);
    buf.putInt(bigendian);

    buf.order(ByteOrder.LITTLE_ENDIAN);
    return buf.getInt(0);
  }

  private static int bigToLittleEndian(short bigendian) {  
    ByteBuffer buf = ByteBuffer.allocate(2);

    buf.order(ByteOrder.BIG_ENDIAN);
    buf.putShort(bigendian);

    buf.order(ByteOrder.LITTLE_ENDIAN);
    return buf.getShort(0);
}
}
