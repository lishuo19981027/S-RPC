package com.lishuo.test;

import com.lishuo.HelloObject;
import com.lishuo.HelloService;
import com.lishuo.rpc.RpcClient;
import com.lishuo.rpc.RpcClientProxy;
import com.lishuo.rpc.netty.client.NettyClient;

/*测试用Netty消费者*/
public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient("127.0.0.1", 9999);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
