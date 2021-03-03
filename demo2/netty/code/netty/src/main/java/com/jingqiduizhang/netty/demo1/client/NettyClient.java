package com.jingqiduizhang.netty.demo1.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * netty 客户端
 * 此种方式存在缺陷 数据发送 数据会黏贴在一起
 */
@Slf4j
public class NettyClient {

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(new ClientHandler());
                    }
                });
      try{
          ChannelFuture cf = b.connect("127.0.0.1", 8888).sync();
          for(int i=0;i<10;i++){
              cf.channel().writeAndFlush(Unpooled.copiedBuffer("测试数据".getBytes()));
          }
          //等待客户端端口关闭
          cf.channel().closeFuture().sync();

      }catch (Exception e){
            e.printStackTrace();
      }finally {
          group.shutdownGracefully();
      }
    }

}
