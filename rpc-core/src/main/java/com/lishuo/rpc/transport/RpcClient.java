package com.lishuo.rpc.transport;

import com.lishuo.entity.RpcRequest;
import com.lishuo.rpc.serializer.CommonSerializer;

/*客户端类通用接口*/
public interface RpcClient {

    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    Object sendRequest(RpcRequest rpcRequest);

}
