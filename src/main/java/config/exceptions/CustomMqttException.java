package config.exceptions;

/**
 * @author wangjianlin
 * @description 自定义mqtt异常类
 * @date 2020-07-08 15:36
 */
public class CustomMqttException extends RuntimeException {

    public CustomMqttException(Throwable cause) {
        super(cause);
    }

    public CustomMqttException() {
        super();
    }

    public CustomMqttException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomMqttException(String message) {
        super(message);
    }
}
