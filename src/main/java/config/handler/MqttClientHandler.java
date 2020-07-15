package config.handler;

import config.MqttConnectOptions;
import config.MqttSession;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.mqtt.*;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * message handler
 *
 * @author wjl
 * @date 2020-07/14
 */
public class MqttClientHandler extends ChannelInboundHandlerAdapter {

    private MqttSession mqttSession;
    private MqttConnectOptions mqttConnectOptions;
    private MqttMessageCallback mqttMessageCallback;

    public MqttClientHandler(String clientId, String userName, String password, MqttSession mqttSession, MqttMessageCallback mqttMessageCallback) {
        this(new MqttConnectOptions.Builder()
            .clientId(clientId)
            .userName(userName)
            .password(password.getBytes())
            .build(), mqttSession, mqttMessageCallback);
    }

    public MqttClientHandler(MqttConnectOptions mqttConnectOptions, MqttSession mqttSession, MqttMessageCallback mqttMessageCallback) {
        this. mqttConnectOptions = mqttConnectOptions;
        this.mqttMessageCallback = mqttMessageCallback;
        this.mqttSession = mqttSession;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (null == mqttMessageCallback) {
            System.out.println("no MqttMessageCallback can be used");
            return;
        }
        mqttMessageCallback.handler(ctx, msg);
    }

    /**
     * send connection and subscribe topics
     *
     * @author wjl
     * @date 2020/7/14
     * @param ctx channelHandlerContext
     * @return void
     **/
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        MqttFixedHeader connectFixedHeader =
                new MqttFixedHeader(MqttMessageType.CONNECT
                    , mqttConnectOptions.isDup()
                    , mqttConnectOptions.getMqttQoS()
                    , mqttConnectOptions.isRetain()
                    , mqttConnectOptions.getRemainingLength());
        MqttConnectVariableHeader connectVariableHeader =
                new MqttConnectVariableHeader(mqttConnectOptions.getMqttVersion().getName()
                    , mqttConnectOptions.getMqttVersion().getVersion()
                    , mqttConnectOptions.isHasUserName()
                    , mqttConnectOptions.isHasPassword()
                    , mqttConnectOptions.isWillRetain()
                    , mqttConnectOptions.getWillQos()
                    , mqttConnectOptions.isWillFlag()
                    , mqttConnectOptions.isCleanSession()
                    , mqttConnectOptions.getKeepAliveTimeSeconds());
        MqttConnectPayload connectPayload = new MqttConnectPayload(mqttConnectOptions.getClientId()
            , mqttConnectOptions.getWillTopic()
            , mqttConnectOptions.getWillMessage()
            , mqttConnectOptions.getUserName()
            , mqttConnectOptions.getPassword());
        MqttConnectMessage connectMessage =
                new MqttConnectMessage(connectFixedHeader, connectVariableHeader, connectPayload);
        ctx.writeAndFlush(connectMessage);
        System.out.println("Sent CONNECT");
        mqttSession.initChannel(ctx.channel());


    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            MqttFixedHeader pingreqFixedHeader =
                    new MqttFixedHeader(MqttMessageType.PINGREQ, false, MqttQoS.AT_MOST_ONCE, false, 0);
            MqttMessage pingreqMessage = new MqttMessage(pingreqFixedHeader);
            ctx.writeAndFlush(pingreqMessage);
            System.out.println("Sent PINGREQ");
        } else {
            super.userEventTriggered(ctx, evt);
        }

        // test publish message
        mqttSession.publish("test data", "$msg/up");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}
