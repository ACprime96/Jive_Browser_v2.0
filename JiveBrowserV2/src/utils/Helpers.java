/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;
/**
 *
 * @author Anush kumar.v
 * @author Aditya Chandra
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;


import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
public class Helpers {
    public static boolean ASC = true;
    public static boolean DESC = false;
    private static Gson gson = new Gson();
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final String ABOUT_INFORMATION = "Jive Browser v2.0\nA Java Internet Viewing Experience\nJive Browser v1.0 was Developed by Arjun Rao and Anisha Mascarenhas\nIt was further extended to v2.0 by Anush Kumar V and Aditya Chandra\n\nIn Jive Browser ver2.0 we have added features like:\nMenu-bar Features:\n-> File >> New Window,New Tab,Open File,Close Tab,Close Window\n-> View >> History,Bookmarks,Session-save,Session-restore\n-> Help >> About JiveBrowser\n"
            + "In search panel we also have\nOptions >> Save,Save as,Print\nEdit >> Copy,Cut,Paste,Select All\nBookmark,Forward and Backward navigation buttons as well!!";
    // Used by HistoryController to sort history data by timestamps.
    public static Map<String, Date> sortByDate(Map<String, Date> unsortMap, final boolean order)
    {

        List<Entry<String, Date>> list = new LinkedList<Entry<String, Date>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Date>>()
        {
            public int compare(Entry<String, Date> o1,
                    Entry<String, Date> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Date> sortedMap = new LinkedHashMap<String, Date>();
        for (Entry<String, Date> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
    
    
    // Used to read history data from file and set up HashMap
    public static HashMap<String,Date> readHistory() throws IOException
    {
    	HashMap<String,Date> history;
    	//reads HashMap from file and deserializes before returning
    	String configFile = System.getProperty("user.home")+"/.jivehistory";
    	BufferedReader br = new BufferedReader(new FileReader(configFile));
        try {            
            String jsonString = br.readLine();
            Type historyCollection = new TypeToken<HashMap<String,Date>>(){}.getType();
            history = gson.fromJson(jsonString, historyCollection);
        } finally {
            br.close();
        }
    	 
    	return history;
    }
    
    // Used to write history data from hashmap to file.
    public static void writeHistory(HashMap<String,Date> map) throws IOException
    {
    	//Writes map to file by serializing 
    	String json = gson.toJson(map);
    	String home = System.getProperty("user.home");
    	File configFile = new File(home + "/.jivehistory");
    	
    	PrintWriter pw = new PrintWriter(new FileWriter(configFile));
    	try {	
    		pw.write(json);    		
    	} finally {
    		pw.close();
    	}
    	
    }
    
    // Used to set title of a tab and add ellipses ("...") in case of long titles
    public static String trimTitle(String title)
    {
    	String trimmedTitle = title;
    	if(title.length() > 10)
    	{
    		trimmedTitle = title.substring(0, 8) + "...";
    	}
    	return trimmedTitle;
    }
    
    
    // Used to get history data as a html string.
    public static String printMap(Map<String, Date> map)
    {
    	String html="<!DOCTYPE html><html><head><title>History</title></head><body><h1>History</h1><table><tr><td>URL</td><td>Time</td></tr>";
        for (Entry<String, Date> entry : map.entrySet())
        {
            html+="<tr><td><a href='" + entry.getKey()+"'>"+ entry.getKey() + "</a></td><td>"+ sdf.format(entry.getValue())+"</td></tr>";
        }
        html+="</table></body></html>";
        return html;
    }
    
    
}
