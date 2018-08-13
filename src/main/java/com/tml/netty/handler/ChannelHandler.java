package com.tml.netty.handler;

import com.tml.netty.core.ConnectPool;
import com.tml.netty.entity.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;


@Component
@io.netty.channel.ChannelHandler.Sharable
public class ChannelHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message o) throws Exception {
        String fromId = o.getFromId()+"";
        ConnectPool.put(fromId,channelHandlerContext);
        if(o.getToId() > 0) {
            ChannelHandlerContext ctx1 = ConnectPool.get(o.getToId() + "");
            if(ctx1 == null || ctx1.isRemoved()) {
                o.setContent("对方不在线");
                o.setToId(0);
                channelHandlerContext.writeAndFlush(o);
            }else {
                ctx1.writeAndFlush(o);
            }
        }
    }
}
