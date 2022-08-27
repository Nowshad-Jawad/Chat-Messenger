import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Scanner;

public class my_client extends JFrame {

    private JTextField chatbox;
    private JTextArea chatroom;
    private JButton enter;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message="";
    private String ServerIP;
    private Socket connection;
    
    public my_client(String host){
    super("MiniMessenger!");
    ServerIP = host;
    
    chatbox=new JTextField();
    chatbox.setEditable(false);
    chatbox.addActionListener(
    new ActionListener(){
    public void actionPerformed(ActionEvent event){
    sendmessage(event.getActionCommand());
    chatbox.setText("");
        
    }
    
    }
    
    );
   
    add(chatbox,BorderLayout.SOUTH);
    chatbox.setPreferredSize(new Dimension(200,100));
    
    enter=new JButton("Send");
    add(enter,BorderLayout.EAST);
    
    enter.addActionListener(
    new ActionListener(){
    public void actionPerformed(ActionEvent event){
    String message=chatbox.getText();
    sendmessage(message);
    chatbox.setText("");
        
    }
    
    }
    
    );
    
    chatroom=new JTextArea();
    add(new JScrollPane(chatroom));
    setSize(600,400);
    setLocationRelativeTo(null);
    setVisible(true);
    
    
    }
    
    
    public void startRunning(){
    
    try{
    
    connectToServer();
    setupStreams();
    whileChatting();
    
    }catch(EOFException e){
    
    showmessage("\n Client Terminated Connection.");
    
    }catch(IOException e){
    
    showmessage("\nERROR");
        
    }finally{
    
    closeTasks();
    
    }
    
    }
    
    
    private void connectToServer() throws IOException{
    
    showmessage("Attempting Connection...");
    connection=new Socket(InetAddress.getByName(ServerIP),7037);
    showmessage("\nConnected to "+connection.getInetAddress().getHostName());
    
    }
    
    private void setupStreams() throws IOException{
    
    output=new ObjectOutputStream(connection.getOutputStream());
    output.flush();
    input=new ObjectInputStream(connection.getInputStream());
    
    showmessage("\nStreams Are Ready.");
    
    }
    
    private void whileChatting() throws IOException{
    
    ableToType(true);
    do{
    try{
    message=(String)input.readObject();
    showmessage("\n"+message);
    }catch(Exception e){
    showmessage("ERROR");
    }

    }while(!message.equals("SERVER - END"));
    
    }
    
    private void closeTasks(){
    showmessage("\n Closing Connections...");
    ableToType(false);
    try{
    input.close();
    output.close();
    connection.close();
    
    }catch(IOException e){
    showmessage("Unable To Close!!!");   
    }
    
    }
    
    
    private void sendmessage(String message){
    try{
    output.writeObject("CLIENT - "+message);
    output.flush();
    showmessage("\nCLIENT - "+message);
    
    }catch(IOException e){
    chatroom.append("Unable To Send Message!!!");
    }
        
    }
    
    
    private void showmessage(final String text){
    SwingUtilities.invokeLater(
    
    new Runnable(){
    public void run(){
    chatroom.append(text);
    }
    
    }

    );
    
    }
    
    private void ableToType(final boolean tof){
    SwingUtilities.invokeLater(
    new Runnable(){
    public void run(){
    chatbox.setEditable(tof);
    }
    
    }
    
    );
    
    }
            
            
            
}
