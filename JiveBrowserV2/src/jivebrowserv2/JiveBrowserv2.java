/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jivebrowserv2;

/**
 *
 * @author Anush kumar.v
 * @author Aditya Chandra
 */
import components.*;
import controllers.SeesionController;
public class JiveBrowserv2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        BrowserWindow mainFrame = new BrowserWindow("Jive Browser",new SeesionController());    	
    	mainFrame.initialize();
    }
    
}
