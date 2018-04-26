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
import controllers.*;
import utils.*;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.DefaultLoadHandler;
import com.teamdev.jxbrowser.chromium.LoadParams;
import com.teamdev.jxbrowser.chromium.events.FailLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.events.NetError;
import com.teamdev.jxbrowser.chromium.events.TitleEvent;
import com.teamdev.jxbrowser.chromium.events.TitleListener;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import com.teamdev.jxbrowser.chromium.SavePageType;
import com.teamdev.jxbrowser.chromium.*;
import com.teamdev.jxbrowser.chromium.PrintJob;
import com.teamdev.jxbrowser.chromium.events.PrintJobEvent;
import com.teamdev.jxbrowser.chromium.events.PrintJobListener;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.*;
import java.util.List;
import java.io.*;
public class BrowserTab extends JPanel{
    int filecount = 0;
    private static final long serialVersionUID = -8970636815130099199L;
	// These are the buttons for iterating through the navigation-history list.
    private JButton backButton, forwardButton,goButton, bookmark,inde;
    // Browser is a java wrapper around Chromium provided by JxBrowser.
    public Browser browser = new Browser();
    // TextField to store URL
    private JTextField locationTextField;
    //private SeesionController scon = new SeesionController();
    // Store Parent references
    private final JTabbedPane pane; 
    private final int tabIndex; 
    
    //Uses URL below to perform google search on pages that result in 404 errors.
    private static final String GOOGLE_SEARCH = "https://www.google.com/search?q=";
     public BrowserTab(final JTabbedPane pane,final int tabIndex) {
    	
    	this.setLayout(new BorderLayout());    	
        
    	BrowserView mBrowserView = new BrowserView(browser);  
    	//BrowserView is the main swing component that renders web pages using WebKit internally
        mBrowserView.setVisible(true);               
        
        createButtonPanel();
        
        add(mBrowserView,BorderLayout.CENTER);
        
        this.pane = pane;
        this.tabIndex = tabIndex;
        
        //Title listener handles changing of Tab titles dynamically. 
        browser.addTitleListener(new TitleListener(){

			@Override
			public void onTitleChange(TitleEvent event) {
				pane.setTitleAt(tabIndex,Helpers.trimTitle(event.getTitle()));	
				pane.setToolTipTextAt(tabIndex, event.getTitle());
			}
    		
    	});
        SessionHelper.getInstance().store_ref(this);

    }
    
