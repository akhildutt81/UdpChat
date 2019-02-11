package chatApp;

//import com.sun.jndi.cosnaming.IiopUrl;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTextField txtName;
    private JTextField txtAddress;
    private JLabel lblName;
    private JLabel lblAddress;
    private JTextField txtPort;
    private JLabel lblPort;

    public Login() {
        try{
            UIManager.setLookAndFeel(String.valueOf(UIManager.getAuxiliaryLookAndFeels())
            );
        }
        catch (Exception e) {
            System.out.println(e);
        }
        setResizable(false);
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300,380);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        txtName=new JTextField();
        txtName.setBounds(65,50,165,28);
        contentPane.add(txtName);
        txtName.setColumns(10);

        lblName=new JLabel("UserName:");
        lblName.setBounds(108,34,100,16);
        contentPane.add(lblName);

        txtAddress=new JTextField();
        txtAddress.setBounds(65,116,165,28);
        contentPane.add(txtAddress);
        txtAddress.setColumns(10);

        lblAddress=new JLabel("IP Adress:");
        lblAddress.setBounds(108,96,100,16);
        contentPane.add(lblAddress);

        txtPort=new JTextField();
        txtPort.setBounds(65,191,165,28);
        contentPane.add(txtPort);
        txtPort.setColumns(10);
        txtPort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login(txtName.getText(),txtAddress.getText(),Integer.parseInt(txtPort.getText()));
            }
        });

        lblPort=new JLabel("Port:");
        lblPort.setBounds(131,171,45,16);
        contentPane.add(lblPort);

        JButton btnLogin=new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login(txtName.getText(),txtAddress.getText(),Integer.parseInt(txtPort.getText()));
            }
        });
        btnLogin.setBounds(89,250,117,29);
        contentPane.add(btnLogin);
    }

    public void login(String name, String address,int port){
        new clientWindow(address,name,port);
    }
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Login frame = new Login();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
