package com.lishuo.test;

import com.lishuo.HelloObject;
import com.lishuo.HelloService;
import com.lishuo.rpc.serializer.CommonSerializer;
import com.lishuo.rpc.transport.RpcClient;
import com.lishuo.rpc.transport.RpcClientProxy;
import com.lishuo.rpc.transport.netty.client.NettyClient;
import com.lishuo.rpc.serializer.ProtobuffSerializer;

/*测试用Netty消费者*/
public class NettyTestClient {
    public static void main(String[] args) {
        //client.setSerializer(new HessianSerializer());
        RpcClient client = new  NettyClient(CommonSerializer.PROTOBUF_SERIALIZER);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
