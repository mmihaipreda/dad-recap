# dad-recap
Revision of Distributed Applications Development
This readme contains all the steps for developing practic example of 
- JMS 
- JAVA REST
- Node REST
- RMI
- OpenMPI
- OpenMP


## JMS steps

### More information:
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



## JavaREST steps

### More information:
```
https://github.com/critoma/dad/tree/master/lectures/c08/rest/s08_jweb_jaxrs_server
```

1. Create a folder in src named `bean` ( `src/eu/ase/java/bean` )
2. Create model class/classes
- - 2.1.`Product.java`
- - 2.2.`Status.java`
 3. Create a folder in src name `rest` (  `src/eu/ase/java/rest`  )
 4. Create REST Server
- - 4.1.`MyRESTServices.java`
 - - 4.2.`ProductCatalogResource.java`

###1. Create a folder in src named `bean`

###2. Create model class/classes

#### 2.1.`Product.java`

```
package eu.ase.java.bean;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class Product implements Serializable {
    private static final long serialVersionUID = 6826191735682596960L;
    private int id;
    private String name;
    private String category;
    private double unitPrice;

    public Product() {} // needed for JAXB
    public Product(int id, String name, String category, double unitPrice) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public double getUnitPrice() {
        return unitPrice;
    }
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}

```

#### 2.2.`Status.java`

```
package eu.ase.java.bean;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class Status implements Serializable {
    private static final long serialVersionUID = -9130603850117689481L;
    private String status;
    private String message;

    public Status() {} // needed for JAXB

    public Status(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

```

### 3. Create a folder in src name `rest`

### 4. Create REST Server

#### 4.1.`MyRESTServices.java`

```
package eu.ase.java.rest;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

// https://www.pegaxchange.com/2016/08/11/jax-rs-java-rest-service-eclipse-tomcat/
// http://127.0.0.1:8080/s08_jweb_jaxrs_server/restservices/productcatalog/search/category/electronics
// http://127.0.0.1:8080/s08_jweb_jaxrs_server/restservices/productcatalog/search/category/hardware
// http://127.0.0.1:8080/s08_jweb_jaxrs_server/restservices/productcatalog/search/category/books

// http://127.0.0.1:8080/s08_jweb_jaxrs_server/restservices/productcatalog/search?name=Hammer

// insert into Postman in Chrome using POST instead of GET:
// http://127.0.0.1:8080/s08_jweb_jaxrs_server/restservices/productcatalog/insert
/*
Content-Type: application/json

{
 "id":11,
 "name":"Drill",
 "category":"Hardware",
 "unitPrice":294.39
}
 */


@ApplicationPath("restservices")
public class MyRESTServices extends ResourceConfig {
    public MyRESTServices() {
        packages("com.fasterxml.jackson.jaxrs.json");
        packages("eu.ase.java.rest");
    }
}

```

#### 4.2.`ProductCatalogResource.java`

```
package eu.ase.java.rest;

import java.util.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import eu.ase.java.bean.Product;
import eu.ase.java.bean.Status;

@Path("productcatalog")
public class ProductCatalogResource {
    private static List<Product> productCatalog = null;
    public ProductCatalogResource() {
        initializeProductCatalog();
    }
    @GET
    @Path("search/category/{category}")
    @Produces(MediaType.APPLICATION_JSON)
    public Product[] searchByCategory(@PathParam("category") String category) {
        List<Product> products = new ArrayList<Product>();
        for (Product p : productCatalog) {
            if (category != null && category.equalsIgnoreCase(p.getCategory())) {
                products.add(p);
            }
        }
        return products.toArray(new Product[products.size()]);
        //return (Product[])products.toArray();
    }

    @GET
    @Path("search")
    @Produces(MediaType.APPLICATION_JSON)
    public Product[] searchByName(@QueryParam("name") String name) {
        List<Product> products = new ArrayList<Product>();
        for (Product p : productCatalog) {
        //for (Product p : (Product[])productCatalog.toArray()) {
            if (name != null && name.toLowerCase().startsWith(p.getName().toLowerCase())) {
                products.add(p);
            }
        }
        return products.toArray(new Product[products.size()]);
        //return (Product[])products.toArray();
    }

    @POST
    @Path("insert")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Status insert(Product product) {
        productCatalog.add(product);
        return new Status("SUCCESS", "Inserted " + product.getName());
    }
    private void initializeProductCatalog() {
        if (productCatalog == null) {
            productCatalog = new ArrayList<Product>();
            productCatalog.add(new Product(1, "Keyboard", "Electronics", 29.99D));
            productCatalog.add(new Product(2, "Mouse", "Electronics", 9.95D));
            productCatalog.add(new Product(3, "17\" Monitor", "Electronics", 159.49D));
            productCatalog.add(new Product(4, "Hammer", "Hardware", 9.95D));
            productCatalog.add(new Product(5, "Screwdriver", "Hardware", 7.95D));
            productCatalog.add(new Product(6, "English Dictionary", "Books", 11.39D));
            productCatalog.add(new Product(7, "A House in Bali", "Books", 15.99D));
            productCatalog.add(new Product(8, "An Alaskan Odyssey", "Books", 799.99D));
            productCatalog.add(new Product(9, "LCD Projector", "Electronics", 1199.19D));
            productCatalog.add(new Product(10, "Smart Thermostat", "Electronics", 1199.19D));
        }
    }
}


```

