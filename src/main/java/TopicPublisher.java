import org.apache.log4j.BasicConfigurator;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

public class TopicPublisher {
    public static void main(String[] args) throws Exception {
        //thiết lập môi trường cho JMS logging 
        BasicConfigurator.configure();
        //thiết lập môi trường cho JJNDI  	 
        Properties settings = new Properties();
        settings.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        settings.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
        //tạo context 
        Context ctx = new InitialContext(settings);
        //lookup JMS connection factory 
        Object obj = ctx.lookup("TopicConnectionFactory");
        ConnectionFactory factory = (ConnectionFactory) obj;
        //tạo connection 
        Connection con = factory.createConnection("admin", "admin");
        //nối đến MOM  	 	
        con.start();
        //tạo session 
        Session session = con.createSession(
                /*transaction*/false,
                /*ACK*/Session.AUTO_ACKNOWLEDGE
        );
        Destination destination = (Destination) ctx.lookup("dynamicTopics/thanthidet");
        MessageProducer producer = session.createProducer(destination);
        //Tạo 1 message 
        Message msg = session.createTextMessage("xin chào người dẹp");  //gửi 
        producer.send(msg);
//        shutdown connection 
        session.close();
        con.close();
        
        System.out.println("Finished...");
        
    }
}
