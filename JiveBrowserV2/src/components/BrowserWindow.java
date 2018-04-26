/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

/**
 *
 * @author Anush kumar.v
 * @author Aditya Chandra
 */
import java.io.*;
import com.teamdev.jxbrowser.chromium.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import utils.*;
import controllers.*;
public class BrowserWindow extends JFrame{
    private static final long serialVersionUID = -2709500178705880563L;
	private final JTabbedPane pane = new JTabbedPane();// Tabbed view in browser
    public static int tabCount = 0; 
    public int tabid = -1;
    SeesionController scn = null;
    // Sets up a BrowserWindow with a given window title. Used in RunBrowser to start first browser window. 
    public BrowserWindow(String title, SeesionController scn) {
    	super(title);
        this.scn = scn;
        try{
            scn.loadObject();
        }
        catch(Exception e){
            System.out.println(e+"window constructor");
        }
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // triggered with close button --- exit the application  	
    	initMenuBar(); 
    	addTab(0);
    }
    
    // initialize () is called in RunBrowser after creating a BrowserWindow. 
    // Sets up Swing parameters to display the browser window.
    public void initialize() {
        pane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT); //Behaviour of tabs
        setSize(new Dimension(1024, 768));
        setLocationRelativeTo(null);
        
    	getContentPane().add(pane, BorderLayout.CENTER);
        setVisible(true);
    }
    //adding opentab
    private void addOpenTab(int i,String path){
    	
    	String title = "Open File";
    	BrowserTab openTab = new BrowserTab(pane,i);
    	openTab.browser.loadURL(path);
        pane.add(title,openTab);
        initTabComponent(i);
        pane.setSelectedIndex(i);
    }
    //end of open tab
    
    // Used to add a new tab to the window 
    // parameter i is a unique tab identifier in sequential order.
    private void addTab(int i){
    	String title = "New Tab";
        BrowserTab bt = new BrowserTab(pane,i);
        scn.getinfo(++tabid, bt);
        pane.add(title, bt);
        initTabComponent(i);
        pane.setSelectedIndex(i);
    }
    
    // Used to add a new tab and open the browser's history (Refer HistoryController.java)
    private void addHistoryTab(int i){
    	
    	String html = HistoryController.getInstance().printHistory();
    	String title = "History";
    	BrowserTab historyTab = new BrowserTab(pane,i);
        scn.getinfo(++tabid, historyTab);
    	historyTab.setHTML(html);
        pane.add(title,historyTab);
        initTabComponent(i);
        pane.setSelectedIndex(i);
    }
    private void addSessionTab(int i){
    	
    	
    }
    private void addBookmarkstab(int i){
        try{
            String html = BookmarkController.getInstance().showBookmarks();
            String title = "Bookmarks";
            BrowserTab bookmarkTab = new BrowserTab(pane,i);
            scn.getinfo(++tabid, bookmarkTab);
            bookmarkTab.setHTML(html);
            pane.add(title,bookmarkTab);
            initTabComponent(i);
            pane.setSelectedIndex(i);
        }
        catch(Exception e){
            System.out.println("Unable to load bookmark-browserwindow "+e);
        } 
    }
    
    // Initializes a tab with close button and title being set using TabButt onComponent Class
    // Refer to Swing JTabbedPane examples on official JavaDocs page.
    private void initTabComponent(int i) {
        pane.setTabComponentAt(i,
                 new TabButtonComponent(pane,tabid,scn));
    }    
 
    // Initializes Menu Bar with File, View and Help Menus and sub menu items.
    public void initMenuBar() {
    	
    	// Initializes the main menu bar
    	JMenuBar menuBar = new JMenuBar();
    	
    	//Code for File Menu Starts Here
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        //New Window option starts here
        JMenuItem newWindowMenuItem = new JMenuItem("New Window");
        
        newWindowMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        
        newWindowMenuItem.addActionListener(new ActionListener() {
     
            public void actionPerformed(ActionEvent e) {
            	BrowserWindow newMainFrame = new BrowserWindow("Jive Browser",new SeesionController());    	
                newMainFrame.initialize();
            }
        });
         //New Window option ends here
         
         //Open File starts here
         JMenuItem openMenuItem = new JMenuItem("Open File");
        
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        
        openMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result;
                result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String getfilepath = "file://" + selectedFile.getAbsolutePath();
                    System.out.println(getfilepath);
                    tabCount++;
                    addOpenTab(tabCount,getfilepath);
                }
            }
        });
        //Open File ends here
        
        
        JMenuItem addTabMenuItem = new JMenuItem("New Tab");
        
        
        addTabMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK));
        
        addTabMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               	tabCount++;
            	addTab(tabCount);
            }
        });
        
        JMenuItem closeTabMenuItem = new JMenuItem("Close Tab");
        closeTabMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
        closeTabMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	int i = pane.getSelectedIndex();
                if (i != -1) {
                    pane.remove(i);
                    tabCount--;
                }
                if(pane.getTabCount() == 0){
                	System.exit(0);
                }
            }
        });
        
        JMenuItem closeWindowMenuItem = new JMenuItem("Close Window");
        closeWindowMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	System.exit(0);
            }
        });
        fileMenu.add(newWindowMenuItem);
        fileMenu.add(openMenuItem);
      
        fileMenu.add(addTabMenuItem);
        fileMenu.add(closeTabMenuItem);
        fileMenu.add(closeWindowMenuItem);
        fileMenu.setMnemonic(KeyEvent.VK_F);    	
        //adding saves menu
        
        //Code for File Menu Ends Here                
    	//Code for View Menu Starts Here
        
        JMenu viewMenu = new JMenu("View");
                
        JMenuItem historyMenuItem = new JMenuItem("History");
        historyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK));
        
        historyMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {            	            
            	tabCount++;
            	addHistoryTab(tabCount);
            }
        });
        //adding bookmark option
        JMenuItem BookmarkMenuItem = new JMenuItem("Bookmarks");
        BookmarkMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {            	            
            	tabCount++;
            	addBookmarkstab(tabCount);
            }
        });
        //adding session option
        JMenuItem SessionMenuItem = new JMenuItem("Session-save");
        SessionMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {            	            
            	scn.show();
                try{
                    scn.saveObject();
                }
                catch(Exception c){
                    System.out.println(c+"from save window");
                }
            }
        });
        JMenuItem SessionRestoreItem = new JMenuItem("Session-restore");
        SessionRestoreItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {            	            
            	scn.display();
            }
        });
        viewMenu.add(historyMenuItem);
        viewMenu.add(BookmarkMenuItem);
        viewMenu.add(SessionMenuItem);
        viewMenu.add(SessionRestoreItem);
       
        viewMenu.setMnemonic(KeyEvent.VK_V);
    	
        // Code for View Menu Ends Here
    	// Code for Help Menu Starts Here
        
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutMenuItem = new JMenuItem("About JiveBrowser");
        aboutMenuItem.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(pane, Helpers.ABOUT_INFORMATION,"About Jive",JOptionPane.INFORMATION_MESSAGE);
				
			}
        	
        });
        helpMenu.add(aboutMenuItem);
        helpMenu.setMnemonic(KeyEvent.VK_H);
        
    	// Code for Help Menu Ends Here
        
        
        // Add all sub menus to menubar and menubar to Frame
        
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }  
    
}
