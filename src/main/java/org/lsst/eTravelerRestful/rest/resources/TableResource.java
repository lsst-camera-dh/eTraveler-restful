/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lsst.eTravelerRestful.rest.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;
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
import org.lsst.eTravelerRestful.rest.model.TableData;
import org.lsst.eTravelerRestful.rest.model.ColumnData;

/**
 *
 * @author jrb
 */
@Path("/table")
public class TableResource {
  private UriInfo uriInfo;
  
  public TableResource(@Context UriInfo uriInfo) {
    this.uriInfo = uriInfo;
  }
   /**
     * This is the most basic request method. It is located under /example1
     * 
     * This is just returning a hard-coded object if there is no query parameter.
     * 
     * If there is, it will return the user with the query parameter.
     * 
     * Get XML with cURL:
     *   curl --header "Accept: application/json" http://localhost:8080/etraveler_rest/rest/table
     * 
     * Get JSON with cURL:
     *   curl --header "Accept: application/xml" http://localhost:8080/etraveler_rest/rest/table
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})          // This is a list of the things we can know, through Jackson
    public Response getStaticObject(@QueryParam("name") String name){           // how to serialize too. 
      if(name != null){
        // We don't support it
        return Response.status(Response.Status.BAD_REQUEST).build();
      }
      TableData t = new TableData();
      t.setTitle("Fixed Table");
      int nEntries = 5;
      ArrayList<ColumnData> columns = new ArrayList<ColumnData>(2);
      t.setColumns(columns);
      ArrayList<Object> columnEntries0 = new ArrayList<Object>(nEntries);
      for (int i = 0; i < nEntries; i++) {
        columnEntries0.add((Object) (2*i));
      }
      t.getColumns().add(new ColumnData("Integer column", "Integer", "",  columnEntries0));
      ArrayList<Object> columnEntries1 = new ArrayList<Object>(nEntries);
      for (float f = 0; f < nEntries; f += 0.5) {
        columnEntries1.add((Object) (f));
      } 
      t.getColumns().add(new ColumnData("Float column", "Float", "",  columnEntries1));

      return Response.ok().entity(new GenericEntity<TableData>(t){}).build();
    }
}
