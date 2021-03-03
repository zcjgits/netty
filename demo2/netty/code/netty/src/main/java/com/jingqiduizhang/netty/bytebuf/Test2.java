package com.jingqiduizhang.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class Test2 {

    public static void main(String[] args) {
        //创建一个16字节的buffer,这里默认是创建heap buffer
        ByteBuf buf = Unpooled.buffer(16);
        //写数据到buffer
//        for(int i=0; i<16; i++){
//            buf.writeByte(i+1);
//        }
        buf.writeByte(1);
        buf.writeInt(12);
        //读数据
//        for(int i=0; i<buf.capacity(); i++){
////            System.out.print(buf.getByte(i)+", ");
////        }
        System.out.println("size1 "+buf.capacity());
        byte b1 = buf.readByte();
        byte b2 = buf.getByte(0);
        int b3 = buf.readInt();
        System.out.println(b1);
        System.out.println(b2);
        System.out.println(b3);
        System.out.println(buf.toString());
        System.out.println("size2 "+buf.capacity());
    }
}
