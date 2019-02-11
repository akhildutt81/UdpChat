package chatApp.server;

import java.net.InetAddress;

public class serverClient {
    public String name;
    public InetAddress address;
    public int port;
    public final int id;
    public int attemp=0;

    public serverClient(String name,InetAddress address,int port,int id) {
        this.id = id;
        this.address=address;
        this.name=name;
        this.port=port;
    }
    public String toString(){
        return name+" "+address.toString()+" "+port;
    }
}
