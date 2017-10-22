package entities;

/**
 *
 * @author Luke
 */
public class Message {

    private long ssn;
    private int creditScore;
    private double loanAmount;
    private int loanDuration;
    private String[] banks;

    public Message(long ssn, int creditScore, double loanAmount, int loanDuration) {
        this.ssn = ssn;
        this.creditScore = creditScore;
        this.loanAmount = loanAmount;
        this.loanDuration = loanDuration;
    }

    public Message(long ssn, int creditScore, double loanAmount, int loanDuration, String[] banks) {
        this.ssn = ssn;
        this.creditScore = creditScore;
        this.loanAmount = loanAmount;
        this.loanDuration = loanDuration;
        this.banks = banks;
    }

    public long getSsn() {
        return ssn;
    }

    public void setSsn(long ssn) {
        this.ssn = ssn;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public int getLoanDuration() {
        return loanDuration;
    }

    public void setLoanDuration(int loanDuration) {
        this.loanDuration = loanDuration;
    }

    public String[] getBanks() {
        return banks;
    }

    public void setBanks(String[] banks) {
        this.banks = banks;
    }
}
