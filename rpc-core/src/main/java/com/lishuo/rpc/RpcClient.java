package com.lishuo.rpc;

import com.lishuo.entity.RpcRequest;

/*客户端类通用接口*/
public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);
}
