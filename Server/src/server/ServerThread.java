/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MinhVu
 */
public class ServerThread implements Runnable{
    Thread t;
    
    ServerThread(){
        t = new Thread(this);
        t.start();
    }
    @Override
    public void run() {
        try {
            while(true){
                Socket clientSocket = Server.s.accept();
                ServerHandleClient clientThread = new ServerHandleClient(clientSocket);
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
