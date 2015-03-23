// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.mdm.webapp.general.client.layout;

import java.util.ArrayList;
import java.util.List;

import org.talend.mdm.webapp.base.client.SessionAwareAsyncCallback;
import org.talend.mdm.webapp.general.client.General;
import org.talend.mdm.webapp.general.client.GeneralServiceAsync;
import org.talend.mdm.webapp.general.client.i18n.MessageFactory;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

public class PortletConfigFieldSet extends FieldSet {

    private static PortletConfigFieldSet instance;

    private GeneralServiceAsync service = (GeneralServiceAsync) Registry.get(General.OVERALL_SERVICE);

    private static final String NAME_START = "start", NAME_PROCESS = "process", NAME_ALERT = "alert", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            NAME_SEARCH = "search", NAME_TASKS = "tasks", NAME_CHART_DATA = "chart_data", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            NAME_CHART_ROUTING_EVENT = "chart_routing_event", NAME_CHART_JOURNAL = "chart_journal", NAME_CHART_MATCHING = "chart_matching"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    private CheckBox startWidgetCheckBox;

    private CheckBox processWidgetCheckBox;

    private CheckBox alertWidgetCheckBox;

    private CheckBox searchWidgetCheckBox;

    private CheckBox tasksWidgetCheckBox;

    private CheckBox chartParentCheckBox;

    private CheckBox dataChartCheckBox;

    private CheckBox routtingEventChartCheckBox;

    private CheckBox journalChartCheckBox;

    private CheckBox matchingChartCheckBox;

    private Button saveButton;

    private Radio col2Radio;

    private Radio col3Radio;

    private static String WELCOMEPORTAL_CONTEXT = "welcomeportal"; //$NON-NLS-1$

    private boolean isEnterprise;

    private static String WELCOMEPORTAL_APP = "WelcomePortal"; //$NON-NLS-1$

    private List<String> allPortlets;

    private List<String> portletsToCheck;

    private int colNum = 3;

