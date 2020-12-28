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
    public static Socket s;
    public static String name = "";
    
    public static String SendMsg = "";
    public static String ReceiveMsg = "";
    public static String Conversation = "";
    
    public static Client_LogIn login_screen = new Client_LogIn();
    public static Client_SignUp signup_screen = new Client_SignUp();
    public static Client_View main_screen = new Client_View();
    static void ConnectToServer(){
        try{
            s = new Socket("localhost", 3200);
            ClientHandleThread t = new ClientHandleThread();
        }catch(IOException e){
            System.out.println("There're some error");
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        ConnectToServer();
    }
    
}
