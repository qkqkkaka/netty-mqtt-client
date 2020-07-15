import config.MqttConnectOptions;
import config.MqttSession;
import config.MqttVersionEnum;
import config.exceptions.CustomMqttException;
import config.handler.MqttClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.mqtt.*;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * Mqtt client
 *
 * @author wjl
 * @date 2020-07-14
 */
public class MqttClientApplication {

    public static void main(String[] args) {
        MqttConnectOptions connectOptions = new MqttConnectOptions.Builder()
            .isCleanSession(false)
            .isWillFlag(false)
            .isWillRetain(false)
            .keepAliveTimeSeconds(20)
            .willQos(0)
            .hasPassword(true)
            .hasUserName(true)
            .userName("userName")
            .password("password".getBytes())
            .clientId("clientId")
            .isDup(false)
            .isRetain(false)
            .mqttQoS(MqttQoS.AT_MOST_ONCE)
            .mqttVersion(MqttVersionEnum.MQTT_3_1_1)
            .willMessage(null)
            .willQos(0)
            .willTopic(null)
            .remainingLength(0)
            .build();
        MqttSession mqttSession = new MqttSession();

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.handler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast("encoder", MqttEncoder.INSTANCE);
                ch.pipeline().addLast("decoder", new MqttDecoder());
                ch.pipeline().addLast("heartBeatHandler", new IdleStateHandler(0, 10, 0, TimeUnit.SECONDS));
                ch.pipeline().addLast("handler", new MqttClientHandler(connectOptions, mqttSession, (ctx, msg) -> {
                    if (!(msg instanceof MqttMessage)) {
                        return;
                    }
                    MqttMessage mqttMessage = (MqttMessage) msg;

                    switch (mqttMessage.fixedHeader().messageType()) {
                        case CONNACK:
                            handlerConnAck((MqttConnAckMessage) mqttMessage, mqttSession);
                            break;
                        case PINGRESP:
                            break;
                        case DISCONNECT:
                            handleDisconnect(mqttMessage);
                            break;
                        case PUBLISH:
                            handlePublish((MqttPublishMessage) mqttMessage);
                            break;
                        case PUBACK:
                            handlePubAck((MqttPubAckMessage) mqttMessage);
                            break;
                        case SUBACK:
                            break;
                        case UNSUBACK:
                            break;
                        default:
                            System.out.println("receive unexpected message type:" + mqttMessage.fixedHeader().messageType());
                            System.out.println(mqttMessage);
                    }
                }));
            }
        });

        String remoteHost = "host";
        int remotePort = 1883;

        try {
            ChannelFuture f = b.connect(remoteHost, remotePort).sync();
            System.out.println("Client connected");
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    private static void handlerConnAck(MqttConnAckMessage mqttConnAckMessage, MqttSession mqttSession) {
        if (mqttConnAckMessage.variableHeader().connectReturnCode().equals(MqttConnectReturnCode.CONNECTION_ACCEPTED)) {
            // sub topic
            mqttSession.subscribe("$msg/up", "$msg/down");
            System.out.println("sub topic");
            return;
        }
        System.out.println("connect error:" + mqttConnAckMessage.variableHeader().connectReturnCode());
        throw new CustomMqttException("connect error");
    }

    private static void handlePublish(MqttPublishMessage mqttPublishMessage) {
        ByteBuf payload = mqttPublishMessage.payload();
        byte[] payloadInBytes = new byte[payload.readableBytes()];
        payload.readBytes(payloadInBytes);
        String strPayload = new String(payloadInBytes);
        System.out.println("public message payload:" + strPayload);
    }

    private static void handlePubAck(MqttPubAckMessage mqttPubAckMessage) {
        System.out.println(mqttPubAckMessage);

    }

    private static void handleDisconnect(MqttMessage mqttMessage) {

    }
}
