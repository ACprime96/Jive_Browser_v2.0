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
import java.util.ArrayList;
import components.BrowserTab;
public class SessionHelper {
    ArrayList<BrowserTab> al;
    static SessionHelper sh;
    private SessionHelper(){
        if(al==null){
            al = new ArrayList<BrowserTab>();
        }
    }
    public static SessionHelper getInstance(){
        if(sh == null){
            sh = new SessionHelper();
        }
        return sh;
    }
    public void store_ref(BrowserTab bt){
        al.add(bt);
    }
    public void del_ref(BrowserTab bt){
        al.remove(bt);
        for(BrowserTab b : al){
            System.out.println(b);
        }
    }
    
    
}
