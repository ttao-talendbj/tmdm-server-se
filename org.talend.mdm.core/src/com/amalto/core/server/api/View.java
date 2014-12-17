/*
 * Generated by XDoclet - Do not edit!
 */
package com.amalto.core.server.api;

import com.amalto.core.objects.view.ViewPOJO;
import com.amalto.core.objects.view.ViewPOJOPK;

public interface View {
    /**
     * Creates or updates a View
     */
    public ViewPOJOPK putView(ViewPOJO view)
            throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

    /**
     * Get item
     */
    public ViewPOJO getView(ViewPOJOPK pk)
            throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

    /**
     * Get a View - no exception is thrown: returns null if not found
     */
    public ViewPOJO existsView(ViewPOJOPK pk)
            throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

    /**
     * Remove an item
     */
    public ViewPOJOPK removeView(ViewPOJOPK pk)
            throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

    /**
     * Retrieve all View PKS
     */
    public java.util.Collection getViewPKs(String regex)
            throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

    /**
     * Retrieve all Views
     */
    public java.util.ArrayList getAllViews(String regex)
            throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

}