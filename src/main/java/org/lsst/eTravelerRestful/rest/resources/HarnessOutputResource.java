/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lsst.eTravelerRestful.rest.resources;

import java.net.URI;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ConcurrentHashMap;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.lsst.eTravelerRestful.db.DbConnection;
import org.lsst.eTravelerRestful.db.MysqlDbConnection;
import org.lsst.eTravelerRestful.rest.model.TableData;
import org.lsst.eTravelerRestful.rest.model.ColumnData;

/**
 *
 * @author jrb
 */
@Path("/harnessOutput")
public class HarnessOutputResource {
  private UriInfo uriInfo;
  private DbConnection conn;
  
  public HarnessOutputResource(@Context UriInfo uriInfo) {
    this.uriInfo = uriInfo;
  }
  
  @GET
  @Path("/{jobid}")
  @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  public Response getJobdata(@PathParam("jobid") String jobid,
                             @QueryParam("db") @DefaultValue("Dev") String db,
                             @QueryParam("experiment") @DefaultValue("LSST-CAMERA")
                             String experiment) {
    // Should we be doing this every time or is there a way to reuse a connection?
    conn = makeConnection(db, experiment);
    /*
     * Really should distinguish between two possible failures
     *    bad db name
     *    couldn't get connection, even though it should have been possible
     */
    if (conn == null) return Response.status(Response.Status.BAD_REQUEST).build();
    ArrayList<TableData> data = getJobdata(jobid);
    
    return Response.ok().entity(new GenericEntity<ArrayList<TableData> >(data){}).build();
  }
  static private DbConnection makeConnection(String dbType, String experiment){
    DbConnection conn = new MysqlDbConnection();
    conn.setSourceDb(dbType);
    String datasource=null;
    // ugly hardcoding for now. These values are correct if
    // expermiment must be LSST-CAMERA
    if (dbType.equals("Prod")) datasource = "jdbc/rd-lsst-cam";
    if (dbType.equals("Dev")) datasource = "jdbc/rd-lsst-cam-dev";
    if (dbType.equals("Test")) datasource = "jdbc/rd-lsst-cmft";
    if (dbType.equals("Raw")) datasource = "jdbc/rd-lsst-camt";
    if (datasource == null) return null;
    boolean isOpen = conn.openTomcat(datasource);
    if (isOpen) {
      System.out.println("Successfully connected to " + datasource);    
      return conn;
    }
    else {
      System.out.println("Failed to connect");
      return null;
    }
  }
  private static ConcurrentHashMap<String, String> dbTables=null;
  private static String[ ] 
      getCols = {"schemaName", "name", "schemaInstance", "value"};
  static void initDbTables() {
    dbTables = new ConcurrentHashMap<String, String>();
    dbTables.put("IntResultHarnessed", "Integer");
    dbTables.put("FloatResultHarnessed", "Float");
    dbTables.put("StringResultHarnessed", "String");
    dbTables.put("FilepathResultHarnessed", "String");
  }
  private ArrayList<TableData> getJobdata(String jobid)  {
    // Make queries to the 4 output tables for harnessed jobs
    if (dbTables == null) initDbTables();
    ArrayList<TableData> dataTables = new ArrayList<TableData>();
    HashMap<String, TableData> titles = new HashMap<String, TableData>();
    String where = " where activityId='" + jobid + 
        "' order by schemaName, name, schemaInstance ";
    ResultSet rs;
    try {
      for (String dbTable: dbTables.keySet()) {
        PreparedStatement stmt = conn.prepareQuery(dbTable, getCols, where);
        rs = stmt.executeQuery();
        boolean more = rs.first();
        while (more) {
          String schema = rs.getString(1);
          String header = rs.getString(2);

          if (!titles.containsKey(schema)) {
            TableData newTable = new TableData(schema, null);
            titles.put(schema, newTable);
            dataTables.add(newTable);
          }
          TableData ourTable = titles.get(schema);
          String dtype = dbTables.get(dbTable);
          // Check if ourTable has column with header = db name field.
          ColumnData ourColumn = ourTable.findColumn(header);
          if (ourColumn == null) {  // add it
            ourColumn = new ColumnData(header, dtype, "", null);
            ourTable.addColumn(ourColumn);
          }
          if (dtype.equals("Integer")) {
            int val = rs.getInt(4);
            ourColumn.addEntry(val);
          } else if (dtype.equals("Float")) {
            float val = rs.getFloat(4);
            ourColumn.addEntry(val);
          } else {
            String val = rs.getString(4);
            ourColumn.addEntry(val);
          }
          // Add value to the column
          // Add 
          more = rs.next();
        }
      }
    
    } catch (SQLException ex) {
      
    }
    // Handle FilePathResultHarnessed separately since it always
    // goes in its own table
    
    return dataTables;
  }
  
}
