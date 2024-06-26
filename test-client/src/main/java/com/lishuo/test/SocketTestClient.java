package com.lishuo.test;


import com.lishuo.HelloObject;
import com.lishuo.HelloService;
import com.lishuo.rpc.serializer.CommonSerializer;
import com.lishuo.rpc.transport.RpcClientProxy;
import com.lishuo.rpc.serializer.KryoSerializer;
import com.lishuo.rpc.transport.socket.client.SocketClient;

/*测试用消费者（客户端）*/
public class SocketTestClient {
    public static void main(String[] args) {
        SocketClient client = new SocketClient(CommonSerializer.KRYO_SERIALIZER);
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        for(int i = 0; i < 20; i ++) {
            String res = helloService.hello(object);
            System.out.println(res);
        }
    }
}
