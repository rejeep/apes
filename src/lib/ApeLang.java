package apes.lib;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Intended to parse a ApeLang file and store it for later use.
 *
 * @author Simon Holm
 */
public class ApeLang
{
  private Map<String, String> dictionary;
  private static final int INDENTATION = 2;

  /**
   * Constructor, parses an ApeLang file.
   *
   * @param path The path to the location of the file to read from.
   * @param file The name of the file to read.
   * @throws Exception Throws exception when the parsing goes wrong.
   */
  public ApeLang( String path, String file ) throws Exception
  {
    dictionary = new HashMap<String, String>();
    parse( path, file );
  }

  private void parse( String path, String file ) throws Exception
  {
    InputStreamReader inputStreamReader;
    inputStreamReader = new InputStreamReader( new FileInputStream( path + "/" + file ) );
    BufferedReader bufferedReader = new BufferedReader( inputStreamReader );

    int depth = 0;
    int nLine = 0;

    String group = "";
    String line;

    //TODO: Speed up
    while( (line = bufferedReader.readLine() ) != null )
    {
      depth = 0;
      for(int i = 0; line.charAt(i) == ' '; ++i)
        if((i & 1) == 1)
          depth++;

      if( depth == 0 || line.charAt( depth * INDENTATION - 1 ) == ' ' )
      {
        // Pattern that matches a tag with following text: "key: Some text"
        Pattern withTextPattern = Pattern.compile( "^\\s*([a-z_]+):\\s+(.+)$" );
        Matcher withTextMatcher = withTextPattern.matcher( line );

        // Pattern that matches a tag without any following text: "key:"
        Pattern withoutTextPattern = Pattern.compile( "^\\s*([a-z_]+):\\s*$" );
        Matcher withoutTextMatcher = withoutTextPattern.matcher( line );

        String[] tokens = group.split( "\\." );
        group = "";

        for( int i = 0; i < depth; ++i )
        {
          group = group + "." + tokens[i + 1];
        }

        if( withoutTextMatcher.matches() )
        {
          String key = withoutTextMatcher.group( 1 );
          group = group + "." + key;
          ++depth;
        }
        else if( withTextMatcher.matches() )
        {
          String key = withTextMatcher.group( 1 );
          String text = withTextMatcher.group( 2 );

          group += "." + key;

          dictionary.put( group.substring( 1, group.length() ), text );
        }
        else
        {
          throw new Exception( "Syntax error: Line " + nLine + ", bad word match." );
        }
      }
      else
      {
        throw new Exception( "Syntax error: Line " + nLine + ", bad whitespace match." );
      }

      ++nLine;
    }
  }

  /**
   * Looks for the value corresponding to the key.
   *
   * @param key The key for the word.
   * @return Returns the word that corresponds to the key in the dictionary.
   */
  public String get( String key )
  {
    return dictionary.get( key );
  }
}
