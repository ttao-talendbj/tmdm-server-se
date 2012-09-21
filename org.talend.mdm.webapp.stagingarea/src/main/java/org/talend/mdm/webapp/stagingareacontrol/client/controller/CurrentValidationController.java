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
package org.talend.mdm.webapp.stagingareacontrol.client.controller;

import org.talend.mdm.webapp.base.client.SessionAwareAsyncCallback;
import org.talend.mdm.webapp.base.client.model.UserContextModel;
import org.talend.mdm.webapp.base.client.util.UserContextUtil;
import org.talend.mdm.webapp.stagingareacontrol.client.StagingareaControl;
import org.talend.mdm.webapp.stagingareacontrol.client.model.StagingAreaValidationModel;
import org.talend.mdm.webapp.stagingareacontrol.client.rest.RestServiceHandler;
import org.talend.mdm.webapp.stagingareacontrol.client.view.CurrentValidationView;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Timer;


public class CurrentValidationController extends AbstractController {

    private CurrentValidationView view;

    private Timer timer;

    public CurrentValidationController(final CurrentValidationView view) {
        setBindingView(view);
        this.view = (CurrentValidationView) bindingView;
        timer = new Timer() {

            @Override
            public void run() {
                if (Document.get().isOrHasChild(view.getElement())) {
                    refreshView();
                } else {
                    this.cancel();
                }
            }
        };
    }

    public void autoRefresh(boolean auto) {
        if (auto) {
            timer.scheduleRepeating(StagingareaControl.getStagingAreaConfig().getRefreshIntervals());
        } else {
            timer.cancel();
        }
    }

    public void refreshView() {
        UserContextModel ucx = UserContextUtil.getUserContext();
        RestServiceHandler.get().getValidationTaskStatus(ucx.getDataContainer(),
                new SessionAwareAsyncCallback<StagingAreaValidationModel>() {
                    public void onSuccess(StagingAreaValidationModel result) {
                        if (result != null) {
                            ControllerContainer.get().getSummaryController().setEnabledStartValidation(false);
                            view.setStatus(CurrentValidationView.Status.HasValidation);
                            view.refresh(result);
                        } else {
                            ControllerContainer.get().getSummaryController().setEnabledStartValidation(true);
                            view.setStatus(CurrentValidationView.Status.None);
                        }
                    }

                    @Override
                    protected void doOnFailure(Throwable caught) {
                        ControllerContainer.get().getSummaryController().setEnabledStartValidation(true);
                        view.setStatus(CurrentValidationView.Status.None);
                    }
                });
    }

    public void cancelValidation() {
        UserContextModel ucx = UserContextUtil.getUserContext();
        RestServiceHandler.get().cancelValidationTask(ucx.getDataContainer(),
                new SessionAwareAsyncCallback<Boolean>() {
            public void onSuccess(Boolean result) {
                if (result){
                    autoRefresh(false);
                    view.setStatus(CurrentValidationView.Status.None);
                    ControllerContainer.get().getSummaryController().setEnabledStartValidation(true);
                }
            }
        });
    }
}
