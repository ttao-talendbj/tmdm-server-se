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
package org.talend.mdm.webapp.browserecords.client.widget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.talend.mdm.webapp.base.client.SessionAwareAsyncCallback;
import org.talend.mdm.webapp.base.client.model.ItemBaseModel;
import org.talend.mdm.webapp.base.client.model.MultipleCriteria;
import org.talend.mdm.webapp.base.client.model.SimpleCriterion;
import org.talend.mdm.webapp.base.client.util.CriteriaUtil;
import org.talend.mdm.webapp.browserecords.client.BrowseRecords;
import org.talend.mdm.webapp.browserecords.client.BrowseRecordsEvents;
import org.talend.mdm.webapp.browserecords.client.BrowseRecordsServiceAsync;
import org.talend.mdm.webapp.browserecords.client.creator.ItemCreator;
import org.talend.mdm.webapp.browserecords.client.i18n.MessagesFactory;
import org.talend.mdm.webapp.browserecords.client.model.BreadCrumbModel;
import org.talend.mdm.webapp.browserecords.client.model.ItemBean;
import org.talend.mdm.webapp.browserecords.client.model.QueryModel;
import org.talend.mdm.webapp.browserecords.client.resources.icon.Icons;
import org.talend.mdm.webapp.browserecords.client.util.CommonUtil;
import org.talend.mdm.webapp.browserecords.client.util.Locale;
import org.talend.mdm.webapp.browserecords.client.util.UserSession;
import org.talend.mdm.webapp.browserecords.client.util.ViewUtil;
import org.talend.mdm.webapp.browserecords.client.widget.ForeignKey.FKRelRecordWindow;
import org.talend.mdm.webapp.browserecords.client.widget.SearchPanel.AdvancedSearchPanel;
import org.talend.mdm.webapp.browserecords.client.widget.SearchPanel.SimpleCriterionPanel;
import org.talend.mdm.webapp.browserecords.client.widget.inputfield.ComboBoxField;
import org.talend.mdm.webapp.browserecords.client.widget.integrity.ContainerUpdate;
import org.talend.mdm.webapp.browserecords.client.widget.integrity.DeleteAction;
import org.talend.mdm.webapp.browserecords.client.widget.integrity.DeleteCallback;
import org.talend.mdm.webapp.browserecords.client.widget.integrity.ListRefresh;
import org.talend.mdm.webapp.browserecords.client.widget.integrity.LogicalDeleteAction;
import org.talend.mdm.webapp.browserecords.client.widget.integrity.NoOpPostDeleteAction;
import org.talend.mdm.webapp.browserecords.client.widget.integrity.PostDeleteAction;
import org.talend.mdm.webapp.browserecords.shared.EntityModel;
import org.talend.mdm.webapp.browserecords.shared.ViewBean;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Image;

public class ItemsToolBar extends ToolBar {

    private final static int PAGE_SIZE = 10;

    private boolean isSimple;

    private static String userCluster = null;

    private SimpleCriterionPanel<?> simplePanel;

    private AdvancedSearchPanel advancedPanel;

    private ComboBoxField<ItemBaseModel> entityCombo = new ComboBoxField<ItemBaseModel>();

    public final Button searchBut = new Button(MessagesFactory.getMessages().search_btn());

    private final ToggleButton advancedBut = new ToggleButton(MessagesFactory.getMessages().advsearch_btn());

    private final Button managebookBtn = new Button();

    private final Button bookmarkBtn = new Button();

    private Button createBtn = new Button(MessagesFactory.getMessages().create_btn());

    private Button deleteMenu = new Button(MessagesFactory.getMessages().delete_btn());

    private Button uploadBtn = new Button(MessagesFactory.getMessages().itemsBrowser_Import_Export());

    private BrowseRecordsServiceAsync service = (BrowseRecordsServiceAsync) Registry.get(BrowseRecords.BROWSERECORDS_SERVICE);

    private List<ItemBaseModel> userCriteriasList;

    private boolean advancedPanelVisible = false;

    private boolean bookmarkShared = false;

    private String bookmarkName = null;

    private ItemBaseModel currentModel = null;

    private FKRelRecordWindow relWindow = new FKRelRecordWindow();

    /*************************************/

    private static ItemsToolBar instance;

    public static ItemsToolBar getInstance() {
        if (instance == null) {
            instance = new ItemsToolBar();
        }

        return instance;
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        instance = null;
    }

    private ItemsToolBar() {
        // init user saved model
        userCluster = BrowseRecords.getSession().getAppHeader().getDatacluster();
        this.setBorders(false);
        this.setId("ItemsToolBar"); //$NON-NLS-1$
        this.setLayout(new ToolBarLayoutEx());
        initToolBar();
        relWindow.setHeading(MessagesFactory.getMessages().fk_RelatedRecord());
    }

