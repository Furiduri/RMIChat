/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import interfaces.IChatServer;
import interfaces.Client;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Json;

/**
 *
 * @author Jorge Osvaldo perez Mendoza
 */
public class ChatServer implements IChatServer {

    public ArrayList<Client> cliets;
    private static final int PORT = 1234;

    public ChatServer() {
        cliets = new ArrayList<Client>();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws RemoteException, AlreadyBoundException, UnknownHostException {
        // TODO code application logic here
        Remote remote = UnicastRemoteObject.exportObject(new ChatServer(), PORT);
        InetAddress address = InetAddress.getLocalHost();
        Registry registry = LocateRegistry.createRegistry(PORT);
        System.out.println("Servidor escuchando en el puerto " + String.valueOf(PORT));
        System.out.println("IP: " + address.getHostAddress());
        registry.bind("ChatServe", remote); // Registrar ChatServer        
    }

    @Override
    public String Connect(String UserName, int Port) throws RemoteException {
        try {
            String ip = RemoteServer.getClientHost();
            Client cl = new Client(UserName, ip, Port);
            Optional<Client> clList = cliets.stream().filter(e -> e.UserName.equals(cl.UserName)).findFirst();
            if (!clList.isPresent()) {
                cliets.add(cl);
                System.out.println("New User Login: " + cl.UserName);
            } else {
                cliets.get(cliets.indexOf(clList.get())).UserName = cl.UserName;
                System.out.println("User Reconected: " + cl.UserName);
            }

        } catch (ServerNotActiveException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        String res = Json.toJSON(cliets);
        return res;
    }

    @Override
    public boolean Disconect() throws RemoteException {
        try {
            String ip = RemoteServer.getClientHost();
            Optional<Client> clList = cliets.stream().filter(e -> e.IP.equals(ip)).findFirst();
            if (clList.isPresent()) {
                System.out.println("LogOut User: " + clList.get().UserName);
                cliets.remove(clList.get());
            }
            return true;
        } catch (ServerNotActiveException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean Send(String msg) throws RemoteException {
        try {
            String ip = RemoteServer.getClientHost();
            Optional<Client> clList = cliets.stream().filter(e -> e.IP.equals(ip)).findFirst();
            Client cl = clList.get();
            for (Client item : cliets) {
                if (item.UserName != cl.UserName) {
                    System.out.println("User:" + cl.UserName + " Msg: " + msg);
                    boolean res = item.SendMsg(cl.UserName + ": " + msg);
                }
            }
            return true;
        } catch (Exception ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public String GetListConnect() throws RemoteException {
        try {
            for (Client item : cliets) {
                if (!item.TestConect()) {
                    cliets.remove(item);
                }
            }
        } catch (NotBoundException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        String res = Json.toJSON(cliets);
        return res;
    }

}
