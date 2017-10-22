
package entities;

/**
 *
 * @author Luke
 */
public class LoanResponse {
   long ssn;
   double interestRate;
   String bankName;

    public LoanResponse(long ssn, double interestRate, String bank) {
        this.ssn = ssn;
        this.interestRate = interestRate;
        this.bankName = bank;
    }

    public LoanResponse(long ssn, double interestRate) {
        this.ssn = ssn;
        this.interestRate = interestRate;
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
        return bankName;
    }

    public void setBank(String bank) {
        this.bankName = bank;
    }
}
