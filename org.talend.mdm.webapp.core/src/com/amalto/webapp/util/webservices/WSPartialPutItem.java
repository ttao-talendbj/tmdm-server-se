// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation 
// Generated source version: 1.1.2

package com.amalto.webapp.util.webservices;


public class WSPartialPutItem {
    protected java.lang.String xml;
    protected java.lang.String datacluster;
    protected java.lang.String pivot;
    protected java.lang.String datamodel;
    protected java.lang.String keyXPath;
    protected java.lang.Integer startingPosition;
    protected java.lang.Boolean overwrite;
    protected boolean report;
    
    public WSPartialPutItem() {
    }
    
    public WSPartialPutItem(java.lang.String xml, java.lang.String datacluster, java.lang.String pivot, java.lang.String datamodel, java.lang.String keyXPath, java.lang.Integer startingPosition, java.lang.Boolean overwrite, boolean report) {
        this.xml = xml;
        this.datacluster = datacluster;
        this.pivot = pivot;
        this.datamodel = datamodel;
        this.keyXPath = keyXPath;
        this.startingPosition = startingPosition;
        this.overwrite = overwrite;
        this.report = report;
    }
    
    public java.lang.String getXml() {
        return xml;
    }
    
    public void setXml(java.lang.String xml) {
        this.xml = xml;
    }
    
    public java.lang.String getDatacluster() {
        return datacluster;
    }
    
    public void setDatacluster(java.lang.String datacluster) {
        this.datacluster = datacluster;
    }
    
    public java.lang.String getPivot() {
        return pivot;
    }
    
    public void setPivot(java.lang.String pivot) {
        this.pivot = pivot;
    }
    
    public java.lang.String getDatamodel() {
        return datamodel;
    }
    
    public void setDatamodel(java.lang.String datamodel) {
        this.datamodel = datamodel;
    }
    
    public java.lang.String getKeyXPath() {
        return keyXPath;
    }
    
    public void setKeyXPath(java.lang.String keyXPath) {
        this.keyXPath = keyXPath;
    }
    
    public java.lang.Integer getStartingPosition() {
        return startingPosition;
    }
    
    public void setStartingPosition(java.lang.Integer startingPosition) {
        this.startingPosition = startingPosition;
    }
    
    public java.lang.Boolean getOverwrite() {
        return overwrite;
    }
    
    public void setOverwrite(java.lang.Boolean overwrite) {
        this.overwrite = overwrite;
    }
    
    public boolean isReport() {
        return report;
    }
    
    public void setReport(boolean report) {
        this.report = report;
    }
}