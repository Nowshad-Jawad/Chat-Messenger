import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class my_server extends JFrame {
        
   private JTextArea chatroom;
   private JTextField chatbox;
   private JButton enter;
   private ObjectInputStream input;
   private ObjectOutputStream output;
   private Socket connection;
   private ServerSocket Andromeda;
   
   public my_server(){
   
   super("MiniMessenger!");
   chatbox = new JTextField();
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
   
   chatroom = new JTextArea();
   add(new JScrollPane(chatroom));
   setSize(600,400);
   setVisible(true);
   
   
   }
   
   public void startRunning(){
   	
   	try{
   	
   	Andromeda = new ServerSocket(7037);   	
   		
   	while(true){
   		
   	try{
   			
   		waitForConnection();
   		setupStreams();
   		whileChatting();	
   			
   	}catch(EOFException e){
   		
   		System.out.print("Serever Ended the Conversation!!!");
   		
   	}finally {
   		
   		closeTasks();
   		
   		}
   		
   	}
   	
   	   						
   	 }catch(IOException e){
   	 
   	 System.out.print("Unable To Run!!! ");
   	 
   	 }
   	
   	}
   	
   	private void waitForConnection() throws IOException{
   		
   		showmessage("Waiting For Connection...");
   		connection=Andromeda.accept();
   		showmessage("\nConnected to "+connection.getInetAddress().getHostName());
   		
   		}
   		
   	private void setupStreams() throws IOException{
   		
   		output=new ObjectOutputStream(connection.getOutputStream());
   		output.flush();
   		input=new ObjectInputStream(connection.getInputStream());
   		
   		showmessage("\nStreams Are Ready.");
   		
   		}
   		
   	private void whileChatting() throws IOException{
   		
   		
   		String message="Messenger Is Ready.";
   		sendmessage(message);
   		ableToType(true);
   		do {
   		
   		try{
   			
   		message=(String)input.readObject();	
   		showmessage("\n"+message);
   		
   		}catch(Exception e){
   			
   			System.out.print("ERROR");
   			
   			}
   		
   			
   		}while(!message.equals("CLIENT - END"));
   		
   		}
   		
   	private void closeTasks(){
   		
   		showmessage("\n Closing Connections...");
   		ableToType(false);
   		
   		try{
   			
   			output.close();
   			input.close();
   			connection.close();
   			
   			}catch(IOException e){
   				
   				System.out.print("Unable To Close!!!");
   				
   			}
   		
   		}
   		
   	private void sendmessage(String message){
   		
   		try{
   			
   			output.writeObject("SERVER - "+message);
   			output.flush();
   			showmessage("\nSERVER - "+message);
   			
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





























