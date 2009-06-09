// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation ��1.1.2_01������� R40��
// Generated source version: 1.1.2

package com.amalto.webapp.util.webservices;

import com.sun.xml.rpc.encoding.*;
import com.sun.xml.rpc.encoding.xsd.XSDConstants;
import com.sun.xml.rpc.encoding.literal.*;
import com.sun.xml.rpc.encoding.literal.DetailFragmentDeserializer;
import com.sun.xml.rpc.encoding.simpletype.*;
import com.sun.xml.rpc.encoding.soap.SOAPConstants;
import com.sun.xml.rpc.encoding.soap.SOAP12Constants;
import com.sun.xml.rpc.streaming.*;
import com.sun.xml.rpc.wsdl.document.schema.SchemaConstants;
import javax.xml.namespace.QName;
import java.util.List;
import java.util.ArrayList;

public class WSWhereCondition_LiteralSerializer extends LiteralObjectSerializerBase implements Initializable  {
    private static final QName ns1_leftPath_QNAME = new QName("", "leftPath");
    private static final QName ns3_string_TYPE_QNAME = SchemaConstants.QNAME_TYPE_STRING;
    private CombinedSerializer ns3_myns3_string__java_lang_String_String_Serializer;
    private static final QName ns1_operator_QNAME = new QName("", "operator");
    private static final QName ns2_WSWhereOperator_TYPE_QNAME = new QName("urn-com-amalto-xtentis-webservice", "WSWhereOperator");
    private CombinedSerializer ns2myns2_WSWhereOperator__WSWhereOperator_LiteralSerializer;
    private static final QName ns1_rightValueOrPath_QNAME = new QName("", "rightValueOrPath");
    private static final QName ns1_stringPredicate_QNAME = new QName("", "stringPredicate");
    private static final QName ns2_WSStringPredicate_TYPE_QNAME = new QName("urn-com-amalto-xtentis-webservice", "WSStringPredicate");
    private CombinedSerializer ns2myns2_WSStringPredicate__WSStringPredicate_LiteralSerializer;
    private static final QName ns1_spellCheck_QNAME = new QName("", "spellCheck");
    private static final QName ns3_boolean_TYPE_QNAME = SchemaConstants.QNAME_TYPE_BOOLEAN;
    private CombinedSerializer ns3_myns3__boolean__boolean_Boolean_Serializer;
    
    public WSWhereCondition_LiteralSerializer(QName type, String encodingStyle) {
        this(type, encodingStyle, false);
    }
    
    public WSWhereCondition_LiteralSerializer(QName type, String encodingStyle, boolean encodeType) {
        super(type, true, encodingStyle, encodeType);
    }
    
    public void initialize(InternalTypeMappingRegistry registry) throws Exception {
        ns3_myns3_string__java_lang_String_String_Serializer = (CombinedSerializer)registry.getSerializer("", java.lang.String.class, ns3_string_TYPE_QNAME);
        ns2myns2_WSWhereOperator__WSWhereOperator_LiteralSerializer = (CombinedSerializer)registry.getSerializer("", com.amalto.webapp.util.webservices.WSWhereOperator.class, ns2_WSWhereOperator_TYPE_QNAME);
        ns2myns2_WSStringPredicate__WSStringPredicate_LiteralSerializer = (CombinedSerializer)registry.getSerializer("", com.amalto.webapp.util.webservices.WSStringPredicate.class, ns2_WSStringPredicate_TYPE_QNAME);
        ns3_myns3__boolean__boolean_Boolean_Serializer = (CombinedSerializer)registry.getSerializer("", boolean.class, ns3_boolean_TYPE_QNAME);
    }
    
