# web-recap

Revision of Distributed Applications Development
This readme contains all the steps for developing practic example of

-   JAVA REST
-   Node REST
-   JSP (HTML + Java Servlet)
-   SERVLET ( only Java )
-   JSP + SERVLET
-   ASP Theory
-   WebSockets

## JavaREST steps

### More information:

```
https://github.com/critoma/dad/tree/master/lectures/c08/rest/s08_jweb_jaxrs_server
```

1. Create a folder in src named `bean` ( `src/eu/ase/java/bean` )
2. Create model class/classes

-   -   2.1.`Product.java`
-   -   2.2.`Status.java`

3.  Create a folder in src name `rest` ( `src/eu/ase/java/rest` )
4.  Create REST Server

-   -   4.1.`MyRESTServices.java`
-   -   4.2.`ProductCatalogResource.java`

### 1. Create a folder in src named `bean`

### 2. Create model class/classes

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

---

## NodeREST steps

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

### Another example of REST server and client

### Description

```
   1. REST Client (in a web page) which send REST requests (POST and DELETE) for an additional JSON for the user
   2. Enhance the restServer.js from the lecture and lab in order to:
   2.1) encrypt/decrypt json payload
   2.2) insert in users.json file and respectively delete the user from the users.json
   2.3) optional replace users.json file with a connection to MongoDB instance

```

1. In `src`, create a folder `public`
1.  1. Create the following files : `client.js`, `client.css`, `client.html`
1. In `src`, create the following files: `server.js`, `users.json`, `usersBackup.json`

#### `client.html`

```
<!DOCTYPE html>
<html>
	<head>
		<title>Homework - Exercise 2 and 3</title>
		<script
			src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/4.0.0/crypto-js.min.js"
			integrity="sha512-nOQuvD9nKirvxDdvQ9OMqe2dgapbPB7vYAMrzJihw5m+aNcf0dX53m6YxM4LgA9u8e9eg9QX+/+mPu8kCNpV2A=="
			crossorigin="anonymous"
			referrerpolicy="no-referrer"
		></script>
		<script
			src="https://cdnjs.cloudflare.com/ajax/libs/axios/0.21.1/axios.min.js"
			integrity="sha512-bZS47S7sPOxkjU/4Bt0zrhEtWx0y0CRkhEp8IckzK+ltifIIE9EMIMTuT/mEzoIMewUINruDBIR/jJnbguonqQ=="
			crossorigin="anonymous"
			referrerpolicy="no-referrer"
		></script>
		<link rel="stylesheet" href="./client.css" />
	</head>
	<body>
		<div id="information">
			Readme:
			<ul>
				<li>You need internet connection (i used CDN link for some libraries)</li>
				<li>The address of server is http://localhost:8338</li>
				<li>
					in order to start use <b><i>npm install</i></b> then <b><i>npm start</i></b>
				</li>
				<li>
					In order to add a user (to exemplify the POST request) fill the form and hit <b><i>Add User</i></b>
				</li>
				<li>In order to delete a user (to exemplify the DELETE request) double click on any row</li>
				<li>
					For convenince, click on <b><i>Load backup</i></b> to get a sample of users
				</li>
				<li>
					Both POST and DELETE requests encrypts the payload at <b><i>Client</i></b> and decrypts it at <b><i>Server</i></b> and vice-versa (Exercise 3)
				</li>
				<li>Both POST and DELETE requests also read and rewrite the JSON file (Exercise 3)</li>
				<li>In the right you also have the actions performed mini console, to see each action that you have performed</li>
			</ul>
		</div>
		<div id="content">
			<div class="form-container">
				<div class="field">
					<label for="name">Name</label>
					<input type="text" id="name" />
				</div>
				<div class="field">
					<label for="surname">Surname</label>
					<input type="text" id="surname" />
				</div>
				<div class="field">
					<label for="age">Age</label>
					<input type="number" id="age" />
				</div>
				<button id="addUser">Add User</button>
			</div>
			<div id="table"></div>
		</div>
		<button id="loadBackup">Load backup</button>
		<div id="actionsPerformed"></div>
		<script type="text/javascript" src="./client.js"></script>
	</body>
</html>

```

#### `client.css`

```
.form-container {
	display: grid;
	width: 300px;
	margin: auto;
}
.field {
	display: grid;
	margin-top: 20px;
	grid-template-columns: 28% auto;
}
#addUser {
	margin-top: 20px;
	margin-bottom: 20px;
}
.row-highlight:hover {
	background: rgb(12, 73, 102);
	color: rgb(255, 255, 255);
	cursor: pointer;
}
#content {
	text-align: center;
}
#table {
	margin: auto;
	width: 300px;
}
#information {
	top: 20px;
	padding: 50px;
	font-size: 1.2rem;
	background: aliceblue;
	margin-bottom: 20px;
}
#loadBackup {
	position: absolute;
	top: 56%;
}
#actionsPerformed {
	background: #7b8792;
	height: 250px;
	width: 431px;
	left: 64%;
	top: 44%;
	position: absolute;
	color: white;
	padding: 16px;
	overflow-y: scroll;
}

```

