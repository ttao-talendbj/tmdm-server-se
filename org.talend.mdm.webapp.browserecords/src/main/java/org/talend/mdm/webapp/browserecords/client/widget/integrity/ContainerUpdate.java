package org.talend.mdm.webapp.browserecords.client.widget.integrity;

import org.talend.mdm.webapp.browserecords.client.widget.ItemsListPanel;
import org.talend.mdm.webapp.browserecords.client.widget.ItemsMainTabPanel;

import com.extjs.gxt.ui.client.widget.TabItem;

/**
 * An implementation of {@link PostDeleteAction} that performs item browser container operations (such as closing tabs
 * if needed...).
 */
public class ContainerUpdate implements PostDeleteAction {

    private final PostDeleteAction next;

    /**
     * @param next If you don't know what to pass as <code>next</code> argument, check the
     * constant {@link NoOpPostDeleteAction#INSTANCE}.
     */
    public ContainerUpdate(PostDeleteAction next) {
        this.next = next;
    }

    public void doAction() {

        // After item has been deleted, close its view tab.
        TabItem tabItem = ItemsMainTabPanel.getInstance().getSelectedItem();

        if (tabItem != null) {
            tabItem.removeFromParent();
        }
        ItemsListPanel itemsListPanel = ItemsListPanel.getInstance();
        itemsListPanel.getGrid().getSelectionModel().select(-1, false);

        next.doAction();
    }
}
