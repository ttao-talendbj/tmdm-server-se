// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation （1.1.2_01，编译版 R40）
// Generated source version: 1.1.2

package com.amalto.core.webservice;


import java.util.Map;
import java.util.HashMap;

public class WSLanguage {
    private java.lang.String value;
    private static Map valueMap = new HashMap();
    public static final String _FRString = "FR";
    public static final String _ENString = "EN";
    
    public static final java.lang.String _FR = new java.lang.String(_FRString);
    public static final java.lang.String _EN = new java.lang.String(_ENString);
    
    public static final WSLanguage FR = new WSLanguage(_FR);
    public static final WSLanguage EN = new WSLanguage(_EN);
    
    protected WSLanguage(java.lang.String value) {
        this.value = value;
        valueMap.put(this.toString(), this);
    }
    
    public java.lang.String getValue() {
        return value;
    }
    
    public static WSLanguage fromValue(java.lang.String value)
        throws java.lang.IllegalStateException {
        if (FR.value.equals(value)) {
            return FR;
        } else if (EN.value.equals(value)) {
            return EN;
        }
        throw new IllegalArgumentException();
    }
    
    public static WSLanguage fromString(String value)
        throws java.lang.IllegalStateException {
        WSLanguage ret = (WSLanguage)valueMap.get(value);
        if (ret != null) {
            return ret;
        }
        if (value.equals(_FRString)) {
            return FR;
        } else if (value.equals(_ENString)) {
            return EN;
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
        if (!(obj instanceof WSLanguage)) {
            return false;
        }
        return ((WSLanguage)obj).value.equals(value);
    }
    
    public int hashCode() {
        return value.hashCode();
    }
}
