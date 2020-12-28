/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.Socket;

/**
 *
 * @author MinhVu
 */
public class ClientConnection {
    public Socket s;
    public String username;
    
    ClientConnection(Socket socket, String name){
        s = socket;
        username = name;
    }
}
