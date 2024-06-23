package com.lishuo.server;


import com.lishuo.registry.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;


/*远程方法调用的提供者（服务端）*/
public class RpcServer {

    private static final Logger logger =
            LoggerFactory.getLogger(RpcServer.class);

        private static final int CORE_POOL_SIZE = 5;
        private static final int MAXIMUM_POOL_SIZE = 50;
        private static final int KEEP_ALIVE_TIME = 60;
        private static final int BLOCKING_QUEUE_CAPACITY = 100;
        private final ExecutorService threadPool;
        private RequestHandle requestHandle = new RequestHandle();
        private final ServiceRegistry serviceRegistry;

    public RpcServer(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
        BlockingQueue<Runnable> workingQueue =
                new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE,
                MAXIMUM_POOL_SIZE,KEEP_ALIVE_TIME,TimeUnit.SECONDS,
                workingQueue,Executors.defaultThreadFactory());
    }

    public void start(int port){
        try(ServerSocket serverSocket = new ServerSocket(port)){
            logger.info("服务器启动……");
            Socket socket;
            while ((socket=serverSocket.accept())!=null){
                logger.info("消费者连接: {}:{}", socket.getInetAddress(), socket.getPort());
                threadPool.execute(new RequestHandleThread(socket, requestHandle, serviceRegistry));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            logger.error("服务器启动时有错误发生:",e);
        }
    }
}
