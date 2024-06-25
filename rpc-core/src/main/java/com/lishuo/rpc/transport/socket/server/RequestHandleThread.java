package com.lishuo.rpc.transport.socket.server;

import com.lishuo.entity.RpcRequest;
import com.lishuo.entity.RpcResponse;
import com.lishuo.rpc.registry.ServiceRegistry;
import com.lishuo.rpc.RequestHandle;
import com.lishuo.rpc.serializer.CommonSerializer;
import com.lishuo.rpc.transport.socket.util.ObjectReader;
import com.lishuo.rpc.transport.socket.util.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/*处理RpcRequest的工作线程*/
public class RequestHandleThread implements Runnable{
        private static final Logger logger = LoggerFactory.getLogger(RequestHandleThread.class);

        private Socket socket;
        private RequestHandle requestHandle;
        private ServiceRegistry serviceRegistry;

       private CommonSerializer serializer;

    public RequestHandleThread(Socket socket, RequestHandle requestHandler,
                               ServiceRegistry serviceRegistry,CommonSerializer serializer) {
            this.socket = socket;
            this.requestHandle = requestHandler;
            this.serviceRegistry = serviceRegistry;
            this.serializer = serializer;
        }

        @Override
        public void run() {
            try (InputStream inputStream = socket.getInputStream();
                 OutputStream outputStream = socket.getOutputStream()) {
                RpcRequest rpcRequest = (RpcRequest) ObjectReader.readObject(inputStream);
                Object result = requestHandle.handle(rpcRequest);
                RpcResponse<Object> response = RpcResponse.success(result,
                        rpcRequest.getRequestId());
                ObjectWriter.writeObject(outputStream, response, serializer);
            }catch (IOException e) {
                logger.error("调用或发送时有错误发生：", e);
            }
        }
    }
