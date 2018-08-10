package com.tml.netty.core;

import io.netty.channel.ChannelHandlerContext;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectPool {

    private static Map<String, ChannelHandlerContext> pool = new ConcurrentHashMap<>();

    public static void put(String id,ChannelHandlerContext ctx) {
        ChannelHandlerContext channelHandlerContext = pool.get(id);
        if(channelHandlerContext == null || channelHandlerContext.isRemoved()) {
            pool.put(id,ctx);
        }
    }

    public static ChannelHandlerContext get(String id) {
        return pool.get(id);
    }

    public static Collection<ChannelHandlerContext> connectPool() {
        return pool.values();
    }

    public static boolean hasId(String id) {
        return pool.containsKey(id) && pool.get(id) != null;
    }
}
