package com.lishuo.test;


import com.lishuo.HelloService;
import com.lishuo.registry.DefaultServiceRegistry;
import com.lishuo.registry.ServiceRegistry;
import com.lishuo.server.RpcServer;

/*测试用服务提供方（服务端）*/
public class TestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        RpcServer rpcServer = new RpcServer(serviceRegistry);
        rpcServer.start(9000);
    }
}
