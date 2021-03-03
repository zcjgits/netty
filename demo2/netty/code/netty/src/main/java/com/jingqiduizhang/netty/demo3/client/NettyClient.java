package com.jingqiduizhang.netty.demo3.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * netty 客户端
 *
 * 设置字符串形式的解码
 *
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
                        //设置特殊分隔符
                        ByteBuf buf = Unpooled.copiedBuffer("$_".getBytes());
                        sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, buf));
                       //设置字符串形式的解码
                        sc.pipeline().addLast(new StringDecoder());
                        sc.pipeline().addLast(new ClientHandler());
                    }
                });
      try{
          ChannelFuture cf = b.connect("127.0.0.1", 8888).sync();
          for(int i=0;i<10;i++){
              cf.channel().writeAndFlush(Unpooled.copiedBuffer("测试数据$_".getBytes()));
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
