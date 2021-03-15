package org.simple.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 队列持久化消息
 */
public class JmsQueuePersistentProducer {

    private static final String MQ_URL="tcp://49.235.2.116:61616";

    private static final String QUEUE_NAME="sample-queue";

    public static void main(String[] args) throws JMSException {
        ConnectionFactory factory=new ActiveMQConnectionFactory(MQ_URL);
        Connection connection=factory.createConnection();
        Session session=connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Queue queue=session.createQueue(QUEUE_NAME);
        MessageProducer producer=session.createProducer(queue);
        //设置消息持久化
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        for(int i=1;i<=6;i++){
            TextMessage message=session.createTextMessage("来自队列"+QUEUE_NAME+"的消息"+i);
            producer.send(message);
        }
        System.out.println("*******消息发送完毕");
        producer.close();
        session.close();
        connection.close();
    }
}
