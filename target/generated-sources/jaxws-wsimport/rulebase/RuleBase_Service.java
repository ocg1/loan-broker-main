
package rulebase;

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
@WebServiceClient(name = "RuleBase", targetNamespace = "http://rulebase/", wsdlLocation = "http://localhost:8080/RuleBase?wsdl")
public class RuleBase_Service
    extends Service
{

    private final static URL RULEBASE_WSDL_LOCATION;
    private final static WebServiceException RULEBASE_EXCEPTION;
    private final static QName RULEBASE_QNAME = new QName("http://rulebase/", "RuleBase");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:8080/RuleBase?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        RULEBASE_WSDL_LOCATION = url;
        RULEBASE_EXCEPTION = e;
    }

    public RuleBase_Service() {
        super(__getWsdlLocation(), RULEBASE_QNAME);
    }

    public RuleBase_Service(WebServiceFeature... features) {
        super(__getWsdlLocation(), RULEBASE_QNAME, features);
    }

    public RuleBase_Service(URL wsdlLocation) {
        super(wsdlLocation, RULEBASE_QNAME);
    }

    public RuleBase_Service(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, RULEBASE_QNAME, features);
    }

    public RuleBase_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public RuleBase_Service(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns RuleBase
     */
    @WebEndpoint(name = "RuleBasePort")
    public RuleBase getRuleBasePort() {
        return super.getPort(new QName("http://rulebase/", "RuleBasePort"), RuleBase.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns RuleBase
     */
    @WebEndpoint(name = "RuleBasePort")
    public RuleBase getRuleBasePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://rulebase/", "RuleBasePort"), RuleBase.class, features);
    }

    private static URL __getWsdlLocation() {
        if (RULEBASE_EXCEPTION!= null) {
            throw RULEBASE_EXCEPTION;
        }
        return RULEBASE_WSDL_LOCATION;
    }

}
