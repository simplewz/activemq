package org.simple.activemq.spring;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.TextMessage;

/**
 * Spring整合ActiveMQ的生产者
 */
@Service
public class JmsSpringProducer {

    @Autowired
    JmsTemplate jmsTemplate;

    public static void main(String[] args) {
        //获取Spring容器
        ApplicationContext context=new ClassPathXmlApplicationContext("applicationContext.xml");
        //本类添加了@Service注解，所以在Spring的容器中有对应单个实例对象，名称为类的全名的首字母小写
        JmsSpringProducer producer= (JmsSpringProducer) context.getBean("jmsSpringProducer");
        //利用jmsTemplate发送消息
        producer.jmsTemplate.send((session) -> {
            TextMessage textMessage = session.createTextMessage("****Spring整合ActiveMQ的Case....");
            return textMessage;
        });
        System.out.println("Spring整合ActiveMQ完成,消息发送成功");
    }
}
