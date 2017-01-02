/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.ivr.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.bvukic.telehealth.ivr.util.model.MalformedSipAddressException;
import si.bvukic.telehealth.ivr.util.model.TelType;



/**
 *
 * @author VukicB
 */
public class SipAddressParser {
    
    private static final Logger LOG = LoggerFactory.getLogger(SipAddressParser.class);
    private static final String TEL_PATTERN = "^sip:\\+?([0-9A-Fa-f]+)@.*$";
    private static final String ANONYMOUS_PATTERN = "^sip:\\+?0?(?:anonymous|unknown|unavailable|notset|unset|null)@.*$";
    
    
    /**
     * Parses the sip address string and returns the telephone number with leading 0
     * @param sipAddress
     * @param type Supported types are TelType.INTERNATIONAL, TelType.NATIONAL
     * @return
     * @throws MalformedSipAddressException 
     */
    public static String getTel(String sipAddress, TelType type) throws MalformedSipAddressException {
        LOG.debug("Input: {}", sipAddress);
        Pattern anonymous = Pattern.compile(ANONYMOUS_PATTERN, Pattern.CASE_INSENSITIVE);
        if (anonymous.matcher(sipAddress).matches()) {
            LOG.debug("Output: {}", "anonymous");
            return "anonymous";
        }
        Pattern pattern = Pattern.compile(TEL_PATTERN);
        Matcher matcher = pattern.matcher(sipAddress);
        
        if (matcher.matches()) {
            String tel = matcher.group(1);
            
            switch (type) {
                case NATIONAL:
                    // Add leading 0 if the number is without leading 0
                    tel = (!tel.startsWith("0")) ?  "0" + tel : tel;            
                    // Add leading 0 if number is international
                    tel = (tel.length() > 9 ) ?  "0" + tel : tel;
                    // Make slovenian inernational numbers national
                    tel = (tel.startsWith("00386")) ? "0" + tel.substring(5) : tel;
                    break;
                case INTERNATIONAL:
                    //Remove leading 0 if number is local (shorter than 10 digits) and make it international
                    tel = (tel.startsWith("0") && tel.length() < 10 ) ? "00386" + tel.substring(1) : tel;
                    // Add leading 00 if number starts with 386 and it is slovenian
                    tel = (tel.startsWith("386") && tel.length() > 10 ) ? "00" + tel : tel;
                    // Add leading 00386 if number is local 
                    tel = (tel.length() < 9 ) ?  "00386" + tel : tel;
                    // Add leading 0 if international number has only 1 leading 0
                    tel = (!tel.startsWith("00") ) ?  "0" + tel : tel;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown TelType: " + type);
            }
            LOG.debug("Output: {}", tel);
            return tel;
        } else {
            LOG.error("Malformed sipAddress: {}", sipAddress);
            throw new MalformedSipAddressException();
        }
       
    }
    
}