    public void setQueryModel(QueryModel qm) {
        qm.setDataClusterPK(userCluster);
        qm.setView(BrowseRecords.getSession().getCurrentView());
        qm.setModel(BrowseRecords.getSession().getCurrentEntityModel());
        UserSession userSession = BrowseRecords.getSession();
        EntityModel entityModel = (EntityModel) userSession.get(UserSession.CURRENT_ENTITY_MODEL);
        if (isSimple || advancedPanel == null) {
            SimpleCriterion simpCriterion = simplePanel.getCriteria();
            MultipleCriteria criteriaStore = (MultipleCriteria) BrowseRecords.getSession().get(
                    UserSession.CUSTOMIZE_CRITERION_STORE);
            if (criteriaStore == null) {
                criteriaStore = new MultipleCriteria();
                criteriaStore.setOperator("AND"); //$NON-NLS-1$
            } else {
                BrowseRecords.getSession().getCustomizeCriterionStore().getChildren().clear();
            }
            criteriaStore.add(simpCriterion);
            BrowseRecords.getSession().put(UserSession.CUSTOMIZE_CRITERION_STORE, criteriaStore);
            qm.setCriteria(simplePanel.getCriteria().toString());
            if (!CommonUtil.validateSearchValue(entityModel.getMetaDataTypes(),simpCriterion.getValue())){
                qm.setErrorValue(simpCriterion.getValue());
            }
        } else {
            if (!advancedPanel.isVisible()) {
                advancedPanel.setVisible(true);
                advancedBut.toggle(true);
                advancedPanelVisible = true;
                ItemsSearchContainer.getInstance().resizeTop(30 + advancedPanel.getOffsetHeight());
            }
            qm.setCriteria(advancedPanel.getCriteria());
            MultipleCriteria multipleCriteria = (MultipleCriteria) BrowseRecords.getSession().get(UserSession.CUSTOMIZE_CRITERION_STORE);
            List<SimpleCriterion> simpleCriterions = CriteriaUtil.getSimpleCriterions(multipleCriteria);
            for (SimpleCriterion criteria : simpleCriterions){
                if (!CommonUtil.validateSearchValue(entityModel.getMetaDataTypes(),criteria.getValue())){
                    qm.setErrorValue(criteria.getValue());
                    break;
                }             
            }            
        }
    }

    public void updateToolBar(ViewBean viewBean) {
        simplePanel.updateFields(viewBean);
        if (advancedPanel != null) {
            advancedPanel.setView(viewBean);
            advancedPanel.cleanCriteria();
        }
        // reset search results

        ItemsListPanel.getInstance().resetGrid();
        ItemsMainTabPanel.getInstance().removeAll();

        searchBut.setEnabled(true);
        advancedBut.setEnabled(true);
        managebookBtn.setEnabled(true);
        bookmarkBtn.setEnabled(true);

        createBtn.setEnabled(false);
        uploadBtn.setEnabled(true);
        uploadBtn.getMenu().getItemByItemId("importRecords").setEnabled(false); //$NON-NLS-1$
        deleteMenu.setEnabled(false);
        String concept = ViewUtil.getConceptFromBrowseItemView(entityCombo.getValue().get("value").toString());//$NON-NLS-1$
        if (!viewBean.getBindingEntityModel().getMetaDataTypes().get(concept).isDenyCreatable()) {
            createBtn.setEnabled(true);
            uploadBtn.getMenu().getItemByItemId("importRecords").setEnabled(true); //$NON-NLS-1$
        }
        boolean denyLogicalDelete = viewBean.getBindingEntityModel().getMetaDataTypes().get(concept).isDenyLogicalDeletable();
        boolean denyPhysicalDelete = viewBean.getBindingEntityModel().getMetaDataTypes().get(concept).isDenyPhysicalDeleteable();

        if (denyLogicalDelete && denyPhysicalDelete)
            deleteMenu.setEnabled(false);
        else {
            deleteMenu.setEnabled(true);
            if (denyPhysicalDelete)
                deleteMenu.getMenu().getItemByItemId("physicalDelMenuInGrid").setEnabled(false); //$NON-NLS-1$
            else
                deleteMenu.getMenu().getItemByItemId("physicalDelMenuInGrid").setEnabled(true); //$NON-NLS-1$
            if (denyLogicalDelete)
                deleteMenu.getMenu().getItemByItemId("logicalDelMenuInGrid").setEnabled(false); //$NON-NLS-1$
            else
                deleteMenu.getMenu().getItemByItemId("logicalDelMenuInGrid").setEnabled(true); //$NON-NLS-1$
        }

        updateUserCriteriasList();
        this.layout(true);
    }

    public int getSelectItemNumber() {
        int number = 0;
        number = ItemsListPanel.getInstance().getGrid().getSelectionModel().getSelectedItems().size();
        return number;
    }

