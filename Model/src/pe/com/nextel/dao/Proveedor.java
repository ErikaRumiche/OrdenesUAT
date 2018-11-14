package pe.com.nextel.dao;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.sql.DataSource;

import pe.com.nextel.util.StaticProperties;


public class Proveedor{

    /**Prueba de Conexión con pool**/
    public static Connection getConnection() {
        Context ctx = null;
        Hashtable ht = new Hashtable();
        Connection connection = null;
        try{

            StaticProperties singleton = StaticProperties.instance();
            Properties properties = singleton.props;
            ht.put(Context.INITIAL_CONTEXT_FACTORY, properties.getProperty("JNDI.INITIAL_CONTEXT_FACTORY"));
            ht.put(Context.PROVIDER_URL,properties.getProperty("JNDI.PROVIDER_URL"));
            ht.put(Context.SECURITY_PRINCIPAL, properties.getProperty("JNDI.SECURITY_PRINCIPAL"));
            ht.put(Context.SECURITY_CREDENTIALS, properties.getProperty("JNDI.SECURITY_CREDENTIALS"));
            DataSource ods = null;
            ctx = new InitialContext(ht);
            ods = (DataSource)ctx.lookup(properties.getProperty("JNDI.DATASOURCE"));
            connection = ods.getConnection();
        } catch(SQLException e) {
            System.out.println("Error getting pooled connection from Factory Pooling:" + e);

            try {
                if (connection != null){
                    connection.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error closing connection :" + ex);
            }
        } catch (Exception e) {
            System.out.println("Error getting pooled connection from Factory Pooling:" + e);
            try {
                if (connection != null){
                    connection.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error closing connection :" + ex);
            }
        }
        return connection;
    }

}