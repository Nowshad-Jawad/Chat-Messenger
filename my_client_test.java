import javax.swing.JFrame;

public class my_client_test {
    
    public static void main(String[] args){
    my_client Damien;
    Damien=new my_client("127.0.0.1");
    Damien.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Damien.startRunning();
    
}
}
