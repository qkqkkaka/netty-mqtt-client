package config.handler;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author wangjianlin
 * @description message callback
 * @date 2020-07-15 15:35
 */
public interface MqttMessageCallback {

    void handler(ChannelHandlerContext ctx, Object msg);
}