    public Object doDeserialize(XMLReader reader,
        SOAPDeserializationContext context) throws Exception {
        com.amalto.webapp.util.webservices.WSWhereCondition instance = new com.amalto.webapp.util.webservices.WSWhereCondition();
        Object member=null;
        QName elementName;
        List values;
        Object value;
        
        reader.nextElementContent();
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_leftPath_QNAME)) {
                member = ns3_myns3_string__java_lang_String_String_Serializer.deserialize(ns1_leftPath_QNAME, reader, context);
                if (member == null) {
                    throw new DeserializationException("literal.unexpectedNull");
                }
                instance.setLeftPath((java.lang.String)member);
                reader.nextElementContent();
            } else {
                throw new DeserializationException("literal.unexpectedElementName", new Object[] { ns1_leftPath_QNAME, reader.getName() });
            }
        }
        else {
            throw new DeserializationException("literal.expectedElementName", reader.getName().toString());
        }
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_operator_QNAME)) {
                member = ns2myns2_WSWhereOperator__WSWhereOperator_LiteralSerializer.deserialize(ns1_operator_QNAME, reader, context);
                if (member == null) {
                    throw new DeserializationException("literal.unexpectedNull");
                }
                instance.setOperator((com.amalto.webapp.util.webservices.WSWhereOperator)member);
                reader.nextElementContent();
            } else {
                throw new DeserializationException("literal.unexpectedElementName", new Object[] { ns1_operator_QNAME, reader.getName() });
            }
        }
        else {
            throw new DeserializationException("literal.expectedElementName", reader.getName().toString());
        }
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_rightValueOrPath_QNAME)) {
                member = ns3_myns3_string__java_lang_String_String_Serializer.deserialize(ns1_rightValueOrPath_QNAME, reader, context);
                if (member == null) {
                    throw new DeserializationException("literal.unexpectedNull");
                }
                instance.setRightValueOrPath((java.lang.String)member);
                reader.nextElementContent();
            } else {
                throw new DeserializationException("literal.unexpectedElementName", new Object[] { ns1_rightValueOrPath_QNAME, reader.getName() });
            }
        }
        else {
            throw new DeserializationException("literal.expectedElementName", reader.getName().toString());
        }
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_stringPredicate_QNAME)) {
                member = ns2myns2_WSStringPredicate__WSStringPredicate_LiteralSerializer.deserialize(ns1_stringPredicate_QNAME, reader, context);
                if (member == null) {
                    throw new DeserializationException("literal.unexpectedNull");
                }
                instance.setStringPredicate((com.amalto.webapp.util.webservices.WSStringPredicate)member);
                reader.nextElementContent();
            } else {
                throw new DeserializationException("literal.unexpectedElementName", new Object[] { ns1_stringPredicate_QNAME, reader.getName() });
            }
        }
        else {
            throw new DeserializationException("literal.expectedElementName", reader.getName().toString());
        }
        elementName = reader.getName();
        if (reader.getState() == XMLReader.START) {
            if (elementName.equals(ns1_spellCheck_QNAME)) {
                member = ns3_myns3__boolean__boolean_Boolean_Serializer.deserialize(ns1_spellCheck_QNAME, reader, context);
                if (member == null) {
                    throw new DeserializationException("literal.unexpectedNull");
                }
                instance.setSpellCheck(((Boolean)member).booleanValue());
                reader.nextElementContent();
            } else {
                throw new DeserializationException("literal.unexpectedElementName", new Object[] { ns1_spellCheck_QNAME, reader.getName() });
            }
        }
        else {
            throw new DeserializationException("literal.expectedElementName", reader.getName().toString());
        }
        
        XMLReaderUtil.verifyReaderState(reader, XMLReader.END);
        return (Object)instance;
    }
    
    public void doSerializeAttributes(Object obj, XMLWriter writer, SOAPSerializationContext context) throws Exception {
        com.amalto.webapp.util.webservices.WSWhereCondition instance = (com.amalto.webapp.util.webservices.WSWhereCondition)obj;
        
    }
    public void doSerialize(Object obj, XMLWriter writer, SOAPSerializationContext context) throws Exception {
        com.amalto.webapp.util.webservices.WSWhereCondition instance = (com.amalto.webapp.util.webservices.WSWhereCondition)obj;
        
        if (instance.getLeftPath() == null) {
            throw new SerializationException("literal.unexpectedNull");
        }
        ns3_myns3_string__java_lang_String_String_Serializer.serialize(instance.getLeftPath(), ns1_leftPath_QNAME, null, writer, context);
        if (instance.getOperator() == null) {
            throw new SerializationException("literal.unexpectedNull");
        }
        ns2myns2_WSWhereOperator__WSWhereOperator_LiteralSerializer.serialize(instance.getOperator(), ns1_operator_QNAME, null, writer, context);
        if (instance.getRightValueOrPath() == null) {
            throw new SerializationException("literal.unexpectedNull");
        }
        ns3_myns3_string__java_lang_String_String_Serializer.serialize(instance.getRightValueOrPath(), ns1_rightValueOrPath_QNAME, null, writer, context);
        if (instance.getStringPredicate() == null) {
            throw new SerializationException("literal.unexpectedNull");
        }
        ns2myns2_WSStringPredicate__WSStringPredicate_LiteralSerializer.serialize(instance.getStringPredicate(), ns1_stringPredicate_QNAME, null, writer, context);
        if (new Boolean(instance.isSpellCheck()) == null) {
            throw new SerializationException("literal.unexpectedNull");
        }
        ns3_myns3__boolean__boolean_Boolean_Serializer.serialize(new Boolean(instance.isSpellCheck()), ns1_spellCheck_QNAME, null, writer, context);
    }
}
