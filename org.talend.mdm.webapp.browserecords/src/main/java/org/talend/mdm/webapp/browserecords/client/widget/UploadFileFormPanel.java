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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talend.mdm.webapp.base.client.model.ItemBaseModel;
import org.talend.mdm.webapp.base.client.util.MultilanguageMessageParser;
import org.talend.mdm.webapp.base.client.util.UrlUtil;
import org.talend.mdm.webapp.base.shared.FileUtil;
import org.talend.mdm.webapp.base.shared.TypeModel;
import org.talend.mdm.webapp.browserecords.client.i18n.MessagesFactory;
import org.talend.mdm.webapp.browserecords.client.util.LabelUtil;
import org.talend.mdm.webapp.browserecords.shared.ComplexTypeModel;
import org.talend.mdm.webapp.browserecords.shared.Constants;
import org.talend.mdm.webapp.browserecords.shared.EntityModel;
import org.talend.mdm.webapp.browserecords.shared.ViewBean;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HiddenField;


/**
 * DOC Administrator  class global comment. Detailled comment
 */
public class UploadFileFormPanel extends FormPanel implements Listener<FormEvent> {

    private String tableName;
    
    private ComboBox<ItemBaseModel> separatorCombo;
    
    private ComboBox<ItemBaseModel> textDelimiterCombo;
    
    private ComboBox<ItemBaseModel> encodingCombo;

    private FileUploadField file;

    private CheckBox headerLine;

    private HiddenField<String> nameField;
        
    private HiddenField<String> headerField;

    private HiddenField<String> mandatoryField;
    
    private HiddenField<String> languageField;
    
    private HiddenField<String> viewableXpathField;
    
    private MessageBox waitBar;
    
    private String type;

    private ViewBean viewBean;
    
    private Window window;
    
    public UploadFileFormPanel(ViewBean viewBean, Window window) {
        
        this.tableName = viewBean.getBindingEntityModel().getConceptName();
        this.viewBean = viewBean;
        this.window = window;
        this.setFrame(false);
        this.setHeaderVisible(false);
        this.setEncoding(Encoding.MULTIPART);
        this.setMethod(Method.POST);
        this.setWidth("100%"); //$NON-NLS-1$
        this.setAction("/browserecords/upload"); //$NON-NLS-1$

        this.renderForm();
    }

    private String getHeaderStr(ViewBean viewBean){
        EntityModel entityModel = viewBean.getBindingEntityModel();         
        Map<String, TypeModel> dataTypes = entityModel.getMetaDataTypes();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        ComplexTypeModel rootModel = (ComplexTypeModel) dataTypes.get(entityModel.getConceptName());
        List<TypeModel> typeModels = rootModel.getSubTypes();
        int size = typeModels.size();
        for (TypeModel typeModel : typeModels) {
            i++;
            sb.append(typeModel.getName() + ":" + typeModel.isVisible()); //$NON-NLS-1$
            if(i < size)
                sb.append("@");  //$NON-NLS-1$
        }
        return sb.toString();
    }
    
    private String getMandatoryStr(ViewBean viewBean){
        EntityModel entityModel = viewBean.getBindingEntityModel();         
        Map<String, TypeModel> dataTypes = entityModel.getMetaDataTypes();
        Set<String> keySet = dataTypes.keySet();
        StringBuilder sb = new StringBuilder();
        
        for(String key : keySet){
            TypeModel typeModel = dataTypes.get(key);
            if(typeModel != null){
                if(typeModel.getMaxOccurs() == 1 && typeModel.getMinOccurs() == 1){
                    sb.append(typeModel.getName()).append("@"); //$NON-NLS-1$
                }
            }
        }

        return sb.toString();
    }
    
