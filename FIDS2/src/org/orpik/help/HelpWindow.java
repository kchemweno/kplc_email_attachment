/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.help;

/**
 *
 * @author SYSTEM
 */
/**
 * This class creates a frame with a JEditorPane for loading HTML
 * help files
 */
//package goes here
import java.io.*;
import javax.swing.event.*;
import javax.swing.*;
import java.net.*;
import java.awt.event.*;
import java.awt.*;

public class HelpWindow extends JFrame implements ActionListener {

    private final int WIDTH = 1000;
    private final int HEIGHT = 750;
    private JEditorPane epHelpViewer;
    private URL helpURL;

    /**
     * HelpWindow constructor
     * @param String and URL
     */
    public HelpWindow(String title, URL hlpURL) {
        super(title);
        helpURL = hlpURL;
        epHelpViewer = new JEditorPane();
        epHelpViewer.setEditable(false);
        try {
            epHelpViewer.setPage(helpURL);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //anonymous inner listener
        epHelpViewer.addHyperlinkListener(new HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent ev) {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    if (ev.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                        epHelpViewer.setPage(ev.getURL());
                    }
                } catch (IOException ex) {
                    //put message in window
                    ex.printStackTrace();
                        } catch (UnsupportedLookAndFeelException uslfEx) {
            System.err.println("Unsupported Look and Feel:" + uslfEx.getMessage());
        } catch (ClassNotFoundException cnfEx) {
            System.err.println("Class Not Found:" + cnfEx.getMessage());
        } catch (InstantiationException iEx) {
            System.err.println("Instantiation:" + iEx.getMessage() + ":Details:" + iEx.toString());
        } catch (IllegalAccessException iAEx) {
            System.err.println("Illegal Access:" + iAEx.getMessage() + ":Details:" + iAEx.toString());
        } catch (NullPointerException nullPointerException) {
            System.err.println("General Error:" + nullPointerException.getMessage() + ":Details:" + nullPointerException.toString());
        }
            }
        });
        getContentPane().add(new JScrollPane(epHelpViewer));
        addButtons();
        // no need for listener just dispose
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        calculateLocation();
        setVisible(true);
        // end constructor
    }

    /**
     * An Actionlistener so must implement this method
     *
     */
    public void actionPerformed(ActionEvent e) {
        String strAction = e.getActionCommand();
        URL tempURL;
        try {
            /*if (strAction.equals("Contents")) {
                tempURL = epHelpViewer.getPage();
                epHelpViewer.setPage(helpURL);
            }*/
            if (strAction.equals("Close")) {// Java Help Files Page 3
// more portable if delegated
                processWindowEvent(new WindowEvent(this,
                        WindowEvent.WINDOW_CLOSING));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * add buttons at the south
     */
    private void addButtons() {
        //JButton btncontents = new JButton("Contents");
        //btncontents.addActionListener(this);
        JButton btnclose = new JButton("Close");
        btnclose.addActionListener(this);
        //put into JPanel
        JPanel panebuttons = new JPanel();
        //panebuttons.add(btncontents);
        panebuttons.add(btnclose);
        //add panel south
        getContentPane().add(panebuttons, BorderLayout.SOUTH);
    }

    /**
     * locate in middle of screen
     */
    private void calculateLocation() {
        Dimension screendim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(new Dimension(WIDTH, HEIGHT));
        int locationx = (screendim.width - WIDTH) / 2;
        int locationy = (screendim.height - HEIGHT) / 2;
        setLocation(locationx, locationy);
    }

    public static void main(String[] args) {

        URL index = ClassLoader.getSystemResource("help.htm");
        new HelpWindow("FIDS User Guide", index);
    }
}
//end HelpWindow class

