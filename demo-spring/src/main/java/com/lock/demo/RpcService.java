package com.lock.demo;

import com.lock.demo.service.UserRpcService;
import lock.rpc.UserRpc;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.slf4j.LoggerFactory;

/**
 * @author Lock
 * @date 2018/5/1 13:50
 */

public class RpcService {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(RpcService.class);
    private final static Integer PORT = 6666;
    public static void main(String[] args) {
        try {
            LOGGER.info("start java rpc service ...");

            TServerSocket serverTransport = new TServerSocket(PORT);
            TServer.Args argsRpc = new TServer.Args(serverTransport);
            TProcessor processUserRpc = new UserRpc.Processor(new UserRpcService());
            TBinaryProtocol.Factory portFactory = new TBinaryProtocol.Factory(true, true);

            TMultiplexedProcessor processor = new TMultiplexedProcessor();
            processor.registerProcessor("userRpc", processUserRpc);


            argsRpc.protocolFactory(portFactory);
            TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(
                    serverTransport).processor(processor));


//            简单的单线程服务模型，一般用于测试 (测试方法2)
//            TProcessor tprocessor = new UserRpc.Processor<UserRpc.Iface>(new UserRpcService());
//            TServerSocket serverTransport = new TServerSocket(PORT);
//            TServer.Args tArgs = new TServer.Args(serverTransport);
//            tArgs.processor(tprocessor);
//            tArgs.protocolFactory(new TBinaryProtocol.Factory());
//            TServer server = new TSimpleServer(tArgs);

            LOGGER.info("start listen port " + PORT);
            server.serve();
        } catch (Exception e) {
            LOGGER.info("have exception, msg is:" + e.getMessage());
        }

    }
}
