// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.mdm.webapp.browserecords.client.widget.treedetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.mdm.webapp.base.client.SessionAwareAsyncCallback;
import org.talend.mdm.webapp.base.client.model.ForeignKeyBean;
import org.talend.mdm.webapp.base.shared.TypeModel;
import org.talend.mdm.webapp.browserecords.client.BrowseRecords;
import org.talend.mdm.webapp.browserecords.client.BrowseRecordsServiceAsync;
import org.talend.mdm.webapp.browserecords.client.i18n.MessagesFactory;
import org.talend.mdm.webapp.browserecords.client.model.ColumnElement;
import org.talend.mdm.webapp.browserecords.client.model.ColumnTreeLayoutModel;
import org.talend.mdm.webapp.browserecords.client.model.ColumnTreeModel;
import org.talend.mdm.webapp.browserecords.client.model.ForeignKeyModel;
import org.talend.mdm.webapp.browserecords.client.model.ItemBean;
import org.talend.mdm.webapp.browserecords.client.model.ItemNodeModel;
import org.talend.mdm.webapp.browserecords.client.util.CommonUtil;
import org.talend.mdm.webapp.browserecords.client.util.Locale;
import org.talend.mdm.webapp.browserecords.client.widget.ItemDetailToolBar;
import org.talend.mdm.webapp.browserecords.client.widget.ItemsDetailPanel;
import org.talend.mdm.webapp.browserecords.shared.ViewBean;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeEventSource;
import com.extjs.gxt.ui.client.data.ChangeListener;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

public class ForeignKeyTreeDetail extends ContentPanel {

    private ViewBean viewBean;

    private ItemDetailToolBar toolBar;

    private ItemNodeModel model;

    private boolean isCreate;

    private ForeignKeyModel fkModel;

    private ColumnTreeLayoutModel columnLayoutModel;

    private Tree tree;

    private TreeItem root;

    private ForeignKeyRender fkRender;

    private Map<String, Field<?>> fieldMap = new HashMap<String, Field<?>>();

    private HashMap<CountMapItem, Integer> occurMap = new HashMap<CountMapItem, Integer>();

    private ClickHandler handler = new ClickHandler() {

        public void onClick(ClickEvent arg0) {
            final DynamicTreeItem selected = (DynamicTreeItem) tree.getSelectedItem();
            final DynamicTreeItem parentItem = (DynamicTreeItem) selected.getParentItem();

            final ItemNodeModel selectedModel = selected.getItemNodeModel();
            final ItemNodeModel parentModel = (ItemNodeModel) selectedModel.getParent();

            final String xpath = selectedModel.getBindingPath();
            final CountMapItem countMapItem = new CountMapItem(xpath, parentItem);
            final int count = occurMap.containsKey(countMapItem) ? occurMap.get(countMapItem) : 0;

            if ("Add".equals(arg0.getRelativeElement().getId()) || "Clone".equals(arg0.getRelativeElement().getId())) { //$NON-NLS-1$ //$NON-NLS-2$               
                if (viewBean.getBindingEntityModel().getMetaDataTypes().get(xpath).getMaxOccurs() < 0
                        || count < viewBean.getBindingEntityModel().getMetaDataTypes().get(xpath).getMaxOccurs()) {
                    // clone a new item
                    ItemNodeModel model = selectedModel.clone("Clone".equals(arg0.getRelativeElement().getId()) ? true : false); //$NON-NLS-1$

                    int selectModelIndex = parentModel.indexOf(selectedModel);
                    parentModel.insert(model, selectModelIndex + 1);
                    // if it has default value
                    if (viewBean.getBindingEntityModel().getMetaDataTypes().get(xpath).getDefaultValue() != null)
                        model.setObjectValue(viewBean.getBindingEntityModel().getMetaDataTypes().get(xpath).getDefaultValue());
                    parentItem.insertItem(buildGWTTree(model, true), parentItem.getChildIndex(selected) + 1);
                    occurMap.put(countMapItem, count + 1);
                } else
                    MessageBox.alert(MessagesFactory.getMessages().status(), MessagesFactory.getMessages()
                            .multiOccurrence_maximize(count), null);
            } else {
                MessageBox.confirm(MessagesFactory.getMessages().confirm_title(), MessagesFactory.getMessages().delete_confirm(),
                        new Listener<MessageBoxEvent>() {

                            public void handleEvent(MessageBoxEvent be) {
                                if (be.getButtonClicked().getItemId().equals(Dialog.YES)) {
                                    if (count > 1
                                            && count > viewBean.getBindingEntityModel().getMetaDataTypes().get(xpath)
                                                    .getMinOccurs()) {
                                        parentItem.removeItem(selected);
                                        parentModel.remove(selectedModel);
                                        occurMap.put(countMapItem, count - 1);
                                    } else
                                        MessageBox.alert(MessagesFactory.getMessages().status(), MessagesFactory.getMessages()
                                                .multiOccurrence_minimize(count), null);
                                }
                            }
                        });
            }
        }
    };

