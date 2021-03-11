package org.simple.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * 消费主题中的消息，需要先将消费者启动
 */
public class JmsTopicConsumer {
    private static final String MQ_URL="tcp://49.235.2.116:61616";
    private static final String TOPIC_NAME="simple-topic";
    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("2号消费者");
        //1. 创建连接工厂
        ConnectionFactory factory=new ActiveMQConnectionFactory(MQ_URL);
        //2. 通过连接工厂创建连接
        Connection connection=factory.createConnection();
        //3. 开启连接
        connection.start();
        //4. 通过连接创建会话
        Session session=connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        //5. 通过会话创建主题
        Topic topic=session.createTopic(TOPIC_NAME);
        //6. 通过会话创建消费者
        MessageConsumer consumer=session.createConsumer(topic);
        //7. 设置消息监听器消费消息(以非阻塞的方式的消费消息)
        consumer.setMessageListener(message -> {
            if(null != message && message instanceof TextMessage){
                TextMessage textMessage= (TextMessage) message;
                try {
                    System.out.println("收到订阅主题:"+TOPIC_NAME+"的消息："+textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        //设置消费者不关闭进程.便于调试观察
        System.in.read();
        //8. 关闭打开的资源
        consumer.close();
        session.close();
        connection.close();
    }

}