#### `client.js`

```
window.onload = async (event) => {
	const actionsPerformedBtn = document.getElementById('actionsPerformed');
	const addUserBtn = document.getElementById('addUser');
	const loadBackupBtn = document.getElementById('loadBackup');
	const nameBtn = document.getElementById('name');
	const surnameBtn = document.getElementById('surname');
	const ageBtn = document.getElementById('age');
	actionsPerformedBtn.innerHTML += `[${new Date().toLocaleTimeString()}]  table has been successfully generated <br/>`;
	const key = 'secret key 123';
	const encrypt = (data) => {
		return CryptoJS.AES.encrypt(JSON.stringify(data), key).toString();
	};

	const decrypt = (data) => {
		const bytes = CryptoJS.AES.decrypt(data, key);
		return JSON.parse(bytes.toString(CryptoJS.enc.Utf8));
	};

	async function generateTable() {
		const data = await axios.get('http://localhost:8338/listUsers');
		const users = decrypt(data.data);
		const myTableDiv = document.getElementById('table');
		const table = document.createElement('TABLE');
		const headerNames = [`id`, `name`, `surname`, `age`];
		for (let i = 0; i < 4; i++) {
			const th = document.createElement('th');
			th.innerHTML = headerNames[i];
			table.appendChild(th);
		}
		table.border = '1';
		const tableBody = document.createElement('TBODY');
		table.appendChild(tableBody);
		for (let i = 0; i < users.length; i++) {
			const tr = document.createElement('TR');
			tr.classList.add('row-highlight');
			tableBody.appendChild(tr);
			tr.addEventListener('dblclick', async () => {
				const deleted = await axios.delete('http://localhost:8338/deleteUser', { data: { payload: encrypt({ index: i }) } });
				const data = decrypt(deleted.data.payload);

				actionsPerformedBtn.innerHTML += `[${new Date().toLocaleTimeString()}]  ${data.message} <br/>`;
				setTimeout(async () => {
					await refreshTable();
				}, 200);
			});
			for (let j = 0; j < headerNames.length; j++) {
				const td = document.createElement('TD');
				td.width = '75';
				td.appendChild(document.createTextNode(users[i][`${headerNames[j]}`]));
				tr.appendChild(td);
			}
		}
		myTableDiv.appendChild(table);
	}
	generateTable();

	const refreshTable = async () => {
		const parent = document.getElementById('table');
		while (parent.firstChild) {
			parent.firstChild.remove();
		}
		await generateTable();
	};

	const addUser = async () => {
		const user = {
			name: nameBtn.value,
			surname: surnameBtn.value,
			age: ageBtn.value,
			id: '',
		};
		const addedUser = await axios.post('http://localhost:8338/addUser', { payload: encrypt(user) });
		const data = decrypt(addedUser.data.payload);
		actionsPerformedBtn.innerHTML += `[${new Date().toLocaleTimeString()}]  ${data.message} <br/>`;
		setTimeout(async () => {
			await refreshTable();
		}, 200);
	};

	async function loadBackup() {
		const backup = await axios.get('http://localhost:8338/backupUsers');
		const data = decrypt(backup.data.payload);
		actionsPerformedBtn.innerHTML += `[${new Date().toLocaleTimeString()}]  ${data.message} <br/>`;
		setTimeout(async () => {
			await refreshTable();
		}, 200);
	}
	addUserBtn.addEventListener('click', addUser);
	loadBackupBtn.addEventListener('click', loadBackup);
};

```

#### `server.js`

```
const express = require('express');
const app = express();
const fs = require('fs');
const CryptoJS = require('crypto-js');

app.use(express.static('public'));
app.use(express.json());

const key = 'secret key 123';
const encrypt = (data) => {
	return CryptoJS.AES.encrypt(JSON.stringify(data), key).toString();
};

const decrypt = (data) => {
	const bytes = CryptoJS.AES.decrypt(data, key);
	return JSON.parse(bytes.toString(CryptoJS.enc.Utf8));
};

app.get('/', (req, res) => {
	res.status(301).redirect('http://localhost:8338/client.html');
});
app.get('/listUsers', function (req, res) {
	// fs.readFile(`${__dirname}/users.json`, 'utf8', function (err, data) {
	// 	res.end(encrypt(data));
	// });
	let data = JSON.parse(fs.readFileSync(`${__dirname}/users.json`, 'utf8'));

	res.status(200).json(encrypt(data));
});

app.get('/backupUsers', function (req, res) {
	let data = JSON.parse(fs.readFileSync(`${__dirname}/usersBackup.json`, 'utf8'));
	fs.writeFileSync(`${__dirname}/users.json`, JSON.stringify(data, null, 2));
	// let data2 = JSON.parse(fs.readFileSync(`${__dirname}/usersBackup.json`, 'utf8'));
	res.status(200).json({ payload: encrypt({ message: `Backup has been successfully loaded` }) });
});

app.post('/addUser', function (req, res) {
	const body = req.body;
	const newUser = decrypt(body.payload);
	let data = JSON.parse(fs.readFileSync(`${__dirname}/users.json`, 'utf8'));
	newUser.id = data.length + 1;
	data.push(newUser);
	fs.writeFileSync(`${__dirname}/users.json`, JSON.stringify(data, null, 2));
	res.status(201).json({ payload: encrypt({ message: `A user has been added successfully` }) });
});

app.delete('/deleteUser', function (req, res) {
	const body = req.body;
	const index = decrypt(body.payload);
	let data = JSON.parse(fs.readFileSync(`${__dirname}/users.json`, 'utf8'));
	data.splice(index, 1);
	fs.writeFileSync(`${__dirname}/users.json`, JSON.stringify(data, null, 2));
	res.status(200).json({ payload: encrypt({ message: `A user has been deleted successfully` }) });
});

app.listen(8338, () => {
	console.log(`Server is listening on port 8338...`);
});

```

