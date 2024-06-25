package com.lishuo.test;


import com.lishuo.HelloService;
import com.lishuo.rpc.serializer.HessianSerializer;
import com.lishuo.rpc.transport.socket.server.SocketServer;

/*测试用服务提供方（服务端）*/
public class SocketTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        SocketServer socketServer = new SocketServer("127.0.0.1", 9998);
        socketServer.setSerializer(new HessianSerializer());
        socketServer.publishService(helloService, HelloService.class);
    }
}
