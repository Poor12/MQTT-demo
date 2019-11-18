import com.sun.security.ntlm.Server;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class ServerMQTT {
    //服务器地址
    public static final String Host="tcp://192.168.137.179:1883";
    //主题
    public static final String TOPIC="mtopic";
    //定义mqtt的id
    private static final String clientid="server11";

    private MqttClient client;
    private MqttTopic topic11;
    private String userName="stc";
    private String passWord="123456";
    private MqttMessage message;

    public ServerMQTT() throws MqttException{
        client=new MqttClient(Host,clientid,new MemoryPersistence());
        connect();//,连接服务器
    }

    public static byte[] getContent(String filepath) throws IOException {
        File file=new File(filepath);
        long filesize=file.length();
        if (filesize>Integer.MAX_VALUE){
            System.out.println("file too big...");
            return null;
        }
        FileInputStream fi=new FileInputStream(file);
        byte[] buffer=new byte[(int)filesize];
        int offset=0;
        int numRead=0;
        while(offset<filesize&&(numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0)
        {
            offset+=numRead;
        }
        if(offset!=buffer.length){
            throw new IOException("Could not completely read file "
                    + file.getName());
        }
        fi.close();
        return buffer;
    }

    private void connect(){
        MqttConnectOptions options=new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        //设置超时
        options.setConnectionTimeout(10);
        //设置心跳
        options.setKeepAliveInterval(20);
        try{
            client.setCallback(new PushCallback());
            client.connect(options);
            topic11=client.getTopic(TOPIC);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void publish(MqttTopic topic,MqttMessage message) throws MqttPersistenceException,MqttException{
        MqttDeliveryToken token=topic.publish(message);
        token.waitForCompletion();
        System.out.println("message is published completely! "+token.isComplete());
    }

    public static void main(String[] args) throws MqttException{
        Scanner in=new Scanner(System.in);
//        System.out.println("请输入用户名：");
//        String user=in.next();
//        System.out.println("请输入密码：");
//        String passwd=in.next();
//        if(!(user.equals("stc")&&passwd.equals("123456"))){
//            System.out.println("认证失败");
//            return;
//        }
//        else{
//            System.out.println("认证成功");
//        }
        ServerMQTT server=new ServerMQTT();
        server.message=new MqttMessage();
        server.message.setQos(1);
        server.message.setRetained(true);
        System.out.println("请输入命令：");
        String order=in.next();
        if (order.equals("tm")) {
            System.out.println("请输入message：");
            String m=in.next();
            server.message.setPayload(m.getBytes());
        }
        else if(order.equals("tf")){
            System.out.println("请输入文件路径：");
            String file=in.next();
            try {
                server.message.setPayload(getContent(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //发布消息
        server.publish(server.topic11,server.message);//
        System.out.println(server.message.isRetained()+"-------retained状态");
    }
}
