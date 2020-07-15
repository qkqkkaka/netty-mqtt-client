package config;

import config.exceptions.CustomMqttException;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageBuilders;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;

import java.util.Arrays;
import java.util.Random;

/**
 * mqtt状态类
 *
 * @author wjl
 * @date 2020-06-28
 */
public class MqttSession {

    private Channel channel;

    private Random random;

    public MqttSession() {}

    public MqttSession(Channel channel) {
        this.channel = channel;
        this.random = new Random();
    }

    public void initChannel(Channel channel) {
        this.channel = channel;
    }

    public void publish(String data, String topic, MqttQoS qos) {
        MqttPublishMessage message = MqttMessageBuilders
                .publish()
                .messageId(0)
                .qos(qos)
                .payload(Unpooled.wrappedBuffer(data.getBytes()))
                .topicName(topic)
                .build();
        writeMsg(message);
    }

    public void publish(String data, String topic) {
        publish(data, topic, MqttQoS.AT_MOST_ONCE);
    }

    public void subscribe(String... topics) {
        MqttMessageBuilders.SubscribeBuilder subscribeBuilder = MqttMessageBuilders.subscribe().messageId(1);
        Arrays.asList(topics).forEach(topic -> subscribeBuilder.addSubscription(MqttQoS.AT_MOST_ONCE, topic));
        writeMsg(subscribeBuilder.build());
    }

    private void writeMsg(MqttMessage message) {
        if (!channel.isWritable()) {
            throw new CustomMqttException("mqtt channal is not writable");
        }
        channel.writeAndFlush(message);
    }
}
