import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
public class PushCallback implements MqttCallback {

    public void connectionLost(Throwable throwable) {
        System.out.println("连接断开，重连处理");
    }

    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
       System.out.println("接收消息主题："+s);
       System.out.println("接收消息Qos："+mqttMessage.getQos());
       System.out.println("接收消息内容："+new String(mqttMessage.getPayload()));
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        System.out.println("deliveryComplete------"+iMqttDeliveryToken.isComplete());
    }

}
