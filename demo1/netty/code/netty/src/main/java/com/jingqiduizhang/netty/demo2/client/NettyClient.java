package com.jingqiduizhang.netty.demo2.client;

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
import lombok.extern.slf4j.Slf4j;

/**
 * netty 客户端
 * 解决粘包方式一
 *
 * 1.设置特殊分隔符
 * ByteBuf buf = Unpooled.copiedBuffer("$_".getBytes());
 * sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, buf));
 * 2.发送内容添加后缀符号
 * Unpooled.wrappedBuffer("测试数据$_".getBytes())
 *
 * 解决粘包方式二
 * 设置定长字符串接收
 * 	sc.pipeline().addLast(new FixedLengthFrameDecoder(5));
 * 	发送数据超过长度就会别截取。
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
                        sc.pipeline().addLast(new ClientHandler());
                    }
                });
      try{
          ChannelFuture cf = b.connect("127.0.0.1", 8888).sync();
          for(int i=0;i<10;i++){
              cf.channel().writeAndFlush(Unpooled.wrappedBuffer("测试数据$_".getBytes()));
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
