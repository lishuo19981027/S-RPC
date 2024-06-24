package com.lishuo.rpc;

import com.lishuo.entity.RpcRequest;
import com.lishuo.rpc.serializer.CommonSerializer;

/*客户端类通用接口*/
public interface RpcClient {

    Object sendRequest(RpcRequest rpcRequest);

    void setSerializer(CommonSerializer serializer);

}
