/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aggregator;

import entities.LoanResponse;
import entities.Message;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author williambech
 */
public class AggregatorFactory {
   
    long ssn;
    ArrayList<LoanResponse> messages;
    Date timeCreated;

    AggregatorFactory(long ssn) {
        this.ssn = ssn;
        this.timeCreated = new Date();
        this.messages = new ArrayList<>();
    }

    public long getSsn() {
        return ssn;
    }

    public void addMessage(LoanResponse message) {
        this.messages.add(message);
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public long getWaitTime() {
        Date date = new Date();
        long timePassed = (date.getTime()- getTimeCreated().getTime()) / 1000;
    
        return timePassed;
    }

    public LoanResponse getBestBank() {
        LoanResponse bestBank = messages.get(0);
        try {
            for (int i = 1; i < messages.size(); i++) {
                if (this.messages.get(i).getInterestRate() < bestBank.getInterestRate()) {
                    bestBank = messages.get(i);
                }
            }
        } catch (Exception exception) {
            System.out.println("Something went wrong: getBestBank");
        }

        return bestBank;
    }
    
    
    
}
