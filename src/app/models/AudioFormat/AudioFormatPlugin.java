package src.app.models.AudioFormat;

import src.app.models.InternalFormat;

public interface AudioFormatPlugin
{
  InternalFormat importFile( String path, String filename ) throws Exception;

  void exportFile( InternalFormat internalFormat, String path, String filename ) throws Exception;
}
