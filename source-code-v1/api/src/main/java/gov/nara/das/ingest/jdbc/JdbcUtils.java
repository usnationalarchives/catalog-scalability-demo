package gov.nara.das.ingest.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import gov.nara.das.ingest.configs.DatabaseConfig;

import static gov.nara.das.util.Utils.*;

@Repository
public class JdbcUtils {
	
	@Autowired
	private DatabaseConfig config;
	/**
	 * 
	 * @param dbms
	 * @param dbname
	 * @param server
	 * @param port
	 * @param user
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public  Connection getConnection() throws SQLException {
		String dbms=config.getDbms();
	    String dbname=config.getName();
		String url=config.getURL();
		String user=config.getUserName();
		String password=config.getPassword();
	    String schema=config.getSchema();
		Connection conn = null;
//		if(1==1)throw new RuntimeException("dbms="+dbms
//				+ "\ndbname=" +dbname
//				+ "\nurl=" + url 
//				+ "\nuser=" + user
//				+ "\npassword=" + password
//				+ "\nschema=" + schema
//		);
		try {
			Properties connectionProps = new Properties();
			connectionProps.put("user", user);
			if(schema!=null){
				String qs="?currentSchema="+schema;
				url+=qs;
			}
			Class.forName("org.postgresql.Driver");
			Properties props = new Properties();
			props.setProperty("user",user);
			props.setProperty("ssl","false");
			props.setProperty("password",password);
		    Object conn0 = DriverManager.getConnection(url, props);
			conn=(org.postgresql.jdbc.PgConnection)conn0;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return conn;
	}
}