##  NodeREST steps

### More information:
```
https://github.com/critoma/dad/tree/master/lectures/c09/nodejs

```
### 1. Create the file

```
const express = require('express');
const fs = require('fs');
const app = express();
app.use(express.json());

app.get('/', (req, res) => {
	const data = JSON.parse(fs.readFileSync('./data.json'));
	res.status(200).send(data);
});

app.get('/:id', (req, res) => {
	const id = req.params.id;
	const userData = JSON.parse(fs.readFileSync('./data.json')).find((e) => e.id == id);
	res.status(200).send(userData);
});
app.listen(8080, () => {
	console.log('Server is listening on 127.0.0.1:8080....');
});

/**
 * 32. please write down using nodejs and express module
 * for handling a http post request in order to have an addition of two arrays from the json payload
 */
/**
 *
 * {
    "array1":[1,2,3,4,5,6,7,8],
    "array2":[1,2,3,4,5,6,9,8]
}
 */

app.post('/sum', (req, res) => {
	const payload = req.body;
	const minLength = Math.min(payload.array1.length, payload.array2.length);
	const result = new Array(minLength).fill(0);
	for (let i = 0; i < minLength; i++) {
		result[i] = payload.array1[i] + payload.array2[i];
	}
	res.status(201).send(result);
});

```

### `Post payload`

```
{
    "array1":[1,2,3,4,5,6,7,8],
    "array2":[1,2,3,4,5,6,9,8]
}
```

### `data.json`

```
[
	{
		"userId": 1,
		"id": 1,
		"title": "delectus aut autem",
		"completed": false
	},
	{
		"userId": 1,
		"id": 2,
		"title": "quis ut nam facilis et officia qui",
		"completed": false
	},
	{
		"userId": 1,
		"id": 3,
		"title": "fugiat veniam minus",
		"completed": false
	},
	{
		"userId": 1,
		"id": 4,
		"title": "et porro tempora",
		"completed": true
	},
	{
		"userId": 1,
		"id": 5,
		"title": "laboriosam mollitia et enim quasi adipisci quia provident illum",
		"completed": false
	},
	{
		"userId": 1,
		"id": 6,
		"title": "qui ullam ratione quibusdam voluptatem quia omnis",
		"completed": false
	},
	{
		"userId": 1,
		"id": 7,
		"title": "illo expedita consequatur quia in",
		"completed": false
	},
	{
		"userId": 1,
		"id": 8,
		"title": "quo adipisci enim quam ut ab",
		"completed": true
	},
	{
		"userId": 1,
		"id": 9,
		"title": "molestiae perspiciatis ipsa",
		"completed": false
	},
	{
		"userId": 1,
		"id": 10,
		"title": "illo est ratione doloremque quia maiores aut",
		"completed": true
	},
	{
		"userId": 1,
		"id": 11,
		"title": "vero rerum temporibus dolor",
		"completed": true
	}
]

```
## RMI Steps

### More information:
```
https://github.com/critoma/dad/tree/master/lectures/c06/src/S12_RMI

```

1. Define ServerInterface ( extend Remote)
2. Implement ServerInterface
3. Write policy
4. Define Main Prog Server
5. Define Main Prog Client and call the method

### 1. Define ServerInterface ( extend Remote)

```
package eu.ase.rmi;

import java.rmi.*;

public interface SampleServerInterface extends Remote
{
  public int sum(int a, int b) throws RemoteException;
}
```

### 2. Implement ServerInterface

```
package eu.ase.rmi;

import java.rmi.*;
import java.rmi.server.*;

public class SampleServerImpl extends UnicastRemoteObject
   implements SampleServerInterface
{
  SampleServerImpl() throws RemoteException
  {
     super();
  }

  public int sum(int a,int b) throws RemoteException
  {
     return a + b;
  }
}

```

### 3. Write policy

```
// this policy file should only be used for testing and not deployed
grant {
    permission java.security.AllPermission;
};

```

### 4. Define Main Prog Server

```
package eu.ase.rmi;

import java.rmi.*;
import java.rmi.registry.*;

public class SampleServerProgMain
{
  public static void main(String args[])
  {
    //set the security manager
    try {
        System.setSecurityManager(new RMISecurityManager());

        //create a local instance of the object
        SampleServerImpl Server = new SampleServerImpl();

        //put the local instance in the registry
        //Naming.rebind("SAMPLE-SERVER" , Server);
	Naming.rebind("rmi://127.0.0.1:1099/SAMPLE-SERVER", Server);

        System.out.println("Server waiting.....");
    } catch (java.net.MalformedURLException me) {
         System.out.println("Malformed URL: " + me.toString());
    } catch (RemoteException re) {
         System.out.println("Remote exception: " + re.toString());
    }

  } //end method

} //end class

```

### 5. Define Main Prog Client and call the method

