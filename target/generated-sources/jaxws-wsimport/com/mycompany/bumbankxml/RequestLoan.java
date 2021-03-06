
package com.mycompany.bumbankxml;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.8
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "requestLoan", targetNamespace = "http://bumbankxml.mycompany.com/", wsdlLocation = "http://localhost:8080/BumBankXML/requestLoan?wsdl")
public class RequestLoan
    extends Service
{

    private final static URL REQUESTLOAN_WSDL_LOCATION;
    private final static WebServiceException REQUESTLOAN_EXCEPTION;
    private final static QName REQUESTLOAN_QNAME = new QName("http://bumbankxml.mycompany.com/", "requestLoan");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:8080/BumBankXML/requestLoan?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        REQUESTLOAN_WSDL_LOCATION = url;
        REQUESTLOAN_EXCEPTION = e;
    }

    public RequestLoan() {
        super(__getWsdlLocation(), REQUESTLOAN_QNAME);
    }

    public RequestLoan(WebServiceFeature... features) {
        super(__getWsdlLocation(), REQUESTLOAN_QNAME, features);
    }

    public RequestLoan(URL wsdlLocation) {
        super(wsdlLocation, REQUESTLOAN_QNAME);
    }

    public RequestLoan(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, REQUESTLOAN_QNAME, features);
    }

    public RequestLoan(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public RequestLoan(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns BumBankXML
     */
    @WebEndpoint(name = "BumBankXMLPort")
    public BumBankXML getBumBankXMLPort() {
        return super.getPort(new QName("http://bumbankxml.mycompany.com/", "BumBankXMLPort"), BumBankXML.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns BumBankXML
     */
    @WebEndpoint(name = "BumBankXMLPort")
    public BumBankXML getBumBankXMLPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://bumbankxml.mycompany.com/", "BumBankXMLPort"), BumBankXML.class, features);
    }

    private static URL __getWsdlLocation() {
        if (REQUESTLOAN_EXCEPTION!= null) {
            throw REQUESTLOAN_EXCEPTION;
        }
        return REQUESTLOAN_WSDL_LOCATION;
    }

}
