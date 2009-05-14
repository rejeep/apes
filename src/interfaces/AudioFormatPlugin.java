package apes.interfaces;

import apes.models.InternalFormat;

/**
 * TODO: Comment
 * 
 * @author Simon Holm
 */
public interface AudioFormatPlugin extends Plugin
{
  /**
   * Returns the extension of the file format.
   * 
   * @return file extension a <code>String</code>
   */
  String getExtension();

  /**
   * Describe <code>importFile</code> method here.
   * 
   * @param path a <code>String</code> value
   * @param filename a <code>String</code> value
   * @return an <code>InternalFormat</code> value
   * @exception Exception if an error occurs
   */
  InternalFormat importFile(String path, String filename) throws Exception;

  /**
   * Describe <code>exportFile</code> method here.
   * 
   * @param internalFormat an <code>InternalFormat</code> value
   * @param path a <code>String</code> value
   * @param filename a <code>String</code> value
   * @exception Exception if an error occurs
   */
  void exportFile(InternalFormat internalFormat, String path, String filename) throws Exception;
}
