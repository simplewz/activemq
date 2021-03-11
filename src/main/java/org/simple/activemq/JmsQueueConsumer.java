package org.simple.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class JmsQueueConsumer {

    private static final String ACTIVEMQ_URL="tcp://49.235.2.116:61616";
    private static final String QUEUE_NAME="queue01";

    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("2号消费者");
        //1. 创建连接工厂，按照给定的URL地址，采用默认的用户名和密码
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2. 通过连接工程获得连接connection并启动访问
        Connection connection = factory.createConnection();
        connection.start();
        //3. 根据获取到的连接获取会话session,有两个参数，第一个表示是否开启事物，第二个表示签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4. 创建目的地,具体是队列(queue)/主题(topic)
        Queue queue = session.createQueue(QUEUE_NAME);
        //5. 创建消费者
        MessageConsumer consumer = session.createConsumer(queue);
        //6. 消费者接受并消费消息
        /*
         * 6.1 同步阻塞方式接收消息receive();
         * 订阅者或接收者调用MessageConsumer的receive()方法时，receive方法在能接受到消息之前或(超时之前)将一直阻塞。
         *
         */
        /*
        while(true){
            TextMessage message = (TextMessage) consumer.receive(4000L);
            if(message!=null){
                System.out.println("***消费者接受到消息："+message.getText());
            }else{
                break;
            }
        }
        */

        /*
         * 6.2 通过监听的方式来消费消息，如果消息队列有消息就消费消息,没有消息就不做处理
         *
         * 异步非阻塞方式
         * 订阅者或者接收者通过MessageConsumer的setMessageListener(MessageListener listener)注册一个监听器
         * 当消息到达之后，系统自动调用监听器MessageListener的onMessage(Message message)方法
         *
         * 需要费消费者一定的时间进行消息的消费
         */
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if(null != message && message instanceof TextMessage){
                    TextMessage textMessage= (TextMessage) message;
                    try {
                        System.out.println("***消费者接受到消息："+textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //保持进程，等待消息被消费
        System.in.read();
        /*
         * 1. 生产者先生产消息，只启动1号消费者。
         *    1号消费者肯定能消费到消息。
         * 2. 生产者先生产消息，先启动1号消费者，再启动2号消费者。
         *    1号消费者可以消费消息，2号消费者不能消费消息。
         * 3. 先启动1号消费者,再启动2号消费者，然后再生产消息。消息的消费情况是怎么样的。
         *    1号、2号消费者按照消费者的启动顺序消费消息队列中消息
         */

        //7. 关闭资源
        consumer.close();
        session.close();
        connection.close();
    }
}
