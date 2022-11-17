/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author Jorge Osvaldo perez Mendoza
 */
public class Client {
    public  String UserName;
    public String IP;
    public int PORT;

    public Client(String UserName, String IP, int PORT){
        this.UserName = UserName;
        this.IP = IP;
        this.PORT = PORT;
    } 

    public boolean SendMsg(String user,String msg) throws RemoteException, NotBoundException {                
        IChatClient clientS = StartClient();
        return clientS.Recive(user,msg);
    }
    
    
    public boolean SendMsgPrivade(String user,String msg) throws RemoteException, NotBoundException {                
        IChatClient clientS = StartClient();
        return clientS.RecivePrivate(user,msg);
    }
    
    private IChatClient StartClient() throws RemoteException, NotBoundException {        
            Registry registry = LocateRegistry.getRegistry(this.IP, this.PORT);
            return  (IChatClient) registry.lookup(this.UserName); 
    }
    
    public boolean TestConect() throws RemoteException, NotBoundException{        
       IChatClient clientS = StartClient();
        return clientS.Test();
    }
}