    public PortletConfigFieldSet() {
        FormLayout formLayout = new FormLayout();
        formLayout.setLabelAlign(LabelAlign.TOP);
        setLayout(formLayout);
        setHeading(MessageFactory.getMessages().portal_configuration());

        service.isEnterpriseVersion(new SessionAwareAsyncCallback<Boolean>() {

            @Override
            public void onSuccess(Boolean isEE) {
                isEnterprise = isEE;
                initWidgetCheckBox();
                initLayoutRadio();
                saveButton = new Button(MessageFactory.getMessages().save());
                saveButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        List<String> configUpdates = getPortalConfigUpdate();
                        switchToWelcomeportal();
                        refreshPortal(configUpdates.toString());

                    }
                });
                FormData buttonFormData = new FormData();
                buttonFormData.setMargins(new Margins(2, 0, 2, 0));
                add(saveButton, buttonFormData);
                layout(true);
            }
        });
    }

    public static PortletConfigFieldSet getInstance() {
        if (instance == null) {
            instance = new PortletConfigFieldSet();
        }
        return instance;
    }

    private void initWidgetCheckBox() {
        CheckBoxGroup widgetGroup = new CheckBoxGroup();
        widgetGroup.setFieldLabel(MessageFactory.getMessages().portal_portlets());
        widgetGroup.setOrientation(Orientation.VERTICAL);

        FormData widgetFormData = new FormData();
        widgetFormData.setMargins(new Margins(2, -20, 2, 20));

        startWidgetCheckBox = new CheckBox();
        startWidgetCheckBox.setBoxLabel(MessageFactory.getMessages().portlet_start());
        startWidgetCheckBox.setValue(false);
        startWidgetCheckBox.setVisible(true);
        widgetGroup.add(startWidgetCheckBox);

        processWidgetCheckBox = new CheckBox();
        processWidgetCheckBox.setBoxLabel(MessageFactory.getMessages().portlet_process());
        processWidgetCheckBox.setValue(false);
        processWidgetCheckBox.setVisible(true);
        widgetGroup.add(processWidgetCheckBox);

        alertWidgetCheckBox = new CheckBox();
        alertWidgetCheckBox.setBoxLabel(MessageFactory.getMessages().portlet_alert());
        alertWidgetCheckBox.setValue(false);
        alertWidgetCheckBox.setVisible(true);
        widgetGroup.add(alertWidgetCheckBox);

        searchWidgetCheckBox = new CheckBox();
        searchWidgetCheckBox.setBoxLabel(MessageFactory.getMessages().portlet_search());
        searchWidgetCheckBox.setValue(false);
        searchWidgetCheckBox.setVisible(true);
        widgetGroup.add(searchWidgetCheckBox);

        tasksWidgetCheckBox = new CheckBox();
        tasksWidgetCheckBox.setBoxLabel(MessageFactory.getMessages().portlet_tasks());
        tasksWidgetCheckBox.setValue(false);
        tasksWidgetCheckBox.setVisible(true);
        widgetGroup.add(tasksWidgetCheckBox);

        if (isEnterprise) {
            chartParentCheckBox = new CheckBox() {

                @Override
                protected void onClick(ComponentEvent be) {
                    if (this.getValue()) {
                        showChartCheckBox();
                        dataChartCheckBox.setValue(true);
                        routtingEventChartCheckBox.setValue(true);
                        journalChartCheckBox.setValue(true);
                        matchingChartCheckBox.setValue(true);
                    } else {
                        dataChartCheckBox.setValue(false);
                        routtingEventChartCheckBox.setValue(false);
                        journalChartCheckBox.setValue(false);
                        matchingChartCheckBox.setValue(false);
                    }
                }
            };
            chartParentCheckBox.setBoxLabel(MessageFactory.getMessages().portal_chart_portlets());
            chartParentCheckBox.setValue(false);
            chartParentCheckBox.setVisible(true);
            widgetGroup.add(chartParentCheckBox);
            add(widgetGroup, widgetFormData);
            initChartCheckBox();
        } else {
            add(widgetGroup, widgetFormData);
        }
    }

    private void initChartCheckBox() {
        CheckBoxGroup chartGroup = new CheckBoxGroup();
        chartGroup.setFieldLabel(MessageFactory.getMessages().portal_portlets());
        chartGroup.setOrientation(Orientation.VERTICAL);
        chartGroup.setHideLabel(true);
        FormData chartFormData = new FormData();
        chartFormData.setMargins(new Margins(-2, -40, 2, 40));

        dataChartCheckBox = new CheckBox() {

            @Override
            protected void onClick(ComponentEvent be) {
                updateChartParentCheckBox(this.getValue());
            }
        };
        dataChartCheckBox.setBoxLabel(MessageFactory.getMessages().portlet_data());
        dataChartCheckBox.setValue(false);
        dataChartCheckBox.setVisible(false);
        chartGroup.add(dataChartCheckBox);

        routtingEventChartCheckBox = new CheckBox() {

            @Override
            protected void onClick(ComponentEvent be) {
                updateChartParentCheckBox(this.getValue());
            }
        };
        routtingEventChartCheckBox.setBoxLabel(MessageFactory.getMessages().portlet_routing());
        routtingEventChartCheckBox.setValue(false);
        routtingEventChartCheckBox.setVisible(false);
        chartGroup.add(routtingEventChartCheckBox);

        journalChartCheckBox = new CheckBox() {

            @Override
            protected void onClick(ComponentEvent be) {
                updateChartParentCheckBox(this.getValue());
            }
        };
        journalChartCheckBox.setBoxLabel(MessageFactory.getMessages().portlet_journal());
        journalChartCheckBox.setValue(false);
        journalChartCheckBox.setVisible(false);
        chartGroup.add(journalChartCheckBox);

        matchingChartCheckBox = new CheckBox() {

            @Override
            protected void onClick(ComponentEvent be) {
                updateChartParentCheckBox(this.getValue());
            }
        };
        matchingChartCheckBox.setBoxLabel(MessageFactory.getMessages().portlet_matching());
        matchingChartCheckBox.setValue(false);
        matchingChartCheckBox.setVisible(false);
        chartGroup.add(matchingChartCheckBox);
        add(chartGroup, chartFormData);
    }

    private void initLayoutRadio() {
        RadioGroup colRadioGroup = new RadioGroup();
        colRadioGroup.setFieldLabel(MessageFactory.getMessages().portal_columns());
        colRadioGroup.setOrientation(Orientation.VERTICAL);
        FormData layoutFormData = new FormData();
        layoutFormData.setMargins(new Margins(2, -20, 2, 20));

        col2Radio = new Radio();
        col2Radio.setBoxLabel(MessageFactory.getMessages().portal_columns_two());
        colRadioGroup.add(col2Radio);
        col3Radio = new Radio();
        col3Radio.setBoxLabel(MessageFactory.getMessages().portal_columns_three());
        colRadioGroup.add(col3Radio);
        add(colRadioGroup, layoutFormData);
    }

    private void updateChartParentCheckBox(boolean value) {
        if (value) {
            chartParentCheckBox.setValue(true);
        } else if (!dataChartCheckBox.getValue() && !routtingEventChartCheckBox.getValue() && !journalChartCheckBox.getValue()
                && !matchingChartCheckBox.getValue()) {
            chartParentCheckBox.setValue(false);
        }
    }

    private void updatePortletConfig(String dataString) {
        unCheckWidgetCheckBox();
        String[] temp = dataString.split("; "); //$NON-NLS-1$
        String[] selectedCheckBox = temp[1].substring(1, temp[1].length() - 1).split(", "); //$NON-NLS-1$
        for (String checkBox : selectedCheckBox) {
            updateWidgetCheckBox(checkBox, true);
        }
        int columnNumber = Integer.parseInt(temp[2]);
        col3Radio.setValue(((columnNumber == 3) ? true : false));
        col2Radio.setValue(((columnNumber == 2) ? true : false));
        layout(true);
    }

    private List<String> getPortalConfigUpdate() {
        List<String> updates = new ArrayList<String>();
        if (startWidgetCheckBox.getValue()) {
            updates.add(NAME_START);
        }
        if (processWidgetCheckBox.getValue()) {
            updates.add(NAME_PROCESS);
        }
        if (alertWidgetCheckBox.getValue()) {
            updates.add(NAME_ALERT);
        }
        if (searchWidgetCheckBox.getValue()) {
            updates.add(NAME_SEARCH);
        }
        if (tasksWidgetCheckBox.getValue()) {
            updates.add(NAME_TASKS);
        }
        if (isEnterprise) {
            if (dataChartCheckBox.getValue()) {
                updates.add(NAME_CHART_DATA);
            }
            if (routtingEventChartCheckBox.getValue()) {
                updates.add(NAME_CHART_ROUTING_EVENT);
            }
            if (journalChartCheckBox.getValue()) {
                updates.add(NAME_CHART_JOURNAL);
            }
            if (matchingChartCheckBox.getValue()) {
                updates.add(NAME_CHART_MATCHING);
            }
        }
        updates.add((col3Radio.getValue() ? "3" : "2")); //$NON-NLS-1$ //$NON-NLS-2$
        return updates;
    }

    public void updateWidgetCheckBox(String name, Boolean value) {
        if (NAME_START.equals(name)) {
            startWidgetCheckBox.setValue(value);
        } else if (NAME_PROCESS.equals(name)) {
            processWidgetCheckBox.setValue(value);
        } else if (NAME_ALERT.equals(name)) {
            alertWidgetCheckBox.setValue(value);
        } else if (NAME_SEARCH.equals(name)) {
            searchWidgetCheckBox.setValue(value);
        } else if (NAME_TASKS.equals(name)) {
            tasksWidgetCheckBox.setValue(value);
        } else if (NAME_CHART_DATA.equals(name)) {
            dataChartCheckBox.setValue(value);
            if (!dataChartCheckBox.isVisible()) {
                showChartCheckBox();
            }
        } else if (NAME_CHART_ROUTING_EVENT.equals(name)) {
            routtingEventChartCheckBox.setValue(value);
            if (!routtingEventChartCheckBox.isVisible()) {
                showChartCheckBox();
            }
        } else if (NAME_CHART_JOURNAL.equals(name)) {
            journalChartCheckBox.setValue(value);
            if (!journalChartCheckBox.isVisible()) {
                showChartCheckBox();
            }
        } else if (NAME_CHART_MATCHING.equals(name)) {
            matchingChartCheckBox.setValue(value);
            if (!matchingChartCheckBox.isVisible()) {
                showChartCheckBox();
            }
        }
    }

    private void showChartCheckBox() {
        dataChartCheckBox.setVisible(true);
        routtingEventChartCheckBox.setVisible(true);
        journalChartCheckBox.setVisible(true);
        matchingChartCheckBox.setVisible(true);
    }

    private void unCheckWidgetCheckBox() {
        startWidgetCheckBox.setValue(false);
        processWidgetCheckBox.setValue(false);
        alertWidgetCheckBox.setValue(false);
        searchWidgetCheckBox.setValue(true);
        tasksWidgetCheckBox.setValue(false);
        dataChartCheckBox.setValue(true);
        routtingEventChartCheckBox.setValue(false);
        journalChartCheckBox.setValue(false);
        matchingChartCheckBox.setValue(false);
    }

    protected void switchToWelcomeportal() {
        display(WELCOMEPORTAL_CONTEXT, WELCOMEPORTAL_APP);
        AccordionMenus.getInstance().selectedItem(AccordionMenus.getInstance().getWelcomeportalItem());
    }

    // call refresh in WelcomePortal
    private native void refreshPortal(String portalConfig)/*-{
		$wnd.amalto.core.refreshPortal(portalConfig);
    }-*/;

    private native void display(String context, String application)/*-{
		if ($wnd.amalto[context]) {
			if ($wnd.amalto[context][application]) {
				$wnd.amalto[context][application].init();
			}
		}
    }-*/;

    public void activateSaveButton() {
        if (!saveButton.isEnabled()) {
            saveButton.enable();
        }
    }
}