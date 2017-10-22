package config;

/**
 *
 * @author Luke
 */
public class Constants {
    // Queues
    public static final String SENDING_GET_CREDIT_SCORE = "XYZ_A";
    public static final String LISTENING_GET_BANKS = SENDING_GET_CREDIT_SCORE;

    public static final String SENDING_GET_BANKS = "XYZ_B";
    public static final String LISTENING_RECIP_LIST = SENDING_GET_BANKS;
    
    public static final String TRANSLATOR_EXCHANGE = "XYZ_C";
    
    public static final String SENDING_OUR_JSON_TRANSLATOR = "XYZ_D";
    public static final String LISTENING_OUR_JSON_BANK = SENDING_OUR_JSON_TRANSLATOR;

    public static final String SENDING_OUR_XML_TRANSLATOR = "XYZ_E";
    public static final String SENDING_SCHOOL_JSON_TRANSLATOR = "XYZ_F";
    public static final String SENDING_SCHOOL_XML_TRANSLATOR = "XYZ_G";
    
    public static final String SENDING_BANKS = "XYZ_H";


    
    
// Bank names
    public static final String SCHOOL_JSON = "cphbusiness.bankJSON";
    public static final String SCHOOL_XML = "cphbusiness.bankXML";

    public static final String OUR_JSON = "Gringotts";
    public static final String OUR_XML = "BumBank";

}
