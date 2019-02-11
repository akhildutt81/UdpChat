package chatApp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

public class Client extends JFrame{

    private static final long serialVersionUID = 1L;
    private Thread send;
    private JPanel contentPane;
    private String name,address;
    private int port;
    private JTextField txtMessage;
    private JButton btnSend;
    private JTextArea txthis;
    private DatagramSocket socket;
    private InetAddress ip;
    private int ID=-1;
    Client(String address,String name,int port){
        this.address=address;
        this.port=port;
        this.name=name;
    }

    public boolean openConnectioin(String address){
        try {
            ip=InetAddress.getByName(address);
            socket=new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();

            return false;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
        System.out.println("Conn to port "+socket.getLocalPort());
        return true;
    }
    public String receive(){
       byte[] data=new byte[1024];
        DatagramPacket packet=new DatagramPacket(data,data.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String message=new String(packet.getData(),packet.getOffset(),packet.getLength());
        if(message.startsWith("/c/")){
            System.out.println("Inside receive of client");
            System.out.println(message);
            this.ID=Integer.parseInt(message.substring(3));
            return "/c/"+ID;
        }
        else if(message.startsWith("/m/")){
            return message;
        }
        return message;
    }
    public void send(final byte[] data){
        send=new Thread("Send"){
            public void run(){
                DatagramPacket packet=new DatagramPacket(data,data.length,ip,port);
                try {
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        send.start();
    }
    public String getaddress(){
        System.out.println("returning "+address);
        return address;
    }
    public String getname(){

        System.out.println("Returning"+name);
        return name;
    }
    public String getID(){
        return Integer.toString(ID);
    }
    public int getport(){
        return port;
    }

    public void setID(int id) {
        this.ID=id;
    }

    public void close() {
        new Thread() {
            public void run() {
                synchronized (socket) {
                    socket.close();
                }
            }
        }.start();
    }
}
