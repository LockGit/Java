package com.lock.chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.net.InetAddress;

/**
 * Created by lock on 2017/2/18.
 */
public class ServerFrame extends JFrame implements ActionListener{
    String LOG_PATH = "/tmp/javaChat.log";
    JTabbedPane tpServer;

    // 服务器信息面板
    JPanel pnlServer, pnlServerInfo;
    JLabel lblNumber, lblServerName, lblIP, lblPort, lblLog;
    JTextField txtNumber, txtServerName, txtIP, txtPort;
    JButton btnStop, btnSaveLog;
    TextArea taLog;

    // 用户信息面板
    JPanel pnlUser;
    JLabel lblUser;
    JList lstUser;
    JScrollPane spUser;

    // 关于本软件
    JPanel pnlAbout;
    JLabel lblVersionNo, lblSpeak, lblAbout;

    public ServerFrame(){
        super("A Chat ServerFrame For Java");
        //初始化服务窗口
        this.initServerWindow();
    }

    public static void main(String args[]){
        new ServerFrame();
    }

    /**
     * interface must implement
     * @param evt
     */
    public void actionPerformed(ActionEvent evt) {
    }

    /**
     * init window
     */
    private void initServerWindow(){
        setSize(550, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();// 在屏幕居中显示
        Dimension fra = this.getSize();
        if (fra.width > scr.width) {
            fra.width = scr.width;
        }
        if (fra.height > scr.height) {
            fra.height = scr.height;
        }
        this.setLocation((scr.width - fra.width) / 2,
                (scr.height - fra.height) / 2);

        // 服务器信息
        pnlServerInfo = new JPanel(new GridLayout(14, 1));
        pnlServerInfo.setBackground(new Color(52, 130, 203));
        pnlServerInfo.setFont(new Font("宋体", 0, 12));
        pnlServerInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(1, 1, 1, 1)));

        lblNumber = new JLabel("当前在线人数:");
        lblNumber.setForeground(Color.YELLOW);
        lblNumber.setFont(new Font("宋体", 0, 12));
        txtNumber = new JTextField("0 人", 12);
        txtNumber.setBackground(Color.decode("#d6f4f2"));
        txtNumber.setFont(new Font("宋体", 0, 12));
        txtNumber.setEditable(false);

        lblServerName = new JLabel("服务器名称:");
        lblServerName.setForeground(Color.YELLOW);
        lblServerName.setFont(new Font("宋体", 0, 12));
        txtServerName = new JTextField(12);
        txtServerName.setBackground(Color.decode("#d6f4f2"));
        txtServerName.setFont(new Font("宋体", 0, 12));
        txtServerName.setEditable(false);

        lblIP = new JLabel("服务器IP:");
        lblIP.setForeground(Color.YELLOW);
        lblIP.setFont(new Font("宋体", 0, 12));
        txtIP = new JTextField(12);
        txtIP.setBackground(Color.decode("#d6f4f2"));
        txtIP.setFont(new Font("宋体", 0, 12));
        txtIP.setEditable(false);

        lblPort = new JLabel("服务器端口:");
        lblPort.setForeground(Color.YELLOW);
        lblPort.setFont(new Font("宋体", 0, 12));
        txtPort = new JTextField("", 12);
        txtPort.setBackground(Color.decode("#d6f4f2"));
        txtPort.setFont(new Font("宋体", 0, 12));
        txtPort.setEditable(false);

