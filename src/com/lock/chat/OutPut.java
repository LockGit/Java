package com.lock.chat;

import javax.swing.*;

/**
 * Created by lock on 2017/2/18.
 */
public class OutPut {
    public static void println(String msg){
        System.out.println(msg);
    }

    public static void print(String msg){
        System.out.print(msg);
    }

    public static void popWindows(String strWarning, String strTitle) {
        JOptionPane.showMessageDialog(null,strWarning, strTitle,
                JOptionPane.INFORMATION_MESSAGE);
    }
}
