package com.lishuo.rpc;

import com.lishuo.entity.RpcRequest;
import com.lishuo.entity.RpcResponse;
import com.lishuo.enumeration.ResponseCode;
import com.lishuo.rpc.provider.ServiceProvider;
import com.lishuo.rpc.provider.ServiceProviderImpl;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/*进行过程调用的处理器*/
public class RequestHandle {

    private static final Logger logger =
            LoggerFactory.getLogger(RequestHandle.class);

    private static ServiceProvider serviceProvider;

    static {
        serviceProvider = new ServiceProviderImpl();
    }

    public Object handle(RpcRequest rpcRequest) {
        Object service =
                serviceProvider.getServiceProvider(rpcRequest.getInterfaceName());
        return invokeTargetMethod(rpcRequest, service);
    }

    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        Object result;
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(),
                    rpcRequest.getParamTypes());
            result = method.invoke(service, rpcRequest.getParameters());
            logger.info("服务:{} 成功调用方法:{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return RpcResponse.fail(ResponseCode.NOT_FOUND_METHOD, rpcRequest.getRequestId());
        }
        return result;
    }
}
