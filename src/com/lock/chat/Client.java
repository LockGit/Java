package com.lock.chat;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * Created by lock on 2017/2/18.
 */
public class Client extends JFrame implements ActionListener{
    private int PORT = 8888;
    JFrame clientFrame = new JFrame("A Chat For Java");

    GridBagLayout gl;
    BorderLayout bdl;
    GridBagConstraints gbc;

    // 聊天界面
    JPanel pnlBack, pnlTalk;
    JButton btnTalk;
    JTextArea txtViewTalk;
    JLabel lblTalk, lblTo;
    JComboBox listOnline;

    // 登陆界面
    JPanel pnlLogin;
    JLabel lblServerIP, lblName, lblPassword;
    JTextField txtTalk, txtServerIP, txtName;
    JPasswordField txtPassword;
    JButton btnLogin, btnReg, btnExit;

    JDialog dialogLogin = new JDialog(this, "登陆", true);

    Socket socket = null;
    BufferedReader in = null;
    PrintWriter outStream = null;

    String strSend, strReceive, strKey, strStatus;
    private StringTokenizer st;

    public Client(){
        //初始登录窗口
        this.initWindow();
    }


    /**
     * 事件监听
     * @param evt
     */
    public void actionPerformed(ActionEvent evt){
        Object evtObj = evt.getSource();
        try{
            if(evtObj.equals(btnExit)){
                //退出
                System.exit(0);
            }else if (evtObj.equals(btnLogin)){
                //登录
                char[] pass = txtPassword.getPassword();
                String password = new String(pass);
                if ((txtServerIP.getText().length() > 0)
                        && (txtName.getText().length() > 0)
                        && (password.length() > 0)) {
                    this.connect();
                    strSend = "login|" + txtName.getText() + "|"
                            + String.valueOf(txtPassword.getPassword());
                    outStream.println(strSend);
                    initLogin();
                } else {
                    OutPut.popWindows("请输入完整信息", "ERROR");
                }
            }else if(evtObj.equals(btnReg)) {
                //注册
                char[] pass = txtPassword.getPassword();
                String password = new String(pass);
                if (txtName.getText().length() > 0 && password.length() > 0) {
                    //create sockts link
                    this.connect();
                    strSend = "reg|" + txtName.getText() + "|"
                            + String.valueOf(password);
                    outStream.println(strSend);
                    this.initLogin();
                }else{
                    OutPut.popWindows("请输入完整信息", "ERROR");
                }
            }else if(evtObj.equals(btnTalk)){
                //发消息
                if (txtTalk.getText().length() > 0) {
                    outStream.println("talk|" + txtTalk.getText() + "|"
                            + txtName.getText() + "|"
                            + listOnline.getSelectedItem().toString());//
                    txtTalk.setText("");
                }
            } else {
                OutPut.popWindows("不存在的操作", "异常");
            }
        }catch (Exception e){
            OutPut.popWindows(e.getMessage(),"异常");
        }



    }


    public static void main(String args[]){
        new Client();
    }

    /**
     * 多线程
     */
    class ClientThread implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String strReceive, strKey;
        private Thread threadTalk;
        private StringTokenizer st;

        public ClientThread(Socket s) throws IOException {
            this.socket = s;
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            threadTalk = new Thread(this);
            threadTalk.start();
        }

        public void run(){
            //synchronized 阻塞线程,一个时间内只能有一个线程得到执行。
            //另一个线程必须等待当前线程执行完这个代码块以后才能执行该代码块。
            while (true) synchronized (this) {
                try {
                    strReceive = in.readLine();
                    st = new StringTokenizer(strReceive, "|");
                    strKey = st.nextToken();
                    if (strKey.equals("talk")) {
                        String strTalk = st.nextToken();
                        strTalk = txtViewTalk.getText() + "\r\n   "
                                + strTalk;
                        txtViewTalk.setText(strTalk);
                    } else if (strKey.equals("online")) {
                        String strOnline;
                        while (st.hasMoreTokens()) {
                            strOnline = st.nextToken();
                            //增加前检测
                            boolean exists = false;
                            for (int index = 0; index < listOnline.getItemCount() && !exists; index++) {
                                if (strOnline.equals(listOnline.getItemAt(index))) {
                                    exists = true;
                                }
                            }
                            if (!exists) {
                                listOnline.addItem(strOnline);
                            }
                        }
                    } else if (strKey.equals("remove")) {
                        String strRemove;
                        while (st.hasMoreTokens()) {
                            strRemove = st.nextToken();
                            listOnline.removeItem(strRemove);
                        }
                    } else if (strKey.equals("warning")) {
                        String strWarning = st.nextToken();
                        OutPut.popWindows(strWarning, "Warning");
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    OutPut.popWindows(e.getMessage(), "Thread Exception");
                } catch (IOException e) {
                    OutPut.popWindows(e.getMessage(), "Thread IO Exception");
                }
            }
        }
    }


