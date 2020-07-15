package config;

import io.netty.handler.codec.mqtt.MqttQoS;

/**
 * mqtt connect options
 *
 * @author wjl
 * @date 2020-07-14
 */
public class MqttConnectOptions {

    private boolean hasUserName;

    private boolean hasPassword;

    private boolean isWillRetain;

    private int willQos;

    private boolean isWillFlag;

    private boolean isCleanSession;

    private int keepAliveTimeSeconds;

    private MqttQoS mqttQoS;

    private boolean isDup;

    private boolean isRetain;

    private int remainingLength;

    private MqttVersionEnum mqttVersion;

    private String willTopic;

    private byte[] willMessage;

    private String userName;

    private byte[] password;

    private String clientId;

    private MqttConnectOptions(Builder builder) {
        this.hasPassword = builder.hasPassword;
        this.hasUserName = builder.hasUserName;
        this.isCleanSession = builder.isCleanSession;
        this.isWillFlag = builder.isWillFlag;
        this.isWillRetain = builder.isWillRetain;
        this.keepAliveTimeSeconds = builder.keepAliveTimeSeconds;
        this.willQos = builder.willQos;
        this.mqttQoS = builder.mqttQoS;
        this.mqttVersion = builder.mqttVersion;
        this.isDup = builder.isDup;
        this.isRetain = builder.isRetain;
        this.remainingLength = builder.remainingLength;
        this.clientId = builder.clientId;
        this.userName = builder.userName;
        this.password = builder.password;
        this.willTopic = builder.willTopic;
        this.willMessage = builder.willMessage;
    }

    public boolean isHasUserName() {
        return hasUserName;
    }

    public boolean isHasPassword() {
        return hasPassword;
    }

    public boolean isWillRetain() {
        return isWillRetain;
    }

    public int getWillQos() {
        return willQos;
    }

    public boolean isWillFlag() {
        return isWillFlag;
    }

    public boolean isCleanSession() {
        return isCleanSession;
    }

    public int getKeepAliveTimeSeconds() {
        return keepAliveTimeSeconds;
    }

    public MqttQoS getMqttQoS() {
        return mqttQoS;
    }

    public boolean isDup() {
        return isDup;
    }

    public boolean isRetain() {
        return isRetain;
    }

    public int getRemainingLength() {
        return remainingLength;
    }

    public MqttVersionEnum getMqttVersion() {
        return mqttVersion;
    }

    public String getWillTopic() {
        return willTopic;
    }

    public byte[] getWillMessage() {
        return willMessage;
    }

    public String getUserName() {
        return userName;
    }

    public byte[] getPassword() {
        return password;
    }

    public String getClientId() {
        return clientId;
    }

    public static class Builder {

        private boolean hasUserName = true;

        private boolean hasPassword = true;

        private boolean isWillRetain = false;

        private int willQos = 0;

        private boolean isWillFlag = false;

        private boolean isCleanSession = false;

        private int keepAliveTimeSeconds = 20;

        private MqttQoS mqttQoS = MqttQoS.AT_MOST_ONCE;

        private boolean isDup = false;

        private boolean isRetain = false;

        private int remainingLength = 0;

        private MqttVersionEnum mqttVersion = MqttVersionEnum.MQTT_3_1_1;

        private String willTopic = null;

        private byte[] willMessage = null;

        private String userName = "userName";

        private byte[] password = "password".getBytes();

        private String clientId = "mqtt-client";

        public Builder hasUserName(boolean hasUserName) {
            this.hasUserName = hasUserName;
            return this;
        }

        public Builder hasPassword(boolean hasPassword) {
            this.hasPassword = hasPassword;
            return this;
        }

        public Builder isWillRetain(boolean isWillRetain) {
            this.isWillRetain = isWillFlag;
            return this;
        }

        public Builder willQos(int willQos) {
            this.willQos = willQos;
            return this;
        }

        public Builder isWillFlag(boolean isWillFlag) {
            this.isWillFlag = isWillFlag;
            return this;
        }

        public Builder isCleanSession(boolean isCleanSession) {
            this.isCleanSession = isCleanSession;
            return this;
        }

        public Builder keepAliveTimeSeconds(int keepAliveTimeSeconds) {
            this.keepAliveTimeSeconds = keepAliveTimeSeconds;
            return this;
        }

        public Builder mqttQoS(MqttQoS mqttQoS) {
            this.mqttQoS = mqttQoS;
            return this;
        }

        public Builder isDup(boolean isDup) {
            this.isDup = isDup;
            return this;
        }

        public Builder isRetain(boolean isRetain) {
            this.isRetain = isRetain;
            return this;
        }

        public Builder remainingLength(int remainingLength) {
            this.remainingLength = remainingLength;
            return this;
        }

        public Builder mqttVersion(MqttVersionEnum mqttVersion) {
            this.mqttVersion = mqttVersion;
            return this;
        }

        public Builder willTopic(String willTopic) {
            this.willTopic = willTopic;
            return this;
        }

        public Builder willMessage(byte[] willMessage) {
            this.willMessage = willMessage;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder password(byte[] password) {
            this.password = password;
            return this;
        }

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public MqttConnectOptions build() {
            return new MqttConnectOptions(this);
        }
    }
}
