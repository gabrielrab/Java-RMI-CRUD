/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testando;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Interface extends Remote {

    Object[][] ListarCategorias() throws RemoteException;

    boolean ExcluirCategorias(int id) throws RemoteException;

    boolean InserirCategorias(String name) throws RemoteException;

    boolean EditarCategorias(String name, int id) throws RemoteException;

    String getNome(int id) throws RemoteException;

    Object[][] ListarProdutos() throws RemoteException;

    boolean ExcluirProdutos(int id) throws RemoteException;

    boolean InserirProdutos(String name, String estoque, int id_categoria) throws RemoteException;

    Object[][] getProduto(int id) throws RemoteException;

    boolean EditarProdutos(String nome, String estoque, int id_categoria, int id) throws RemoteException;
}
