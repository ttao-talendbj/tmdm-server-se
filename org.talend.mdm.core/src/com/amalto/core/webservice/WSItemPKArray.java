/*
 * Copyright (C) 2006-2018 Talend Inc. - www.talend.com
 * 
 * This source code is available under agreement available at
 * %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
 * 
 * You should have received a copy of the agreement along with this program; if not, write to Talend SA 9 rue Pages
 * 92150 Suresnes, France
 */
package com.amalto.core.webservice;

import javax.xml.bind.annotation.XmlType;

@XmlType(name="WSItemPKArray")
public class WSItemPKArray {
    protected com.amalto.core.webservice.WSItemPK[] wsItemPK;
    
    public WSItemPKArray() {
    }
    
    public WSItemPKArray(com.amalto.core.webservice.WSItemPK[] wsItemPK) {
        this.wsItemPK = wsItemPK;
    }
    
    public com.amalto.core.webservice.WSItemPK[] getWsItemPK() {
        return wsItemPK;
    }
    
    public void setWsItemPK(com.amalto.core.webservice.WSItemPK[] wsItemPK) {
        this.wsItemPK = wsItemPK;
    }
}