#### `users.json`

```
[
	{
		"name": "Gabriel",
		"surname": "Luis",
		"age": 24,
		"id": 1
	},
	{
		"name": "Stoian",
		"surname": "Florin",
		"age": 41,
		"id": 2
	},
	{
		"name": "Velcu",
		"surname": "Andrei",
		"age": 31,
		"id": 3
	},
	{
		"name": "Popescu",
		"surname": "Dan",
		"age": "27",
		"id": 4
	}
]

```

#### `usersBackup.json`

```
[
	{
		"name": "Gabriel",
		"surname": "Luis",
		"age": 24,
		"id": 1
	},
	{
		"name": "Stoian",
		"surname": "Florin",
		"age": 41,
		"id": 2
	},
	{
		"name": "Velcu",
		"surname": "Andrei",
		"age": 31,
		"id": 3
	},
	{
		"name": "Popescu",
		"surname": "Dan",
		"age": "27",
		"id": 4
	}
]
```

---

## JSP (HTML + Java Servlet)

1. In Eclipse `Create new Dynamic Project`
2. Create in `src/main/java` a package `exercises`
3. Create in `src/webapp` `index.html`
4. Create a `Utils` class to generate HTML responses more easily
5. Add different exercises (e.g. `Exercise1.java` )

### Exercise 1 - Please give the source code statement for handling a HTTP POST request in a servlet with parameter p1 and extract the value of it and convert to the Integer if the case.

### `Utils.java`

```
package exercises;

import java.io.PrintWriter;

public class Utils {

	public static void generateHTML(PrintWriter out, String divId, String title, String parameter) {

		 out.write("<html><body><div id='"+ divId +"'>");
		 out.write("<h2>" + title +"</h2>");
		 out.write("<p>param1: " + parameter + "</p>");
		 out.write("</div></body></html>");


	}
}

```

### `Index.html`

```
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Web and Cloud security - Recap</title>
</head>
<body>


	<h3>Web and Cloud security - Recap</h3>

	<ul>
			<!-- http://localhost:8080/Web_Recap/Exercise1?p1=443 -->
		<li><a href="Exercise1" target="_blank">Exercise 1 (doPost)</a> </li>
	</ul>
</body>
</html>
```

### `Exercise1.java`

```
package exercises;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *

2.Please give the source code statement for handling a HTTP POST request in a servlet
with parameter p1 and extract the value of it and convert to the Integer if the case.
 */
@WebServlet("/Exercise1")
public class Exercise1 extends HttpServlet{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public Exercise1() {
		super();

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		String p1 =req.getParameter("p1");
		if(p1==null || p1.length() == 0) {
			throw new ServletException();
		}else {
			Integer intP1= Integer.parseInt(p1);
			PrintWriter out = resp.getWriter();
			Utils.generateHTML(out, "divID1", "Response Exercise 1", intP1.toString());
			out.close();
		}
	}



}

```

### Exercise 2 - Please write down java ee/spring api the code the instantiate the bean when http request is handled.

### `indexWithBeans.jsp`

```
<%@page import="java.io.PrintWriter"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Web and Cloud security - Recap</title>
</head>
<body  >


	<h3>Web and Cloud security - Recap</h3>

	<ul>
		<li>
			<jsp:useBean id="ex2" class="exercises.Exercise2" />
			<%
		 			ex2.setName("Exercise 2 MAMA LOR de HOTI");
				ex2.printExercise2(out, "divID2", "Response Exercise 2", ex2.getName());
			%>
		</li>

	</ul>
</body>
</html>
```

### `Exercise2.java`

```
package exercises;

import java.io.IOException;


import javax.servlet.jsp.JspWriter;

public class Exercise2 {
	private String name;
	private int age;

	public Exercise2(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}

	public Exercise2() {
		super();
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public void printExercise2(JspWriter out, String divId, String title, String parameter) throws IOException {
		Utils.generateHTML(out, divId, title, parameter);
	}

}

```

### `Utils.java`

```
package exercises;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.jsp.JspWriter;

public class Utils {

	public static void generateHTML(PrintWriter out, String divId, String title, String parameter) {
		 out.write("<html><body><div id='"+ divId +"'>");
		 out.write("<h2>" + title +"</h2>");
		 out.write("<p>param1: " + parameter + "</p>");
		 out.write("</div></body></html>");


	}
	public static void generateHTML(JspWriter out, String divId, String title, String parameter) throws IOException {

		 out.print("<html><body><div id='"+ divId +"'>");
		 out.print("<h2>" + title +"</h2>");
		 out.print("<p>param1: " + parameter + "</p>");
		 out.print("</div></body></html>");


	}
}


```

