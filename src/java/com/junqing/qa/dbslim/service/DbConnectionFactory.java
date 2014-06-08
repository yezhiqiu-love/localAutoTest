package com.junqing.qa.dbslim.service;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

public class DbConnectionFactory
{
  protected static Map<String, DbConnectionDetails> connectionDetailsMap = new ConcurrentHashMap();
  
  public static synchronized DataSource getDataSource(String connectionPoolName)
  {
    DbConnectionDetails connectionDetails = (DbConnectionDetails)connectionDetailsMap.get(connectionPoolName);
    if (null == connectionDetails) {
      return null;
    }
    return connectionDetails.dataSource;
  }
  
  public static synchronized DataSource getDataSource(String connectionPoolName, String jdbcDriverClass, String connectURI, String username, String password, int minIdle, int maxActive)
    throws Exception
  {
    if (null == connectionDetailsMap.get(connectionPoolName)) {
      connectionDetailsMap.put(connectionPoolName, new DbConnectionDetails());
    }
    DbConnectionDetails connectionDetails = (DbConnectionDetails)connectionDetailsMap.get(connectionPoolName);
    if ((null != connectionDetails.dataSource) && (String.valueOf(jdbcDriverClass).equals(connectionDetails.jdbcDriverClass)) && (String.valueOf(connectURI).equals(connectionDetails.connectURI)) && (String.valueOf(username).equals(connectionDetails.username)) && (String.valueOf(password).equals(connectionDetails.password)) && (minIdle == connectionDetails.minIdle) && (maxActive == connectionDetails.maxActive)) {
      return connectionDetails.dataSource;
    }
    ComboPooledDataSource pooledDatasource = new ComboPooledDataSource();
    pooledDatasource.setDriverClass(jdbcDriverClass);
    pooledDatasource.setJdbcUrl(connectURI);
    pooledDatasource.setUser(username);
    pooledDatasource.setPassword(password);
    pooledDatasource.setMinPoolSize(minIdle);
    pooledDatasource.setAcquireIncrement(5);
    pooledDatasource.setMaxPoolSize(maxActive);
    
    connectionDetails.jdbcDriverClass = jdbcDriverClass;
    connectionDetails.connectURI = connectURI;
    connectionDetails.username = username;
    connectionDetails.password = password;
    connectionDetails.minIdle = minIdle;
    connectionDetails.maxActive = maxActive;
    connectionDetails.dataSource = pooledDatasource;
    
    return pooledDatasource;
  }
}

