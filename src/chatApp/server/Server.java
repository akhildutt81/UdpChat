package chatApp.server;

import chatApp.Client;

import java.io.IOException;
import java.net.*;
import java.util.*;

public class Server implements Runnable {
    private DatagramSocket socket;
    private boolean running=false;
    private int port;
    private Thread run,manage,send,receive;
    private HashMap<Integer, serverClient> clients=new HashMap<Integer,serverClient>();
    private Random Random;
    private HashSet<Integer> clientResonse;

    public Server(int port){
        this.port=port;
        try{
            socket=new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        Random=new Random();
        run=new Thread(this,"Server");
        run.run();
        clientResonse=new HashSet<>();
    }

    public void run(){
        running=true;
        manageClient();
        receive();
    }

    private void manageClient(){
        manage=new Thread("Manage"){
            public void run(){
                while(running){
                    sendToAll("/i/server");
                    clients.entrySet().removeIf(clie->clientResonse.contains(clie.getKey()));
                }
            }
        };
        manage.start();
    }

    private void receive(){
        receive=new Thread("Receive"){
            public void run(){
                while(running){
                    byte[] data=new byte[1024];
                    DatagramPacket packet=new DatagramPacket(data,data.length);
                    try {
                        socket.receive(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    process(packet);
                    //String temp=new String(packet.getData().toString());
                    //clients.add(new serverClient("akhil",packet.getAddress(),packet.getPort(),99));
                    //System.out.println(new String(packet.getData(),packet.getOffset(),packet.getLength()));
                }
            }
        };
        receive.start();
    }
    private void sendToAll(String msg){
        for(serverClient temp:clients.values()){
            System.out.println("Sending to "+temp.name+" "+temp.address+" "+temp.port+" message"+msg);
            send(msg.getBytes(),temp.address,temp.port);
        }
    }
    private void send(final byte[] msg,final InetAddress address,final int port){
        send=new Thread("Send"){
            public void run(){
                System.out.println("inside send near dp "+msg);
                DatagramPacket pck=new DatagramPacket(msg,msg.length,address,port);
                try{
                    socket.send(pck);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        send.start();
    }
    private void process(DatagramPacket packet){
        String str=new String(packet.getData(),packet.getOffset(),packet.getLength());
        System.out.println("received "+str);
        if(str.startsWith("/c/")){
            int id=Random.nextInt();
            while(clients.containsKey(id)){
                id=Random.nextInt();
            }
            serverClient temp=new serverClient(str.substring(3),packet.getAddress(),packet.getPort(),id);
            clients.put(id,temp);
            System.out.println(temp.name+" connected with id "+temp.id+" str = "+str);
            send(("/c/"+temp.id).getBytes(),packet.getAddress(),packet.getPort());
        }
        else if(str.startsWith("/m/")){
            sendToAll(str);
        }
        else if(str.startsWith("/d/")){
            String temp=str.substring(3);
            clients.remove(Integer.parseInt(temp));
            System.out.println(clients);
        }
        else{
            System.out.println("inside else"+str);
        }
    }
}
