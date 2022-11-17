/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import interfaces.Client;
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
import javax.swing.JTextArea;
import server.ChatServer;

/**
 *
 * @author Jorge Osvaldo perez Mendoza
 */
public class ServerClient implements IChatClient {

    public int PORT;
    public String IP;
    private final JTextArea LogPublic;
    private final JTextArea LogPrivate;

    public ServerClient(int port, String Name, JTextArea txtMsgGrupo, JTextArea txtMsgPrivados) throws UnknownHostException, RemoteException, AlreadyBoundException {
        LogPublic = txtMsgGrupo;
        LogPrivate = txtMsgPrivados;
        PORT = port;
        InetAddress address = InetAddress.getLocalHost();
        IP = address.getHostAddress();
        Remote remote = UnicastRemoteObject.exportObject(this, PORT);
        Registry registry = LocateRegistry.createRegistry(PORT);
        System.out.println("Servidor escuchando en el puerto " + String.valueOf(PORT));
        System.out.println("IP: " + IP);
        registry.bind(Name, remote); // Registrar ChatServer 
    }

    @Override
    public boolean Recive(String user, String msg) throws RemoteException {
        System.out.println("User:" + user + " Msg: " + msg);
        LogPublic.append("\n" + user + ": " + msg);
        return true;
    }

    @Override
    public boolean Test() throws RemoteException {
        return true;
    }

    @Override
    public boolean RecivePrivate(String user, String msg) throws RemoteException {
        System.out.println("Private User:" + user + " Msg: " + msg);
        LogPrivate.append("\n" + user + ": " + msg);
        return true;
    }

}
