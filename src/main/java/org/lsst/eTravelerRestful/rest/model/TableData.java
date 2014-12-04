/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lsst.eTravelerRestful.rest.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;


/**
 * Generic table description:  a collection of columns with a title
 * @author jrb
 */
@XmlRootElement       // <- This denotes that this is a contained XML or JSON element
public class TableData {
  private String title;
  private ArrayList<ColumnData> columns;
  private ArrayList<String> columnheaders;
  public TableData() {}
  public TableData(String title, ArrayList<ColumnData> columns) {
    this.title = title;
    this.columns = columns;
  }
  @XmlElement
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  @XmlElement
  public ArrayList<ColumnData> getColumns() {
    return columns;
  }
  public void setColumns(ArrayList<ColumnData> columns)  {
    this.columns = columns;
    columnheaders = new ArrayList<String>();
    for (ColumnData col: columns) {
      columnheaders.add(col.getHeader());
    }
  }
  public void addColumn(ColumnData column ) {
    if (columns == null) {
      columns = new ArrayList<ColumnData>();
      columnheaders = new ArrayList<String>();
    }
    columns.add(column);
    columnheaders.add(column.getHeader());
  }
  @XmlTransient
  public ArrayList<String> getColumnheaders() {
    return columnheaders;
  }
  
  public void setColumnheaders(ArrayList<String> headers) {
      this.columnheaders = headers;
  }
  public boolean hasHeader(String header) {
    if (columnheaders == null) return false;
    return columnheaders.contains(header);
  }
  public ColumnData findColumn(String header) {
    if (!hasHeader(header)) return null;
    for (ColumnData col: columns) {
      if (header.equals(col.getHeader())) return col;
    }
    return null;
  }
}

