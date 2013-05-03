package edu.unsw.comp9321;

import java.sql.Connection;

import javax.naming.*;

import edu.unsw.comp9321.jdbc.DBConnectionFactory;

import junit.framework.TestCase;

/**
 * 
 */
public class DBConnectionFactoryTest extends TestCase {

	/**
	 * Constructor for ServiceLocatorTest.
	 * @param arg0
	 */
	
	 public void setUp() throws Exception {
		 super.setUp();
	     
		 // To test JNDI outside Tomcat, we need to set these
		 // values manually ... (just for testing purposes)
		 System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
             "org.apache.naming.java.javaURLContextFactory");
         System.setProperty(Context.URL_PKG_PREFIXES, 
             "org.apache.naming");            

         // Create InitialContext with java:/comp/env/jdbc
         InitialContext ic = new InitialContext();

         ic.createSubcontext("java:");
         ic.createSubcontext("java:comp");
         ic.createSubcontext("java:comp/env");
         ic.createSubcontext("java:comp/env/jdbc");
        
         // Construct BasicDataSource reference
         Reference ref = new Reference("javax.sql.DataSource", "org.apache.commons.dbcp.BasicDataSourceFactory", null);
         ref.add(new StringRefAddr("driverClassName", "org.apache.derby.jdbc.ClientDriver"));
         //TODO: change the following to your own setting
         ref.add(new StringRefAddr("url", "jdbc:derby://localhost:1527/CarReservationSystem"));
         ref.add(new StringRefAddr("username", "sa"));
         ref.add(new StringRefAddr("password", "sa"));
         ic.bind("java:comp/env/jdbc/cs9321", ref);
    }
	 
	public DBConnectionFactoryTest(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		//junit.swingui.TestRunner.run(DBUtilsTest.class);
		junit.textui.TestRunner.run(DBConnectionFactoryTest.class);
	}

	public void testCreateConnection() throws Exception {
		Connection con = DBConnectionFactory.getConnection(); 
		assertNotNull(con);
		if (con != null)
			con.close();
	}
}
