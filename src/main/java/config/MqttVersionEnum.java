package config;

/**
 * @author wangjianlin
 * @description mqtt version enum
 * @date 2020-07-08 15:51
 */
public enum MqttVersionEnum {

    MQTT_3_1_1("MQTT", 4);

    private String name;

    private int version;

    MqttVersionEnum(String name, int version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public int getVersion() {
        return version;
    }
}
