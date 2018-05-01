package com.lock.demo.service;

import lock.rpc.UserRpc;
import org.apache.thrift.TException;
import org.slf4j.LoggerFactory;


/**
 * @author Lock
 * @date 2018/5/1 13:38
 */
public class UserRpcService implements UserRpc.Iface {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(UserRpcService.class);

    @Override
    public String login(String name, String psw) throws TException {
        return "my rpc service test,you name is:" + name + ",you passwd is:" + psw+",from java thrift service";
    }

    @Override
    public void add(String name, String psw) throws TException {
        LOGGER.info("it's a add rpc service ,and the name is :" + name + ",and the passwd is:" + psw);
        return;
    }
}
