package com.lishuo.rpc.transport.netty.client;

import com.lishuo.entity.RpcRequest;
import com.lishuo.entity.RpcResponse;
import com.lishuo.rpc.factory.SingletonFactory;
import com.lishuo.rpc.serializer.CommonSerializer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/*Netty客户端侧处理器*/
public class NettyClientHandle extends
        SimpleChannelInboundHandler<RpcResponse> {
    private static final Logger logger =
            LoggerFactory.getLogger(NettyClientHandle.class);

    private final UnprocessedRequests unprocessedRequests;

    public NettyClientHandle() {
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse rpcResponse)
            throws Exception {
        try {
            logger.info(String.format("客户端接收到消息: %s", rpcResponse));
            unprocessedRequests.complete(rpcResponse);
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
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                logger.info("发送心跳包 [{}]", ctx.channel().remoteAddress());
                Channel channel = ChannelProvider.get((InetSocketAddress) ctx.channel().remoteAddress(), CommonSerializer.getByCode(CommonSerializer.DEFAULT_SERIALIZER));
                RpcRequest rpcRequest = new RpcRequest();
                rpcRequest.setHeartBeat(true);
                channel.writeAndFlush(rpcRequest).
                        addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