    /**
     * initLogin
     */
    private void initLogin() throws IOException{
        strReceive = in.readLine();
        st = new StringTokenizer(strReceive, "|");
        strKey = st.nextToken();
        if (strKey.equals("login")) {
            strStatus = st.nextToken();
            if (strStatus.equals("succeed")) {
                btnLogin.setEnabled(false);
                btnTalk.setEnabled(true);
                pnlLogin.setVisible(false);
                //Component屏幕资源,内存回收至操作系统
                dialogLogin.dispose();
                new ClientThread(socket);
                outStream.println("init|online");
            }
            OutPut.popWindows(strKey + " " + strStatus + "!", "Login");
        }
        if (strKey.equals("warning")) {
            strStatus = st.nextToken();
            OutPut.popWindows(strStatus, "Register");
        }
    }


    private void connect(){
        try {
            socket = new Socket(txtServerIP.getText(), PORT);
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            outStream = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream())), true);
        } catch (ConnectException e) {
            OutPut.popWindows("连接服务器失败!", "连接ERROR");
            txtServerIP.setText("");
            OutPut.popWindows(e.getMessage(), "连接ERROR");
        } catch (Exception e) {
            OutPut.popWindows(e.getMessage(), "ERROR");
        }
    }

    private void initWindow() {
        //初始化client 端窗口
        gl = new GridBagLayout();
        bdl = new BorderLayout();
        gbc = new GridBagConstraints();
        pnlBack = (JPanel) getContentPane();
        pnlBack.setLayout(bdl);

        // 初始化控件
        pnlLogin = new JPanel();
        pnlLogin.setLayout(gl);

        lblServerIP = new JLabel("服务器IP:");
        lblName = new JLabel("    用户名:");
        lblPassword = new JLabel("     密码:  ");
        txtServerIP = new JTextField(12);
        txtName = new JTextField(12);
        txtPassword = new JPasswordField(12);

        txtServerIP.setText("127.0.0.1");

        btnLogin = new JButton("登陆");
        btnReg = new JButton("注册");
        btnExit = new JButton("退出");

        btnTalk = new JButton("发送");
        lblTalk = new JLabel("发言:");

        lblTo = new JLabel(" To :");
        txtTalk = new JTextField(30);
        pnlTalk = new JPanel();
        txtViewTalk = new JTextArea(18, 40);
        listOnline = new JComboBox();
        txtViewTalk.setForeground(Color.blue);
        btnTalk.addActionListener(this);

        btnLogin.addActionListener(this);
        btnReg.addActionListener(this);
        btnExit.addActionListener(this);

        listOnline.addItem("All");

        pnlTalk.add(lblTalk);
        pnlTalk.add(txtTalk);
        pnlTalk.add(lblTo);
        pnlTalk.add(listOnline);
        pnlTalk.add(btnTalk);
        pnlBack.add("Center", txtViewTalk);
        pnlBack.add("South", pnlTalk);
        pnlTalk.setBackground(Color.cyan);
        btnTalk.setEnabled(false);

        clientFrame.getContentPane().add(pnlBack);
        clientFrame.setSize(800, 450);
        clientFrame.setVisible(true);
        clientFrame.setResizable(false);
        clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 登陆对话框初始化
        dialogLogin.getContentPane().setLayout(new FlowLayout());
        dialogLogin.getContentPane().add(lblServerIP);
        dialogLogin.getContentPane().add(txtServerIP);
        dialogLogin.getContentPane().add(lblName);
        dialogLogin.getContentPane().add(txtName);
        dialogLogin.getContentPane().add(lblPassword);
        dialogLogin.getContentPane().add(txtPassword);
        dialogLogin.getContentPane().add(btnLogin);
        dialogLogin.getContentPane().add(btnReg);
        dialogLogin.getContentPane().add(btnExit);

        dialogLogin.setBounds(300, 300, 250, 200);
        dialogLogin.getContentPane().setBackground(Color.gray);
        //dialogLogin.show(true); 过时，用setVisible取代
        dialogLogin.setVisible(true);
    }

}
