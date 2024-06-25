package com.lishuo.rpc.registry;

import java.net.InetSocketAddress;
/*服务发现接口*/
public interface ServiceDiscovery {

    /*根据服务名称查找服务实体*/
    InetSocketAddress lookupService(String serviceName);
}