---

## SERVLET ( only Java )

1. In Eclipse `Create new Dynamic Project`
2. Create in `src/main/java` a package `exercises`
3. Add different exercises (e.g. `Exercise2.java` )

### Exercise 3 - Write the code for jsp bean instantiation and specify the concurrency and security issues

```
<jsp:useBean id= "instanceName" scope= "page | request | session | application"
class= "packageName.className" type= "packageName.className"
beanName="packageName.className | <%= expression >" >
</jsp:useBean>
```

-   When a JSP is requested for the first time or when the web app starts up, the servlet container will compile it into a class extending HttpServlet and use it during the web app's lifetime (source).JSP have a Page Directive Attributes. You can check it in the specification. Specifically, there is a attribute that you can change on the page:

```
<%@ page isThreadSafe="false" %>
```

-   -   If `false` then the JSP container shall dispatch multiple outstanding client requests, one at a time, in the order they were received, to the page implementation for processing.
-   -   If `true` then the JSP container may choose to dispatch multiple outstanding client requests to the page simultaneously.

-   Page authors using `true` must ensure that they properly synchronize access to the shared state of the page. Default is `true`. Note that even if the `isThreadSafe` attribute is `false` the JSP page author must ensure that accesses to any shared objects are properly synchronized. The objects may be shared in either the `ServletContext` or the `HttpSession`.
-   So, if you set the `isThreadSafe` attribute to `false` (thus that the resulting servlet should implement the `SingleThreadModel`) and you make sure that your scriplet do not use objects that shared in either the `ServletContext` or the `HttpSession`, then it may be a good way to resolve the concurrency issues.

### Exercise 4 - Session hijacking - please provide a sample in node.js and other language-platform for this action

1. Suppose you are running this `ShowSession.java` that shows some cookies
2. You create session with nodejs and you highjack the initial session with the cookies

##### 1. Suppose you are running this `ShowSession.java` that shows some cookies

```
package eu.ase.httpservlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SetGetCookie
 */
@WebServlet("/SetGetCookie")
public class SetGetCookie extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SetGetCookie() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static final int SECONDS_PER_YEAR = 60 * 60 * 24 * 365;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	public void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String title = "Set Cookie";
		StringBuffer body = new StringBuffer("<br/>");

		// cookie play
		Cookie[] cookies = request.getCookies();

		// if cookies object is null, it means that no cookie was set before

		if (cookies != null) {

			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if ("CookieGigel".equals(cookie.getName())) {
					body.append("<br/><font color=\"red\"> - ").append(cookie.getName()).append(" : ")
							.append(cookie.getValue()).append("</font>");
				} else if ("CookieIon".equals(cookie.getName())) {
					body.append("<br/><font color=\"green\"> - ").append(cookie.getName()).append(" : ")
							.append(cookie.getValue()).append("</font>");
				} else {
					body.append("<br/><font color=\"blue\"> - ").append(cookie.getName()).append(" : ")
							.append(cookie.getValue()).append("</font>");
				}
			} // end for

		} else {
			// create cookie 1 - implicit value in seconds of cookie is within the session
			Cookie userCookie = new Cookie("CookieGigel", "CucuBau");
			response.addCookie(userCookie);

			// create cookie 2 - is per year
			Cookie userCookie2 = new Cookie("CookieIon", "IONIONION");
			userCookie2.setMaxAge(SECONDS_PER_YEAR);
			response.addCookie(userCookie2);

			body.append("<br/><font color=\"red\">").append(" - Cookie: CookieGigel and CookieIon ")
					.append(" added now</font>");
		}

		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " + "Transitional//EN\">\n" + "<HTML>\n"
				+ "<HEAD><TITLE>" + title + "</TITLE></HEAD>\n");

		out.println("<BODY BGCOLOR=\"#FDF5E6\">\n" + body + "</BODY></HTML>");

	}
}
```

##### 2. You create session with nodejs and you highjack the initial session with the cookies

-   PS replace
-   -   `headers: { Cookie: 'JSESSIONID=3FA3C280D9596DE5995FA4EF66FAF1F4' }` with
-   -   `headers: { Cookie: 'JSESSIONID=<SESSION ID>' }`

```
var http = require('http');
var options = {
	hostname: '127.0.0.1',
	port: '8080',
	path: '/s04_servlet/ShowSession',
	method: 'GET',
	headers: { Cookie: 'JSESSIONID=3FA3C280D9596DE5995FA4EF66FAF1F4' },
};
var results = '';
var req = http.request(options, function (res) {
	res.on('data', function (chunk) {
		results = results + chunk;
		console.log(results);
	});
	res.on('end', function () {
		console.log('end response result');
	});
});

req.on('error', function (e) {
	console.log('error: ' + e);
});

req.end();

```

