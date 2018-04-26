/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

/**
 *
 * @author Anush kumar.v
 * @author Aditya Chandra
 */
import com.google.gson.Gson;
import java.io.*;
import java.util.*;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
public class BookmarkController {
    private HashMap<String,Date> bmark;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static BookmarkController bmark_obj;
    private static Gson gson = new Gson();
    private BookmarkController(){
        String configFile = System.getProperty("user.home")+"/.jivebookmark";
    	
        try {           
            BufferedReader br = new BufferedReader(new FileReader(configFile));
            String jsonString = br.readLine();
            Type BookmarkCollection = new TypeToken<HashMap<String,Date>>(){}.getType();
            bmark = gson.fromJson(jsonString, BookmarkCollection);
            
            br.close();
        } 
        catch(Exception e){
            System.out.println("From constructor "+e);
        }
        finally{
            if(bmark == null){
                bmark = new HashMap<String,Date>();
            }
        }
       
    }
    public static BookmarkController getInstance(){
        if(bmark_obj == null)
            bmark_obj = new BookmarkController();
        return bmark_obj;
    }
    public void addBookmark(String url,Date timestamp)throws Exception{
        
        bmark.put(url,timestamp);
        String json = gson.toJson(bmark);
    	String home = System.getProperty("user.home");
    	File configFile = new File(home + "/.jivebookmark");
    	
    	PrintWriter pw = new PrintWriter(new FileWriter(configFile));
    	try {	
    		pw.write(json);    		
    	} finally {
    		pw.close();
    	}
        
        //JOptionPane.showMessageDialog(cmpnt, pw, json, 0);
    }
    public String showBookmarks()throws Exception{
        
    	//reads HashMap from file and deserializes before returning
    	
        String html="<!DOCTYPE html><html><head><title>Bookmarks</title></head><body><h1>Bookmarks</h1><table><tr><td>URL</td><td>Time</td></tr>";
        for (Map.Entry<String, Date> entry : bmark.entrySet())
        {
            html+="<tr><td><a href='" + entry.getKey()+"'>"+ entry.getKey() + "</a></td><td>"+ sdf.format(entry.getValue())+"</td></tr>";
        }
        html+="</table></body></html>";
        return html;
        
    }
    
}