    public ForeignKeyTreeDetail() {
        this.setHeaderVisible(false);
        this.setHeight(Window.getClientHeight() - (60 + 4 * 20));
        this.setScrollMode(Scroll.AUTO);
        this.setFkRender(new ForeignKeyRenderImpl());
        // display ForeignKey detail information,the tabPanel need to be clear. including create link refresh.
        ItemsDetailPanel.getInstance().clearContent();
    }

    public ForeignKeyTreeDetail(ViewBean viewBean, boolean isCreate) {
        this();
        this.isCreate = isCreate;
        this.viewBean = viewBean;
        this.columnLayoutModel = viewBean.getColumnLayoutModel();
        this.toolBar = new ItemDetailToolBar(new ItemBean(viewBean.getBindingEntityModel().getConceptName(), "", ""), //$NON-NLS-1$//$NON-NLS-2$
                isCreate ? ItemDetailToolBar.CREATE_OPERATION : ItemDetailToolBar.VIEW_OPERATION, true, viewBean);
        this.setTopComponent(toolBar);
        buildPanel(viewBean);
    }

    public ForeignKeyTreeDetail(ForeignKeyModel fkModel, boolean isCreate) {
        this();
        this.isCreate = isCreate;
        this.fkModel = fkModel;
        this.model = fkModel.getNodeModel();
        this.viewBean = fkModel.getViewBean();
        this.columnLayoutModel = viewBean.getColumnLayoutModel();
        this.toolBar = new ItemDetailToolBar(fkModel.getItemBean(), isCreate ? ItemDetailToolBar.CREATE_OPERATION
                : ItemDetailToolBar.VIEW_OPERATION, true, viewBean);
        this.setTopComponent(toolBar);
        ItemsDetailPanel.getInstance().clearContent();
        ItemsDetailPanel.getInstance().initBanner(fkModel.getItemBean().getPkInfoList(), fkModel.getItemBean().getDescription());
        // Update breadcrumb
        ItemsDetailPanel.getInstance().appendBreadCrumb(fkModel.getItemBean().getConcept(), fkModel.getItemBean().getIds());
        buildPanel(viewBean);
    }

    public void buildPanel(final ViewBean viewBean) {
        ItemNodeModel rootModel;
        if (this.model == null && this.isCreate) {
            List<ItemNodeModel> models = CommonUtil.getDefaultTreeModel(
                    viewBean.getBindingEntityModel().getMetaDataTypes().get(viewBean.getBindingEntityModel().getConceptName()),
                    Locale.getLanguage());
            rootModel = models.get(0);
        } else
            rootModel = this.model;
        renderTree(rootModel);
    }

    private void renderTree(ItemNodeModel rootModel) {
        root = buildGWTTree(rootModel, false);
        tree = new Tree();
        tree.addItem(root);
        root.setState(true);
        if (root.getElement().getFirstChildElement() != null)
            root.getElement().getFirstChildElement().setClassName("rootNode"); //$NON-NLS-1$
        if (this.columnLayoutModel != null) {// TODO if create a new ForeignKey, tree UI can not render according to the
                                             // layout template
            HorizontalPanel hp = new HorizontalPanel();
            for (ColumnTreeModel ctm : columnLayoutModel.getColumnTreeModels()) {
                Tree tree = displayGWTTree(ctm);
                hp.add(tree);
            }
            hp.setHeight("570px"); //$NON-NLS-1$
            HorizontalPanel spacehp = new HorizontalPanel();
            spacehp.setHeight("10px"); //$NON-NLS-1$
            add(spacehp);
            add(hp);

        } else {
            add(tree);
        }
    }