### Exercise 5 - Write the JSP which will encrypt and decrypt a parameter with AES which is coming within a HTTP request

```
<%@page import="java.util.Base64"%>
<%@page import="javax.crypto.SecretKey"%>
<%@page import="javax.crypto.KeyGenerator"%>
<%@page import="java.security.Key"%>
<%@page import="javax.crypto.Cipher"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<!-- ### Exercise 5 -Write the JSP which will encrypt and decrypt a parameter with AES which is coming within a HTTP request -->
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%
	response.setContentType("text/html");
	String input =request.getParameter("input");
/***************************ENCRYPTION********************************************************/
	String algorithm="AES/ECB/PKCS5Padding";
    // init key
	KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    keyGenerator.init(128);
    SecretKey key = keyGenerator.generateKey();
	//init AES cipher

	Cipher aesCipher =  Cipher.getInstance(algorithm);
	aesCipher.init(Cipher.ENCRYPT_MODE, key);
	byte[] cipherText = aesCipher.doFinal(input.getBytes());

   String encodedEncrypted = Base64.getEncoder().encodeToString(cipherText);
   /******************************************************************************************/

   /***************************DECRYPTION*****************************************************/

    aesCipher = Cipher.getInstance(algorithm);
    aesCipher.init(Cipher.DECRYPT_MODE, key);
    byte[] plainText = aesCipher.doFinal(Base64.getDecoder().decode(encodedEncrypted));
    String decoded = new String(plainText);

   /******************************************************************************************/
	%>
	<div>
	INPUT :<%=input %>
	</div>
	<br/>

	<div>
	ENCRYPTED AND ENCODED :<%=encodedEncrypted %>
	</div>
	<br/>

	<div>
	DECODED :<%=decoded %>
	</div>
	<br/>
</body>
</html>
```

### Exercise 6 -If you are in the JSP page with the following source code:

-   What value will have `accessCount` after 2 connected browsers which are doing 2 HTPP requests each for the JSP page?

```
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<ul>
		<li>
			<b>Declaration (plus expression).</b>
			<br/>
			<%!private int accessCount = 0;%>
                        Accesses to page since server reboot: <%=++accessCount%>
		</li>
	</ul>
</body>
</html>
```

-   <strong>Answer: 4 (Watchout for prefix or postfix)</strong>
-   -   `++accessCount` - first time is 1
-   -   `accessCount++` - first time is 0

## JSP + SERVLET

### Exercise 6 - Please give the source code statement for handling a HTTP POST request in a JSP and instantiate a bean for inserting the parameters values into a relational database

#### `Exercise6.java`

```
package exercises;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Exercise6")
public class Exercise6 extends HttpServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public Exercise6() {
		super();
	}

	public void duplicate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String first_name = req.getParameter("first_name");
		String last_name = req.getParameter("last_name");
		String city_name = req.getParameter("city_name");
		String email = req.getParameter("email");
		System.out.println(first_name + " " + last_name + " " + city_name + " " + email);
		try {
			PrintWriter out = resp.getWriter();
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
			String insertStatement = "insert into users values(?,?,?,?)";
			PreparedStatement pStatement = conn.prepareStatement(insertStatement);
			pStatement.setString(1, first_name);
			pStatement.setString(2, last_name);
			pStatement.setString(3, city_name);
			pStatement.setString(4, email);
			int row = pStatement.executeUpdate();
			resp.setStatus(row, "Data is successfully inserted!");
			out.println("Data is successfully inserted!");
			out.close();

		} catch (Exception e) {
			System.out.print(e);
			e.printStackTrace();
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.duplicate(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.duplicate(req, resp);
	}

}

```

#### `Exercise6.jsp`

```

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
	<jsp:useBean id="Exercise6" class="exercises.Exercise6"></jsp:useBean>
</head>
<body>


	<form method="post" action="Exercise6">
	First name:<br>
	<input type="text" name="first_name">
	<br>
	Last name:<br>
	<input type="text" name="last_name">
	<br>
	City name:<br>
	<input type="text" name="city_name">
	<br>
	Email Id:<br>
	<input type="email" name="email">
	<br><br>
	<input type="submit" value="submit">
	</form>

</body>
</html>
```

#### `Index.html`

```
<%@ page import="java.util.*"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Web and Cloud security - Recap</title>
</head>
<body>


	<h3>Web and Cloud security - Recap</h3>

	<ul>
			<!-- http://localhost:8080/Web_Recap/Exercise1?p1=443 -->
		<li><a href="Exercise1" target="_blank">Exercise 1 (doPost)</a> </li>
		<li><a href="Exercise6" target="_blank">Exercise 6 (doPost)</a> </li>

	</ul>
</body>
</html>
```

### Exercise 8 - Create a JSP that take a Http Post and have as payload a JSON object and display it

#### `Exercise8.java`

