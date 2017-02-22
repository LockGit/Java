package com.lock.chat;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by lock on 2017/2/18.
 */
public class Server{
    //define listen port
    private final int SERVER_PORT = 8888;
    private final String USER_LIST_FILE = "/tmp/java_chat_user.md";

    //Frame
    ServerFrame serverFrame = null;
    // create Server sockts
    ServerSocket serverSocket = null;

    public Server(){
        try {
            // 启动服务
            serverSocket = new ServerSocket(SERVER_PORT);
            serverFrame = new ServerFrame();
            this.setServerIP();
            OutPut.println("Server port is:" + SERVER_PORT);
            serverFrame.taLog.setText("[start]服务器已经启动...");
            //先生成空文件
            RandomAccessFile userFile = new RandomAccessFile(USER_LIST_FILE, "rw");
            userFile.writeBytes("");
            while (true) {
                // 监听客户端的连接请求，并返回客户端socket
                Socket socket = serverSocket.accept();
                // 创建一个新线程来处理与该client的通信
                new ServerProcess(socket, serverFrame);
            }
        } catch (BindException e) {
            OutPut.popWindows(e.getMessage(), "端口被占用");
            System.exit(0);
        } catch (IOException e) {
            OutPut.popWindows(e.getMessage(), "ERROR");
            System.exit(0);
        }
        OutPut.println("Start thread...");
        //启动线程
        //this.start();
    }

    // 获取服务器的主机名和IP地址
    private void setServerIP() {
        try {
            InetAddress serverAddress = InetAddress.getLocalHost();
            byte[] ipAddress = serverAddress.getAddress();
            serverFrame.txtServerName.setText(serverAddress.getHostName());
            serverFrame.txtIP.setText(serverAddress.getHostAddress());
            serverFrame.txtPort.setText(SERVER_PORT+"");
            OutPut.println("Server IP is:" + (ipAddress[0] & 0xff) + "."
                    + (ipAddress[1] & 0xff) + "." + (ipAddress[2] & 0xff) + "."
                    + (ipAddress[3] & 0xff));
        } catch (Exception e) {
            OutPut.popWindows(e.getMessage(),"获取IP失败");
        }
    }

    public static void main(String args[]){
        new Server();
    }

}