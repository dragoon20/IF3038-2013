
package test;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the test package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _HelloName_QNAME = new QName("http://test.com/", "helloName");
    private final static QName _HelloNameResponse_QNAME = new QName("http://test.com/", "helloNameResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: test
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link HelloName }
     * 
     */
    public HelloName createHelloName() {
        return new HelloName();
    }

    /**
     * Create an instance of {@link HelloNameResponse }
     * 
     */
    public HelloNameResponse createHelloNameResponse() {
        return new HelloNameResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HelloName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://test.com/", name = "helloName")
    public JAXBElement<HelloName> createHelloName(HelloName value) {
        return new JAXBElement<HelloName>(_HelloName_QNAME, HelloName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HelloNameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://test.com/", name = "helloNameResponse")
    public JAXBElement<HelloNameResponse> createHelloNameResponse(HelloNameResponse value) {
        return new JAXBElement<HelloNameResponse>(_HelloNameResponse_QNAME, HelloNameResponse.class, null, value);
    }

}