```
package exercises;


import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;



@WebServlet("/Exercise8")
public class Exercise8 extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private String name;
	private int age;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Exercise8() {
		super();

	}
	@SuppressWarnings("resource")
	public  String extractPostRequestBody(HttpServletRequest request) throws IOException {
	    if ("POST".equalsIgnoreCase(request.getMethod())) {
			Scanner s = new Scanner(request.getInputStream(), "UTF-8").useDelimiter("\\A");
	        return s.hasNext() ? s.next() : "";
	    }
	    return "";
	}
	public void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, ParseException{

		if ("POST".equalsIgnoreCase(req.getMethod())) {
			String data = this.extractPostRequestBody(req);
			JSONParser parser = new JSONParser(data);
			LinkedHashMap<String, Object> fields =  parser.parseObject();
			String nameJSON =(String)fields.get("name");
			BigInteger ageJSON =(BigInteger)fields.get("age");
			this.setName(nameJSON);
			this.setAge(ageJSON.intValue());
		    PrintWriter out = resp.getWriter();
		    this.printValues(out);
		    out.close();
		}else {
		    PrintWriter out = resp.getWriter();
		    out.write("<div>Access the following link : (Make a POST request)</div>");
		    out.write("<div> http://localhost:8080/Web_Recap/Exercise8 </div>");
		    out.write("<div>payload:</div>");
		    out.write("{\r\n"
		    		+ "    \"name\":\"Boala copiilor\",\r\n"
		    		+ "    \"age\":45\r\n"
		    		+ "}");
			out.close();
		}

	}
	public void printValues(PrintWriter out) {
		out.write("<div> Name :" + this.name + "</div>");
		out.write("<br/>");
		out.write("<div> Age :" + this.age + "</div>");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			this.processRequest(req, resp);
		} catch (ServletException | IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			this.processRequest(req, resp);
		} catch (ServletException | IOException | ParseException e) {
			e.printStackTrace();
		}
	}

}

```

#### `Exercise8.jsp`

```
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>

</head>
<body>
	<jsp:useBean id="object" class="exercises.Exercise8"></jsp:useBean>
</body>
</html>
```

#### `Index.html`

```
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Web and Cloud security - Revision </title>
</head>
<body>
	<h3>Web and Cloud security - Revision</h3>
	<ul>
			<!-- http://localhost:8080/Web_Recap/Exercise1?p1=443 -->
		<li><a href="Exercise1" target="_blank">Exercise 1 (doPost)</a> </li>
		<li><a href="Exercise6" target="_blank">Exercise 6 (doPost)</a> </li>
		<li><a href="Exercise8" target="_blank">Exercise 8 (doPost JSON)</a> </li>
	</ul>
</body>
</html>
```

---

## ASP theory

### 1. Difference between Authentication and Authorization

-   <strong>Authentication</strong> – who you are ?
-   <strong>Authorization</strong> – what you want to do?

In it’s simplest form, adding the `[Authorize]` attribute to a controller or action method will limit access to that controller or action method to users who are authenticated. That is, only users who are logged in will be able to access those controllers or action methods.

### 2. Write an example for ASP.NET Core showing how we can restrict the access to all the actions in a controller only to authenticated users.

<strong>The following code limits access to the `AccountController` to authenticated users:
You can also use the `AllowAnonymous` attribute to allow access by non-authenticated users to individual actions.</strong>

```
[Authorize]
public class AccountController : Controller
{
    [AllowAnonymous]
    public ActionResult Login()
    {
    }

    public ActionResult Logout()
    {
    }
}
```

### 3. What security vulnerabilities do you identify in the following code? How would you solve them?

```
[Authorize]
public class TestController()
{
    [HttpPost][ValidateAntiForgeryToken]
    public async Task<IActionResult> Index()
    {
    var user = await GetCurrentUserAsync();
    ....
    }
    public async Task<AppUser> GetCurrentUserAsync()
    {
    return await _userManager.GetUserAsync(HttpContext.User);
    }
}

```

-   <strong>The `[ValidateAntiForgeryToken]` is added to prevent CSRF attacks ASP.NET Core is adding an element to the form tags.
-   What element, what does it contain and for what purpose?
-   -   Its an hidden input element used to create a token to hide the session cookie</strong>

### 4. Against what types of attacks we use `[ValidateAntiForgeryToken]`?

-   <strong>Cross-Site Request Forgery (CSRF) is an attack that forces an end user to execute unwanted actions on a web application in which they’re currently authenticated</strong>

### 5. CSRF – Cross Site Request Forgery Should actions decorated with `[HttpGet]` be protected with `[ValidateAntiForgeryToken]`?

-   <strong>The Asp.net MVC `AntiForgeryToken` won't work through `HTTP GET`, because it relies on cookies which rely on `HTTP POST` (it uses the `"Double Submit Cookies"` technique described in the OWASP XSRF Prevention Cheat Sheet). You can also additionally protect the cookies sent to the client by setting the as httponly, so they cannot be spoofed via a script.</strong>

### 6. Against what types of attacks we use `[ValidateAntiForgeryToken]`?

-   <strong>Cross-Site Request Forgery (CSRF)</strong>

### 7. Should actions decorated with `[HttpGet]` be protected with `[ValidateAntiForgeryToken]`?

