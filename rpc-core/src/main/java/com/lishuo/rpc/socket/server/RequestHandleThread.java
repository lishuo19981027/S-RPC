package com.lishuo.rpc.socket.server;

import com.lishuo.entity.RpcRequest;
import com.lishuo.entity.RpcResponse;
import com.lishuo.rpc.registry.ServiceRegistry;
import com.lishuo.rpc.RequestHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/*处理RpcRequest的工作线程*/
public class RequestHandleThread implements Runnable{
        private static final Logger logger = LoggerFactory.getLogger(RequestHandleThread.class);

        private Socket socket;
        private RequestHandle requestHandle;
        private ServiceRegistry serviceRegistry;

    public RequestHandleThread(Socket socket, RequestHandle requestHandler, ServiceRegistry serviceRegistry) {
            this.socket = socket;
            this.requestHandle = requestHandler;
            this.serviceRegistry = serviceRegistry;
        }

        @Override
        public void run() {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
                RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
                String interfaceName = rpcRequest.getInterfaceName();
                Object service = serviceRegistry.getService(interfaceName);
                Object result = requestHandle.handle(rpcRequest, service);
                objectOutputStream.writeObject(RpcResponse.success(result));
                objectOutputStream.flush();
            } catch (IOException | ClassNotFoundException e) {
                logger.error("调用或发送时有错误发生：", e);
            }
        }
    }
