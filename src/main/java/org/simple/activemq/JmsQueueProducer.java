package org.simple.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsQueueProducer {

    private static final String ACTIVEMQ_URL="tcp://49.235.2.116:61616";
    private static final String QUEUE_NAME="queue01";

    public static void main(String[] args) throws JMSException {
        //1. 创建连接工厂，按照给定的URL地址，采用默认的用户名和密码
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2. 通过连接工程获得连接connection并启动访问
        Connection connection = factory.createConnection();
        connection.start();
        //3. 根据获取到的连接获取会话session,有两个参数，第一个表示是否开启事物，第二个表示签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4. 创建目的地,具体是队列(queue)/主题(topic)
        Queue queue = session.createQueue(QUEUE_NAME);
        //5. 创建消息的生产者
        MessageProducer producer = session.createProducer(queue);
        //6. 通过使用消息生产者生产3条消息发送到MQ消息队列
        for(int i=1;i<=6;i++){
            //7. 通过session创建消息
            TextMessage textMessage = session.createTextMessage("messageListener--->"+i);
            //8. 通过producer将消息发送给消息中间件
            producer.send(textMessage);
        }
        //9. 关闭资源
        producer.close();
        session.close();
        connection.close();
        System.out.println("*****消息发送到MQ完成*********");
    }
}