-   <strong>No</strong>
-   <strong> The Asp.net MVC `AntiForgeryToken` won't work through `HTTP GET`, because it relies on cookies which rely on `HTTP POST` (it uses the `"Double Submit Cookies"` technique described in the OWASP XSRF Prevention Cheat Sheet). You can also additionally protect the cookies sent to the client by setting the as httponly, so they cannot be spoofed via a script.</strong>
-   Its only necessary for unsafe `HTTP VERBS` such as <strong>POST</strong>, <strong>PATCH</strong>, <strong>PUT</strong> ,<strong>DELETE</strong>

### 8. Write two different approaches for performing validations on the viewmodel received by an action as a parameter?

-   <strong>Declarative</strong> and <strong>imperative</strong>

### 9. Write the code for a controller having an action than adds a new user with the username `“test@test.com”` and password `“Password1”`.

```
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using static Microsoft.AspNetCore.Identity.UI.V4.Pages.Account.Internal.LoginModel;

namespace Controllers
{
    public class AddController : Controller
    {
        private readonly UserManager<IdentityUser> _userManager;

        [HttpPost]
        [ValidateAntiForgeryToken]
        [Authorize]
        public async Task<IActionResult>  AddUser()
        {
            var user = new IdentityUser { UserName = "test@test.com", Email = "test@test.com" };
            var result = await _userManager.CreateAsync(user, "Password1");
            return View();
        }

    }
}
```

### 10. Please define the route that will call the `“Edit”` action of the `“ProductController”` with a `“productId”` parameter for the following url `“Product/1/Edit”`?

```
endpoints.MapControllerRoute("edit", "Product/{productId}/Edit", new { Controller = "ProductController", action = "Edit" });
```

### 11. Are we able to check in a View if the currently authenticated user has a certain role? Write the code that only displays a div if the user has the `"Management"` role.

```
@if (User.IsInRole("Management"))
{
    <div>Classified information</div>
}
```

### 12. What approaches can be used to send data between an Action and the View?

    <strong>ViewBag / ViewData</strong>

### 13. In the “ConfigureServices” method let's say that you have

    `services.AddTransient<IProductRepository, ProductRepository>();` How many times will the ProductReporitory be instantiated if we ask for it in 5 separate requests made at the same time?

-   <strong>5 times because:</strong>

<strong>Transient objects are always different; a new instance is provided to every controller and every service.
Scoped objects are the same within a request, but different across different requests.
Singleton objects are the same for every object and every request.</strong>

-   ##### Life time 1:

-   -   ![Lifetimes 1](lifetimes1.png)

-   ##### Life time 2:

-   -   ![Lifetimes 1](lifetimes2.png)

### 14. In the MVC pattern, should we send data between an Action and a View?

-   Through <strong>ViewBag / ViewData</strong>

### 15. To prevent CSRF attacks ASP.NET Core is adding an element to the form tags.What element, what does it contain and for what purpose? Write an action that is protected?

-   <strong>MVC's anti-forgery support writes a unique value to an HTTP-only cookie and then the same value is written to the form. When the page is submitted, an error is raised if the cookie value doesn't match the form value. It's important to note that the feature prevents cross site request forgeries. That is, a form from another site that posts to your site in an attempt to submit hidden content using an authenticated user's credentials. The attack involves tricking the logged in user into submitting a form, or by simply programmatically triggering a form when the page loads.The feature doesn't prevent any other type of data forgery or tampering based attacks.</strong>

```
  public class HomeController : Controller
    {

        [HttpPost]
        [ValidateAntiForgeryToken]
        [Authorize]
        public IActionResult Edit( User user)
        {
            return View(user);
        }
    }
```

### 16. Can we check in a View if the currently authenticated user has a certain role?

    Write the code that only displays a `<div>` if the user has the "Management" role.

```
@if( User.IsInRole("Management") )
{
    <div> whatever </div>
}
```

### 17. Create an action that only can be execute by a User that has `"Management"` role

```
public class HomeController : Controller
    {
        [Authorize(Roles = "Management")]
        public IActionResult Edit( User user)
        {
            return View(user);
        }
    }
```

### 18. Example of a Model Class with annotations:

-   `[ValidateNever]`: The ValidateNeverAttribute indicates that a property or parameter should be excluded from validation.
-   `[CreditCard]`: Validates that the property has a credit card format. Requires jQuery Validation Additional Methods.
-   `[Compare]`: Validates that two properties in a model match.
-   `[EmailAddress]`: Validates that the property has an email format.
-   `[Phone]`: Validates that the property has a telephone number format.
-   `[Range]`: Validates that the property value falls within a specified range.
-   `[RegularExpression]`: Validates that the property value matches a specified regular expression.
-   `[Required]`: Validates that the field is not null. See [Required] attribute for details about this attribute's behavior.
-   `[StringLength]`: Validates that a string property value doesn't exceed a specified length limit.
-   `[Url]`: Validates that the property has a URL format.
-   `[Remote]`: Validates input on the client by calling an action method on the server.

