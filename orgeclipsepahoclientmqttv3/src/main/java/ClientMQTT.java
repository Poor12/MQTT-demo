import java.util.concurrent.ScheduledExecutorService;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
public class ClientMQTT {
    public static final String HOST = "tcp://192.168.137.179:1883";
    public static final String TOPIC = "mtopic";
    private static final String clientid = "client11";
    private MqttClient client;
    private MqttConnectOptions options;
    private String userName = "stc";
    private String passWord = "123456";

    private ScheduledExecutorService scheduler;

    private void start(){
        try{
            client=new MqttClient(HOST,clientid,new MemoryPersistence());//设置为内存保存
            options=new MqttConnectOptions();
            //设置是否清空session，false表示服务器保留客户端的连接记录，true表示每次连接都用新的身份连接
            options.setCleanSession(true);
            options.setUserName(userName);
            options.setPassword(passWord.toCharArray());
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(20);
            //设置回调
            client.setCallback(new PushCallback());
            MqttTopic topic=client.getTopic(TOPIC);
            options.setWill(topic,"close".getBytes(),2,true);
            client.connect(options);

            //订阅消息
            int[] Qos={1};
            String[] topic1={TOPIC};
            client.subscribe(topic1,Qos);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws MqttException{
        ClientMQTT clientMQTT=new ClientMQTT();
        clientMQTT.start();
    }
}
