package chatApp.server;

public class ServerMain {
    private Server server;
    private int port;

    public ServerMain(int port){
        this.port=port;
        server=new Server(port);
    }
    public static void main(String[] args){
        int port;
        if(args.length!=1){
            System.out.println("usage : java -jar abc.jar [port]");
            return;
        }
        port=Integer.parseInt(args[0]);
        new ServerMain(port);
    }
}
