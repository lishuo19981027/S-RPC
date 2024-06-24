package com.lishuo.rpc.netty.client;

import com.lishuo.entity.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*Netty客户端侧处理器*/
public class NettyClientHandle extends
        SimpleChannelInboundHandler<RpcResponse> {
    private static final Logger logger =
            LoggerFactory.getLogger(NettyClientHandle.class);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse rpcResponse)
            throws Exception {
        try {
            logger.info(String.format("客户端接收到消息: %s", rpcResponse));
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse"
                    + rpcResponse.getRequestId());
            ctx.channel().attr(key).set(rpcResponse);
            ctx.channel().close();
        } finally {
            ReferenceCountUtil.release(rpcResponse);
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        logger.error("过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }

}
