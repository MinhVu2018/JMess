/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
/**
 *
 * @author MinhVu
 */
public class ServerHandleClient implements Runnable{
    ClientConnection curClient;
    boolean isRunning = false;
    Thread t;
    
    ServerHandleClient(Socket s){
        t = new Thread(this);
        t.start();
        curClient = new ClientConnection(s,"");
        isRunning = true;
    }
    @Override
    public void run() {
        BufferedReader in = null;
        BufferedWriter out = null;
        
        try{
            in = new BufferedReader(new InputStreamReader(curClient.s.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(curClient.s.getOutputStream()));
            while(true){
                // read incoming stream
                String clientCommand = in.readLine();
                System.out.println("Client says:" + clientCommand);
                String[] temp = clientCommand.split(",",3);
                
                switch(temp[0]){
                    case "0":   // log out but dont need :)))
                        System.out.println("Log Out");
                        break;
                    case "1":   // log in
                        System.out.println("Log In");
                        if (userLogIn(temp[1], temp[2])){
                            System.out.println("Success");
                            out.write("1,success");
                            out.newLine();
                            out.flush();
                            
                            curClient.username = temp[1];
                            Server.clients.add(curClient);
                        }
                        else{
                            System.out.println("Fail");
                            out.write("1,fail");
                            out.newLine();
                            out.flush();
                        }
                        break;
                    case "2": // sign up
                        System.out.println("Sign Up");
                        if (userSignUp(temp[1], temp[2])){
                            out.write("2,success");
                            out.newLine();
                            out.flush();
                        }
                        else{
                            out.write("2,fail");
                            out.newLine();
                            out.flush();
                        }
                        break;
                    case "3":   // client send a message
                        System.out.println("Msg");
                        sendToAllClients(temp[0], temp[1], temp[2]);
                        break;
                    case "4":   // client upload a file
                        System.out.println("Upload file");
                        
                        DataInputStream dIn = new DataInputStream(curClient.s.getInputStream());
                        
                        int length = dIn.readInt();
                        System.out.println("Byte length: " + length);
                        
                        byte[] bytes = new byte[length];
                        dIn.readFully(bytes, 0, bytes.length); // read the message
                        
                        Files.write(Paths.get("D:/" + temp[2]), bytes);
                        Server.files.add(temp[2]);
                        System.out.println("Available" + dIn.available());
                        dIn.skip(length);
                        
                        //Notify to all clients
                        sendToAllClients("3", temp[1], "uploaded a new file(" + temp[2] + ")");
                        
                        dIn.skip(dIn.available());
                        
                        break;
                    case "5":   // show list files
                        System.out.println("Show list of files");
                        String msg = "";
                        System.out.println("Number of files: " + Server.clients.size());
                        for (int i=0; i<Server.files.size(); i++){
                            msg += Server.files.get(i) + ",";
                        }
                        msg = msg.substring(0, msg.length()-1);
                    
                        out.write("5,," + msg);
                        out.newLine();
                        out.flush();
                        break;
                    case "6":   // show list members
                        System.out.println("Show list of members");
                        
                        msg = "";
                        System.out.println("Number of clients: " + Server.clients.size());
                        for (int i=0; i<Server.clients.size(); i++){
                            msg += Server.clients.get(i).username + ",";
                        }
                        msg = msg.substring(0, msg.length()-1);
                    
                        out.write("6,," + msg);
                        out.newLine();
                        out.flush();
                        break;
                    case "7":   // client download file
                        System.out.println("Download file");
                        
                        out.write(temp[0]+","+temp[1]+","+temp[2]);
                        out.newLine();
                        out.flush();
                        
                        File file = new File("D:/" + temp[2]); // default for server
                        
                        DataOutputStream dOut = new DataOutputStream(curClient.s.getOutputStream()) ;
                        byte[] array = Files.readAllBytes(Paths.get(file.getPath()));
                        dOut.writeInt(array.length);
                        dOut.write(array);
                        dOut.flush();
                        
                        break;
                        
                    default:
                        break;
                }
            }
        }
        catch(IOException e){ 
            e.printStackTrace(); 
        } 
        finally{ // Clean up 
            try
            {                   
                in.close(); 
                out.close(); 
                curClient.s.close(); 
                Server.clients.remove(curClient);
            } 
            catch(IOException e) 
            { 
                e.printStackTrace(); 
            } 
        } 
    }
    
    private boolean userLogIn(String username, String password){
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader(Server.file_path));
            String line = reader.readLine();
            while(line != null){
                String temp = username + "," + password;
                if (temp.equals(line)){
                    reader.close();
                    return true;
                }
                line = reader.readLine();
            }
            reader.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        
        return false;
    }
    
    private boolean checkUserExist(String username){
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader(Server.file_path));
            String line = reader.readLine();
            while(line != null){
                String[] temp = line.split(",", 2);
                if (username.equals(temp[0])){
                    reader.close();
                    return true;
                }
                line = reader.readLine();
            }
            reader.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return false;
    }
    
    private boolean userSignUp(String username, String password){
        if (checkUserExist(username))
            return false;
        
        BufferedWriter writer;
        try{
            // flag true means append
            writer = new BufferedWriter(new FileWriter(Server.file_path, true));
            writer.newLine();
            writer.write(username+","+password);
            writer.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return true;
    }
    
    private void sendToAllClients(String flag, String username, String msg){
        for(int i=0; i<Server.clients.size(); i++){
            try{
                Socket s = Server.clients.get(i).s;
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                out.write(flag + "," + username + "," + msg);
                out.newLine();
                out.flush();
            }catch(IOException e){
                System.err.println("sth wrong");
            }
        }
    }
}
