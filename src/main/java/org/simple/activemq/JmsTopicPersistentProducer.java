package org.simple.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 持久化主题消息生产者
 * 在调用connection的start方法之前需要设置消息是否持久化
 *  producer.setDeliveryMode(DeliveryMode.PERSISTENT);
 */
public class JmsTopicPersistentProducer {
    private static final String MQ_URL="tcp://49.235.2.116:61616";
    private static final String TOPIC_NAME="simple-topic";
    public static void main(String[] args) throws JMSException {
        //1. 创建连接工厂,指定需要连接到的消息中间件的地址
        ConnectionFactory factory=new ActiveMQConnectionFactory(MQ_URL);
        //2. 通过连接工厂创建连接
        Connection connection=factory.createConnection();
        //3. 启动连接
        //connection.start();
        //4. 通过连接创建会话
        Session session=connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        //5. 通过会话创建主题/消息队列
        Topic topic=session.createTopic(TOPIC_NAME);
        //6. 通过会话创建消息生产者，并指定生产的消息需要发送的目的地(消息队列/主题)
        MessageProducer producer=session.createProducer(topic);
        //设置持久化后打开连接
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        connection.start();

        for(int i=1;i<=6;i++){
            //7. 通过会话创建消息
            TextMessage message=session.createTextMessage("来自主题："+TOPIC_NAME+"的消息");
            //8. 生产者将消息发送到指定的消息队列/主题中,供消费者消费
            producer.send(message);
        }
        System.out.println("消息生产投递到主题完毕");
        //9. 关闭打开的资源
        producer.close();
        session.close();
        connection.close();
    }
}
