/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

/**
 *
 * @author MinhVu
 */
import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class ProgressBar extends JFrame {
    JProgressBar curProgressBar = new JProgressBar(0, 100);
    DataOutputStream dOut; 
    File file;
    public ProgressBar(File f) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel pane = new JPanel(); 
        curProgressBar.setValue(0);
        curProgressBar.setStringPainted(true);
        pane.add(curProgressBar);
        setContentPane(pane);
        pack();
        file = f;
    }
    
    int sentByte = 0;
    public void displayProgress() {
        SwingWorker worker = new SwingWorker<Object, Object>() {
            @Override
            protected Object doInBackground() throws Exception {
                dOut = new DataOutputStream(Client.s.getOutputStream()) ;
                byte[] array = Files.readAllBytes(Paths.get(file.getPath()));
                dOut.writeInt(array.length);
                
                dOut.write(array);
                dOut.flush();
                
                byte[] buffer = new byte[512];
                while (sentByte < array.length) {
                    try {
                        Thread.sleep(125);
                    } catch (InterruptedException e) {
                    }
                    sentByte += 512;
                    int p = Math.round(100*sentByte/array.length);
                    setProgress(p);
                }
                return null;
            }
            
            @Override
            protected void done(){
                setProgress(100);
            }
        };
        worker.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String name = evt.getPropertyName();
                if ("progress".equals(name)) {
                    SwingWorker worker = (SwingWorker) evt.getSource();
                    curProgressBar.setValue(worker.getProgress());
                } 
            }
        });
        worker.execute();
    }

    //for testing the app
    public static void main(String[] arguments) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    System.out.println("Something error");
                }
            }
        });
    }
}