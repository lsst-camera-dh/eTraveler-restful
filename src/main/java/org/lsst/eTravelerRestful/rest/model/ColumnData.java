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
 *
 * @author jrb
 */
public class ColumnData {
  private String header;
  private String dtype; // one of int, float, string
  private String units;  // e.g. "mm". But we probably won't have this info
  private ArrayList<Object> entries;
  public ColumnData() {}
  public ColumnData(String header, String dtype, String units, ArrayList<Object> entries) {
    this.header = header;
    this.dtype = dtype;
    this.units = units;
    this.entries = entries;
  }
  public ColumnData(String header, String dtype, String units) {
    this.header = header;
    this.dtype = dtype;
    this.units = units;
  }
  @XmlElement
  public String getHeader() {
    return header;
  }
  public void setHeader(String header) {
    this.header = header;
  }
  @XmlElement
  public String getDtype() {
    return dtype;
  }
  public void setDtype(String dtype)  {
    this.dtype = dtype;
  }
  @XmlElement
  public String getUnits() {
    return units;
  }
  public void setUnits(String units) {
    this.units = units;
  }
  @XmlElement
  public ArrayList<Object> getEntries() {
    return entries;
  }
  public void setEntries(ArrayList<Object> entries) {
    this.entries = entries;
  }
  public void addEntry(Object entry) {
    if (entries == null) entries = new ArrayList<Object>();
    entries.add(entry);
  }
}
