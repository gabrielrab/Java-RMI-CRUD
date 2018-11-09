package testando;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class Server implements Interface {

    public Server() {
    }

    public boolean ExcluirCategorias(int id) {
        
        String query = "DELETE FROM categorias WHERE id_categoria=" + id + ";";//Query de Exclusão
        try {
            stmt = connection.createStatement();
            int v = stmt.executeUpdate(query);
            query = "DELETE FROM produtos WHERE id_categoria=" + id + ";";
            try {
            stmt = connection.createStatement();
            int s = stmt.executeUpdate(query);
            }
            catch (SQLException e) {

            }
            return true;
        } catch (SQLException e) {

        }
        return false;
    }
    
    

    public boolean InserirCategorias(String name) {
        String query = "INSERT INTO categorias(nome) VALUES('" + name + "')";
        try {
            stmt = connection.createStatement();
            int v = stmt.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean EditarCategorias(String name, int id) {
        String query = "UPDATE categorias SET nome='" + name + "' WHERE id_categoria=" + id;
        try {
            stmt = connection.createStatement();
            int v = stmt.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public String getNome(int id) {

        String query = "SELECT * FROM categorias WHERE id_categoria=" + id;
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.last();
            rs.beforeFirst();
            String nome = null;
            while (rs.next()) {
                nome = rs.getString("nome");
            }
            return nome;
        } catch (SQLException e) {

        }
        return null;
    }

    @Override
    public Object[][] ListarCategorias() {
        String query = "SELECT * FROM categorias;";
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.last();
            int counter = rs.getRow();
            data = new Object[counter][2];
            rs.beforeFirst();
            int i = 0;
            while (rs.next()) {
                int id_categoria = rs.getInt("id_categoria");
                String nome = rs.getString("nome");
                data[i][0] = id_categoria;
                data[i][1] = nome;
                i++;
            }
            return data;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível recuperar os registros!!!", "RESULTADO:", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return null;
    }

    public Object[][] ListarProdutos() {
        String query = "SELECT * FROM produtos;";
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.last();
            int counter = rs.getRow();
            data = new Object[counter][4];
            rs.beforeFirst();
            int i = 0;
            while (rs.next()) {
                int id_produto = rs.getInt("id_categoria");
                int id_categoria = rs.getInt("id_categoria");
                String Categoria = getNome(id_categoria);
                String nome = rs.getString("nome");
                String estoque = rs.getString("estoque");

                data[i][0] = id_produto;
                data[i][1] = Categoria;
                data[i][2] = nome;
                data[i][3] = estoque;
                i++;
            }
            return data;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível recuperar os registros!!!", "RESULTADO:", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return null;
    }

    public boolean ExcluirProdutos(int id) {
        String query = "DELETE FROM produtos WHERE id_produto=" + id + ";";//Query de Exclusão
        try {
            stmt = connection.createStatement();
            int v = stmt.executeUpdate(query);
            return true;
        } catch (SQLException e) {

        }
        return false;
    }

    public boolean InserirProdutos(String name, String estoque, int id_categoria) {
        String query = "INSERT INTO produtos(id_categoria,nome,estoque) VALUES('" + id_categoria + "', '" + name + "', '" + estoque + "')";
        try {
            stmt = connection.createStatement();
            int v = stmt.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Object[][] getProduto(int id) {
        String query = "SELECT * FROM produtos WHERE id_produto=" + id + ";";
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.last();
            int counter = rs.getRow();
            data = new Object[1][4];
            rs.beforeFirst();
            int i = 0;
            while (rs.next()) {
                int id_produto = rs.getInt("id_produto");
                int id_categoria = rs.getInt("id_categoria");

                String nome = rs.getString("nome");
                String estoque = rs.getString("estoque");

                data[i][0] = id_produto;
                data[i][1] = id_categoria;
                data[i][2] = nome;
                data[i][3] = estoque;
                i++;
            }
            return data;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível recuperar os registros!!!", "RESULTADO:", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return null;
    }

    public boolean EditarProdutos(String nome, String estoque, int id_categoria, int id) {
        String query = "UPDATE produtos SET nome='" + nome + "', estoque='" + estoque + "',id_categoria=" + id_categoria + " WHERE id_produto=" + id;
        try {
            stmt = connection.createStatement();
            int v = stmt.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    static Connection connection = null;
    Statement stmt;
    Object[][] data;

    public static void main(String args[]) {
        conectarMySQL(3306, "root", "");

        try {
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            Server obj = new Server();
            Interface stub = (Interface) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
//            java.rmi.registry.LocateRegistry.createRegistry(1099);
            registry.bind("Hello", stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    private static void conectarMySQL(int port, String user, String password) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("MySQL JDBC Driver registrado!");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver JDBC não encontrado. Verifique as configurações das bibliotecas.");
            e.printStackTrace();
            return;
        }

        try {
            // DriverManager: The basic service for managing a set of JDBC drivers.
            connection = DriverManager.getConnection("jdbc:mysql://localhost:" + port + "/aw2", user, password);
            if (connection != null) {
                System.out.println("Conexão bem sucedida!! Agora é possível manipular o banco de dados.");
            } else {
                System.out.println("Falha na conexão!");
            }
        } catch (SQLException e) {
            System.out.println("Falha na conexão!");
            e.printStackTrace();
            return;
        }
    }

}
