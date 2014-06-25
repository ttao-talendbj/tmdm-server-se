// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.mdm.webapp.itemsbrowser2.shared;

import java.util.ArrayList;
import java.util.List;

import org.talend.mdm.webapp.base.client.model.DataType;
import org.talend.mdm.webapp.base.shared.TypeModel;

/**
 * DOC chliu class global comment. Detailled comment
 */
public class ComplexTypeModel extends TypeModel {

    private static final long serialVersionUID = 1L;

    private List<TypeModel> subTypes = new ArrayList<TypeModel>();

    public ComplexTypeModel() {
        super();
    }

    public ComplexTypeModel(String name, DataType dataType) {
        super(name, dataType);
    }

    public List<TypeModel> getSubTypes() {
        return subTypes;
    }

    public void addSubType(TypeModel subType) {

        if (this.subTypes != null)
            subTypes.add(subType);
    }

    @Override
    public boolean isSimpleType() {
        return false;
    }

    @Override
    public boolean hasEnumeration() {
        return false;
    }
}