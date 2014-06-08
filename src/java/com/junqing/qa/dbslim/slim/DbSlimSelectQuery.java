package com.junqing.qa.dbslim.slim;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import com.junqing.qa.dbslim.service.DbConnectionFactory;

public class DbSlimSelectQuery
{
  private String connectionPoolName;
  private String sql;
  private List dataTable;
  
  public DbSlimSelectQuery(String sql)
  {
    this(DbSlimSetup.DEFAULT_CONNECTION_POOL_NAME, sql);
  }
  
  public DbSlimSelectQuery(String connectionPoolName, String sql)
  {
    this.connectionPoolName = connectionPoolName;
    
    sql = sql.replaceAll("\\n", " ");
    sql = sql.replaceAll("\\t", " ");
    sql = sql.replaceAll("<br/>", " ");
    sql = sql.trim();
    
    this.sql = sql;
  }
  
  public void table(List<List<String>> table) {}
  
  public List<Object> query()
  {
    List<List<List<String>>> dataTable = getDataTable();
    return new ArrayList(dataTable);
  }
  
  public String data(String columnId, String rowIndex)
  {
    try
    {
      Integer.parseInt(columnId);
      return dataByColumnIndexAndRowIndex(columnId, rowIndex);
    }
    catch (NumberFormatException e) {}
    return dataByColumnNameAndRowIndex(columnId, rowIndex);
  }
  
  public String dataByColumnIndexAndRowIndex(String columnIndex, String rowIndex)
  {
    int rowIndexInteger = Integer.parseInt(rowIndex);
    int columnIndexInteger = Integer.parseInt(columnIndex);
    
    List<List<List<String>>> dataTable = getDataTable();
    
    return (String)((List)((List)dataTable.get(rowIndexInteger)).get(columnIndexInteger)).get(1);
  }
  
  public String dataByColumnNameAndRowIndex(String columnName, String rowIndex)
  {
    int rowIndexInteger = Integer.parseInt(rowIndex);
    
    List<List<List<String>>> dataTable = getDataTable();
    
    List<List<String>> dataRow = (List)dataTable.get(rowIndexInteger);
    for (List<String> dataItem : dataRow) {
      if (String.valueOf(dataItem.get(0)).toUpperCase().equals(String.valueOf(columnName).toUpperCase())) {
        return (String)dataItem.get(1);
      }
    }
    return null;
  }
  
  protected List<List<List<String>>> getDataTable()
  {
    DataSource dataSource = DbConnectionFactory.getDataSource(this.connectionPoolName);
    Connection conn = null;
    Statement stmt = null;
    ResultSet rset = null;
    
    dataTable = new ArrayList();
    try
    {
      conn = dataSource.getConnection();
      stmt = conn.createStatement();
      rset = stmt.executeQuery(this.sql);
      
      ArrayList<String> columnNames = new ArrayList();
      
      int numcols = rset.getMetaData().getColumnCount();
      for (int i = 1; i <= numcols; i++) {
        columnNames.add(rset.getMetaData().getColumnName(i));
      }
      while (rset.next())
      {
        ArrayList<List<String>> dataRow = new ArrayList();
        for (int i = 1; i <= numcols; i++)
        {
          ArrayList<String> dataItem = new ArrayList();
          dataItem.add(String.valueOf(columnNames.get(i - 1)));
          dataItem.add(String.valueOf(rset.getString(i)));
          dataRow.add(dataItem);
        }
        dataTable.add(dataRow);
      }
      return dataTable;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    finally
    {
      try
      {
        if (rset != null) {
          rset.close();
        }
      }
      catch (Exception e) {}
      try
      {
        if (stmt != null) {
          stmt.close();
        }
      }
      catch (Exception e) {}
      try
      {
        if (conn != null) {
          conn.close();
        }
      }
      catch (Exception e) {}
    }
  }
}
