package com.junqing.qa.dbslim.slim;

import com.junqing.qa.dbslim.service.DbConnectionFactory;

public class DbSlimSetup
{
  public static String DEFAULT_CONNECTION_POOL_NAME = "default";
  public static int DEFAULT_CONNECTION_POOL_MIN_IDLE = 1;
  public static int DEFAULT_CONNECTION_POOL_MAX_AXTIVE = 5;
  
  public DbSlimSetup(String jdbcDriverClass, String connectURI, String username, String password)
    throws Exception
  {
    DbConnectionFactory.getDataSource(DEFAULT_CONNECTION_POOL_NAME, jdbcDriverClass, connectURI, username, password, DEFAULT_CONNECTION_POOL_MIN_IDLE, DEFAULT_CONNECTION_POOL_MAX_AXTIVE);
  }
  
  public DbSlimSetup(String jdbcDriverClass, String connectURI, String username, String password, int minIdle, int maxActive)
    throws Exception
  {
    DbConnectionFactory.getDataSource(DEFAULT_CONNECTION_POOL_NAME, jdbcDriverClass, connectURI, username, password, minIdle, maxActive);
  }
  
  public DbSlimSetup(String jdbcDriverClass, String connectionPoolName, String connectURI, String username, String password, int minIdle, int maxActive)
    throws Exception
  {
    DbConnectionFactory.getDataSource(connectionPoolName, jdbcDriverClass, connectURI, username, password, minIdle, maxActive);
  }
}