```
package eu.ase.rmi;

import java.rmi.*;
import java.rmi.server.*;

public class SampleClient
{
   public static void main(String[]  args)
   {
      // set the security manager for the client
      System.setSecurityManager(new RMISecurityManager());
      //get the remote object from the registry
      try {
          System.out.println("Security Manager loaded");
          String url = "rmi://127.0.0.1:1099/SAMPLE-SERVER";
          SampleServerInterface remoteObject = (SampleServerInterface)Naming.lookup(url);
          System.out.println("Got remote object");
          //narrow the object down to a specific one
          //System.out.println("Location: " + System.getProperty("LOCATION"));
          // make the invocation
	  int result = remoteObject.sum(1,2);
          System.out.println(" 1 + 2 = " +  result);
      } catch (RemoteException exc) {
          System.out.println("Error in lookup: " + exc.toString());
      } catch (java.net.MalformedURLException exc) {
          System.out.println("Malformed URL: " + exc.toString());
      } catch (java.rmi.NotBoundException exc) {
          System.out.println("NotBound: " + exc.toString());
      }

   } //end method
} //end server

```
## Open MP steps
### More information:
```
https://github.com/critoma/dad/tree/master/lectures/c08/HPC_OpenMP

```
##### 1.Please write C code using OpenMP for addition in paralel of the items from 1 array.
##### 2.Please write C code using OpenMP for finding the minimum of items from 1 array.
##### 3. OpenMP code for calculating in parallel of average o an array

### 1.Please write C code using OpenMP for addition in paralel of the items from 1 array.

```
#include<stdio.h>
#include <stdlib.h>
#include<omp.h>
#include<limits>
using namespace std;
#define N 1000
int main()
{
	int ar[N];
	int sumParallel=0;
	int sumSq = 0;
	srand(1);
	for (int i = 0; i < N; i++) {
		ar[i] = rand();
		sumSq += ar[i];
	}
#pragma omp parallel for reduction(+:sumParallel)
	for (int i = 0; i < N; i++) {

		sumParallel += ar[i];
	}
	printf("Sum S= %d\n", sumSq);
	printf("Sum P= %d\n", sumParallel);

	return 0;
}
```

### 2.Please write C code using OpenMP for finding the minimum of items from 1 array.

```
#include<stdio.h>
#include <stdlib.h>
#include<omp.h>
#include<limits>
using namespace std;
#define N 1000
int main()
{
	int ar[N];
	srand(1);
	int minS=INT_MAX;
	int minP= INT_MAX;
	int noThreads = 4;
	for (int i = 0; i < N; i++) {
		ar[i] = rand();
		minS = minS > ar[i] ? ar[i] : minS;
	}
	int partialMin[4][100];
	for (int i = 0; i < noThreads; i++)
	{
		partialMin[i][0] = INT_MAX;
	}
#pragma omp parallel num_threads(noThreads)
	{
		int id = omp_get_thread_num();
		int chunks = N / noThreads;
		int lowLim = id * chunks;
		int upLim = id == (noThreads - 1) ? N : (id + 1) * chunks;
		for (int i = lowLim; i < upLim; i++) {
			partialMin[id][0] = partialMin[id][0]> ar[i]? ar[i]: partialMin[id][0];
		}
	}
	for (int i = 0; i < noThreads; i++)
	{
		minP = minP > partialMin[i][0] ? partialMin[i][0] : minP;
	}
	printf("Min S= %d\n", minS);
	printf("Min P= %d\n", minP);
	return 0;
}

```

### 3. OpenMP code for calculating in parallel of average o an array

```
float arrayAverage(int* a, int n){
    float avg=0;
    int localSum=0;
#pragma omp parallel for scheduled(static) reduction(+:localSum)
    localSum=0;
    for(int i=0;i<n;i++){
        localSum+=a[i];
    }
    avg=localSum;
    return avg/n;
}
```

## Open MPI steps

### More information:
```
https://github.com/critoma/dad/tree/master/lectures/c08/openmpi

```
#### The sum of two arrays in MPI

```
#include<stdio.h>
#include<mpi.h>
#define SIZE 2
int main(){
    MPI_Init(NULL,NULL);
    int world_size;
    int rank;
    int finalSum = 0;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &world_size);

    int array[SIZE][3*SIZE] ={
        {1,2,3,4,5,6},
        {1,2,3,4,5,6},
    };
    int received[3*SIZE];
    int partialSums[SIZE] = {0,0};
    int nodeSum = 0;
    if(world_size == SIZE){
        MPI_Scatter(array, 3*SIZE, MPI_INT, received, 3*SIZE,MPI_INT, 0, MPI_COMM_WORLD);

        for(int i=0;i<3*SIZE;i++){
            nodeSum += received[i];
        }
        MPI_Gather(&nodeSum,1,MPI_INT, partialSums,1,MPI_INT,0,MPI_COMM_WORLD);

        if(rank == 0){
            for(int i=0;i<SIZE;i++){
                finalSum += partialSums[i];
            }
            printf("%d",finalSum);
        }
    }

    MPI_Finalize();
    return 0;
}

```
