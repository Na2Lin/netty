package com.tml.netty.handler;

import com.alibaba.fastjson.JSONObject;
import com.tml.netty.core.ConnectPool;
import com.tml.netty.entity.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class DecoderHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        byte[] bytes = ByteBufUtil.getBytes(byteBuf);
        String s = new String(bytes);
        Message o = JSONObject.parseObject(s, Message.class);
        String fromId = o.getFromId()+"";
        if(!ConnectPool.hasId(fromId)) {
            ConnectPool.put(fromId,channelHandlerContext);
        }
        if(o.getToId() > 0) {
            ChannelHandlerContext ctx1 = ConnectPool.get(o.getToId() + "");
            ctx1.writeAndFlush(Unpooled.copiedBuffer(s,CharsetUtil.UTF_8));
        }
    }
}
