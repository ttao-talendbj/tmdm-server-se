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
package org.talend.mdm.webapp.browserecords.client.widget.inputfield;

import org.talend.mdm.webapp.browserecords.client.i18n.MessagesFactory;
import org.talend.mdm.webapp.browserecords.client.resources.icon.Icons;
import org.talend.mdm.webapp.browserecords.client.util.CommonUtil;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.XDOM;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ComponentHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.MultiField;
import com.extjs.gxt.ui.client.widget.form.PropertyEditor;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Image;

public class PictureField extends TextField<String> {

    private static final String CONTEXT_PATH = GWT.getModuleBaseURL().replaceFirst(GWT.getModuleName() + "/", ""); //$NON-NLS-1$ //$NON-NLS-2$
    
    private static final String DefaultImage = CONTEXT_PATH + "resources/images/talend/no_image.png"; //$NON-NLS-1$

    protected El wrap = new El(DOM.createSpan());

    Image image = new Image(DefaultImage);

    protected El input = new El(image.getElement());

    protected Element delHandler = new Image(Icons.INSTANCE.clear_icon()).getElement();

    protected Element addHandler = new Image(Icons.INSTANCE.image_add()).getElement();

    private EditWindow editWindow = new EditWindow();

    private boolean readOnly;

    private Dialog dialog = new Dialog() {

        @Override
        protected void onButtonPressed(Button button) {
            super.onButtonPressed(button);
            if (button == getButtonBar().getItemByItemId(YES)) {

                // Only delete it from client side
                setValue(null);
                dialog.hide();

                /*
                RequestBuilder reqBuilder = new RequestBuilder(RequestBuilder.GET,
                        "/imageserver/secure/ImageDeleteServlet?uri=" + value);//$NON-NLS-1$

                reqBuilder.setCallback(new RequestCallback() {

                    public void onResponseReceived(Request request, Response response) {
                        String json = response.getText();
                        JSONObject jsObject = JSONParser.parse(json).isObject();
                        JSONBoolean success = jsObject.get("success").isBoolean(); //$NON-NLS-1$
                        JSONString message = jsObject.get("message").isString(); //$NON-NLS-1$
                        boolean succeed = success.booleanValue();
                        MessageBox.alert(succeed ? MessagesFactory.getMessages().message_success() : MessagesFactory
                                .getMessages().message_fail(), message.stringValue(), null);
                        if (succeed) {
                            setValue(null);
                        }
                        dialog.hide();
                    }

                    public void onError(Request request, Throwable exception) {
                        MessageBox.alert(MessagesFactory.getMessages().error_title(), exception.getMessage(), null);
                    }
                });
                try {
                    reqBuilder.send();
                } catch (RequestException e) {
                    MessageBox.alert("RequestException", e.getMessage(), null); //$NON-NLS-1$
                }
                */

            } else if (button == getButtonBar().getItemByItemId(NO)) {
                dialog.hide();
            }
        }
    };

    public PictureField() {
        setFireChangeEventOnSetValue(true);
        regJs(delHandler);
        regJs(addHandler);
        dialog.setHeading(MessagesFactory.getMessages().confirm_title());
        dialog.addText(MessagesFactory.getMessages().confirm_delete_img());
        dialog.setModal(true);
        dialog.setBlinkModal(true);
        dialog.setButtons(Dialog.YESNO);

        propertyEditor = new PropertyEditor<String>() {

            public String getStringValue(String value) {
                return value;
            }

            public String convertStringValue(String value) {
                return PictureField.this.value;
            }
        };
        
        image.addLoadHandler(new LoadHandler(){
            public void onLoad(LoadEvent event){
                com.google.gwt.dom.client.Element element = event.getRelativeElement();
                if (element == image.getElement()){
                    int width = image.getWidth();
                    int height = image.getHeight();
                    if (width > 0 && width > height) {
                        if (width > 150)
                            image.setPixelSize(150, (int) (height * 150 / width));
                    } else if (height > 150) {
                        image.setPixelSize((int) (width * 150 / height), 150);
                    }
                }
            }
        });
    }

    @Override
    protected void onRender(Element target, int index) {
        input.setId(XDOM.getUniqueId());
        input.makePositionable();

        input.dom.getStyle().setMarginRight(5, Unit.PX);
        wrap.dom.appendChild(input.dom);
        wrap.dom.appendChild(delHandler);
        wrap.dom.appendChild(addHandler);

        setElement(wrap.dom, target, index);
        ComponentHelper.doAttach(image);
        super.onRender(target, index);
    }

    private void handlerClick(Element el) {
        if (!readOnly) {
            if (el == addHandler) {
                editWindow.show();
            }else if (el == delHandler) {
                dialog.show();
            }
        }
    }

    private native void regJs(Element el)/*-{
        var instance = this;
        el.onclick = function() {
            instance.@org.talend.mdm.webapp.browserecords.client.widget.inputfield.PictureField::handlerClick(Lcom/google/gwt/user/client/Element;)(this);
        };
    }-*/;

    @Override
    public void setValue(String value) {
        if ("http://".equalsIgnoreCase(value)) { //$NON-NLS-1$
            return;
        }
        
        String oldValue = this.value;
        this.value = value;

        if (value != null && value.length() != 0) {
            if (!value.startsWith("/")) //$NON-NLS-1$
                value = "/" + value; //$NON-NLS-1$
           
            if (!value.startsWith("/imageserver")) //$NON-NLS-1$
                this.value = "/imageserver" + value; //$NON-NLS-1$
            image.setUrl(this.value); 

        } else {
            image.setUrl(DefaultImage);
        }
        if (isFireChangeEventOnSetValue()) {
            fireChangeEvent(oldValue, value);
        }
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        this.readOnly = readOnly;
    }

