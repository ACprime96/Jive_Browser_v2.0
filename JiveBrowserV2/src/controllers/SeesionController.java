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
import java.io.*;
import java.util.*;
import components.BrowserTab;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
public class SeesionController {
    HashMap<Integer,BrowserTab> ssmap = new HashMap<Integer,BrowserTab>();
    HashMap<Integer,String> storemap;
    ArrayList<HashMap<Integer,String>> sslist;
    public void getinfo(int i,BrowserTab bt){
        ssmap.put(i,bt);
    }
    public void show(){
        storemap = new HashMap<Integer,String>();
        for (Map.Entry<Integer, BrowserTab> entry : ssmap.entrySet()) {
            int key = entry.getKey();
            BrowserTab value = entry.getValue();
//            System.out.println(value.browser.getURL());
            storemap.put(key,value.browser.getURL());
        }
    }
    public void remove(int i){
        ssmap.remove(i);
    }
    public void saveObject() throws IOException {
        
        System.out.println("SESSION SAVED!!");
        sslist.add(storemap);
       
        String configFile = System.getProperty("user.home")+"/.session";
        System.out.println(configFile);
        File sess = new File(configFile);
       
        if(!sess.exists()) {
            sess.createNewFile();
        }
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(sess));
        
        oos.writeObject(sslist);
        oos.flush();
        oos.close();
    }
    public void loadObject() throws IOException, ClassNotFoundException{
        String configFile = System.getProperty("user.home")+"/.session";
        File sess = new File(configFile);
        if(!sess.exists()) {
            sess.createNewFile();
            
            sslist = new ArrayList<HashMap<Integer,String>>();
            return;
        }
        System.out.println("Here");
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(sess));
        
        Object result = ois.readObject();
        
        sslist = (ArrayList<HashMap<Integer,String>>)result;
        
        
        
        
    }
    public void display(){
        System.out.println("SESSION RESTORED!!");
        for(HashMap<Integer,String> hm : sslist){
            for(Map.Entry<Integer,String> entry : hm.entrySet()){
                
                System.out.println(entry.getValue());
            }
            
            System.out.println("");
        }
        
    }
}
