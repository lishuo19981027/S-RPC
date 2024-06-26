package com.lishuo.test;

import com.lishuo.HelloService;
import com.lishuo.rpc.serializer.CommonSerializer;
import com.lishuo.rpc.transport.netty.server.NettyServer;
/*测试用Netty服务提供者（服务端）*/
public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        NettyServer server = new NettyServer("127.0.0.1",9999,
                CommonSerializer.PROTOBUF_SERIALIZER);
        //server.setSerializer(new KryoSerializer());
        server.publishService(helloService, HelloService.class);
    }
}
