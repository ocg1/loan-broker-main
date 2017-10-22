
package entities;

/**
 *
 * @author Luke
 */
public class LoanResponse {
   long ssn;
   double interestRate;
   String bank;

    public LoanResponse(long ssn, double interestRate, String bank) {
        this.ssn = ssn;
        this.interestRate = interestRate;
        this.bank = bank;
    }

    public long getSsn() {
        return ssn;
    }

    public void setSsn(long ssn) {
        this.ssn = ssn;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }
}
