package entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Luke
 */
@XmlRootElement(name = "LoanResponse")
public class LoanXMLResponse {

    @XmlElement
    long ssn;

    @XmlElement
    double interestRate;

    @XmlElement
    String bankName;

    public long getSsn() {
        return ssn;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public String getBank() {
        return bankName;
    }

    public void setBank(String bankName) {
        this.bankName = bankName;
    }
}
