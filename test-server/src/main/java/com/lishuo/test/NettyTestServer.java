package com.lishuo.test;

import com.lishuo.HelloService;
import com.lishuo.rpc.netty.server.NettyServer;
import com.lishuo.rpc.registry.DefaultServiceRegistry;
import com.lishuo.rpc.registry.ServiceRegistry;
import com.lishuo.rpc.serializer.KryoSerializer;

/*测试用Netty服务提供者（服务端）*/
public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(helloService);
        NettyServer server = new NettyServer();
        server.setSerializer(new KryoSerializer());
        server.start(9999);
    }
}
