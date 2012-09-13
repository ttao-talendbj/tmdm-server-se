// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.mdm.webapp.general.gwt;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class GwtWebContext {

    private final HttpServletRequest request;

    private final HttpServletResponse response;

    private final HttpServlet servlet;

    public static final String GENERALCONTEXT = "/general"; //$NON-NLS-1$

    public GwtWebContext(HttpServletRequest request, HttpServletResponse response, HttpServlet servlet) {
        this.request = request;
        this.response = response;
        this.servlet = servlet;
    }

    public HttpSession getSession() {
        return getSession();
    }

    public HttpSession getSession(boolean create) {
        return request.getSession(create);
    }

    public HttpServletRequest getHttpServletRequest() {
        return request;
    }

    public HttpServletResponse getHttpServletResponse() {
        return response;
    }

    public HttpServlet getServlet() {
        return servlet;
    }

    public HttpSession getDefaultConfigurationSession() {
        ServletContext context = this.servlet.getServletContext();
        if (context != null) {
            ServletContext generalAppContext = context.getContext(GENERALCONTEXT);
            if (generalAppContext != null) {
                if (getSession() == null)
                    return null;
                return (HttpSession) generalAppContext.getAttribute(GENERALCONTEXT + getSession().getId());
            }
        }
        return null;
    }
}
