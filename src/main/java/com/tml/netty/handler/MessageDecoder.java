package com.tml.netty.handler;

import com.alibaba.fastjson.JSONObject;
import com.tml.netty.entity.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        String msg = new String(ByteBufUtil.getBytes(byteBuf),CharsetUtil.UTF_8);
        Message message = JSONObject.parseObject(msg, Message.class);
        list.add(message);
        // 读取所有字节
        byteBuf.skipBytes(byteBuf.readableBytes());
    }
}
