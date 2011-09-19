/*
 * Generated by XDoclet - Do not edit!
 */
package com.amalto.core.ejb.remote;

import com.amalto.core.integrity.FKIntegrityCheckResult;

/**
 * Remote interface for ItemCtrl2.
 * @xdoclet-generated
 * @copyright The XDoclet Team
 * @author XDoclet
 * @version ${version}
 */
public interface ItemCtrl2
   extends javax.ejb.EJBObject
{
   /**
    * Creates or updates a item
    * @throws XtentisException
    */
   public com.amalto.core.ejb.ItemPOJOPK putItem( com.amalto.core.ejb.ItemPOJO item,com.amalto.core.objects.datamodel.ejb.DataModelPOJO datamodel )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * updates a item taskId
    * @throws XtentisException
    */
   public com.amalto.core.ejb.ItemPOJOPK updateItemMetadata( com.amalto.core.ejb.ItemPOJO item )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Get item
    * @throws XtentisException
    */
   public com.amalto.core.ejb.ItemPOJO getItem( com.amalto.core.ejb.ItemPOJOPK pk )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Get item with revisionID
    * @throws XtentisException
    */
   public com.amalto.core.ejb.ItemPOJO getItem( java.lang.String revisionID,com.amalto.core.ejb.ItemPOJOPK pk )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Is Item modified by others - no exception is thrown: true|false
    * @throws XtentisException
    */
   public boolean isItemModifiedByOther( com.amalto.core.ejb.ItemPOJOPK item,long time )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Get an item - no exception is thrown: returns null if not found
    * @throws XtentisException
    */
   public com.amalto.core.ejb.ItemPOJO existsItem( com.amalto.core.ejb.ItemPOJOPK pk )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Remove an item - returns null if no item was deleted
    * @throws XtentisException
    */
   public com.amalto.core.ejb.ItemPOJOPK deleteItem( com.amalto.core.ejb.ItemPOJOPK pk )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Delete items in a stateless mode: open a connection --> perform delete --> close the connection
    * @throws XtentisException
    */
   public int deleteItems( com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK,java.lang.String conceptName,com.amalto.xmlserver.interfaces.IWhereItem search,int spellTreshold )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Drop an item - returns null if no item was dropped
    * @throws XtentisException
    */
   public com.amalto.core.ejb.DroppedItemPOJOPK dropItem( com.amalto.core.ejb.ItemPOJOPK itemPOJOPK,java.lang.String partPath )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Search Items thru a view in a cluster and specifying a condition
    * @param dataClusterPOJOPK The Data Cluster where to run the query
    * @param viewPOJOPK The View
    * @param whereItem The condition
    * @param spellThreshold The condition spell checking threshold. A negative value de-activates spell
    * @param start The first item index (starts at zero)
    * @param limit The maximum number of items to return
    * @return The ordered list of results
    * @throws XtentisException
    */
   public java.util.ArrayList viewSearch( com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK,com.amalto.core.objects.view.ejb.ViewPOJOPK viewPOJOPK,com.amalto.xmlserver.interfaces.IWhereItem whereItem,int spellThreshold,int start,int limit )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Search ordered Items thru a view in a cluster and specifying a condition
    * @param dataClusterPOJOPK The Data Cluster where to run the query
    * @param viewPOJOPK The View
    * @param whereItem The condition
    * @param spellThreshold The condition spell checking threshold. A negative value de-activates spell
    * @param orderBy The full path of the item user to order
    * @param direction One of {@link IXmlServerSLWrapper#ORDER_ASCENDING} or {@link IXmlServerSLWrapper#ORDER_DESCENDING}
    * @param start The first item index (starts at zero)
    * @param limit The maximum number of items to return
    * @return The ordered list of results
    * @throws XtentisException
    */
   public java.util.ArrayList viewSearch( com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK,com.amalto.core.objects.view.ejb.ViewPOJOPK viewPOJOPK,com.amalto.xmlserver.interfaces.IWhereItem whereItem,int spellThreshold,java.lang.String orderBy,java.lang.String direction,int start,int limit )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Returns an ordered collection of results searched in a cluster and specifying an optional condition<br/> The results are xml objects made of elements constituted by the specified viewablePaths
    * @param dataClusterPOJOPK The Data Cluster where to run the query
    * @param forceMainPivot An optional pivot that will appear first in the list of pivots in the query<br> : This allows forcing cartesian products: for instance Order Header vs Order Line
    * @param viewablePaths The list of elements returned in each result
    * @param whereItem The condition
    * @param spellThreshold The condition spell checking threshold. A negative value de-activates spell
    * @param start The first item index (starts at zero)
    * @param limit The maximum number of items to return
    * @return The ordered list of results
    * @throws XtentisException
    */
   public java.util.ArrayList xPathsSearch( com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK,java.lang.String forceMainPivot,java.util.ArrayList viewablePaths,com.amalto.xmlserver.interfaces.IWhereItem whereItem,int spellThreshold,int start,int limit )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Returns an ordered collection of results searched in a cluster and specifying an optional condition<br/> The results are xml objects made of elements constituted by the specified viewablePaths
    * @param dataClusterPOJOPK The Data Cluster where to run the query
    * @param forceMainPivot An optional pivot that will appear first in the list of pivots in the query<br> : This allows forcing cartesian products: for instance Order Header vs Order Line
    * @param viewablePaths The list of elements returned in each result
    * @param whereItem The condition
    * @param spellThreshold The condition spell checking threshold. A negative value de-activates spell
    * @param orderBy The full path of the item user to order
    * @param direction One of {@link IXmlServerSLWrapper#ORDER_ASCENDING} or {@link IXmlServerSLWrapper#ORDER_DESCENDING}
    * @param start The first item index (starts at zero)
    * @param limit The maximum number of items to return
    * @return The ordered list of results
    * @throws XtentisException
    */
   public java.util.ArrayList xPathsSearch( com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK,java.lang.String forceMainPivot,java.util.ArrayList viewablePaths,com.amalto.xmlserver.interfaces.IWhereItem whereItem,int spellThreshold,java.lang.String orderBy,java.lang.String direction,int start,int limit )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Get items hierarchical tree according to pivots
    * @param clusterName The Data Cluster where to run the query
    * @param mainPivotName The main Business Concept name
    * @param pivotWithKeys The pivots with their IDs which selected to be the catalog of the hierarchical tree
    * @param indexPaths The title as the content of each leaf node of the hierarchical tree
    * @param whereItem The condition
    * @param pivotDirections One of {@link IXmlServerSLWrapper#ORDER_ASCENDING} or {@link IXmlServerSLWrapper#ORDER_DESCENDING}
    * @param indexDirections One of {@link IXmlServerSLWrapper#ORDER_ASCENDING} or {@link IXmlServerSLWrapper#ORDER_DESCENDING}
    * @param start The first item index (starts at zero)
    * @param limit The maximum number of items to return
    * @return The ordered list of results
    * @throws XtentisException
    */
   public java.util.ArrayList getItemsPivotIndex( java.lang.String clusterName,java.lang.String mainPivotName,java.util.LinkedHashMap pivotWithKeys,java.lang.String[] indexPaths,com.amalto.xmlserver.interfaces.IWhereItem whereItem,java.lang.String[] pivotDirections,java.lang.String[] indexDirections,int start,int limit )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   public java.util.ArrayList getChildrenItems( java.lang.String clusterName,java.lang.String conceptName,java.lang.String[] PKXpaths,java.lang.String FKXpath,java.lang.String labelXpath,java.lang.String fatherPK,com.amalto.xmlserver.interfaces.IWhereItem whereItem,int start,int limit )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Count the items denoted by concept name meeting the optional condition whereItem
    * @param dataClusterPOJOPK
    * @param conceptName
    * @param whereItem
    * @param spellThreshold
    * @return The number of items found
    * @throws XtentisException
    */
   public long count( com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK,java.lang.String conceptName,com.amalto.xmlserver.interfaces.IWhereItem whereItem,int spellThreshold )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Search ordered Items thru a view in a cluster and specifying a condition
    * @param dataClusterPOJOPK The Data Cluster where to run the query
    * @param viewPOJOPK The View
    * @param searchValue The value/sentenced searched
    * @param matchAllWords If <code>true</code>, the items must match all the words in the sentence
    * @param spellThreshold The condition spell checking threshold. A negative value de-activates spell
    * @param orderBy The full path of the item user to order
    * @param direction One of {@link IXmlServerSLWrapper#ORDER_ASCENDING} or {@link IXmlServerSLWrapper#ORDER_DESCENDING}
    * @param start The first item index (starts at zero)
    * @param limit The maximum number of items to return
    * @return The ordered list of results
    * @throws XtentisException
    */
   public java.util.ArrayList quickSearch( com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK,com.amalto.core.objects.view.ejb.ViewPOJOPK viewPOJOPK,java.lang.String searchValue,boolean matchAllWords,int spellThreshold,java.lang.String orderBy,java.lang.String direction,int start,int limit )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Get the possible value for the business Element Path, optionally filtered by a condition
    * @param dataClusterPOJOPK The data cluster where to run the query
    * @param businessElementPath The business element path. Must be of the form <code>ConceptName/[optional sub elements]/element</code>
    * @param whereItem The optional condition
    * @param spellThreshold The condition spell checking threshold. A negative value de-activates spell
    * @param orderBy The full path of the item user to order
    * @param direction One of {@link IXmlServerSLWrapper#ORDER_ASCENDING} or {@link IXmlServerSLWrapper#ORDER_DESCENDING}
    * @return The list of values
    * @throws XtentisException
    */
   public java.util.ArrayList getFullPathValues( com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK,java.lang.String businessElementPath,com.amalto.xmlserver.interfaces.IWhereItem whereItem,int spellThreshold,java.lang.String orderBy,java.lang.String direction )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Extract results thru a view and transform them using a transformer<br/> This call is asynchronous and results will be pushed via the passed {@link TransformerCallBack}
    * @param dataClusterPOJOPK The Data Cluster where to run the query
    * @param context The {@link TransformerContext} containi the inital context and the transformer name
    * @param globalCallBack The callback function called by the transformer when it completes a step
    * @param viewPOJOPK A filtering view
    * @param whereItem The condition
    * @param spellThreshold The condition spell checking threshold. A negative value de-activates spell
    * @param orderBy The full path of the item user to order
    * @param direction One of {@link IXmlServerSLWrapper#ORDER_ASCENDING} or {@link IXmlServerSLWrapper#ORDER_DESCENDING}
    * @param start The first item index (starts at zero)
    * @param limit The maximum number of items to return
    */
   public void extractUsingTransformerThroughView( com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK,com.amalto.core.objects.transformers.v2.util.TransformerContext context,com.amalto.core.objects.transformers.v2.util.TransformerCallBack globalCallBack,com.amalto.core.objects.view.ejb.ViewPOJOPK viewPOJOPK,com.amalto.xmlserver.interfaces.IWhereItem whereItem,int spellThreshold,java.lang.String orderBy,java.lang.String direction,int start,int limit )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Extract results thru a view and transform them using a transformer<br/> This call is asynchronous and results will be pushed via the passed {@link TransformerCallBack}
    * @param dataClusterPOJOPK The Data Cluster where to run the query
    * @param transformerPOJOPK The transformer to use
    * @param viewPOJOPK A filtering view
    * @param whereItem The condition
    * @param spellThreshold The condition spell checking threshold. A negative value de-activates spell
    * @param orderBy The full path of the item user to order
    * @param direction One of {@link IXmlServerSLWrapper#ORDER_ASCENDING} or {@link IXmlServerSLWrapper#ORDER_DESCENDING}
    * @param start The first item index (starts at zero)
    * @param limit The maximum number of items to return
    */
   public com.amalto.core.objects.transformers.v2.util.TransformerContext extractUsingTransformerThroughView( com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK,com.amalto.core.objects.transformers.v2.ejb.TransformerV2POJOPK transformerPOJOPK,com.amalto.core.objects.view.ejb.ViewPOJOPK viewPOJOPK,com.amalto.xmlserver.interfaces.IWhereItem whereItem,int spellThreshold,java.lang.String orderBy,java.lang.String direction,int start,int limit )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   public java.util.ArrayList runQuery( java.lang.String revisionID,com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK,java.lang.String query,java.lang.String[] parameters )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Returns a map with keys being the concepts found in the Data Cluster and as value the revisionID
    * @param dataClusterPOJOPK
    * @param universe
    * @return A {@link TreeMap} of concept names to revision IDs
    * @throws XtentisException
    */
   public java.util.TreeMap getConceptsInDataCluster( com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Returns a map with keys being the concepts found in the Data Cluster and as value the revisionID
    * @param dataClusterPOJOPK
    * @param universe
    * @return A {@link TreeMap} of concept names to revision IDs
    * @throws XtentisException
    */
   public java.util.TreeMap getConceptsInDataCluster( com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK,com.amalto.core.objects.universe.ejb.UniversePOJO universe )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   public long countItemsByCustomFKFilters( com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK,java.lang.String conceptName,java.lang.String injectedXpath )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   public java.util.ArrayList getItemsByCustomFKFilters( com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK,java.lang.String conceptName,java.util.ArrayList viewablePaths,java.lang.String injectedXpath,int start,int limit,java.lang.String orderbyPath,java.lang.String direction )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Get unordered items of a Concept using an optional where condition
    * @param dataClusterPOJOPK The Data Cluster where to run the query
    * @param conceptName The name of the concept
    * @param whereItem The condition
    * @param spellThreshold The condition spell checking threshold. A negative value de-activates spell
    * @param start The first item index (starts at zero)
    * @param limit The maximum number of items to return
    * @return The ordered list of results
    * @throws XtentisException    */
   public java.util.ArrayList getItems( com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK,java.lang.String conceptName,com.amalto.xmlserver.interfaces.IWhereItem whereItem,int spellThreshold,int start,int limit,boolean totalCountOnFirstRow )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

   /**
    * Get potentially ordered items of a Concept using an optional where condition
    * @param dataClusterPOJOPK The Data Cluster where to run the query
    * @param conceptName The name of the concept
    * @param whereItem The condition
    * @param spellThreshold The condition spell checking threshold. A negative value de-activates spell
    * @param orderBy The full path of the item user to order
    * @param direction One of {@link IXmlServerSLWrapper#ORDER_ASCENDING} or {@link IXmlServerSLWrapper#ORDER_DESCENDING}
    * @param start The first item index (starts at zero)
    * @param limit The maximum number of items to return
    * @return The ordered list of results
    * @throws XtentisException    */
   public java.util.ArrayList getItems( com.amalto.core.objects.datacluster.ejb.DataClusterPOJOPK dataClusterPOJOPK,java.lang.String conceptName,com.amalto.xmlserver.interfaces.IWhereItem whereItem,int spellThreshold,java.lang.String orderBy,java.lang.String direction,int start,int limit,boolean totalCountOnFirstRow )
      throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;

    /**
     *
     * @param dataClusterPK
     * @param concept
     * @param ids
     * @return
     */
   public FKIntegrityCheckResult checkFKIntegrity(String dataClusterPK, String dataModel, String concept, String[] ids) throws com.amalto.core.util.XtentisException, java.rmi.RemoteException;
}
