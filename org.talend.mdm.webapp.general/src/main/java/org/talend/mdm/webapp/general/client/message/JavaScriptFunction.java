/*
 * Copyright (C) 2006-2018 Talend Inc. - www.talend.com
 * 
 * This source code is available under agreement available at
 * %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
 * 
 * You should have received a copy of the agreement along with this program; if not, write to Talend SA 9 rue Pages
 * 92150 Suresnes, France
 */
package org.talend.mdm.webapp.general.client.message;

import com.google.gwt.core.client.JavaScriptObject;

public class JavaScriptFunction {

	JavaScriptObject fn;

	
	public JavaScriptFunction(JavaScriptObject fn){
		this.fn = fn;
	}
	
	public native void executeFn(String msgId, Object data)/*-{
		var fn = this.@org.talend.mdm.webapp.general.client.message.JavaScriptFunction::fn;
		fn(msgId, data);
	}-*/;
}
