package com.lishuo.rpc;


import com.lishuo.rpc.serializer.CommonSerializer;

/*服务器类通用接口*/
public interface RpcServer  {

    void start(int port);

    void setSerializer(CommonSerializer serializer);
}