    private void renderForm() {

        nameField = new HiddenField<String>();
        nameField.setName("concept");//$NON-NLS-1$
        nameField.setValue(tableName);
        this.add(nameField);
                
        headerField = new HiddenField<String>();
        headerField.setName("header");//$NON-NLS-1$
        headerField.setValue(this.getHeaderStr(viewBean));
        this.add(headerField);
        
        mandatoryField = new HiddenField<String>();
        mandatoryField.setName("mandatoryField");//$NON-NLS-1$
        mandatoryField.setValue(this.getMandatoryStr(viewBean));
        this.add(mandatoryField);
        
        languageField = new HiddenField<String>();
        languageField.setName("language");//$NON-NLS-1$
        languageField.setValue(UrlUtil.getLanguage());
        this.add(languageField);
        
        viewableXpathField = new HiddenField<String>();
        viewableXpathField.setName("viewableXpath");//$NON-NLS-1$
        viewableXpathField.setValue(LabelUtil.convertList2String(viewBean.getViewableXpaths(), "@")); //$NON-NLS-1$
        this.add(viewableXpathField);
        
        file = new FileUploadField();
        file.setAllowBlank(false);
        file.setName("file"); //$NON-NLS-1$
        file.setId("fileUpload");//$NON-NLS-1$   
        file.setFieldLabel(MessagesFactory.getMessages().label_field_file());
        file.addListener(Events.Change, new Listener<BaseEvent>() {

            public void handleEvent(BaseEvent be) {                
                type =  FileUtil.getFileType(file.getValue());
                if (type.equalsIgnoreCase("CSV")) { //$NON-NLS-1$
                    separatorCombo.enable();
                    textDelimiterCombo.enable();
                    encodingCombo.enable();
                    
                    separatorCombo.setAllowBlank(false);
                    textDelimiterCombo.setAllowBlank(false);
                    encodingCombo.setAllowBlank(false);
                } else {
                    separatorCombo.disable();
                    textDelimiterCombo.disable();
                    encodingCombo.disable();
                    
                    separatorCombo.setAllowBlank(true);
                    separatorCombo.validate();
                    textDelimiterCombo.setAllowBlank(true);
                    textDelimiterCombo.validate();
                    encodingCombo.setAllowBlank(true);
                    encodingCombo.validate();
                } 
            }
        });
        this.add(file);

        List<ItemBaseModel> list = new ArrayList<ItemBaseModel>();
        ItemBaseModel excel = new ItemBaseModel();
        excel.set("label", "Excel"); //$NON-NLS-1$ //$NON-NLS-2$
        excel.set("key", "excel"); //$NON-NLS-1$ //$NON-NLS-2$
        list.add(excel);

        ItemBaseModel csv = new ItemBaseModel();
        csv.set("label", "CSV"); //$NON-NLS-1$ //$NON-NLS-2$
        csv.set("key", "csv"); //$NON-NLS-1$ //$NON-NLS-2$
        list.add(csv);
        ListStore<ItemBaseModel> typeList = new ListStore<ItemBaseModel>();
        typeList.add(list);

        headerLine = new CheckBox();
        headerLine.setId("headersOnFirstLine");//$NON-NLS-1$
        headerLine.setName("headersOnFirstLine");//$NON-NLS-1$
        headerLine.setValueAttribute("on"); //$NON-NLS-1$
        headerLine.setFieldLabel(MessagesFactory.getMessages().label_field_header_first());
        headerLine.setValue(true);
        headerLine.setInputStyleAttribute("left", "10px"); //$NON-NLS-1$ //$NON-NLS-2$
        headerLine.setInputStyleAttribute("position", "absolute"); //$NON-NLS-1$ //$NON-NLS-2$
        this.add(headerLine);

        List<ItemBaseModel> separatorList = new ArrayList<ItemBaseModel>();
        ItemBaseModel comma = new ItemBaseModel();
        comma.set("label", "comma"); //$NON-NLS-1$ //$NON-NLS-2$
        comma.set("key", "comma"); //$NON-NLS-1$ //$NON-NLS-2$
        separatorList.add(comma);

        ItemBaseModel semicolon = new ItemBaseModel();
        semicolon.set("label", "semicolon"); //$NON-NLS-1$ //$NON-NLS-2$
        semicolon.set("key", "semicolon"); //$NON-NLS-1$ //$NON-NLS-2$
        separatorList.add(semicolon);

        ListStore<ItemBaseModel> separatorStoreList = new ListStore<ItemBaseModel>();
        separatorStoreList.add(separatorList);

        separatorCombo = new ComboBox<ItemBaseModel>();
        separatorCombo.setId("sep");//$NON-NLS-1$
        separatorCombo.setName("sep");//$NON-NLS-1$
        separatorCombo.setFieldLabel(MessagesFactory.getMessages().label_field_separator());
        separatorCombo.setDisplayField("label"); //$NON-NLS-1$
        separatorCombo.setValueField("key"); //$NON-NLS-1$
        separatorCombo.setStore(separatorStoreList);
        separatorCombo.setTriggerAction(TriggerAction.ALL);
        this.add(separatorCombo);

        List<ItemBaseModel> textDelimiterList = new ArrayList<ItemBaseModel>();
        ItemBaseModel doubleDelimiter = new ItemBaseModel();
        doubleDelimiter.set("label", "\""); //$NON-NLS-1$ //$NON-NLS-2$
        doubleDelimiter.set("key", "\""); //$NON-NLS-1$ //$NON-NLS-2$
        textDelimiterList.add(doubleDelimiter);

        ItemBaseModel singleDelimiter = new ItemBaseModel();
        singleDelimiter.set("label", "\'"); //$NON-NLS-1$ //$NON-NLS-2$
        singleDelimiter.set("key", "\'"); //$NON-NLS-1$ //$NON-NLS-2$
        textDelimiterList.add(singleDelimiter);

        ListStore<ItemBaseModel> textDelimiterStoreList = new ListStore<ItemBaseModel>();
        textDelimiterStoreList.add(textDelimiterList);

        textDelimiterCombo = new ComboBox<ItemBaseModel>();
        textDelimiterCombo.setId("delimiter");//$NON-NLS-1$
        textDelimiterCombo.setName("delimiter");//$NON-NLS-1$
        textDelimiterCombo.setFieldLabel(MessagesFactory.getMessages().label_field_delimiter());
        textDelimiterCombo.setDisplayField("label"); //$NON-NLS-1$
        textDelimiterCombo.setValueField("key"); //$NON-NLS-1$
        textDelimiterCombo.setStore(textDelimiterStoreList);
        textDelimiterCombo.setTriggerAction(TriggerAction.ALL);
        this.add(textDelimiterCombo);

        List<ItemBaseModel> encodingList = new ArrayList<ItemBaseModel>();
        ItemBaseModel utf8 = new ItemBaseModel();
        utf8.set("label", "UTF-8"); //$NON-NLS-1$ //$NON-NLS-2$
        utf8.set("key", "utf-8"); //$NON-NLS-1$ //$NON-NLS-2$
        encodingList.add(utf8);

        ItemBaseModel iso88591 = new ItemBaseModel();
        iso88591.set("label", "ISO-8859-1"); //$NON-NLS-1$ //$NON-NLS-2$
        iso88591.set("key", "iso-8859-1"); //$NON-NLS-1$ //$NON-NLS-2$
        encodingList.add(iso88591);

        ItemBaseModel iso885915 = new ItemBaseModel();
        iso885915.set("label", "iso885915"); //$NON-NLS-1$ //$NON-NLS-2$
        iso885915.set("key", "iso-8859-15"); //$NON-NLS-1$ //$NON-NLS-2$
        encodingList.add(iso885915);

        ItemBaseModel cp1252 = new ItemBaseModel();
        cp1252.set("label", "cp1252"); //$NON-NLS-1$ //$NON-NLS-2$
        cp1252.set("key", "cp-1252"); //$NON-NLS-1$ //$NON-NLS-2$
        encodingList.add(cp1252);

        ListStore<ItemBaseModel> encodingStoreList = new ListStore<ItemBaseModel>();
        encodingStoreList.add(encodingList);

        encodingCombo = new ComboBox<ItemBaseModel>();
        encodingCombo.setId("encodings");//$NON-NLS-1$
        encodingCombo.setName("encodings");//$NON-NLS-1$
        encodingCombo.setFieldLabel(MessagesFactory.getMessages().label_field_encoding());
        encodingCombo.setDisplayField("label"); //$NON-NLS-1$
        encodingCombo.setValueField("key"); //$NON-NLS-1$
        encodingCombo.setStore(encodingStoreList);
        encodingCombo.setTriggerAction(TriggerAction.ALL);
        this.add(encodingCombo);

        Button submit = new Button(MessagesFactory.getMessages().label_button_submit());
        submit.setId("btnSubmit");//$NON-NLS-1$
        submit.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (!UploadFileFormPanel.this.isValid())
                    return;    

                if(!Constants.FileType_Imported.contains(type.toLowerCase())){
                    MessageBox.alert(MessagesFactory.getMessages().error_title(), 
                            MessagesFactory.getMessages().error_incompatible_file_type(), null);
                    return;
                }

                UploadFileFormPanel.this.submit();
                waitBar = MessageBox.wait(MessagesFactory.getMessages().import_progress_bar_title(), MessagesFactory
                        .getMessages().import_progress_bar_message(), MessagesFactory
                        .getMessages().import_progress_bar_laod());
            }
        });

        this.add(submit);

        separatorCombo.disable();
        textDelimiterCombo.disable();
        encodingCombo.disable();

        this.setLabelWidth(200);
        this.addListener(Events.Submit, this);
    }

    public void handleEvent(FormEvent be) {        
        String result = be.getResultHtml().replace("pre>", "f>"); //$NON-NLS-1$//$NON-NLS-2$

        waitBar.close();
        if (result.equals("<f>true</f>")) { //$NON-NLS-1$
            window.hide();
            MessageBox.alert(MessagesFactory.getMessages().info_title(), MessagesFactory.getMessages().import_success_label(), null);
            ItemsListPanel.getInstance().refreshGrid();
        } else {
            MessageBox.alert(MessagesFactory.getMessages().error_title(), MultilanguageMessageParser.pickOutISOMessage(extractErrorMessage(result)), null);
        }
    }

    public HiddenField<String> getNameField() {
        return nameField;
    }
    
    public static String extractErrorMessage(String errMsg) {
        String saveExceptionString = "com.amalto.core.save.SaveException: Exception occurred during save: "; //$NON-NLS-1$
        int saveExceptionIndex = errMsg.indexOf(saveExceptionString);
        if (saveExceptionIndex > -1) { 
            errMsg = errMsg.substring(saveExceptionIndex + saveExceptionString.length());
        }
        
        return errMsg;
    }
}
