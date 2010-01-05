// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation ��1.1.2_01������� R40��
// Generated source version: 1.1.2

package com.amalto.core.webservice;


import java.util.Map;
import java.util.HashMap;

public class WSWhereOperator {
    private java.lang.String value;
    private static Map valueMap = new HashMap();
    public static final String _JOINString = "JOIN";
    public static final String _CONTAINSString = "CONTAINS";
    public static final String _STARTSWITHString = "STARTSWITH";
    public static final String _STRICTCONTAINSString = "STRICTCONTAINS";
    public static final String _EQUALSString = "EQUALS";
    public static final String _NOT_EQUALSString = "NOT_EQUALS";
    public static final String _GREATER_THANString = "GREATER_THAN";
    public static final String _GREATER_THAN_OR_EQUALString = "GREATER_THAN_OR_EQUAL";
    public static final String _LOWER_THANString = "LOWER_THAN";
    public static final String _LOWER_THAN_OR_EQUALString = "LOWER_THAN_OR_EQUAL";
    public static final String _NO_OPERATORString = "NO_OPERATOR";
    public static final String _FULLTEXTSEARCHString = "FULLTEXTSEARCH";
    
    public static final java.lang.String _JOIN = new java.lang.String(_JOINString);
    public static final java.lang.String _CONTAINS = new java.lang.String(_CONTAINSString);
    public static final java.lang.String _STARTSWITH = new java.lang.String(_STARTSWITHString);
    public static final java.lang.String _STRICTCONTAINS = new java.lang.String(_STRICTCONTAINSString);
    public static final java.lang.String _EQUALS = new java.lang.String(_EQUALSString);
    public static final java.lang.String _NOT_EQUALS = new java.lang.String(_NOT_EQUALSString);
    public static final java.lang.String _GREATER_THAN = new java.lang.String(_GREATER_THANString);
    public static final java.lang.String _GREATER_THAN_OR_EQUAL = new java.lang.String(_GREATER_THAN_OR_EQUALString);
    public static final java.lang.String _LOWER_THAN = new java.lang.String(_LOWER_THANString);
    public static final java.lang.String _LOWER_THAN_OR_EQUAL = new java.lang.String(_LOWER_THAN_OR_EQUALString);
    public static final java.lang.String _NO_OPERATOR = new java.lang.String(_NO_OPERATORString);
    public static final java.lang.String _FULLTEXTSEARCH = new java.lang.String(_FULLTEXTSEARCHString);
    
    public static final WSWhereOperator JOIN = new WSWhereOperator(_JOIN);
    public static final WSWhereOperator CONTAINS = new WSWhereOperator(_CONTAINS);
    public static final WSWhereOperator STARTSWITH = new WSWhereOperator(_STARTSWITH);
    public static final WSWhereOperator STRICTCONTAINS = new WSWhereOperator(_STRICTCONTAINS);
    public static final WSWhereOperator EQUALS = new WSWhereOperator(_EQUALS);
    public static final WSWhereOperator NOT_EQUALS = new WSWhereOperator(_NOT_EQUALS);
    public static final WSWhereOperator GREATER_THAN = new WSWhereOperator(_GREATER_THAN);
    public static final WSWhereOperator GREATER_THAN_OR_EQUAL = new WSWhereOperator(_GREATER_THAN_OR_EQUAL);
    public static final WSWhereOperator LOWER_THAN = new WSWhereOperator(_LOWER_THAN);
    public static final WSWhereOperator LOWER_THAN_OR_EQUAL = new WSWhereOperator(_LOWER_THAN_OR_EQUAL);
    public static final WSWhereOperator NO_OPERATOR = new WSWhereOperator(_NO_OPERATOR);
    public static final WSWhereOperator FULLTEXTSEARCH = new WSWhereOperator(_FULLTEXTSEARCH);
    
    protected WSWhereOperator(java.lang.String value) {
        this.value = value;
        valueMap.put(this.toString(), this);
    }
    
    public java.lang.String getValue() {
        return value;
    }
    
    public static WSWhereOperator fromValue(java.lang.String value)
        throws java.lang.IllegalStateException {
        if (JOIN.value.equals(value)) {
            return JOIN;
        } else if (CONTAINS.value.equals(value)) {
            return CONTAINS;
        } else if (STARTSWITH.value.equals(value)) {
            return STARTSWITH;
        } else if (STRICTCONTAINS.value.equals(value)) {
            return STRICTCONTAINS;
        } else if (EQUALS.value.equals(value)) {
            return EQUALS;
        } else if (NOT_EQUALS.value.equals(value)) {
            return NOT_EQUALS;
        } else if (GREATER_THAN.value.equals(value)) {
            return GREATER_THAN;
        } else if (GREATER_THAN_OR_EQUAL.value.equals(value)) {
            return GREATER_THAN_OR_EQUAL;
        } else if (LOWER_THAN.value.equals(value)) {
            return LOWER_THAN;
        } else if (LOWER_THAN_OR_EQUAL.value.equals(value)) {
            return LOWER_THAN_OR_EQUAL;
        } else if (NO_OPERATOR.value.equals(value)) {
            return NO_OPERATOR;
        } else if (FULLTEXTSEARCH.value.equals(value)) {
            return FULLTEXTSEARCH;
        }
        throw new IllegalArgumentException();
    }
    
    public static WSWhereOperator fromString(String value)
        throws java.lang.IllegalStateException {
        WSWhereOperator ret = (WSWhereOperator)valueMap.get(value);
        if (ret != null) {
            return ret;
        }
        if (value.equals(_JOINString)) {
            return JOIN;
        } else if (value.equals(_CONTAINSString)) {
            return CONTAINS;
        } else if (value.equals(_STARTSWITHString)) {
            return STARTSWITH;
        } else if (value.equals(_STRICTCONTAINSString)) {
            return STRICTCONTAINS;
        } else if (value.equals(_EQUALSString)) {
            return EQUALS;
        } else if (value.equals(_NOT_EQUALSString)) {
            return NOT_EQUALS;
        } else if (value.equals(_GREATER_THANString)) {
            return GREATER_THAN;
        } else if (value.equals(_GREATER_THAN_OR_EQUALString)) {
            return GREATER_THAN_OR_EQUAL;
        } else if (value.equals(_LOWER_THANString)) {
            return LOWER_THAN;
        } else if (value.equals(_LOWER_THAN_OR_EQUALString)) {
            return LOWER_THAN_OR_EQUAL;
        } else if (value.equals(_NO_OPERATORString)) {
            return NO_OPERATOR;
        } else if (value.equals(_FULLTEXTSEARCHString)) {
            return FULLTEXTSEARCH;
        }
        throw new IllegalArgumentException();
    }
    
    public String toString() {
        return value.toString();
    }
    
    private Object readResolve()
        throws java.io.ObjectStreamException {
        return fromValue(getValue());
    }
    
    public boolean equals(Object obj) {
        if (!(obj instanceof WSWhereOperator)) {
            return false;
        }
        return ((WSWhereOperator)obj).value.equals(value);
    }
    
    public int hashCode() {
        return value.hashCode();
    }
}
