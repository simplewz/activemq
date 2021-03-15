package org.simple.activemq;

import org.apache.activemq.broker.BrokerService;

/**
 * 内嵌式的broker
 */
public class EmbedBroker {
    public static void main(String[] args) throws Exception {
        BrokerService brokerService=new BrokerService();
        brokerService.setUseJmx(true);
        brokerService.addConnector("tcp://localhost:61616");
        brokerService.start();
    }
}
