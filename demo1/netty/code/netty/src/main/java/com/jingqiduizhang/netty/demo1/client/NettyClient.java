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
    //https://blog.csdn.net/jone_lu/article/details/48269465

    public static void main(String[] args) {
        //调度模块组 一个EventLoopGroup有多个EventLoop，一个EventLoop有多个channel
        EventLoopGroup group = new NioEventLoopGroup();
        // 负责初始话netty服务器，并且开始监听端口的socket请求
        Bootstrap b = new Bootstrap();
        b.group(group)
                //NioServerSocketChannel（用于服务端非阻塞地接收TCP连接）、NioSocketChannel（用于维持非阻塞的TCP连接）、NioDatagramChannel（用于非阻塞地处理UDP连接）、OioServerSocketChannel（用于服务端阻塞地接收TCP连接）
                //OioSocketChannel（用于阻塞地接收TCP连接）、OioDatagramChannel（用于阻塞地处理UDP连接）
                .channel(NioSocketChannel.class)
                //主要目的是为程序员提供了一个简单的工具，用于在某个Channel注册到EventLoop后，对这个Channel执行一些初始化操作。
                // ChannelInitializer虽然会在一开始会被注册到Channel相关的pipeline里但是在初始化完成之后，ChannelInitializer会将自己从pipeline中移除，不会影响后续的操作
                //a. 在ServerBootstrap初始化时，为监听端口accept事件的Channel添加ServerBootstrapAcceptor
                //b. 在有新链接进入时，为监听客户端read/write事件的Channel添加用户自定义的ChannelHandler
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(new ClientHandler());
                    }
                });
      try{
          //https://blog.csdn.net/lgj123xj/article/details/78577945?utm_medium=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromMachineLearnPai2-1.control&dist_request_id=008157ca-2545-4fe5-9e01-a0a421f98c32&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromMachineLearnPai2-1.control
          ChannelFuture cf = b.connect("127.0.0.1", 8888).sync();
          for(int i=0;i<10;i++){
              //发送数据
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
