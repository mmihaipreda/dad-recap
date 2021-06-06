## Pasi JMS

```
https://github.com/critoma/dad/tree/master/lectures/c10/src/apachetomeejms

```

1. Define `MessageProducer`
2. Define `MessageReceiver`
3. Define `JMS`

### 1. Define `MessageProducer`

```
package apachetomeejms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.broker.BrokerService;

public class MessageProducerClient {

 public static Properties getProp() {
	 Properties props = new Properties();
	 props.setProperty(Context.INITIAL_CONTEXT_FACTORY,
	 "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
	 props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61617");
	 return props;
 }

 public static void initBroker() throws Exception {
	 BrokerService broker = new BrokerService();
	 // configure the broker
	 broker.addConnector("tcp://localhost:61617");
	 broker.start();
 }

 public static void main(String args[]) {

	 Connection connection = null;
	 try {
		 initBroker();
		 InitialContext jndiContext = new InitialContext(getProp());
		 ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext
		 .lookup("ConnectionFactory");
		 connection = connectionFactory.createConnection();
		 connection.setClientID("durable");
		 Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		 Destination destination = session.createTopic("jms/topic/test");
		 MessageProducer producer = session.createProducer(destination);
		 TextMessage msg = session.createTextMessage();
		 msg.setText("Hello, This is JMS example !!");
		 BufferedReader reader = new BufferedReader(new InputStreamReader(
		 System.in));

	 while (true) {
		 System.out
		 .println("Enter Message to Topic or Press 'Q' for Close this Session");
		 String input = reader.readLine();
		 if ("Q".equalsIgnoreCase(input.trim())) {
		 	break;
		 }
		 msg.setText(input);
		 producer.send(msg);
	 }
	 } catch (JMSException e) {
	 	e.printStackTrace();
	 } catch (IOException e) {
	 	e.printStackTrace();
	 } catch (NamingException e) {
	 	e.printStackTrace();
	 } catch (Exception e) {
	 	e.printStackTrace();
	 } finally {
		 try {
		 	if (connection != null) {
		 		connection.close();
		 	}
		 } catch (JMSException e) {
		 	e.printStackTrace();
		 }
	 }

 }
}
```

### 2. Define `MessageReceiver`

```
package apachetomeejms;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

//import javax.jms.Connection;
//import javax.jms.ConnectionFactory;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;

import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.util.Properties;

import org.apache.activemq.ActiveMQConnectionFactory;

import java.io.IOException;
import java.io.InputStreamReader;

public class MessageReceiverClient {
 protected static final String url = "tcp://localhost:61617";
 public static void main(String[] args) {
	String topicName = null;
	Context jndiContext = null;
	TopicConnectionFactory topicConnectionFactory = null;
	TopicConnection topicConnection = null;
	TopicSession topicSession = null;
	Topic topic = null;
	TopicSubscriber topicSubscriber = null;
	TextListener topicListener = null;
	//TextMessage message = null;
	InputStreamReader inputStreamReader = null;
	char answer = '\0';
	/*
		if(args.length != 1) {
			System.out.println("Usage: java SimpleTopicSubscriber <topic-name>");
			System.exit(1);
		}
	*/
	topicName = new String(args[1]);
        //topicName = new String("jms/topic/test");
	System.out.println("Topic name = "+topicName);

	//JNDI context look-up + look-up factory & topic
	try {
                Properties props = new Properties();
	 	 props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
	 	 //props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61617");
		props.setProperty(Context.PROVIDER_URL, args[0]);

		jndiContext = new InitialContext(props);
		//jndiContext = new InitialContext();

		topicConnectionFactory = (TopicConnectionFactory)jndiContext.lookup("ConnectionFactory");
		//topic = (Topic)jndiContext.lookup(topicName);

	} catch(NamingException ne) {
		ne.printStackTrace();
		System.exit(2);
	}

	try {
		topicConnection = topicConnectionFactory.createTopicConnection();
		topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		topic = topicSession.createTopic("jms/topic/test");
		topicSubscriber = topicSession.createSubscriber(topic);

		topicListener = new TextListener();
		topicSubscriber.setMessageListener(topicListener);
		topicConnection.start();

			System.out.println("To end program, insert q + CR/LF");
			inputStreamReader = new InputStreamReader(System.in);
			while(!(answer == 'q')) {
				try {
					answer = (char) inputStreamReader.read();
				} catch(IOException ioe) {
					ioe.printStackTrace();
				}
			}
		} catch(JMSException jmse) {
			jmse.printStackTrace();
		} finally {
			if(topicConnection != null) {
				try {
					topicConnection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
	}
}


class TextListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
			TextMessage msg = null;

			try {
				if(message instanceof TextMessage) {
					msg = (TextMessage)message;
					System.out.println("Received message = "+msg.getText());
				} else {
					System.out.println("Binary messsage!");
				}
			} catch(JMSException jmse) {
				jmse.printStackTrace();
			} catch(Throwable t) {
				t.printStackTrace();
			}
	}

}

```

### 3. Define `JMS`

```
package apachetomeejms;

import javax.annotation.Resource;

import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import javax.jms.*;
/*
import javax.jms.Topic;
//import javax.jms.Queue;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
*/
import java.io.IOException;

@WebServlet("/MyJmsServlet")
public class MyJmsServlet extends HttpServlet {

    @Resource(name = "jms/topic/test")
    private Topic testTopic;

    //@Resource(name = "bar")
    //private Queue barQueue;

    @Resource
    private ConnectionFactory connectionFactory;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //...
	try {
        Connection connection = connectionFactory.createConnection();
        connection.start();

        // Create a Session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create a MessageProducer from the Session to the Topic or Queue
        MessageProducer producer = session.createProducer(testTopic);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        // Create a message
        TextMessage message = session.createTextMessage("Hello World!");

        // Tell the producer to send the message
        producer.send(message);

        //...
	} catch(JMSException jmse) {
		jmse.printStackTrace();
	}
    }

}

```
