package com.jingqiduizhang.netty.demo4.client;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 心跳检测处理类
 * readerIdleTimeSeconds: 读超时. 即当在指定的时间间隔内没有从 Channel 读取到数据时,
 * 会触发一个 READER_IDLE 的 IdleStateEvent 事件.
 * writerIdleTimeSeconds: 写超时. 即当在指定的时间间隔内没有数据写入到 Channel 时,
 * 会触发一个 WRITER_IDLE 的 IdleStateEvent 事件.
 * allIdleTimeSeconds: 读/写超时. 即当在指定的时间间隔内没有读或写操作时,
 * 会触发一个 ALL_IDLE 的 IdleStateEvent 事件.
 *注：这三个参数默认的时间单位是秒。若需要指定其他时间单位，可以使用另一个构造方法：
 * IdleStateHandler(boolean observeOutput, long readerIdleTime, long writerIdleTime, long allIdleTime, TimeUnit unit)
 *
 * 检查写入超时时间 如果1分钟内 客户端未写入数据发送到服务端 则进行发送心跳信息。
 * @Author: duran
 * @Date: 2020.09.05
 */
@Slf4j
public class HeartBeatIdleStateHandler extends IdleStateHandler {
    private static final int writeIdleTime = 5;//20秒

    public HeartBeatIdleStateHandler() {
        super(0, writeIdleTime, 0, TimeUnit.SECONDS);
        log.info("客户端 开启心跳检测...");
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) {

        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            //如果是写超时 则发送心跳
            if(state ==IdleState.WRITER_IDLE ){
                log.info("心跳... "+state);
                //发送主链路保持请求信息
                log.info("发送主链路保持请求信息...");
                String message = "链路保持信息";
                String response = message + "$_";
                ctx.writeAndFlush(Unpooled.copiedBuffer(response.getBytes()));
            }
        }
    }


}
