package com.lishuo.test;


import com.lishuo.HelloObject;
import com.lishuo.HelloService;
import com.lishuo.rpc.RpcClientProxy;
import com.lishuo.rpc.serializer.KryoSerializer;
import com.lishuo.rpc.socket.client.SocketClient;

/*测试用消费者（客户端）*/
public class SocketTestClient {
    public static void main(String[] args) {
        SocketClient client = new SocketClient("127.0.0.1", 9000);
        client.setSerializer(new KryoSerializer());
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
