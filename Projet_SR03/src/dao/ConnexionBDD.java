package dao;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;


public final class ConnexionBDD {

	private static volatile ConnexionBDD instance;
	private Connection cnx; 
	
	private ConnexionBDD() {
		try {
			Properties p = new Properties();
			p.load(Thread.currentThread().getContextClassLoader().
						getResourceAsStream("confBDD.properties"));
				
			// Chargement du driver
			Class.forName(p.getProperty("driver"));
			System.out.println(p.getProperty("driver"));
			cnx = DriverManager.getConnection(p.getProperty("url"),
					p.getProperty("user"), p.getProperty("pwd"));
			//Class.forName("org.postgresql.Driver");  
			//cnx = DriverManager.getConnection("jdbc:postgresql://localhost:5555/jeux_video","",""); 			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	
	public static synchronized ConnexionBDD getInstance() {
		if(instance==null)
			instance = new ConnexionBDD();
		
		return instance;
	}

	public Connection getCnx() {
		return cnx;
	}

	public void closeCnx() throws SQLException{
		if(cnx!=null){
			cnx.close();
			instance=null;
		}
	}
}
