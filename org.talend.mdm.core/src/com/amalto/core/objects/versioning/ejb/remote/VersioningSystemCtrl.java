/*
 * Generated by XDoclet - Do not edit!
 */
package com.amalto.core.objects.versioning.ejb.remote;

/**
 * Remote interface for VersioningSystemCtrl.
 * @xdoclet-generated at 12-10-09
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version ${version}
 */
public interface VersioningSystemCtrl
   extends javax.ejb.EJBObject
{
   /**
    * Creates or updates a VersioningSystem
    * @throws XtentisException
    */
   public com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK putVersioningSystem( com.amalto.core.objects.versioning.ejb.VersioningSystemPOJO versioningSystem )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Get item
    * @throws XtentisException
    */
   public com.amalto.core.objects.versioning.ejb.VersioningSystemPOJO getVersioningSystem( com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK pk )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Get a VersioningSystem - no exception is thrown: returns null if not found
    * @throws XtentisException
    */
   public com.amalto.core.objects.versioning.ejb.VersioningSystemPOJO existsVersioningSystem( com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK pk )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Remove an item
    * @throws XtentisException
    */
   public com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK removeVersioningSystem( com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK pk )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Retrieve all VersioningSystem PKS
    * @throws XtentisException
    */
   public java.util.ArrayList getVersioningSystemPKs( java.lang.String regex )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Sets the default versioning system
    * @throws XtentisException
    */
   public com.amalto.core.objects.versioning.util.VersioningServiceCtrlLocalBI setDefaultVersioningSystem( com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK pk )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Returns a string starting with OK if the service is available else returns the reason why it is not Pass null for the pk, if you want to test the availability of the default system
    * @throws XtentisException
    */
   public java.lang.String getVersioningSystemAvailability( com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK pk )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Tag Universe
    * @throws XtentisException
    */
   public com.amalto.core.objects.backgroundjob.ejb.BackgroundJobPOJOPK tagUniverseAsJob( com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK versioningSystemPOJOPK,java.lang.String tag,java.lang.String comment,java.util.Map typeInstances )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Tag Objects
    * @throws XtentisException
    */
   public com.amalto.core.objects.backgroundjob.ejb.BackgroundJobPOJOPK tagObjectsAsJob( com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK versioningSystemPOJOPK,java.lang.String tag,java.lang.String comment,java.lang.String type,java.lang.String[] instances )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Tag Items
    * @throws XtentisException
    */
   public com.amalto.core.objects.backgroundjob.ejb.BackgroundJobPOJOPK tagItemsAsJob( com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK versioningSystemPOJOPK,java.lang.String tag,java.lang.String comment,com.amalto.core.ejb.ItemPOJOPK[] itemPKs )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Restore Objects
    * @throws XtentisException
    */
   public com.amalto.core.objects.backgroundjob.ejb.BackgroundJobPOJOPK restoreObjectsAsJob( com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK versioningSystemPOJOPK,java.lang.String tag,java.lang.String type,java.lang.String[] instances )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Restore Items
    * @throws XtentisException
    */
   public com.amalto.core.objects.backgroundjob.ejb.BackgroundJobPOJOPK restoreItemsAsJob( com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK versioningSystemPOJOPK,java.lang.String tag,com.amalto.core.ejb.ItemPOJOPK[] itemPKs )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Commit Item
    * @throws XtentisException
    */
   public com.amalto.core.ejb.ItemPOJOPK commitItem( com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK versioningSystemPOJOPK,com.amalto.core.ejb.ItemPOJOPK itemPK,java.lang.String comment )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Commit Items
    * @throws XtentisException
    */
   public com.amalto.core.objects.backgroundjob.ejb.BackgroundJobPOJOPK commitItemsAsJob( com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK versioningSystemPOJOPK,com.amalto.core.ejb.ItemPOJOPK[] itemPKs,java.lang.String comment )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Restore Single Item From Revision
    * @throws XtentisException
    */
   public void restoreItemByRevision( com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK versioningSystemPOJOPK,com.amalto.core.ejb.ItemPOJOPK itemPK,java.lang.String revision )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Get Objects Versions
    * @throws XtentisException
    */
   public com.amalto.core.objects.versioning.util.HistoryInfos getObjectsVersions( com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK versioningSystemPOJOPK,java.lang.String type,java.lang.String[] instances )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Get Universe Versions
    * @throws XtentisException
    */
   public com.amalto.core.objects.versioning.util.TagStructureInfo[] getUniverseVersions( com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK versioningSystemPOJOPK )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Get Items Versions
    * @throws XtentisException
    */
   public com.amalto.core.objects.versioning.util.HistoryInfos getItemsVersions( com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK versioningSystemPOJOPK,com.amalto.core.ejb.ItemPOJOPK[] itemPOJOPKs )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Get Item History
    * @throws XtentisException
    */
   public com.amalto.core.objects.versioning.util.HistoryInfos getItemHistory( com.amalto.core.objects.versioning.ejb.VersioningSystemPOJOPK versioningSystemPOJOPK,com.amalto.core.ejb.ItemPOJOPK itemPOJOPK )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

}
