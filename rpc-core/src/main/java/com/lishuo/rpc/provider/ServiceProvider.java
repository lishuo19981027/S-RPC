package com.lishuo.rpc.provider;

/*保存和提供服务实例对象*/
public interface ServiceProvider {

    <T> void addServiceProvider(T service, Class<T> serviceClass);

    Object getServiceProvider(String serviceName);
}
