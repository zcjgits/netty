package com.jingqiduizhang.netty.demo4.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientHandler extends ChannelInboundHandlerAdapter {
    /**
     * 注册
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channelRegistered 注册...");
        super.channelRegistered(ctx);
    }
    /**
     * 激活
     * 通道激活时触发，当客户端connect成功后，
     * 服务端就会接收到这个事件，从而可以把客户端的Channel记录下来，供后面复用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelActive 激活...");
        super.channelActive(ctx);
    }

    /**
     * 断开
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelInactive 断开...");
        super.channelInactive(ctx);
    }

    /**
     * 注销
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channelUnregistered 注销...");
        super.channelUnregistered(ctx);
    }


    /**
     * 读取消息
     * 当收到对方发来的数据后，就会触发，
     * 参数msg就是发来的信息，可以是基础类型，也可以是序列化的复杂对象。
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("channelRead 读取消息...");
        try {
            String response = (String)msg;
            System.out.println("Client: " + response);
        } finally {
            ReferenceCountUtil.release(msg);
        }
//        如果后面还有通道要处理需要放开下面注释 否则需要注释掉  不需要往后传递了，因为这是最后一个处理流程了。
//        super.channelRead(ctx, msg);
    }

    /**
     * 消息读取完成
     * channelRead执行后触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("channelReadComplete 消息读取完成...");
        super.channelReadComplete(ctx);
    }

    /**
     * 用户事件
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("userEventTriggered 用户事件...");
        super.userEventTriggered(ctx, evt);
    }

    /**
     * 可写状态变更
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        log.info("可写状态变更为"+ctx.channel().isWritable());
        super.channelWritabilityChanged(ctx);
    }

    /**
     * 异常出发
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("exceptionCaught 发生异常...");
        super.exceptionCaught(ctx, cause);
    }
}