    public void refreshTree() {
        BrowseRecordsServiceAsync service = (BrowseRecordsServiceAsync) Registry.get(BrowseRecords.BROWSERECORDS_SERVICE);
        final ItemBean item = fkModel.getItemBean();
        item.set("isRefresh", true); //$NON-NLS-1$
        service.getItemNodeModel(item, viewBean.getBindingEntityModel(), Locale.getLanguage(),
                new SessionAwareAsyncCallback<ItemNodeModel>() {

                    public void onSuccess(ItemNodeModel nodeModel) {
                        fkModel.setNodeModel(nodeModel);
                        model = nodeModel;
                        ForeignKeyTreeDetail.this.getItem(0).removeFromParent();
                        item.set("time", nodeModel.get("time")); //$NON-NLS-1$ //$NON-NLS-2$
                        renderTree(nodeModel);
                        ItemsDetailPanel.getInstance().clearChildrenContent();
                        ForeignKeyTreeDetail.this.layout();
                    }

                    @Override
                    protected void doOnFailure(Throwable caught) {
                        MessageBox.alert(MessagesFactory.getMessages().error_title(), MessagesFactory.getMessages().refresh_tip()
                                + " " + MessagesFactory.getMessages().message_fail(), null); //$NON-NLS-1$
                    }
                });

    }

    public ViewBean getViewBean() {
        return viewBean;
    }

    public void setViewBean(ViewBean viewBean) {
        this.viewBean = viewBean;
        buildPanel(viewBean);
    }

    private DynamicTreeItem buildGWTTree(final ItemNodeModel itemNode, boolean withDefaultValue) {
        DynamicTreeItem item = new DynamicTreeItem();
        item.setItemNodeModel(itemNode);
        item.setWidget(TreeDetailUtil.createWidget(itemNode, viewBean, fieldMap, handler));
        item.setUserObject(itemNode);
        if (itemNode.getChildren() != null && itemNode.getChildren().size() > 0) {
            final Map<TypeModel, List<ItemNodeModel>> fkMap = new HashMap<TypeModel, List<ItemNodeModel>>();
            for (ModelData model : itemNode.getChildren()) {
                ItemNodeModel node = (ItemNodeModel) model;
                TypeModel typeModel = viewBean.getBindingEntityModel().getMetaDataTypes().get(node.getBindingPath());
                if (withDefaultValue && typeModel.getDefaultValue() != null
                        && (node.getObjectValue() == null || node.getObjectValue().equals(""))) //$NON-NLS-1$
                    node.setObjectValue(typeModel.getDefaultValue());
                if (this.isCreate && this.model != null && node.isKey()) // duplicate
                    node.setObjectValue(null); // id
                if (typeModel.getForeignkey() != null && fkRender != null) {
                    if (!fkMap.containsKey(typeModel))
                        fkMap.put(typeModel, new ArrayList<ItemNodeModel>());
                    fkMap.get(typeModel).add(node);
                } else if (typeModel.getForeignkey() == null) {
                    item.addItem(buildGWTTree(node, withDefaultValue));
                    int count = 0;
                    CountMapItem countMapItem = new CountMapItem(node.getBindingPath(), item);
                    if (occurMap.containsKey(countMapItem))
                        count = occurMap.get(countMapItem);
                    occurMap.put(countMapItem, count + 1);
                }
            }

            if (fkMap.size() > 0) {
                DeferredCommand.addCommand(new Command() {

                    public void execute() {
                        for (TypeModel model : fkMap.keySet()) {
                            fkRender.RenderForeignKey(itemNode, fkMap.get(model), model, toolBar, viewBean);
                        }
                        itemNode.addChangeListener(new ChangeListener() {

                            public void modelChanged(ChangeEvent event) {
                                if (event.getType() == ChangeEventSource.Remove) {
                                    ItemNodeModel source = (ItemNodeModel) event.getItem();
                                    fkRender.removeRelationFkPanel(source);
                                }
                            }
                        });
                    }
                });
            }

            item.getElement().getStyle().setPaddingLeft(3.0, Unit.PX);
        }

        return item;
    }

    private Tree displayGWTTree(ColumnTreeModel treeModel) {
        Tree tree = new Tree();
        if (root != null && root.getChildCount() > 0) {
            for (ColumnElement ce : treeModel.getColumnElements()) {
                for (int i = 0; i < root.getChildCount(); i++) {
                    TreeItem child = root.getChild(i);
                    ItemNodeModel node = (ItemNodeModel) child.getUserObject();
                    if (("/" + node.getBindingPath()).equals(ce.getxPath())) { //$NON-NLS-1$
                        tree.addItem(child);
                        break;
                    }
                }
            }
        }
        return tree;
    }

    public ItemNodeModel getRootModel() {
        return (ItemNodeModel) root.getUserObject();
    }

    public boolean isCreate() {
        return isCreate;
    }

    public ForeignKeyModel getFkModel() {
        return fkModel;
    }

    public void setFkModel(ForeignKeyModel fkModel) {
        this.fkModel = fkModel;
    }

