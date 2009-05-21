package apes.models;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import apes.views.ApesError;


/**
 * Generic class for classes that works against a configuration file.
 * 
 * @author Johan Andersson (johandy@student.chalmers.se)
 */
public abstract class ApesConfiguration
{
  /**
   * The configuration file.
   */
  private File configurationFile;

  /**
   * The default regex to use when parsing the configuration file.
   */
  private String regex = "^([a-z_]+)\\s*=\\s*([\"]?)(.*)\\2$";

  /**
   * The map that contains all options.
   */
  protected Map<String, String> options;

  public ApesConfiguration()
  {
    options = new TreeMap<String, String>();

    String configurationFolderPath = System.getProperty("user.home") + File.separator + ".apes";
    String configurationFileName = getConfigurationFileName();

    // Make sure folders and files exists.
    String validPath = getConfigurationFolderPath(configurationFolderPath);
    String validFile = getConfigurationFileName(validPath, configurationFileName);

    configurationFile = new File(validFile);
  }

  /**
   * Creates the configuration folder if it does not exists already.
   * 
   * @param configurationFolderPath Path to the configuration folder.
   * @return The absolute path to the configuration folder.
   */
  private String getConfigurationFolderPath(String configurationFolderPath)
  {
    File directory = new File(configurationFolderPath);

    // Create folder if it does not exist.
    // TODO: Show apes message?
    if(!directory.isDirectory())
    {
      directory.mkdir();
    }

    return directory.getAbsolutePath();
  }

  /**
   * Creates the configuration file if it does not exists already.
   * 
   * @param configurationFolderPath The absolute path to the configuration
   *          folder.
   * @param configurationFileName The name of the configuration file.
   * @return The absolute path to the configuration file.
   */
  public String getConfigurationFileName(String configurationFolderPath, String configurationFileName)
  {
    File file = new File(configurationFolderPath, configurationFileName);

    // Create file if it does not exist.
    if(!file.exists())
    {
      try
      {
        file.createNewFile();
      }
      catch(IOException e)
      {
        ApesError.couldNotCreateConfigurationFile();
      }
    }

    return file.getAbsolutePath();
  }

  /**
   * Parses the configuration file.
   */
  public void parse()
  {
    try
    {
      Scanner scanner = new Scanner(configurationFile);
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher;
      String line;

      // Go through all lines in configuration file.
      while(scanner.hasNextLine())
      {
        line = scanner.nextLine();
        matcher = pattern.matcher(line);

        // If line matches an option.
        if(matcher.matches())
        {
          // Call match in subclass.
          match(line, matcher);
        }
      }

      scanner.close();
    }
    catch(FileNotFoundException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Save the configuration file.
   */
  public void save()
  {
    try
    {
      // Read in the whole file. This must be done because a file can
      // not be written to while read from.
      // TODO: This can be done with RandomAccessFile
      List<String> lines = new ArrayList<String>();
      Scanner scanner = new Scanner(configurationFile);

      while(scanner.hasNextLine())
      {
        lines.add(scanner.nextLine());
      }
      scanner.close();

      PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(configurationFile)));
      Set<String> added = new HashSet<String>();
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher;
      String key;

      for(String line : lines)
      {
        matcher = pattern.matcher(line);

        if(matcher.matches())
        {
          // TODO: Call down
          key = matcher.group(1);

          out.write(toOption(key));

          added.add(key);
        }
        else
        {
          out.write(line);
        }

        out.println();
      }

      // Add options that where not in the file already.
      for(String option : options.keySet())
      {
        if(!added.contains(option))
        {
          out.write(toOption(option));

          out.println();
        }
      }

      out.close();
    }
    catch(FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Returns a string of an option.
   * 
   * @param key The key option.
   * @return The option as a string (key = value).
   */
  private String toOption(String key)
  {
    return key + " = " + options.get(key);
  }

  /**
   * Sets the regexp to use when parsing.
   * 
   * @param regex The regex.
   */
  public void setRegex(String regex)
  {
    this.regex = regex;
  }

  /**
   * Returns all options.
   * 
   * @return All options.
   */
  public Map<String, String> getOptions()
  {
    return options;
  }

  /**
   * Sets the options.
   * 
   * @param options The options.
   */
  public void setOptions(Map<String, String> options)
  {
    this.options = options;
  }

  /**
   * When parsing the file and a match is found, this method is called. If the
   * regex has been changed, it is most likely that this method should be
   * overridden in the subclass.
   * 
   * @param line The line that matches.
   * @param matcher The matcher.
   */
  public void match(String line, Matcher matcher)
  {
    options.put(matcher.group(1), matcher.group(3));
  }

  /**
   * Returns the configuration file name.
   * 
   * @return The file name.
   */
  public abstract String getConfigurationFileName();
}
