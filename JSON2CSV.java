/**
javac -cp .;java-json.jar JSON2CSV.java
java -cp .;java-json.jar JSON2CSV <filename>.json <result-node>
java -cp .;java-json.jar JSON2CSV <filename>.csv <result-node>
*/

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSON2CSV 
{
  public static void main (String args[])
  {
    try
    {
      if (args [0].endsWith (".json"))
        json2csv (args [0], args [1]);
      else if (args [0].endsWith (".csv"))
        csv2json (args [0], args [1]);
	  }
	  catch (Exception e)
	  {
      e.printStackTrace ();
      System.err.println ("USAGE: java -cp .;java-json.jar JSON2CSV <json-file> <results-node>");
    }
  }

  public static void csv2json (String inFile, String nodeName) throws IOException, JSONException
  {
    String csvData = readFile (inFile, Charset.defaultCharset ());
    JSONObject jObject = new JSONObject ();
    jObject.put (nodeName, CDL.toJSONArray (csvData));
    writeFile (inFile.replace (".csv", ".json") , jObject.toString (2));  // pretty print with 2 space indent
  }

  public static void json2csv (String inFile, String nodeName) throws IOException, JSONException
  {
    String jsonString = readFile (inFile, Charset.defaultCharset ());
    JSONObject json = new JSONObject (jsonString);
    String csvString = CDL.toString (new JSONArray (json.get (nodeName).toString ()));
    writeFile (inFile.replace (".json", ".csv") , csvString);
  }

	public static void writeFile (String fileName, String fileContents) throws IOException
  {
    Files.write (Paths.get (fileName), fileContents.getBytes (), StandardOpenOption.CREATE);
  }

	public static String readFile (String fileName, Charset fileEncoding) throws IOException
	{
    return new String (Files.readAllBytes (Paths.get (fileName)), fileEncoding);
	}
}
