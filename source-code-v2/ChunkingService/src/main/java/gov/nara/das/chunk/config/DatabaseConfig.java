package gov.nara.das.chunk.config;


import javax.sql.DataSource;

import org.postgresql.jdbc3.Jdbc3PoolingDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@ComponentScan
@EnableTransactionManagement
@PropertySource(value = { "classpath:application.properties" })
@Configuration
public class DatabaseConfig {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Value("${dbms}")
	private String dbms;
	
	@Value("${db.name}")
	private String name;
	
	@Value("${db.job.schema}")
	private String schema;
	
    @Value("${db.url}")
    private String url;
    
    @Value("${db.driver}")
    private String driver;


    @Value("${db.username}")
    private String userName;
    
    @Value("${db.password}")
    private String password;

	/**
	 * @return the dbms
	 */
	public String getDbms() {
		return dbms;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the schema
	 */
	public String getSchema() {
		return schema;
	}

	/**
	 * @return the url
	 */
	public String getURL() {
		return url;
	}
	
	/**
	 * @return the driver
	 */
	public String getDriver() {
		return driver;
	}
	
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	    @Autowired
	    private Environment env;
	 
	    @Value("${init-db:false}")
	    private String initDatabase;
	     
	    @Bean
	    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer()
	    {
	        return new PropertySourcesPlaceholderConfigurer();
	    }    
	 
	    @Bean
	    public JdbcTemplate jdbcTemplate(DataSource dataSource)
	    {
	        return new JdbcTemplate(dataSource);
	    }
	 
	    @Bean
	    public PlatformTransactionManager transactionManager(DataSource dataSource)
	    {
	        return new DataSourceTransactionManager(dataSource);
	    }
	 
	    @Bean
	    public DataSource dataSource()
	    {
	    	log.error("getting datasource");
	    	log.error("db.url="+env.getProperty("db.url"));
	    	Jdbc3PoolingDataSource dataSource = new Jdbc3PoolingDataSource();
	        dataSource.setUrl(env.getProperty("db.url"));
	        dataSource.setUser(env.getProperty("db.username"));
	        dataSource.setPassword(env.getProperty("db.password"));
	        dataSource.setMaxConnections(Integer.parseInt(env.getProperty("db.max.connections")));
	        log.error("db.url="+env.getProperty("db.url"));
	        log.error("got datasource:"+dataSource);
	        log.error("classpath="+System.getProperty("java.class.path"));
	        return dataSource;
	    }
	 

	
} // class DatabaseConfig