    public static class DynamicTreeItem extends TreeItem {

        private ItemNodeModel itemNode;

        public DynamicTreeItem() {
            super();
        }

        private List<TreeItem> items = new ArrayList<TreeItem>();

        public void insertItem(DynamicTreeItem item, int beforeIndex) {
            int count = this.getChildCount();

            for (int i = 0; i < count; i++) {
                items.add(this.getChild(i));
            }

            items.add(beforeIndex, item);
            this.removeItems();

            for (int j = 0; j < items.size(); j++) {
                this.addItem(items.get(j));
            }
            items.clear();
        }

        public void removeItem(DynamicTreeItem item) {
            super.removeItem(item);
        }

        public void setItemNodeModel(ItemNodeModel treeNode) {
            itemNode = treeNode;
        }

        public ItemNodeModel getItemNodeModel() {
            return itemNode;
        }
    }

    private class CountMapItem {

        private String xpath;

        private TreeItem parentItem;

        public CountMapItem(String xpath, TreeItem parentItem) {
            this.xpath = xpath;
            this.parentItem = parentItem;
        }

        public String getXpath() {
            return this.xpath;
        }

        public TreeItem getParentItem() {
            return this.parentItem;
        }

        @Override
        public int hashCode() {
            return xpath.length();
        }

        @Override
        public boolean equals(Object o) {
            CountMapItem item = (CountMapItem) o;
            return item.getXpath().equals(xpath) && item.getParentItem().equals(parentItem);
        }
    }


    public boolean validateTree() {
        boolean flag = true;
        ItemNodeModel rootNode = (ItemNodeModel) tree.getItem(0).getUserObject();
        if (rootNode != null) {
            flag = validateNode(rootNode, flag);
        }
        return flag;
    }

    public boolean validateNode(ItemNodeModel rootNode, boolean flag) {

        if (rootNode.getChildren() != null && rootNode.getChildren().size() > 0) {
            Map<TypeModel, Integer> map = new HashMap<TypeModel, Integer>();
            for (ModelData model : rootNode.getChildren()) {

                ItemNodeModel node = (ItemNodeModel) model;
                if (!node.isValid() && node.getChildCount() == 0) {
                    TypeModel tm = viewBean.getBindingEntityModel().getMetaDataTypes().get(node.getBindingPath());
                    if (tm.getForeignkey() != null) {
                        // fk minOccurs check
                        if (!map.containsKey(tm))
                            map.put(tm, 0);
                        map.put(tm, map.get(tm) + 1);
                        if (map.get(tm) <= tm.getMinOccurs()) {
                            // check value
                            ForeignKeyBean fkBean = (ForeignKeyBean) node.getObjectValue();
                            if (fkBean == null || fkBean.getId() == null) {
                                MessageBox.alert(MessagesFactory.getMessages().error_title(), MessagesFactory.getMessages()
                                        .fk_save_validate(ForeignKeyUtil.transferXpathToLabel(tm, viewBean), tm.getMinOccurs()),
                                        null);
                                flag = false;
                            }
                        }

                    } else {
                        // com.google.gwt.user.client.Window.alert(node.getBindingPath()
                        //                                + "/" + node.getName() + "'Value validate failure"); //$NON-NLS-1$ //$NON-NLS-2$
                        // Message tip should use gxt's messageBox, but not com.google.gwt.user.client.Window
                        MessageBox.alert(MessagesFactory.getMessages().error_title(),
                                node.getBindingPath() + "/" + node.getName() + "'Value validate failure", null); //$NON-NLS-1$//$NON-NLS-2$
                        flag = false;
                    }

                }

                if (node.getChildren() != null && node.getChildren().size() > 0) {
                    flag = validateNode(node, flag);
                }

                if (!flag) {
                    break;
                }
            }
            if (flag) {
                for (TypeModel fkTypeModel : map.keySet()) {
                    if (fkTypeModel.getMinOccurs() > map.get(fkTypeModel)) {
                        MessageBox.alert(
                                MessagesFactory.getMessages().error_title(),
                                MessagesFactory.getMessages().fk_save_validate(
                                        ForeignKeyUtil.transferXpathToLabel(fkTypeModel, viewBean), fkTypeModel.getMinOccurs()),
                                null);
                        flag = false;
                        break;
                    }
                }
            }

        }
        return flag;
    }

    public ForeignKeyRender getFkRender() {
        return fkRender;
    }

    public void setFkRender(ForeignKeyRender fkRender) {
        this.fkRender = fkRender;
    }
}
