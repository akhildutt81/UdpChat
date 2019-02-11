package chatApp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class clientWindow extends JFrame implements Runnable{
    private Thread send;
    private Client client;
    private JPanel contentPane;
    private JTextField txtMessage;
    private JButton btnSend;
    private JTextArea txthis;
    private Thread listen,run;
    private boolean running=false;
    public clientWindow(String Address,String Name,int Port) {
        setVisible(true);
        client=new Client(Address,Name,Port);
        boolean connect=client.openConnectioin(client.getaddress());
        if(!connect){
            System.out.println("Could not connect");
            console("Could not connect");
            return;
        }
        createWindow();
        client.send((client.getname() + " "+client.getaddress()+" "+client.getport()).getBytes());
        client.send(("/c/"+client.getname()).getBytes());
        running=true;
        run=new Thread(this,"Clienntwindow");
        run.start();
    }
    private void createWindow(){
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            System.out.println(e);
        }
        setTitle("Chat Client");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(880,550);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        GridBagLayout gbl=new GridBagLayout();
        gbl.columnWidths=new int[]{28,815,30,7};
        gbl.rowHeights=new int[]{35,475,40};
        gbl.columnWeights=new double[]{1.0,1.0};
        gbl.rowWeights=new double[]{1.0,Double.MIN_VALUE};

        txthis=new JTextArea();
        txthis.setEditable(false);
        JScrollPane scp=new JScrollPane(txthis);
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.insets=new Insets(0,0,5,5);
        gbc.fill=GridBagConstraints.BOTH;
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.gridwidth=3;
        gbc.gridheight=2;
        gbc.insets=new Insets(0,5,0,0);

        txtMessage=new JTextField();
        GridBagConstraints gbt=new GridBagConstraints();
        gbt.insets=new Insets(0,0,0,5);
        gbt.fill=GridBagConstraints.HORIZONTAL;
        gbt.gridx=0;
        gbt.gridy=2;
        gbt.gridwidth=2;
        txtMessage.setColumns(10);
        txtMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode()== KeyEvent.VK_ENTER){
                    console1(txtMessage.getText());
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                String m="/d/"+client.getID();
                client.send(m.getBytes());
                running=false;
                client.close();
            }
        });
        btnSend = new JButton("Send");
        GridBagConstraints gbb=new GridBagConstraints();
        gbb.insets=new Insets(0,0,0,5);
        gbb.gridx=2;
        gbb.gridy=2;
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                console1(txtMessage.getText());
            }
        });

        setVisible(true);
        contentPane.setLayout(gbl);
        contentPane.add(scp,gbc);
        contentPane.add(txtMessage,gbt);
        contentPane.add(btnSend,gbb);
        txtMessage.requestFocusInWindow();
    }
    private  void console1(String m){
        client.send(("/m/"+client.getname()+" : "+m).getBytes());
        //console(m);
    }
    private void console(String message){
        if(message.equals(""))return;
        txthis.append(message+"\n\r");
        txthis.setCaretPosition(txthis.getDocument().getLength());
        txtMessage.setText("");
    }

    public void listen(){
        listen=new Thread("listen"){
            public void run(){
                while(running){
                    String message=client.receive();
                    if(message.startsWith("/c/")){
                        System.out.println("Inside listen of clwin");
                        client.setID(Integer.parseInt(message.substring(3)));
                    }
                    else{
                        String txt=message.substring(3);
                        console(txt);
                    }
                }
            }
        };
        listen.start();
    }

    @Override
    public void run() {
        listen();
    }
}