    private void initToolBar() {
        createBtn.setIcon(AbstractImagePrototype.create(Icons.INSTANCE.Create()));
        createBtn.setEnabled(false);
        createBtn.setId("BrowseRecords_Create"); //$NON-NLS-1$
        add(createBtn);
        createBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                ItemsMainTabPanel.getInstance().removeAll();
                ItemsListPanel.getInstance().setCreate(true);
                String concept = ViewUtil.getConceptFromBrowseItemView(entityCombo.getValue().get("value").toString());//$NON-NLS-1$

                EntityModel entityModel = (EntityModel) BrowseRecords.getSession().getCurrentEntityModel();
                ItemBean itemBean = ItemCreator.createDefaultItemBean(concept, entityModel);

                if (ItemsListPanel.getInstance().getGrid() != null)
                    ItemsListPanel.getInstance().getGrid().getSelectionModel().deselectAll();

                ItemsDetailPanel panel = new ItemsDetailPanel();
                List<String> pkInfoList = new ArrayList<String>();
                pkInfoList.add(itemBean.getLabel());
                panel.initBanner(pkInfoList, itemBean.getDescription());
                List<BreadCrumbModel> breads = new ArrayList<BreadCrumbModel>();
                if (itemBean != null) {
                    breads.add(new BreadCrumbModel("", BreadCrumb.DEFAULTNAME, null, null, false)); //$NON-NLS-1$
                    breads.add(new BreadCrumbModel("", itemBean.getLabel(), null, null, true)); //$NON-NLS-1$
                }
                panel.initBreadCrumb(new BreadCrumb(breads, panel));
                ViewBean viewBean = (ViewBean) BrowseRecords.getSession().get(UserSession.CURRENT_VIEW);
                ItemPanel itemPanel = new ItemPanel(panel);
                panel.addTabItem(itemBean.getLabel(), itemPanel,
                        ItemsDetailPanel.SINGLETON, itemBean.getConcept());
                itemPanel.initTreeDetail(viewBean, itemBean, ItemDetailToolBar.CREATE_OPERATION);
                ItemsMainTabPanel.getInstance().addMainTabItem(itemBean.getLabel(), panel, itemBean.getConcept());

                BrowseRecords.getSession().put(UserSession.CURRENT_CREATED_ENTITY, itemPanel);
            }

        });

        Menu sub = new Menu();
        MenuItem delMenu = new MenuItem(MessagesFactory.getMessages().delete_btn());
        delMenu.setId("physicalDelMenuInGrid");//$NON-NLS-1$
        delMenu.setIcon(AbstractImagePrototype.create(Icons.INSTANCE.Delete()));

        // TODO duplicate with recordToolbar
        delMenu.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {
                if (ItemsListPanel.getInstance().getGrid() == null) {
                    MessageBox.alert(MessagesFactory.getMessages().info_title(), MessagesFactory.getMessages()
                            .select_delete_item_record(), null);
                } else {
                    if (getSelectItemNumber() == 0) {
                        MessageBox.alert(MessagesFactory.getMessages().info_title(), MessagesFactory.getMessages()
                                .select_delete_item_record(), null);
                    } else {
                        MessageBox.confirm(MessagesFactory.getMessages().confirm_title(), MessagesFactory.getMessages()
                                .delete_confirm(), new DeleteItemsBoxListener(service));
                    }
                }
            }
        });

        MenuItem trashMenu = new MenuItem(MessagesFactory.getMessages().trash_btn());
        trashMenu.setId("logicalDelMenuInGrid");//$NON-NLS-1$
        trashMenu.setIcon(AbstractImagePrototype.create(Icons.INSTANCE.Send_to_trash()));
        trashMenu.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {
                
                final ItemsListPanel list = ItemsListPanel.getInstance();
                if (list.getGrid() != null) {
                    PostDeleteAction postDeleteAction = new ListRefresh(new ContainerUpdate(
                            NoOpPostDeleteAction.INSTANCE));
                    DeleteAction deleteAction = new LogicalDeleteAction("/"); //$NON-NLS-1$
                    service.checkFKIntegrity(list.getGrid().getSelectionModel().getSelectedItems(),
                            new DeleteCallback(deleteAction, postDeleteAction, service));
                }
            }
        });

        sub.add(trashMenu);
        sub.add(delMenu);

        deleteMenu.setMenu(sub);
        deleteMenu.setEnabled(false);
        deleteMenu.setId("BrowseRecords_Delete"); //$NON-NLS-1$
        deleteMenu.setIcon(AbstractImagePrototype.create(Icons.INSTANCE.Delete()));
        add(deleteMenu);

        uploadBtn.setIcon(AbstractImagePrototype.create(Icons.INSTANCE.Save()));
        Menu uploadMenu = new Menu();
        MenuItem importMenu = new MenuItem(MessagesFactory.getMessages().import_btn());
        importMenu.setId("importRecords"); //$NON-NLS-1$
        importMenu.setIcon(AbstractImagePrototype.create(Icons.INSTANCE.Save()));
        uploadMenu.add(importMenu);

        importMenu.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {
                final Window window = new Window();
                window.setSize(500, 260);
                window.setPlain(true);
                window.setModal(true);
                window.setBlinkModal(true);
                window.setHeading(MessagesFactory.getMessages().upload_title());
                window.setLayout(new FitLayout());
                window.setClosable(true);

                ViewBean viewBean = BrowseRecords.getSession().getCurrentView();
                UploadFileFormPanel formPanel = new UploadFileFormPanel(viewBean, window);
                window.add(formPanel);
                window.show();
            }
        });

        MenuItem exportMenu = new MenuItem(MessagesFactory.getMessages().export_btn());
        exportMenu.setIcon(AbstractImagePrototype.create(Icons.INSTANCE.Save()));
        uploadMenu.add(exportMenu);

        exportMenu.addSelectionListener(new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {
                QueryModel queryModel = ItemsListPanel.getInstance().getCurrentQueryModel();
                if (queryModel != null) {
                    final Window window = new Window();
                    window.setSize(380, 200);
                    window.setPlain(true);
                    window.setModal(true);
                    window.setBlinkModal(true);
                    window.setHeading(MessagesFactory.getMessages().export_title());
                    window.setLayout(new FitLayout());
                    window.setClosable(true);
                    window.add(new DownloadFilePanel(queryModel, window));
                    window.show();                    
                }
            }
        });

        uploadBtn.setId("BrowseRecords_UploadMenuInGrid"); //$NON-NLS-1$
        uploadBtn.setMenu(uploadMenu);

        uploadBtn.setEnabled(false);
        add(uploadBtn);

        add(new FillToolItem());

        // add entity combo
        RpcProxy<List<ItemBaseModel>> Entityproxy = new RpcProxy<List<ItemBaseModel>>() {

            @Override
            public void load(Object loadConfig, final AsyncCallback<List<ItemBaseModel>> callback) {
                service.getViewsList(Locale.getLanguage(), new SessionAwareAsyncCallback<List<ItemBaseModel>>() {

                    @Override
                    protected void doOnFailure(Throwable caught) {
                        super.doOnFailure(caught);
                        callback.onFailure(caught);
                    }

                    public void onSuccess(List<ItemBaseModel> result) {
                        callback.onSuccess(result);
                        entityCombo.setMode("local"); //$NON-NLS-1$
                        entityCombo.setMinChars(0);
                    }
                });
            }
        };

        if (BrowseRecords.getSession().getEntitiyModelList() == null) {
            service.getViewsList(Locale.getLanguage(), new SessionAwareAsyncCallback<List<ItemBaseModel>>() {

                public void onSuccess(List<ItemBaseModel> modelList) {
                    BrowseRecords.getSession().put(UserSession.ENTITY_MODEL_LIST, modelList);
                }
            });
        }

        ListLoader<ListLoadResult<ItemBaseModel>> Entityloader = new BaseListLoader<ListLoadResult<ItemBaseModel>>(Entityproxy);

        // HorizontalPanel entityPanel = new HorizontalPanel();
        final ListStore<ItemBaseModel> list = new ListStore<ItemBaseModel>(Entityloader);

        entityCombo.setAutoWidth(true);
        entityCombo.setEmptyText(MessagesFactory.getMessages().empty_entity());
        entityCombo.setLoadingText(MessagesFactory.getMessages().loading());
        entityCombo.setStore(list);
        entityCombo.setDisplayField("name");//$NON-NLS-1$
        entityCombo.setValueField("value");//$NON-NLS-1$
        entityCombo.setForceSelection(true);
        entityCombo.setTriggerAction(TriggerAction.ALL);
        entityCombo.setId("BrowseRecords_EntityComboBox");//$NON-NLS-1$
        entityCombo.setStyleAttribute("padding-right", "17px"); //$NON-NLS-1$ //$NON-NLS-2$

        entityCombo.addSelectionChangedListener(new SelectionChangedListener<ItemBaseModel>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<ItemBaseModel> se) {
                String viewPk = se.getSelectedItem().get("value").toString();//$NON-NLS-1$
                Dispatcher.forwardEvent(BrowseRecordsEvents.GetView, viewPk);              
            }
        });        
        add(entityCombo);
        simplePanel = new SimpleCriterionPanel(null, null, searchBut);
        add(simplePanel);

        // add simple search button
        searchBut.setEnabled(false);
        searchBut.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (simplePanel.getCriteria() != null) {
                    isSimple = true;
                    String viewPk = entityCombo.getValue().get("value");//$NON-NLS-1$
                    Dispatcher.forwardEvent(BrowseRecordsEvents.SearchView, viewPk);
                    resizeAfterSearch();
                } else {
                    MessageBox.alert(MessagesFactory.getMessages().error_title(), MessagesFactory.getMessages()
                            .advsearch_lessinfo(), new Listener<MessageBoxEvent>() {

                        public void handleEvent(MessageBoxEvent be) {
                            simplePanel.focusField();
                        }
                    });
                }
            }
        });
        searchBut.setId("BrowseRecords_Search"); //$NON-NLS-1$
        add(searchBut);

        add(new SeparatorToolItem());

        // add advanced search button
        advancedBut.setEnabled(false);
        advancedBut.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                // show advanced Search panel
                advancedPanelVisible = !advancedPanelVisible;
                advancedPanel.setVisible(advancedPanelVisible);
                if (advancedPanelVisible)
                    ItemsSearchContainer.getInstance().resizeTop(30 + advancedPanel.getOffsetHeight());
                else
                    ItemsSearchContainer.getInstance().resizeTop(30);
                advancedPanel.getButtonBar().getItemByItemId("updateBookmarkBtn").setVisible(false); //$NON-NLS-1$

                if (ItemsListPanel.getInstance().gridContainer != null)
                    ItemsListPanel.getInstance().gridContainer.setHeight(ItemsToolBar.this.getParent().getOffsetHeight()
                            - ItemsToolBar.this.getOffsetHeight() - advancedPanel.getOffsetHeight());
                if (isSimple) {
                    MultipleCriteria criteriaStore = (MultipleCriteria) BrowseRecords.getSession().get(
                            UserSession.CUSTOMIZE_CRITERION_STORE);
                    criteriaStore.requestShowAppearance();
                    advancedPanel.setCriteria(criteriaStore.toString());
                    //  advancedPanel.setCriteria("((" + simplePanel.getCriteria().toString() + "))"); //$NON-NLS-1$ //$NON-NLS-2$
                    //                    advancedPanel.setCriteriaAppearance("((" + simplePanel.getCriteria().toString() + "))", "((" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    //                            + simplePanel.getCriteria().toAppearanceString() + "))"); //$NON-NLS-1$
                }

            }

        });
        advancedBut.setId("BrowseRecords_AdvancedSearch"); //$NON-NLS-1$
        add(advancedBut);

        add(new SeparatorToolItem());

        // add bookmark management button
        managebookBtn.setIcon(AbstractImagePrototype.create(Icons.INSTANCE.Display()));
        managebookBtn.setTitle(MessagesFactory.getMessages().bookmarkmanagement_heading());
        managebookBtn.setEnabled(false);
        managebookBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                final Window winBookmark = new Window();
                winBookmark.setHeading(MessagesFactory.getMessages().bookmarkmanagement_heading());
                winBookmark.setAutoHeight(true);
                // winBookmark.setAutoWidth(true);
                winBookmark.setWidth(413);
                winBookmark.setModal(true);
                FormPanel content = new FormPanel();
                FormData formData = new FormData("-10");//$NON-NLS-1$
                content.setFrame(false);
                content.setLayout(new FitLayout());
                content.setBodyBorder(false);
                content.setHeaderVisible(false);
                content.setSize(400, 350);
                winBookmark.add(content);

                // display bookmark grid
                RpcProxy<PagingLoadResult<ItemBaseModel>> proxyBookmark = new RpcProxy<PagingLoadResult<ItemBaseModel>>() {

                    @Override
                    public void load(Object loadConfig, final AsyncCallback<PagingLoadResult<ItemBaseModel>> callback) {
                        service.querySearchTemplates(entityCombo.getValue().get("value").toString(), true, //$NON-NLS-1$
                                (PagingLoadConfig) loadConfig, new SessionAwareAsyncCallback<PagingLoadResult<ItemBaseModel>>() {

                                    @Override
                                    protected void doOnFailure(Throwable caught) {
                                        super.doOnFailure(caught);
                                        callback.onFailure(caught);
                                    }

                                    public void onSuccess(PagingLoadResult<ItemBaseModel> result) {
                                        callback.onSuccess(result);
                                    }
                                });
                    }
                };

                // loader
                final PagingLoader<PagingLoadResult<ItemBaseModel>> loaderBookmark = new BasePagingLoader<PagingLoadResult<ItemBaseModel>>(
                        proxyBookmark);

                ListStore<ItemBaseModel> store = new ListStore<ItemBaseModel>(loaderBookmark);
                store.setDefaultSort("name", SortDir.ASC); //$NON-NLS-1$

                final PagingToolBar pagetoolBar = new PagingToolBar(PAGE_SIZE);
                pagetoolBar.bind(loaderBookmark);

                List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
                columns.add(new ColumnConfig("name", MessagesFactory.getMessages().bookmark_heading(), 200)); //$NON-NLS-1$
                ColumnConfig colEdit = new ColumnConfig("value", MessagesFactory.getMessages().bookmark_edit(), 100); //$NON-NLS-1$
                colEdit.setRenderer(new GridCellRenderer<ItemBaseModel>() {

                    public Object render(final ItemBaseModel model, String property, ColumnData config, int rowIndex,
                            int colIndex, ListStore<ItemBaseModel> store, Grid<ItemBaseModel> grid) {
                        Image image = new Image();
                        image.setResource(Icons.INSTANCE.Edit());
                        if (!ifManage(model))
                            image.addStyleName("x-item-disabled");//$NON-NLS-1$
                        else
                            image.addClickHandler(new ClickHandler() {

                                public void onClick(ClickEvent event) {
                                    // edit the bookmark
                                    if (advancedPanel == null) {
                                        advancedPanel = new AdvancedSearchPanel(simplePanel.getView(), null);
                                    }
                                    service.getCriteriaByBookmark(
                                            model.get("value").toString(), new SessionAwareAsyncCallback<String>() { //$NON-NLS-1$

                                                public void onSuccess(String arg0) {
                                                    // set criteria
                                                    if (arg0 != null) {
                                                        advancedPanel.setCriteria(arg0);
                                                        // advancedPanel.setCriteriaAppearance(arg0, arg0);
                                                    } else
                                                        advancedPanel.cleanCriteria();
                                                    advancedPanelVisible = true;
                                                    advancedPanel.setVisible(advancedPanelVisible);
                                                    ItemsSearchContainer.getInstance().resizeTop(
                                                            30 + advancedPanel.getOffsetHeight());
                                                    advancedPanel.getButtonBar().getItemByItemId("updateBookmarkBtn") //$NON-NLS-1$
                                                            .setVisible(true);
                                                    bookmarkName = model.get("value").toString(); //$NON-NLS-1$
                                                    bookmarkShared = Boolean.parseBoolean(model.get("shared").toString()); //$NON-NLS-1$
                                                    if (ItemsListPanel.getInstance().gridContainer != null)
                                                        ItemsListPanel.getInstance().gridContainer.setHeight(ItemsToolBar.this
                                                                .getParent().getOffsetHeight()
                                                                - ItemsToolBar.this.getOffsetHeight()
                                                                - advancedPanel.getOffsetHeight());
                                                    winBookmark.close();
                                                }

                                            });
                                }

                            });
                        return image;
                    }

                });
                columns.add(colEdit);

                ColumnConfig colDel = new ColumnConfig("value", MessagesFactory.getMessages().bookmark_del(), 100); //$NON-NLS-1$
                colDel.setRenderer(new GridCellRenderer<ItemBaseModel>() {

                    public Object render(final ItemBaseModel model, String property, ColumnData config, int rowIndex,
                            int colIndex, ListStore<ItemBaseModel> store, Grid<ItemBaseModel> grid) {
                        Image image = new Image();
                        image.setResource(Icons.INSTANCE.remove());
                        if (!ifManage(model))
                            image.addStyleName("x-item-disabled"); //$NON-NLS-1$
                        else
                            image.addClickHandler(new ClickHandler() {

                                public void onClick(ClickEvent event) {
                                    MessageBox.confirm(MessagesFactory.getMessages().confirm_title(), MessagesFactory
                                            .getMessages().bookmark_DelMsg(), new Listener<MessageBoxEvent>() {

                                        public void handleEvent(MessageBoxEvent be) {
                                            if (be.getButtonClicked().getItemId().equals(Dialog.YES)) {
                                                // delete the bookmark
                                                service.deleteSearchTemplate(model.get("value").toString(), //$NON-NLS-1$
                                                        new SessionAwareAsyncCallback<Void>() {

                                                            public void onSuccess(Void arg0) {
                                                                loaderBookmark.load();
                                                            }

                                                        });
                                            }
                                        }
                                    });
                                }

                            });
                        return image;
                    }

                });

                columns.add(colDel);

                ColumnConfig colSearch = new ColumnConfig("value", MessagesFactory.getMessages().bookmark_search(), 100); //$NON-NLS-1$
                colSearch.setRenderer(new GridCellRenderer<ItemBaseModel>() {

                    public Object render(final ItemBaseModel model, String property, ColumnData config, int rowIndex,
                            int colIndex, ListStore<ItemBaseModel> store, Grid<ItemBaseModel> grid) {
                        Image image = new Image();
                        image.setResource(Icons.INSTANCE.dosearch());
                        image.addClickHandler(new ClickHandler() {

                            public void onClick(ClickEvent event) {
                                doSearch(model, winBookmark);
                            }

                        });
                        return image;
                    }

                });
                columns.add(colSearch);

                ColumnModel cm = new ColumnModel(columns);

                final Grid<ItemBaseModel> bookmarkgrid = new Grid<ItemBaseModel>(store, cm);
                if (cm.getColumnCount() > 0) {
                    bookmarkgrid.setAutoExpandColumn(cm.getColumn(0).getHeader());
                }
                bookmarkgrid.getView().setForceFit(true);
                bookmarkgrid.addListener(Events.Attach, new Listener<GridEvent<ItemBaseModel>>() {

                    public void handleEvent(GridEvent<ItemBaseModel> be) {
                        PagingLoadConfig config = new BasePagingLoadConfig();
                        config.setOffset(0);
                        config.setLimit(PAGE_SIZE);
                        loaderBookmark.load(config);
                    }
                });

                bookmarkgrid.addListener(Events.OnDoubleClick, new Listener<GridEvent<ItemBaseModel>>() {

                    public void handleEvent(final GridEvent<ItemBaseModel> be) {
                        doSearch(be.getModel(), winBookmark);
                    }
                });

                bookmarkgrid.setLoadMask(true);
                bookmarkgrid.setBorders(false);

                content.setBottomComponent(pagetoolBar);
                content.add(bookmarkgrid, formData);

                winBookmark.show();
            }

        });
        managebookBtn.setId("BrowseRecords_ManageBookMark"); //$NON-NLS-1$
        add(managebookBtn);

        // add bookmark save button
        bookmarkBtn.setIcon(AbstractImagePrototype.create(Icons.INSTANCE.Save()));
        bookmarkBtn.setTitle(MessagesFactory.getMessages().advsearch_bookmark());
        bookmarkBtn.setEnabled(false);
        bookmarkBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                showBookmarkSavedWin(true);
            }

        });
        bookmarkBtn.setId("BrowseRecords_BookMark"); //$NON-NLS-1$
        add(bookmarkBtn);

        initAdvancedPanel();
    }

    private void updateUserCriteriasList() {
        service.getUserCriterias(entityCombo.getValue().get("value").toString(), //$NON-NLS-1$
                new SessionAwareAsyncCallback<List<ItemBaseModel>>() {

                    public void onSuccess(List<ItemBaseModel> list) {
                        userCriteriasList = list;
                    }

                });
    }

    private boolean ifManage(ItemBaseModel model) {
        // only the shared bookmark could be managed
        Iterator<ItemBaseModel> i = userCriteriasList.iterator();
        while (i.hasNext()) {
            if ((i.next()).get("value").equals( //$NON-NLS-1$
                    model.get("value").toString())) { //$NON-NLS-1$    
                return true;
            }
        }

        return false;
    }

    private void doSearch(final ItemBaseModel model, final Window winBookmark) {
        service.getCriteriaByBookmark(model.get("value").toString(), //$NON-NLS-1$
                new SessionAwareAsyncCallback<String>() {

                    public void onSuccess(String arg0) {
                        isSimple = false;
                        if (advancedPanel == null) {
                            advancedPanel = new AdvancedSearchPanel(simplePanel.getView(), null);
                        }

                        MultipleCriteria mutilCriteria = new MultipleCriteria();
                        if (CommonUtil.isSimpleCriteria(arg0)) {
                            SimpleCriterion criteria = buildCriterion(arg0);
                            mutilCriteria.add(criteria);
                        } else if (arg0.indexOf(" AND ") != -1) { //$NON-NLS-1$
                            mutilCriteria.setOperator("AND"); //$NON-NLS-1$
                            String[] xquery = arg0.split("AND"); //$NON-NLS-1$
                            for (int i = 0; i < xquery.length && xquery[i].indexOf(AdvancedSearchPanel.modifiedON) == -1; i++) {
                                mutilCriteria.add(buildCriterion(xquery[i]));
                            }
                        } else if (arg0.indexOf(" OR ") != -1) { //$NON-NLS-1$
                            mutilCriteria.setOperator("OR"); //$NON-NLS-1$
                            String[] xquery = arg0.split("OR"); //$NON-NLS-1$
                            for (int i = 0; i < xquery.length && xquery[i].indexOf(AdvancedSearchPanel.modifiedON) == -1; i++) {
                                mutilCriteria.add(buildCriterion(xquery[i]));
                            }
                        }

                        BrowseRecords.getSession().put(UserSession.CUSTOMIZE_CRITERION_STORE, mutilCriteria);
                        advancedPanel.setCriteria(arg0);
                        ItemsSearchContainer.getInstance().resizeTop(30 + advancedPanel.getOffsetHeight());
                        String viewPk = entityCombo.getValue().get("value"); //$NON-NLS-1$
                        Dispatcher.forwardEvent(BrowseRecordsEvents.SearchView, viewPk);
                        winBookmark.close();
                    }

                });

    }

    private SimpleCriterion buildCriterion(String xquery) {
        String[] query = xquery.trim().split(" "); //$NON-NLS-1$
        SimpleCriterion criteria = null;
        if (query.length == 3) {
            criteria = new SimpleCriterion(query[0].replace("(", ""), query[1], query[2].replace(")", "")); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        }

        return criteria;
    }

    public FormPanel getAdvancedPanel() {
        return advancedPanel;
    }

    private void resizeAfterSearch() {
        advancedPanelVisible = false;
        advancedPanel.setVisible(advancedPanelVisible);
        ItemsSearchContainer.getInstance().resizeTop(30);
        advancedBut.toggle(advancedPanelVisible);
        // resize result grid
        if (ItemsListPanel.getInstance().gridContainer != null)
            ItemsListPanel.getInstance().gridContainer.setHeight(ItemsToolBar.this.getParent().getOffsetHeight()
                    - ItemsToolBar.this.getOffsetHeight() - advancedPanel.getOffsetHeight());
    }

    private void initAdvancedPanel() {
        if (advancedPanel == null) {
            Button searchBtn = new Button(MessagesFactory.getMessages().search_btn());
            advancedPanel = new AdvancedSearchPanel(simplePanel.getView(), searchBtn);
            advancedPanel.setItemId("advancedPanel"); //$NON-NLS-1$
            advancedPanel.setButtonAlign(HorizontalAlignment.CENTER);

            searchBtn.setItemId("searchBtn"); //$NON-NLS-1$
            searchBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    if (advancedPanel.getCriteria() == null || advancedPanel.getCriteria().equals("")) //$NON-NLS-1$
                        MessageBox.alert(MessagesFactory.getMessages().warning_title(), MessagesFactory.getMessages()
                                .search_expression_notempty(), null);
                    else {
                        isSimple = false;
                        String viewPk = entityCombo.getValue().get("value"); //$NON-NLS-1$
                        Dispatcher.forwardEvent(BrowseRecordsEvents.SearchView, viewPk);
                        resizeAfterSearch();
                    }
                }

            });
            advancedPanel.addButton(searchBtn);

            Button advancedBookmarkBtn = new Button(MessagesFactory.getMessages().advsearch_bookmark());
            advancedBookmarkBtn.setItemId("advancedBookmarkBtn"); //$NON-NLS-1$
            advancedBookmarkBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    showBookmarkSavedWin(false);
                }

            });
            advancedPanel.addButton(advancedBookmarkBtn);

            Button updateBookmarkBtn = new Button(MessagesFactory.getMessages().bookmark_update());
            updateBookmarkBtn.setItemId("updateBookmarkBtn"); //$NON-NLS-1$
            updateBookmarkBtn.setVisible(false);
            updateBookmarkBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    saveBookmark(bookmarkName, bookmarkShared, advancedPanel.getCriteria(), null);
                }

            });
            advancedPanel.addButton(updateBookmarkBtn);

            Button cancelBtn = new Button(MessagesFactory.getMessages().button_reset());
            cancelBtn.setItemId("cancelBtn"); //$NON-NLS-1$
            cancelBtn.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    advancedPanel.cleanCriteria();
                    advancedPanel.layout(true);
                    ItemsSearchContainer.getInstance().resizeTop(30 + advancedPanel.getOffsetHeight());

                    if (ItemsListPanel.getInstance().gridContainer != null)
                        ItemsListPanel.getInstance().gridContainer.setHeight(ItemsToolBar.this.getParent().getOffsetHeight()
                                - ItemsToolBar.this.getOffsetHeight() - advancedPanel.getOffsetHeight());
                }

            });
            advancedPanel.addButton(cancelBtn);
            advancedPanel.setVisible(false);
        }
    }

    private void showBookmarkSavedWin(final boolean ifSimple) {
        final Window winBookmark = new Window();
        winBookmark.setHeading(MessagesFactory.getMessages().bookmark_heading());
        // winBookmark.setAutoHeight(true);
        // winBookmark.setAutoWidth(true);
        winBookmark.setWidth(355);
        winBookmark.setHeight(191);
        FormPanel content = new FormPanel();
        content.setFrame(false);
        content.setBodyBorder(false);
        content.setHeaderVisible(false);
        content.setButtonAlign(HorizontalAlignment.CENTER);
        content.setLabelWidth(100);
        content.setFieldWidth(200);
        final CheckBox cb = new CheckBox();
        cb.setFieldLabel(MessagesFactory.getMessages().bookmark_shared());
        content.add(cb);

        final TextField<String> bookmarkfield = new TextField<String>();
        bookmarkfield.setFieldLabel(MessagesFactory.getMessages().bookmark_name());
        Validator validator = new Validator() {

            public String validate(Field<?> field, String value) {
                if (field == bookmarkfield) {
                    if (bookmarkfield.getValue() == null || bookmarkfield.getValue().trim().equals("")) //$NON-NLS-1$

                        return MessagesFactory.getMessages().required_field();
                }

                return null;
            }
        };
        bookmarkfield.setValidator(validator);
        content.add(bookmarkfield);

        Button btn = new Button(MessagesFactory.getMessages().ok_btn());
        btn.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                service.isExistCriteria(entityCombo.getValue().get("value").toString(), bookmarkfield.getValue(), //$NON-NLS-1$
                        new SessionAwareAsyncCallback<Boolean>() {

                            public void onSuccess(Boolean exists) {
                                if (!exists) {
                                    String curCriteria = null;
                                    if (ifSimple)
                                        curCriteria = simplePanel.getCriteria().toString();
                                    else
                                        curCriteria = advancedPanel.getCriteria();
                                    saveBookmark(bookmarkfield.getValue().toString(), cb.getValue(), curCriteria, winBookmark);
                                } else {
                                    MessageBox.alert(MessagesFactory.getMessages().warning_title(), MessagesFactory.getMessages()
                                            .bookmark_existMsg(), null);
                                }
                            }

                        });
            }
        });
        winBookmark.addButton(btn);

        winBookmark.add(content);
        winBookmark.show();
    }

    private void saveBookmark(String name, boolean shared, String curCriteria, final Window winBookmark) {
        if (curCriteria.contains("&#91;") || curCriteria.contains("&#92;") || curCriteria.contains("&#93;")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            curCriteria = curCriteria.replace("&", "&amp;"); //$NON-NLS-1$//$NON-NLS-2$
        service.saveCriteria(entityCombo.getValue().get("value").toString(), name, shared, curCriteria, //$NON-NLS-1$
                new SessionAwareAsyncCallback<Void>() {

                    @Override
                    protected void doOnFailure(Throwable caught) {
                        MessageBox.alert(MessagesFactory.getMessages().error_title(), MessagesFactory.getMessages()
                                .bookmark_saveFailed(), null);
                    }

                    public void onSuccess(Void arg0) {
                        MessageBox.info(MessagesFactory.getMessages().info_title(), MessagesFactory.getMessages()
                                .bookmark_saveSuccess(), null);
                        updateUserCriteriasList();
                        if (winBookmark != null)
                            winBookmark.close();
                    }

                });
    }

    public ItemBaseModel getCurrentModel() {
        return currentModel;
    }

    public void setCurrentModel(ItemBaseModel currentModel) {
        this.currentModel = currentModel;
    }
    
    public SimpleCriterionPanel<?> getSimplePanel() {
        return simplePanel;
    }
    
    public void setSimplePanel(SimpleCriterionPanel<?> simplePanel) {
        this.simplePanel = simplePanel;
    }

    private class DeleteItemsBoxListener implements Listener<MessageBoxEvent> {

        private final ItemsListPanel list;

        private final BrowseRecordsServiceAsync service;

        private DeleteItemsBoxListener(BrowseRecordsServiceAsync service) {
            this.service = service;
            this.list = ItemsListPanel.getInstance();
        }

        public void handleEvent(MessageBoxEvent be) {
            if (be.getButtonClicked().getItemId().equals(Dialog.YES)) {
                if (list.getGrid() != null) {
                    PostDeleteAction postDeleteAction = new ListRefresh(new ContainerUpdate(NoOpPostDeleteAction.INSTANCE));
                    service.checkFKIntegrity(list.getGrid().getSelectionModel().getSelectedItems(), new DeleteCallback(
                            DeleteAction.PHYSICAL, postDeleteAction, service));
                }

            }
        }
    }
}