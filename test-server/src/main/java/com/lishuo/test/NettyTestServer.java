package com.lishuo.test;

import com.lishuo.HelloService;
import com.lishuo.rpc.transport.netty.server.NettyServer;
import com.lishuo.rpc.registry.ServiceRegistry;
import com.lishuo.rpc.serializer.ProtobuffSerializer;

/*测试用Netty服务提供者（服务端）*/
public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        NettyServer server = new NettyServer("127.0.0.1",9999);
        //server.setSerializer(new KryoSerializer());
        server.setSerializer(new ProtobuffSerializer());
        server.publishService(helloService, HelloService.class);
    }
}
