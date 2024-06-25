package com.lishuo.rpc.registry;

import java.net.InetSocketAddress;

/*服务注册表通用接口*/
public interface ServiceRegistry {

    /*将一个服务注册进注册表*/
    void register(String serviceName, InetSocketAddress inetSocketAddress);

}