    //Swing Code to generate the URL bar and navigation buttons
    private void createButtonPanel()
    {
    	JPanel buttonPanel = new JPanel();
        JMenuBar menuBarTwo = new JMenuBar();
        
        JMenu editMenu = new JMenu("Edit");
        //Code for copy starts here
        JMenuItem copyItem = new JMenuItem("Copy");
        copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        copyItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                browser.executeCommand(EditorCommand.COPY);
            }
        });
        //Code for copy ends here
       
        //Code for cut starts here
        JMenuItem cutItem = new JMenuItem("Cut");
        cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        cutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                browser.executeCommand(EditorCommand.CUT);
            }
        });
        //Code for cut ends here
       
        //Code for paste starts here
        JMenuItem pasteItem = new JMenuItem("Paste");
        pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
        pasteItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                browser.executeCommand(EditorCommand.PASTE);
            }
        });
        //Code for paste ends here
        
        //Code for select all starts here
        JMenuItem selectItem = new JMenuItem("Select All");
        selectItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        selectItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                browser.executeCommand(EditorCommand.SELECT_ALL);
            }
        });
        //Code for select all ends here
        
        editMenu.add(copyItem);
        editMenu.add(cutItem);
        editMenu.add(pasteItem);
        editMenu.add(selectItem);
        //Code for Edit Menu Ends Here
        menuBarTwo.add(editMenu);
        buttonPanel.add(menuBarTwo);
        
        backButton = new JButton("< Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                browser.goBack();
            }
        });
        backButton.setEnabled(false);
        buttonPanel.add(backButton);
        forwardButton = new JButton("Forward >");
        forwardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                browser.goForward();
            }
        });
        forwardButton.setEnabled(false);
        buttonPanel.add(forwardButton);
        locationTextField = new JTextField(35);
        locationTextField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    actionGo();
                }
            }
        });
        buttonPanel.add(locationTextField);
        goButton = new JButton("GO");
        goButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionGo();
            }
        });
        buttonPanel.add(goButton);
        //adding bookmark button
        bookmark = new JButton("bookmark");
        bookmark.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addBookmark();
            }
        });
        buttonPanel.add(bookmark);
        
        //end of history button

        //DropDownIcon for saving starts here
        JMenuBar menuBar = new JMenuBar();
        
        JMenu savebutton = new JMenu ("Options");

        //Save button starts here 
        JMenuItem saveItem = new JMenuItem("Save");
        
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
           
        saveItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	 filecount++;
                 String filename = System.getProperty("user.home")+File.separator +"JiveBrowser_pages";
                 String filePath = filename+File.separator+"index"+Integer.toString(filecount)+".html";
                 String dirPath = System.getProperty("user.home")+File.separator +"JiveBrowser_dir";
                 browser.saveWebPage(filePath, dirPath, SavePageType.COMPLETE_HTML);
            }
        });
        savebutton.add(saveItem);
        //Save button for saving ends here
        
        //Save as button for saving starts here
        JMenuItem save_asItem = new JMenuItem("Save as");
           
        save_asItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                 String filename=JOptionPane.showInputDialog(pane,"Enter File Name : ");
                 //to save the web pages in this path
                 String filePath = System.getProperty("user.home")+File.separator +"jivebrowser_pages"+File.separator+filename+".html";
                 String dirPath = System.getProperty("user.home")+File.separator +"JiveBrowser_dir";
                 browser.saveWebPage(filePath, dirPath, SavePageType.COMPLETE_HTML);
            }
        });
        savebutton.add(save_asItem);
        //Save as button for saving ends here
        
        /*
            UNCOMMENT THIS SNIPPET FOR PRINT AS PDF FUNCTION
        //Print button for saving starts here
        JMenuItem printItem = new JMenuItem("Print as PDF");
        
        printItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
        printItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    browser.print();   
                    browser.setPrintHandler(new PrintHandler() {
                        public PrintStatus onPrint(PrintJob printJob) {
                            PrintSettings settings = printJob.getPrintSettings();
                            settings.setPrintToPDF(true);
                            //gets file path for saving the pdf generated from print
                            String getFilenameForPrint =JOptionPane.showInputDialog(pane,"Enter File Name : ");
                            String printFilePath = System.getProperty("user.home")+File.separator +"jivebrowser_pages"+File.separator+getFilenameForPrint+".pdf";
                            settings.setPDFFilePath(printFilePath);
                            settings.setPrintBackgrounds(true);
                            return PrintStatus.CONTINUE;
                        }
                    });
        
            }
        });
        savebutton.add(printItem);
        //Print button for saving ends here
*/
         JMenuItem printItm = new JMenuItem("Print");
         
         printItm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browser.print();
                PrinterJob pj = PrinterJob.getPrinterJob();
                if (pj.printDialog()) {
                    try {pj.print();}
                    catch (PrinterException exc) {
                        System.out.println(exc);
                    }
                }   
            }
        });
        

        browser.setPrintHandler(new PrintHandler() {
            @Override
            public PrintStatus onPrint(PrintJob printJob) {
                PrintSettings printSettings = printJob.getPrintSettings();
                printSettings.setPrinterName("Microsoft XPS Document Writer");
                printSettings.setLandscape(false);
                printSettings.setPrintBackgrounds(false);
                printSettings.setColorModel(ColorModel.COLOR);
                printSettings.setDuplexMode(DuplexMode.SIMPLEX);
                printSettings.setDisplayHeaderFooter(true);
                printSettings.setCopies(1);
                printSettings.setPaperSize(PaperSize.ISO_A4);

                List<PageRange> ranges = new ArrayList<PageRange>();
                ranges.add(new PageRange(0, 3));
                printSettings.setPageRanges(ranges);

                printJob.addPrintJobListener(new PrintJobListener() {
                    @Override
                    public void onPrintingDone(PrintJobEvent event) {
                        System.out.println("Printing is finished successfully: " +
                                event.isSuccess());
                    }
                });
                return PrintStatus.CONTINUE;
            }
        });
         savebutton.add(printItm);
        //print settings code starts here
        
        //print settings code ends here
        
        menuBar.add(savebutton);
        buttonPanel.add(menuBar);
        //DropDownIcon for saving ends here
        
        add(buttonPanel, BorderLayout.NORTH);
    }
    
    //Set HTML of the browser to received value
    public void setHTML(String html)
    {
    	browser.loadHTML(html);
    }
    private void addBookmark(){
        
        BookmarkController bmark_obj = BookmarkController.getInstance();
        String url = browser.getURL();
        if(!url.equals("about:blank")){
            try{
                bmark_obj.addBookmark(url, new Date());
                JOptionPane.showMessageDialog(pane, "Bookmark added","Bookmark info",JOptionPane.INFORMATION_MESSAGE);
            }
            catch(Exception e){
                System.out.println("From Bookmark "+e);
            }
        }
    }
    
    // Load and show the page specified in the location text field.
    private void actionGo() {    	
    	browser.setLoadHandler(new DefaultLoadHandler() {
            public boolean onLoad(LoadParams params) {

            	//enable or disable NavigationHistory
            	if(browser.canGoBack()){
            		backButton.setEnabled(true);
            	}
            	else
            	{
            		backButton.setEnabled(false);
            	}
            	
            	if(browser.canGoForward())
            	{
            		forwardButton.setEnabled(true);
            	}
            	else
            	{
            		forwardButton.setEnabled(false);
            	}
            	            	
                // Return false to continue loading
                return false;
            }          
        });
    	
    	//load URL entered at the textField or perform a google search if it fails to load. 
    	browser.loadURL(locationTextField.getText());
        
    	
    	browser.addLoadListener(new LoadAdapter() {
    		
    		 @Override
    		    public void onFinishLoadingFrame(FinishLoadingEvent event) {
    			 //Handle successful load and update History and tab title
    		        if (event.isMainFrame()) {
    		        	pane.setTitleAt(tabIndex,Helpers.trimTitle(browser.getTitle()));
    		        	pane.setToolTipTextAt(tabIndex, browser.getTitle());
    		        	locationTextField.setText(browser.getURL());
    		        	HistoryController.getInstance().addUrl(browser.getURL(), new Date());
    		        	goButton.transferFocus();
    		        }
    		        
    		    }
    		 
    	    @Override
    	    public void onFailLoadingFrame(FailLoadingEvent event) {
    	        //Handle failed loads by performing Google Search instead
    	    	NetError errorCode = event.getErrorCode();
    	        if (errorCode == NetError.NAME_NOT_RESOLVED) {
    	        	browser.loadURL(GOOGLE_SEARCH + locationTextField.getText());
    	        	HistoryController.getInstance().addUrl(browser.getURL(), new Date());
    	        }
    	    }
      	});
    	
    }
    
    @Override
    protected void finalize() throws Throwable{
        try {
            SessionHelper.getInstance().del_ref(this);
        } finally {
            super.finalize();
        }
    }
    
    
    
}
