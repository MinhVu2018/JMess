/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
/**
 *
 * @author MinhVu
 */
public class Server {
    public static List<ClientConnection> clients = new ArrayList<ClientConnection>();
    public static List<String> files = new ArrayList<String>();
    public static String file_path = "./Data/user.txt";
    public static ServerSocket s;
    
    public static void main(String[] args) throws InterruptedException{
        try{
            s = new ServerSocket(3200);
            ServerThread t = new ServerThread();
        }
        catch(IOException e){
            System.out.println("Something wrong!");
        }
               
    }
    
}