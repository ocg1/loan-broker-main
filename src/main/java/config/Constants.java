package config;

/**
 *
 * @author Luke
 */
public class Constants {
    // Bank names
    public static final String SCHOOL_JSON = "cphbusiness.bankJSON";
    public static final String SCHOOL_XML = "cphbusiness.bankXML";
    public static final String OUR_JSON = "Gringotts";
    public static final String OUR_XML = "BumBank";

    // Queues //
    // between enrichers
    public static final String SENDING_GET_CREDIT_SCORE = "LW: GetCreditScore-to-GetBanks";
    public static final String LISTENING_GET_BANKS = SENDING_GET_CREDIT_SCORE;

    // between getBanks and Recipient List
    public static final String SENDING_GET_BANKS = "LW: GetBanks-to-RecipList";
    public static final String LISTENING_RECIP_LIST = SENDING_GET_BANKS;
    
    // translator exchange
    public static final String TRANSLATOR_EXCHANGE = "LW: Translator-Exchange";
    
    // between gringotts translator and gringotts
    public static final String SENDING_OUR_JSON_TRANSLATOR = "LW: GringottsTranslator-to-Gringotts";
    public static final String LISTENING_OUR_JSON_BANK = SENDING_OUR_JSON_TRANSLATOR;

    // school banks
    public static final String SENDING_SCHOOL_JSON_TRANSLATOR = SCHOOL_JSON;
    public static final String SENDING_SCHOOL_XML_TRANSLATOR = SCHOOL_XML;
    
    // between all the banks and normalizer
    public static final String SENDING_BANKS = "LW: Banks-to-Normalizer";
    public static final String LISTENING_NORMALIZER = "LW: Banks-to-Normalizer";

    // aggregator exchange
    public static final String AGGREGATOR_EXCHANGE = "LW: Aggregator-Exchange";
    public static final String LISTENING_AGGREGATOR = AGGREGATOR_EXCHANGE;
}
