
package com.test.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class was generated by Apache CXF 2.7.4
 * Sat Apr 27 16:45:49 WIT 2013
 * Generated source version: 2.7.4
 */

@XmlRootElement(name = "helloNameResponse", namespace = "http://test.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "helloNameResponse", namespace = "http://test.com/")

public class HelloNameResponse {

    @XmlElement(name = "return")
    private java.lang.String _return;

    public java.lang.String getReturn() {
        return this._return;
    }

    public void setReturn(java.lang.String new_return)  {
        this._return = new_return;
    }

}

