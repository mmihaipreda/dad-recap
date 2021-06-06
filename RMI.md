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
