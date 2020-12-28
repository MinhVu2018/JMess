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
public class ClientHandleThread implements Runnable{
    SendThread send = new SendThread();
    ReceiveThread receive = new ReceiveThread();  
    Thread t;
    
    ClientHandleThread(){
        t = new Thread(this);
        t.start();
    }
    @Override
    public void run() {
        Client.login_screen.setVisible(true);
        try {
            send.t.join();
            receive.t.join();
        } catch (InterruptedException e) {
            System.out.println("Main thread Interrupted");
        }
    }
    
}
