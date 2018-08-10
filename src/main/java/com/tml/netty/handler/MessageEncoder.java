package com.tml.netty.handler;

import com.alibaba.fastjson.JSONObject;
import com.tml.netty.entity.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.springframework.stereotype.Component;

@Component
public class MessageEncoder extends MessageToByteEncoder<Message> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        String s = JSONObject.toJSONString(message);
        byteBuf.writeBytes(s.getBytes());
    }
}