    class EditWindow extends Window {

        private FormPanel editForm = new FormPanel();

        private FileUploadField file = new FileUploadField();
        
        PictureSelector pictureSelector = new PictureSelector(EditWindow.this,PictureField.this);   

        private SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                Button button = ce.getButton();
                if (button == uploadButton) {
                    editForm.submit();
                } else if (button == resetButton) {
                    editForm.reset();
                }
            }
        };

        private Button uploadButton = new Button(MessagesFactory.getMessages().button_upload(), listener);

        private Button resetButton = new Button(MessagesFactory.getMessages().button_reset(), listener);

        public EditWindow() {
            super();
            this.setLayout(new FitLayout());
            this.setHeading(MessagesFactory.getMessages().picture_select_title());
            this.setSize(460, 450);
            this.setModal(true);
            this.setBlinkModal(true);
            
            TabPanel uploadTabPanel = new TabPanel();
            TabItem localTabItem = new TabItem(MessagesFactory.getMessages().picture_upload_local_title());
            ContentPanel localContentPanel = new ContentPanel();
            
            FormData formData = new FormData();
            editForm.setEncoding(FormPanel.Encoding.MULTIPART);
            editForm.setMethod(FormPanel.Method.POST);
            editForm.setAction("/imageserver/secure/ImageUploadServlet"); //$NON-NLS-1$
            editForm.setHeaderVisible(false);
            editForm.setBodyBorder(false);
            editForm.setLabelWidth(110);

            MultiField imgIdRow = new MultiField();
            imgIdRow.setFieldLabel(MessagesFactory.getMessages().picture_field_imgid());

            final TextField<String> name = new TextField<String>();
            name.setFieldLabel(""); //$NON-NLS-1$
            name.setName("fileName"); //$NON-NLS-1$
            name.setAllowBlank(false);
           
            imgIdRow.add(name);
            final LabelField extFileNameLabel = new LabelField();
            imgIdRow.add(extFileNameLabel);
            
            MultiField catalogRow = new MultiField();
            catalogRow.setFieldLabel(MessagesFactory.getMessages().picture_field_imgcatalog());

            final TextField<String> catalog = new TextField<String>();
            catalog.setFieldLabel(""); //$NON-NLS-1$
            catalog.setName("catalogName"); //$NON-NLS-1$
            catalog.setAllowBlank(false);

            catalogRow.add(catalog);
            catalogRow.add(new LabelField());

            file.setAllowBlank(false);
            file.setName("imageFile");//$NON-NLS-1$
            file.setFieldLabel(MessagesFactory.getMessages().picture_field_label());
            file.setFireChangeEventOnSetValue(true);
            file.addListener(Events.Change, new Listener<FieldEvent>() {

                public void handleEvent(FieldEvent be) {
                    // reset imgId
                    catalog.setValue(""); //$NON-NLS-1$
                    name.setValue(""); //$NON-NLS-1$
                    // auto fill img id
                    if ((name.getValue() == null || name.getValue().isEmpty())
                            && (file.getValue() != null && !file.getValue().isEmpty())) {
                        String[] parsedFileName = CommonUtil.parseFileName(file.getValue());
                        name.setValue(parsedFileName[0]);
                        extFileNameLabel.setText(parsedFileName[1].length() == 0 ? "" : "." + parsedFileName[1]); //$NON-NLS-1$ //$NON-NLS-2$
                        // name.focus();
                    }
                }

            });

            editForm.add(file, formData);
            editForm.add(catalogRow, formData);
            editForm.add(imgIdRow, formData);
            editForm.addListener(Events.Submit, new Listener<FormEvent>() {

                public void handleEvent(FormEvent be) {
                    if (name.getValue() != null && name.getValue().trim().length() > 0  && catalog.getValue() != null && catalog.getValue().trim().length() > 0){
                        String json = be.getResultHtml();
                        JSONObject jsObject = JSONParser.parse(json).isObject();
                        JSONBoolean success = jsObject.get("success").isBoolean(); //$NON-NLS-1$
                        JSONString message = jsObject.get("message").isString(); //$NON-NLS-1$
                        if (success.booleanValue())
                            MessageBox.alert(MessagesFactory.getMessages().info_title(), MessagesFactory.getMessages()
                                    .upload_pic_ok(), null);
                        else
                            MessageBox.alert(MessagesFactory.getMessages().error_title(), MessagesFactory.getMessages()
                                    .upload_pic_fail(), null);
                        if (success.booleanValue()) {
                            setValue(message.stringValue());
                        } else {
                            setValue(null);
                        }
                        EditWindow.this.hide();
                    }
                }

            });
            
            localContentPanel.setStyleAttribute("margin", "20px");  //$NON-NLS-1$//$NON-NLS-2$
            localContentPanel.add(editForm);
            editForm.setLayout(new FormLayout());
            localContentPanel.setButtonAlign(HorizontalAlignment.CENTER);
            localContentPanel.addButton(uploadButton);
            localContentPanel.addButton(resetButton);
            localContentPanel.setHeaderVisible(false);
            localTabItem.add(localContentPanel);
            uploadTabPanel.add(localTabItem);  
          
            TabItem remoteTabItem = new TabItem(MessagesFactory.getMessages().picture_upload_remote_title()); 
            remoteTabItem.setLayout(new FitLayout());            
            remoteTabItem.add(pictureSelector); 
            uploadTabPanel.add(remoteTabItem);
            add(uploadTabPanel);
            this.setResizable(false);
        }

        @Override
        protected void onShow() {
            super.onShow();
            pictureSelector.refresh();
            editForm.reset();
        }
    }
}
