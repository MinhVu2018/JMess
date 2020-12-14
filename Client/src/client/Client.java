/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;
import java.io.*;
import java.net.*;
/**
 *
 * @author MinhVu
 */
public class Client {
    
    public static void main(String[] args) {
        Client_View screen = new Client_View();
        screen.setVisible(true);
        
        screen.Connect();
    }
    
}
