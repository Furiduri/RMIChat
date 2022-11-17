/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jorge Osvaldo perez Mendoza
 */
public interface IChatServer extends Remote{
    String Connect(String UserName, int Port) throws RemoteException;
    boolean Disconect()throws RemoteException;
    String Send(String user, String msg) throws RemoteException;
    String GetListConnect() throws RemoteException;
    boolean Test() throws RemoteException;
}
