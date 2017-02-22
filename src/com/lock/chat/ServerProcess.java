package com.lock.chat;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.text.SimpleDateFormat;

/**
 * Created by lock on 2017/2/18.
 */
public class ServerProcess extends Thread {
    /**
     * 存放用户密码与账号文件，实际环境insert db ，encode password
     */
    private final String USER_LIST_FILE = "/tmp/java_chat_user.md";
    //client sockts
    private Socket socket = null;
    ServerFrame sFrame;

    private BufferedReader in;// 定义输入流
    private PrintWriter outStream;// 定义输出流

    //类似动态变化的数组，第二个参数5表示达到10个element的是，在扩充5个元素
    //存放在线用户，sockts至内存
    private static Vector onlineUser = new Vector(10, 5);
    private static Vector socketUser = new Vector(10, 5);

    private String strReceive,strKey;
    private StringTokenizer st;

    public ServerProcess(Socket client, ServerFrame frame) throws IOException {
        socket = client;
        sFrame = frame;

        // 客户端接收
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // 客户端输出
        outStream = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                socket.getOutputStream())), true);
        this.start();
    }

    //实现thread的run方法
    public void run(){
        try{
            while (true){
                strReceive = in.readLine();// 从服务器端接收一条信息后拆分、解析，并执行相应操作
                st = new StringTokenizer(strReceive, "|");
                strKey = st.nextToken();
                if (strKey.equals("login")) {
                    login();
                } else if (strKey.equals("talk")) {
                    talk();
                } else if (strKey.equals("init")) {
                    freshClientsOnline();
                } else if (strKey.equals("reg")) {
                    register();
                }
            }
        }catch (IOException e){
            String leaveUser = closeSocket();
            String dateTime = this.getCurrentTime();
            log("用户" + leaveUser + "已经退出, " + "退出时间:" + dateTime);
            try {
                freshClientsOnline();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.out.println("[SYSTEM] " + leaveUser + " leave chatroom!");
            sendAll("talk|>>>" + leaveUser + " 恋恋不舍的离开了聊天室。");
        }
    }


    /**
     * 登录
     */
    private void login() throws IOException{
        String name = st.nextToken(); // 得到用户名称
        String password = st.nextToken().trim();// 得到用户密码
        boolean succeed = false;
        String dateTime;
        dateTime = this.getCurrentTime();
        log("用户" + name + "正在登陆..." + "\n" + "密码 :" + password + "\n" + "端口 "
                + socket + dateTime);
        OutPut.println("[USER LOGIN] " + name + ":" + password + ":"
                + socket);

        for (int i = 0; i < onlineUser.size(); i++) {
            if (onlineUser.elementAt(i).equals(name)) {
                OutPut.println("[ERROR] " + name + " is logined!");
                outStream.println("warning|" + name + "已经登陆聊天室");
            }
        }
        if (this.checkUserNamePass(name, password)) { // 判断用户名和密码
            this.userLoginSuccess(name);
            succeed = true;
        }
        if (!succeed) {
            outStream.println("warning|" + name + "登陆失败，请检查您的输入!");
            log("用户" + name + "登陆失败！" + dateTime);
            OutPut.println("[SYSTEM] " + name + " login fail!");
        }
    }

    // user register
    private void register() throws IOException {
        String name = st.nextToken(); // 得到用户名称
        String password = st.nextToken().trim();// 得到用户密码
        if (isExistUser(name)) {
            OutPut.println("[ERROR] " + name + " Register fail!");
            outStream.println("warning|该用户已存在，请改名!");
        } else {
            RandomAccessFile userFile = new RandomAccessFile(USER_LIST_FILE, "rw");
            userFile.seek(userFile.length());
            // 在文件尾部加入新用户信息
            userFile.writeBytes(name + "|" + password + "\r\n");
            log("用户" + name + "注册成功, " + "注册时间:" + this.getCurrentTime());
            userLoginSuccess(name); // 自动登陆聊天室
        }
    }

    // talk
    private void talk() throws IOException {
        String strTalkInfo = st.nextToken(); // 得到聊天内容;
        String strSender = st.nextToken(); // 得到发消息人
        String strReceiver = st.nextToken(); // 得到接收人
        OutPut.println("[TALK_" + strReceiver + "] " + strTalkInfo);
        Socket socketSend;
        PrintWriter outSend;

        // 得到当前时间
        strTalkInfo += "("+this.getCurrentTime()+")";

        log("用户" + strSender + "对 " + strReceiver + "说:" + strTalkInfo
                + this.getCurrentTime());

        if (strReceiver.equals("All")) {
            sendAll("talk|" + strSender + " 对所有人说：" + strTalkInfo);
        } else {
            if (strSender.equals(strReceiver)) {
                outStream.println("talk|>>>不能自言自语哦!");
            } else {
                for (int i = 0; i < onlineUser.size(); i++) {
                    if (strReceiver.equals(onlineUser.elementAt(i))) {
                        socketSend = (Socket) socketUser.elementAt(i);
                        //sockts print info
                        outSend = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketSend.getOutputStream())), true);
                        outSend.println("talk|" + strSender + " 对你说：" + strTalkInfo);
                    } else if (strSender.equals(onlineUser.elementAt(i))) {
                        socketSend = (Socket) socketUser.elementAt(i);
                        //sockts print info
                        outSend = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketSend.getOutputStream())), true);
                        outSend.println("talk|你对 " + strReceiver + "说：" + strTalkInfo);
                    }
                }
            }
        }
    }



    /**
     * 广播消息
     * @param strSend
     */
    private void sendAll(String strSend) {
        Socket socketSend;
        PrintWriter outSend;
        try {
            for (int i = 0; i < socketUser.size(); i++) {
                socketSend = (Socket) socketUser.elementAt(i);
                outSend = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socketSend.getOutputStream())),
                        true);
                //向所有人广播
                outSend.println(strSend);
            }
        } catch (IOException e) {
            OutPut.println("[ERROR] send all fail!");
        }
    }


    /**
     * check user exist
     * @param name
     * @return boolean
     */
    private boolean isExistUser(String name){
        String strRead;
        try {
            FileInputStream inputFile = new FileInputStream(USER_LIST_FILE);
            BufferedReader inputData = new BufferedReader(new InputStreamReader(inputFile));
            while ((strRead = inputData.readLine()) != null) {
                StringTokenizer stUser = new StringTokenizer(strRead, "|");
                if (stUser.nextToken().equals(name)) {
                    return true;
                }
            }
        } catch (FileNotFoundException fn) {
            OutPut.println("[ERROR] User File has not exist!" + fn);
            outStream.println("warning|fn check exist读写文件时出错!");
        } catch (IOException ie) {
            OutPut.println("[ERROR] " + ie.getMessage());
            outStream.println("warning|ie check exist读写文件时出错!");
        }
        return false;
    }

    /**
     * check user name and password
     */
    private boolean checkUserNamePass(String name,String password){
        String strRead;
        try{
            FileInputStream inputFile = new FileInputStream(USER_LIST_FILE);
            //方法在新版的jdk中废弃，http://docs.oracle.com/javase/6/docs/api/java/io/DataInputStream.html#readLine%28%29
            //DataInputStream inputData = new DataInputStream(inputFile);
            BufferedReader inputData = new BufferedReader(new InputStreamReader(inputFile));
            while ((strRead = inputData.readLine()) != null) {
                if (strRead.equals(name + "|" + password)) {
                    return true;
                }
            }
        }catch (FileNotFoundException fn){
            OutPut.println("[ERROR] User File has not exist!"+fn.getMessage());
            outStream.println("warning|fn读写文件时出错!");
        }
        catch (IOException ie){
            OutPut.println("[ERROR]"+ie);
            outStream.println("warning|ie读写文件时出错!");
        }
        return false;
    }


    /**
     * 设置登录成功
     * onlineUser，socketUser 内存中增加name与sockts
     * @param name
     */
    private void userLoginSuccess(String name) throws IOException {
        String dateTime = this.getCurrentTime();
        outStream.println("login|succeed");
        sendAll("online|" + name);

        onlineUser.addElement(name);
        socketUser.addElement(socket);

        log("用户" + name + "登录成功，" + "登录时间:" + dateTime);

        this.freshClientsOnline();
        sendAll("talk|>>>欢迎 " + name + " 进来与我们一起交谈!");
        OutPut.println("[SYSTEM] " + name + " login succeed!");
    }

    /**
     * 关闭sockts连接触发以下行为
     * @return strUser
     */
    private String closeSocket(){
        String strUser = "";
        for (int i = 0; i < socketUser.size(); i++) {
            if (socket.equals((Socket) socketUser.elementAt(i))) {
                strUser = onlineUser.elementAt(i).toString();
                socketUser.removeElementAt(i);
                onlineUser.removeElementAt(i);
                try {
                    freshClientsOnline();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sendAll("remove|" + strUser);
            }
        }
        try {
            in.close();
            outStream.close();
            socket.close();
        } catch (IOException e) {
            OutPut.println("[ERROR] " + e.getMessage());
        }
        return strUser;
    }

    /**
     * 刷新在线用户列表,从内存中遍历一次显示
     */
    private void freshClientsOnline() throws IOException {
        String strOnline = "online";
        String[] userList = new String[20];
        String useName = null;

        for (int i = 0; i < onlineUser.size(); i++) {
            strOnline += "|" + onlineUser.elementAt(i);
            useName = " " + onlineUser.elementAt(i);
            userList[i] = useName;
        }
        //frame元素设置
        sFrame.txtNumber.setText("" + onlineUser.size());
        sFrame.lstUser.setListData(userList);
        OutPut.println(strOnline);
        //send to sockts outStream stream
        outStream.println(strOnline);
    }

    /**
     * 记录log函数显示在frame中
     */
    private void log(String log){
        String newLog = sFrame.taLog.getText() + "\n" + log;
        sFrame.taLog.setText(newLog);
    }


    private String getCurrentTime(){
        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

}