        btnStop = new JButton("关闭服务器(C)");
        btnStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                closeServer();
            }
        });
        btnStop.setBackground(Color.ORANGE);
        btnStop.setFont(new Font("宋体", 0, 12));

        pnlServerInfo.setBounds(5, 5, 100, 400);
        pnlServerInfo.add(lblNumber);
        pnlServerInfo.add(txtNumber);
        pnlServerInfo.add(lblServerName);
        pnlServerInfo.add(txtServerName);
        pnlServerInfo.add(lblIP);
        pnlServerInfo.add(txtIP);
        pnlServerInfo.add(lblPort);
        pnlServerInfo.add(txtPort);

        // 服务器面板
        pnlServer = new JPanel();
        pnlServer.setLayout(null);
        pnlServer.setBackground(new Color(52, 130, 203));

        lblLog = new JLabel("[服务器日志]");
        lblLog.setForeground(Color.YELLOW);
        lblLog.setFont(new Font("宋体", 0, 12));
        taLog = new TextArea(20, 80);
        taLog.setFont(new Font("宋体", 0, 12));

        btnSaveLog = new JButton("保存日志(S)");
        btnSaveLog.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                saveLog();
            }
        });
        btnSaveLog.setBackground(Color.ORANGE);
        btnSaveLog.setFont(new Font("宋体", 0, 12));

        lblLog.setBounds(110, 5, 100, 30);
        taLog.setBounds(110, 35, 300, 370);
        btnStop.setBounds(200, 410, 120, 30);
        btnSaveLog.setBounds(320, 410, 120, 30);

        //
        pnlServer.add(pnlServerInfo);
        pnlServer.add(lblLog);
        pnlServer.add(taLog);
        pnlServer.add(btnStop);
        pnlServer.add(btnSaveLog);

        // 用户面板
        pnlUser = new JPanel();
        pnlUser.setLayout(null);
        pnlUser.setBackground(new Color(52, 130, 203));
        pnlUser.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(1, 1, 1, 1)));

        lblUser = new JLabel("[在线用户列表]");
        lblUser.setFont(new Font("宋体", 0, 12));
        lblUser.setForeground(Color.YELLOW);

        lstUser = new JList();
        lstUser.setFont(new Font("宋体", 0, 12));
        lstUser.setVisibleRowCount(17);
        lstUser.setFixedCellWidth(180);
        lstUser.setFixedCellHeight(18);

        spUser = new JScrollPane();
        spUser.setBackground(Color.cyan);
        spUser.setFont(new Font("宋体", 0, 12));
        spUser.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        spUser.getViewport().setView(lstUser);

        pnlUser.setBounds(50, 5, 300, 400);
        lblUser.setBounds(50, 10, 100, 30);
        spUser.setBounds(50, 35, 200, 360);

        pnlUser.add(lblUser);
        pnlUser.add(spUser);

        // 软件信息
        pnlAbout = new JPanel();
        pnlAbout.setLayout(null);
        pnlAbout.setBackground(new Color(52, 130, 203));
        pnlAbout.setFont(new Font("宋体", 0, 14));

        lblVersionNo = new JLabel("Version 1.0");
        lblVersionNo.setFont(new Font("宋体", 0, 14));
        lblVersionNo.setForeground(Color.YELLOW);

        JTextArea lblSpeak = new JTextArea();
        lblSpeak.setBackground(new Color(52, 130, 203));
        lblSpeak.setEditable(false);
        lblSpeak.setLineWrap(true);
        lblSpeak.setWrapStyleWord(true);
        lblSpeak.setText("　　这个世界已经很美好了,不要妄想去改变世界！你要做的就是让这个世界再美好一点！" +
                "你也不需要去重复造轮子,你要做的就是如何使用好现有的轮子!O(∩_∩)O哈哈~," +
                "写完这个JavaChat的时候,我已经经过了大量的Google和Baidu。" +
                "图形界面的绘制非常啰嗦复杂,我几乎复制了UI界面,节省了很多时间。最后面对如此啰嗦的语言，" +
                "我不得不说：PHP和Python才是世界上最好的语言!!!^_^");
        lblSpeak.setForeground(Color.YELLOW);


        lblAbout = new JLabel();
        lblAbout.setFont(new Font("Consolas", 0, 14));
        lblAbout.setText("Author By Lock!");
        lblAbout.setForeground(Color.YELLOW);

        lblVersionNo.setBounds(5, 25, 100, 30);
        lblSpeak.setBounds(5, 55, 500, 80);
        lblAbout.setBounds(5, 85, 400, 120);

        pnlAbout.add(lblVersionNo);
        pnlAbout.add(lblSpeak);
        pnlAbout.add(lblAbout);

        // 主标签面板
        tpServer = new JTabbedPane(JTabbedPane.TOP);
        tpServer.setBackground(Color.CYAN);
        tpServer.setFont(new Font("宋体", 0, 14));

        tpServer.add("服务器管理", pnlServer);
        tpServer.add("在线用户", pnlUser);
        tpServer.add("关于本软件", pnlAbout);

        this.getContentPane().add(tpServer);
        setVisible(true);
    }

    /**
     * 退出Server
     */
    private void closeServer(){
        //销毁所有窗口组件资源，回收内存
        this.dispose();
        System.exit(0);
    }

    private void saveLog(){
        try {
            FileOutputStream fileOutPut = new FileOutputStream(this.LOG_PATH, true);
            String temp = taLog.getText();
            fileOutPut.write(temp.getBytes());
            fileOutPut.close();
        } catch (Exception e) {
            OutPut.popWindows(e.getMessage(),"保存日志异常");
        }
        OutPut.popWindows("保存成功!,文件位置:"+this.LOG_PATH,"保存日志");
    }

}
