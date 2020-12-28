/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import static client.Client.s;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MinhVu
 */
public class SendThread implements Runnable{
    Thread t = null;
    boolean isRunning = false;
    SendThread(){
        t = new Thread(this);
        t.start();
    }
    
    @Override
    public void run() {
        try{
            OutputStream os=Client.s.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

            System.out.println("Talking to Server");
            while(true){
                if (!Client.SendMsg.isEmpty()){
                    if (Client.SendMsg.charAt(0) == '4'){ // upload file
                        String[] temp = Client.SendMsg.split(",", 4);   // get file path
                        File f = new File(temp[3]);
                        
                        ProgressBar frame = new ProgressBar(f);
                        frame.pack();
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);
                        frame.displayProgress();
                        
                        String path = "," + temp[3];
                        Client.SendMsg = Client.SendMsg.replace(path, "");
                        
                        System.out.println("upload " + Client.SendMsg);
                        bw.write(Client.SendMsg);
                        bw.newLine();
                        bw.flush();
                        
                        frame.displayProgress();
                    }
                    else{
                        System.out.println("sent " + Client.SendMsg);
                        bw.write(Client.SendMsg);
                        bw.newLine();
                        bw.flush();
                    }
                    
                    Client.SendMsg = "";
                }
                t.sleep(200);
            }
            
        }
        catch(IOException e)
        {
            System.out.println("There're some error");
        } catch (InterruptedException ex) {
            Logger.getLogger(SendThread.class.getName()).log(Level.SEVERE, null, ex);
        }
   
    }
    
}
