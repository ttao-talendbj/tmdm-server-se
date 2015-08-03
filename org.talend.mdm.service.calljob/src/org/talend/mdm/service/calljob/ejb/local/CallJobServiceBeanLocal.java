// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================

/*
 * Generated by XDoclet - Do not edit!
 */
package org.talend.mdm.service.calljob.ejb.local;

public interface CallJobServiceBeanLocal
   extends javax.ejb.EJBLocalObject
{

   public java.io.Serializable fetchFromOutbound( java.lang.String command,java.lang.String parameters,java.lang.String schedulePlanID ) throws com.amalto.core.util.XtentisException;

   public java.lang.String getDescription( java.lang.String twoLettersLanguageCode ) throws com.amalto.core.util.XtentisException;

   public java.lang.String getJNDIName(  ) throws com.amalto.core.util.XtentisException;

   public java.lang.String getDocumentation( java.lang.String twoLettersLanguageCode ) throws com.amalto.core.util.XtentisException;

   public java.lang.String getStatus(  ) throws com.amalto.core.util.XtentisException;

   public java.lang.String getConfiguration( java.lang.String optionalParameters ) throws com.amalto.core.util.XtentisException;

   public java.lang.String receiveFromInbound( com.amalto.core.ejb.ItemPOJOPK itemPK,java.lang.String routingOrderID,java.lang.String compiledParameters ) throws com.amalto.core.util.XtentisException;

   public java.io.Serializable receiveFromOutbound( java.util.HashMap map ) throws com.amalto.core.util.XtentisException;

   public void start(  ) throws com.amalto.core.util.XtentisException;

   public void stop(  ) throws com.amalto.core.util.XtentisException;

}
