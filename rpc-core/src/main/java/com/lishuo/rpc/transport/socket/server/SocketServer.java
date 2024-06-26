package com.lishuo.rpc.transport.socket.server;

import com.lishuo.enumeration.RpcError;
import com.lishuo.exception.RpcException;
import com.lishuo.rpc.RequestHandle;
import com.lishuo.rpc.hook.ShutdownHook;
import com.lishuo.rpc.provider.NacosServiceRegistry;
import com.lishuo.rpc.provider.ServiceProvider;
import com.lishuo.rpc.provider.ServiceProviderImpl;
import com.lishuo.rpc.transport.RpcServer;
import com.lishuo.rpc.registry.ServiceRegistry;
import com.lishuo.rpc.serializer.CommonSerializer;
import com.lishuo.util.ThreadPoolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/*Socket方式远程方法调用的提供者（服务端）*/
public class SocketServer implements RpcServer {
    private static final Logger logger =
            LoggerFactory.getLogger(SocketServer.class);
    private final ExecutorService threadPool;;
    private final String host;
    private final int port;
    private RequestHandle requestHandler = new RequestHandle();
    private final CommonSerializer serializer;

    private final ServiceRegistry serviceRegistry;
    private final ServiceProvider serviceProvider;

    public SocketServer(String host, int port) {
        this(host, port, DEFAULT_SERIALIZER);
    }

    public SocketServer(String host, int port, Integer serializer) {
        this.host = host;
        this.port = port;
        threadPool = ThreadPoolFactory.createDefaultThreadPool("socket-rpc-server");
        this.serviceRegistry = new NacosServiceRegistry();
        this.serviceProvider = new ServiceProviderImpl();
        this.serializer = CommonSerializer.getByCode(serializer);
    }

    @Override
    public <T> void publishService(T service, Class<T> serviceClass)  {
        if(serializer == null) {
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        serviceProvider.addServiceProvider(service,serviceClass);
        serviceRegistry.register(serviceClass.getCanonicalName(), new InetSocketAddress(host, port));
        start();
    }

    @Override
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress(host, port));
            logger.info("服务器启动……");
            ShutdownHook.getShutdownHook().addClearAllHook();
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                logger.info("消费者连接: {}:{}", socket.getInetAddress(), socket.getPort());
                threadPool.execute(new SocketRequestHandleThread(socket, requestHandler,serializer));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            logger.error("服务器启动时有错误发生:", e);
        }
    }

}