```
  public class Series
    {
        [Key]
        public int SeriesId { get; set; }

        [Display(Name = "Title")]
        [Required(ErrorMessage = "Provide data")]
        [MinLength(1, ErrorMessage = "The text is too short")]
        public string Title { get; set; }

        [Display(Name = "Creator/s")]
        [Required(ErrorMessage = "Provide data")]
        [MinLength(1, ErrorMessage = "The text is too short")]
        public string Creators { get; set; }

        [Display(Name = "Actor/s")]
        [Required(ErrorMessage = "Provide data")]
        [MinLength(1, ErrorMessage = "The text is too short")]
        public string Actors { get; set; }

        [Display(Name = "Genre/s")]
        [Required(ErrorMessage = "Provide data")]
        [MinLength(1, ErrorMessage = "The text is too short")]
        public string Genres { get; set; }

        public DateTime? ReleaseDate { get; set; }

        public string TrailerLink { get; set; }

        public int? Seasons { get; set; }

        public int Likes { get; set; }

        public int Dislikes { get; set; }

        [Required(ErrorMessage = "Provide data")]
        public string Synopsis { get; set; }

        // M:N cardinality table
        public ICollection<SeriesWatchlist> Watchlists { get; set; }
    }
```

```
 public class GuestResponse
    {
        [Required(ErrorMessage = "Please enter your name")]
        public string Name { get; set; }
        [Required(ErrorMessage = "Please enter your email address")]
        [RegularExpression(".+\\@.+\\..+", ErrorMessage = "Please enter a valid email address")]
        public string Email { get; set; }
        [Required(ErrorMessage = "Please enter your phone number")]
        public string Phone { get; set; }
        [Required(ErrorMessage = "Please specify whether you'll attend")]
        public bool? WillAttend { get; set; }
    }
```

### 19 - Declare a controller with two actions, each one of them demonstrating a different approach for checking if a user has a certain role.

```
 public class MyController : Controller
    {
        [Authorize(Roles = "Management")]
        public IActionResult action1()
        {
            return action1();
        }
        public IActionResult action2()
        {
            if (User.IsInRole("Management"))
            {
                return action2();
            }
            else
            {
                return RedirectToAction("Login");
            }

        }
    }
```

---

## WebSockets

### 1. HTML page which encrypt the JSON content with JavaScript and send the content via WS - WebSocket to a server-side program: node.js or Java.

#### `client.html`

```
<!DOCTYPE html>
<html>
	<head>
		<title>Homework - Exercise 1</title>
		<script type="text/javascript" src="./node_modules/crypto-js/crypto-js.js"></script>
		<link rel="stylesheet" href="./client.css" />
	</head>
	<body>
		<div id="information">
			Readme:
			<ul>
				<li>You need internet connection (i used CDN link for some libraries)</li>
				<li>The address of server is http://localhost:8337</li>
				<li>
					in order to start use <b><i>npm install</i></b> then <b><i>npm start</i></b>
				</li>
				Flow :
				<li>
					<b><i>1. Client</i></b> sends an encrypted JSON
				</li>
				<li>
					<b><i>2. Server</i></b> decrypts the encrypted JSON and prints it at the console.
				</li>
				<li>
					<b><i>3. Server</i></b> sends an encrypted JSON to <b><i>Client</i></b>
				</li>
				<li>
					<b><i>4. Client</i></b> decrypts the encrypted JSON and appends it to the div
				</li>
			</ul>
		</div>
		<div id="content"></div>
		<script type="text/javascript" src="./client.js"></script>
	</body>
</html>


```

#### `client.js`

```

const url = 'ws://localhost:8337';
const connection = new WebSocket(url);
const key = 'secret key 123';

connection.onopen = () => {
	const content = document.getElementById('content');
	const myJSON = { from: 'Client', with: 'love' };
	const stringifiedJSON = JSON.stringify(myJSON);
	const encrypted = CryptoJS.AES.encrypt(stringifiedJSON, key).toString();
	content.innerHTML += `Sent message => ${encrypted}<br/>`;
	connection.send(encrypted);
	connection.onerror = (error) => {
		console.log(`WebSocket error: ${error}`);
	};

	connection.onmessage = (e) => {
		const content = document.getElementById('content');
		const bytes = CryptoJS.AES.decrypt(e.data, key);
		const originalText = bytes.toString(CryptoJS.enc.Utf8);
		content.innerHTML += `Decrypted message => ${originalText}<br/>`;
	};
};
```

#### `client.css`

```
#information {
	top: 20px;
	padding: 35px;
	font-size: 1.2rem;
	background: aliceblue;
	margin-bottom: 20px;
}

```

#### `server.js`

```
const WebSocket = require('ws');
const CryptoJS = require('crypto-js');
const wss = new WebSocket.Server({ port: 8337 });

const key = 'secret key 123';

wss.on('connection', (ws) => {
	ws.on('message', (message) => {
		const bytes = CryptoJS.AES.decrypt(message, key);
		const originalText = bytes.toString(CryptoJS.enc.Utf8);
		console.log(`Received message => ${message}`);
		console.log(`Decrypted message => ${originalText}`);
		const myJSON = { from: 'Server', with: 'appreciation' };
		const stringifiedJSON = JSON.stringify(myJSON);
		const encrypted = CryptoJS.AES.encrypt(stringifiedJSON, key).toString();
		ws.send(encrypted);
	});
});

```
