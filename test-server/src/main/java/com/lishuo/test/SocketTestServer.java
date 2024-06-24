package com.lishuo.test;


import com.lishuo.HelloService;
import com.lishuo.rpc.registry.DefaultServiceRegistry;
import com.lishuo.rpc.registry.ServiceRegistry;
import com.lishuo.rpc.RpcServer;
import com.lishuo.rpc.socket.server.SocketServer;

/*测试用服务提供方（服务端）*/
public class SocketTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        SocketServer socketServer = new SocketServer(serviceRegistry);
        socketServer.start(9000);
    }
}
