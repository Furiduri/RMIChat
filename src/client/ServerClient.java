/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import interfaces.IChatClient;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.ChatServer;

/**
 *
 * @author Jorge Osvaldo perez Mendoza
 */
public class ServerClient implements IChatClient{

    public ServerClient(int PORT, String Name) throws UnknownHostException, RemoteException, AlreadyBoundException {
        Remote remote = UnicastRemoteObject.exportObject(this, PORT);
        InetAddress address = InetAddress.getLocalHost();
        Registry registry = LocateRegistry.createRegistry(PORT);
        System.out.println("Servidor escuchando en el puerto " + String.valueOf(PORT));
        System.out.println("IP: "+ address.getHostAddress());
        registry.bind(Name, remote); // Registrar ChatServer 
    }    
    
    @Override
    public boolean Recive(String msg) throws RemoteException {
        try {
            String ip = RemoteServer.getClientHost();     
            System.out.println("User:"+ip+" Msg: "+msg);
        } catch (ServerNotActiveException ex) {
            Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
}
