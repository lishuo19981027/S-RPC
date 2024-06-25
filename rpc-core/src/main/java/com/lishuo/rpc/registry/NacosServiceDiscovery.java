package com.lishuo.rpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.lishuo.util.NacosUtil;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import com.alibaba.nacos.api.naming.NamingService;

import java.net.InetSocketAddress;
import java.util.List;

public class NacosServiceDiscovery implements ServiceDiscovery{

    private static final Logger logger =
            LoggerFactory.getLogger(NacosServiceDiscovery.class);

    private final NamingService namingService;

    public NacosServiceDiscovery() {
        namingService = NacosUtil.getNacosNamingService();
    }
    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try {
            List<Instance> instances =
                    NacosUtil.getAllInstance(namingService, serviceName);
            Instance instance = instances.get(0);
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            logger.error("获取服务时有错误发生:", e);
        }
        return null;
    }
}
