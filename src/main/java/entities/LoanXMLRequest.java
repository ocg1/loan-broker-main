package entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Luke
 */
//@XmlType(factoryMethod = "newInstance")
@XmlRootElement(name = "LoanRequest")
public class LoanXMLRequest {

    @XmlElement
    private final long ssn;

    @XmlElement
    private final int creditScore;

    @XmlElement
    private final double loanAmount;

    @XmlElement
    private final String loanDuration;

    public LoanXMLRequest(long ssn, int creditScore, double loanAmount, String loanDuration) {
        this.ssn = ssn;
        this.creditScore = creditScore;
        this.loanAmount = loanAmount;
        this.loanDuration = loanDuration;
    }

    private LoanXMLRequest() {
        this.ssn = 0;
        this.creditScore = 0;
        this.loanAmount = 0.0;
        this.loanDuration = "";
    }

//    public static Result newInstance() {
//        return new Result();
//    }

//    The bank at exchange cphbusiness.bankXML is XML based.
//    It’s loan requests have this format:
//
//    <LoanRequest>
//        <ssn>12345678</ssn>
//        <creditScore>685</creditScore>
//        <loanAmount>1000.0</loanAmount>
//        <loanDuration>1973-01-01 01:00:00.0 CET</loanDuration>
//    </LoanRequest>
}
