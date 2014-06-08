package com.junqing.qa.dbslim.service;

import javax.sql.DataSource;

class DbConnectionDetails
{
  public DataSource dataSource = null;
  public String jdbcDriverClass = null;
  public String connectURI = null;
  public String username = null;
  public String password = null;
  public int minIdle = 1;
  public int maxActive = 5;
}
