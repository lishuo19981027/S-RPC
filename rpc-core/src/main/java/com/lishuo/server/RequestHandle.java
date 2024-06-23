package com.lishuo.server;

import com.lishuo.entity.RpcRequest;
import com.lishuo.entity.RpcResponse;
import com.lishuo.enumeration.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Arrays;

/*实际进行过程调用的工作线程*/
public class RequestHandle implements Runnable{

    private static final Logger logger =
            LoggerFactory.getLogger(RequestHandle.class);

    private Socket socket;
    private Object service;

    public RequestHandle(Socket socket, Object service) {
        this.socket = socket;
        this.service = service;
    }
    @Override
    public void run() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            Object returnObject = invokeMethod(rpcRequest);
            objectOutputStream.writeObject(RpcResponse.success(returnObject));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException
                 | IllegalAccessException |
                 InvocationTargetException e) {
            logger.error("调用或发送时有错误发生：", e);
        }
    }

    private Object invokeMethod(RpcRequest rpcRequest) throws
            IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        Class<?> clazz = Class.forName(rpcRequest.getInterfaceName());
        if(!clazz.isAssignableFrom(service.getClass())){
            return RpcResponse.fail(ResponseCode.NOT_FOUND_CLASS);
        }
        Method method;
        try {
            method=service.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getParamTypes());
        }catch (NoSuchMethodException e){
            return RpcResponse.fail(ResponseCode.NOT_FOUND_METHOD);
        }
        return method.invoke(service,rpcRequest.getParameters());
    }
}
