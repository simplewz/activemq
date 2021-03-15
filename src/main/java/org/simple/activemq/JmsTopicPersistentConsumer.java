package org.simple.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * 主题消息的持久化消费
 * 1.通过连接工厂创建连接后需要设置连接的ClientID，此时并调用connection的start方法。
 *      connection.setClientID("simple");
 * 2.主题topic创建成功后需要设置主题的订阅者,然后再调用connection的start()方法。
 *      TopicSubscriber topicSubscriber=session.createDurableSubscriber(topic,"remark");
 *      connection.start();
 * 3.消息的消费通过订阅者进行消费
 *      Message message=topicSubscriber.receive();
 */
public class JmsTopicPersistentConsumer {
    private static final String MQ_URL="tcp://49.235.2.116:61616";
    private static final String TOPIC_NAME="simple-topic";
    public static void main(String[] args) throws JMSException{
        System.out.println("2号消费者");
        //1. 创建连接工厂
        ConnectionFactory factory=new ActiveMQConnectionFactory(MQ_URL);
        //2. 通过连接工厂创建连接
        Connection connection=factory.createConnection();
        //3. 开启连接
        //connection.start();
        connection.setClientID("simple");     //设置订阅者ClientId
        //4. 通过连接创建会话
        Session session=connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        //5. 通过会话创建主题
        Topic topic=session.createTopic(TOPIC_NAME);
        //设置主题订阅者
        TopicSubscriber topicSubscriber=session.createDurableSubscriber(topic,"remark");
        connection.start();

        Message message=topicSubscriber.receive();
        while(null!=message){
            TextMessage textMessage= (TextMessage) message;
            System.out.println(textMessage.getText());
            message=topicSubscriber.receive(1000L);
        }

        /*
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
         */
        //8. 关闭打开的资源
        //consumer.close();
        session.close();
        connection.close();
    }
}
