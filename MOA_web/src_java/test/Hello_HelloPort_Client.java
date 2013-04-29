
package test;

/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

/**
 * This class was generated by Apache CXF 2.7.4
 * 2013-04-27T22:05:02.081+07:00
 * Generated source version: 2.7.4
 * 
 */
public final class Hello_HelloPort_Client {

    private static final QName SERVICE_NAME = new QName("http://test.com/", "HelloService");

    private Hello_HelloPort_Client() {
    }

    public static void main(String args[]) throws java.lang.Exception {
        
    	JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();

        factory.setServiceClass(com.test.Hello.class);
        factory.setAddress("http://localhost:8080/MOA_services/services/Hello?wsdl");
        factory.getInInterceptors().add(new LoggingInInterceptor());
        factory.getOutInterceptors().add(new LoggingOutInterceptor());
        com.test.Hello client = (com.test.Hello) factory.create();
        String result = client.helloName("Jordan");
        System.out.println("Server said: " + result);
        System.exit(0);
    	
    	/*URL wsdlURL = HelloService.WSDL_LOCATION;
        if (args.length > 0 && args[0] != null && !"".equals(args[0])) { 
            File wsdlFile = new File(args[0]);
            try {
                if (wsdlFile.exists()) {
                    wsdlURL = wsdlFile.toURI().toURL();
                } else {
                    wsdlURL = new URL(args[0]);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
      
        HelloService ss = new HelloService(wsdlURL, SERVICE_NAME);
        Hello port = ss.getHelloPort();  
        
        {
        System.out.println("Invoking helloName...");
        java.lang.String _helloName_arg0 = "_helloName_arg0-1148696426";
        java.lang.String _helloName__return = port.helloName(_helloName_arg0);
        System.out.println("helloName.result=" + _helloName__return);


        }

        System.exit(0);*/
    }

}
