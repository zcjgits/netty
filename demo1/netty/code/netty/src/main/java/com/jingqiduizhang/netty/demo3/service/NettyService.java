package com.jingqiduizhang.netty.demo3.service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * netty 服务端
 * 设置字符串形式的解码
 */
@Slf4j
public class NettyService {
    private static  EventLoopGroup bossLoopGroup;
    private static EventLoopGroup workerLoopGroup;
    public static void main(String[] args) {
        try {   //1 设置reactor 线程
            ServerBootstrap b = new ServerBootstrap();
            int port = 8888;
            bossLoopGroup = new NioEventLoopGroup(2);
            workerLoopGroup = new NioEventLoopGroup(10);
            b.group(bossLoopGroup, workerLoopGroup);
            //2 设置nio类型的channel
            b.channel(NioServerSocketChannel.class);
            //3 设置监听端口
            b.localAddress(new InetSocketAddress(port));
            //4 设置通道选项
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            //5 装配流水线
            b.childHandler(new ChannelInitializer<SocketChannel>(){
                //有连接到达时会创建一个channel
                protected void initChannel(SocketChannel ch) throws Exception{
                    //设置特殊分隔符
                    ByteBuf buf = Unpooled.copiedBuffer("$_".getBytes());
                    ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, buf));
                    //设置字符串形式的解码
                    ch.pipeline().addLast(new StringDecoder());
                    // 在channel队列中添加一个handler来处理业务
                    ch.pipeline().addLast("serverHandler", new ServerHandler());
                }
            });

            // 6 开始绑定server
            // 通过调用sync同步方法阻塞直到绑定成功
            ChannelFuture channelFuture = b.bind().sync();
            log.info(NettyService.class.getName() +
                    " started and listen on " +
                    channelFuture.channel().localAddress());

            // 7 监听通道关闭事件
            // 应用程序会一直等待，直到channel关闭
            ChannelFuture closeFuture=  channelFuture.channel().closeFuture();
            closeFuture.sync();
        } catch (Exception e){
                e.printStackTrace();
        } finally{
            // 8 优雅关闭EventLoopGroup，
            // 释放掉所有资源包括创建的线程
            workerLoopGroup.shutdownGracefully();
            bossLoopGroup.shutdownGracefully();
        }
    }

}
