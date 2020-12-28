/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author MinhVu
 */
public class ReceiveThread implements Runnable{
    Thread t = null;
    boolean isRunning = false;
    ReceiveThread(){
        t = new Thread(this);
        t.start();
    }
    
    @Override
    public void run() {
        try{
            while(true){
                InputStream is = Client.s.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                Client.ReceiveMsg = br.readLine();
                String[] temp = Client.ReceiveMsg.split(",", 3);
                switch(temp[0]){
                    case "0": // log out
                        break;
                    case "1": // login
                        if(temp[1].equalsIgnoreCase("success")){
                            Client.login_screen.setVisible(false);
                            Client.main_screen.setVisible(true);
                            Client.main_screen.setUsername(Client.name);
                        }
                        else{
                            JOptionPane.showMessageDialog(Client.login_screen, "Log In faled. Please try again");
                        }
                        break;
                        
                    case "2": // sign up
                        if(temp[1].equalsIgnoreCase("success")){
                            Client.signup_screen.setVisible(false);
                            Client.login_screen.setVisible(true);
                        }
                        else{
                            JOptionPane.showMessageDialog(Client.signup_screen, "Sign Up faled. Please try again");
                        }
                        break;
                    
                    case "3":
                        Client.main_screen.addMsg(temp[1], temp[2]);
                        break;
                    case "4":   // upload to server's storage, dont need to receive msg
                        break;
                    case "5":
                        Client.main_screen.listFiles(temp[2]);
                        break;
                    case "6":
                        Client.main_screen.listMembers(temp[2]);
                        break;
                    case "7": //download file
                        JFileChooser folderChooser = new JFileChooser();
                        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        int option = folderChooser.showOpenDialog(Client.main_screen);
                        if(option == JFileChooser.APPROVE_OPTION){
                            File file = folderChooser.getSelectedFile();
                            
                            System.out.println("filepath" + file.getPath());
                            
                            DataInputStream dIn = new DataInputStream(Client.s.getInputStream());
                        
                            int length = dIn.readInt();
                            System.out.println("Byte length: " + length);

                            byte[] bytes = new byte[length];
                            dIn.readFully(bytes, 0, bytes.length); // read the message

                            Files.write(Paths.get(file.getPath()+ "/" + temp[2]), bytes);
                            dIn.skip(dIn.available());
                            
                            Client.main_screen.addMsg(temp[1], "downloaded file(" + temp[2] + ")");
                        }
                        
                        break;
                    default:
                        break;
                }
                
                t.sleep(200);
            }
        }
        catch(IOException e)
        {
            System.out.println("There're some error");
        } catch (InterruptedException ex) {
            Logger.getLogger(ReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
}

