package org.simple.activemq.spring;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class JmsSpringTopicConsumer {
    @Autowired
    @Qualifier("jmsTopicTemplate")
    private JmsTemplate jmsTemplate;

    public static void main(String[] args) {
        //获取Spring容器
        ApplicationContext context=new ClassPathXmlApplicationContext("applicationContext.xml");
        //本类添加了@Service注解，所以在Spring的容器中有对应单个实例对象，名称为类的全名的首字母小写
        JmsSpringTopicConsumer consumer= (JmsSpringTopicConsumer) context.getBean("jmsSpringTopicConsumer");
        //利用jmsTemplate接收消息
        String message = (String)consumer.jmsTemplate.receiveAndConvert();
        System.out.println("Spring整合ActiveMQ完成,消息接收成功："+message);
    }
}
