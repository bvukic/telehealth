/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.ivr.util;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import si.bvukic.telehealth.ivr.util.model.TelType;

/**
 *
 * @author vukicb
 */
public class SipAddressParserTest {
    
    public SipAddressParserTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

     /**
     * Test of getTel method, of class SipAddressParser.
     */
    @Test
    public void testGetTel() throws Exception {
        
        List<String> sipAddressList = new ArrayList();
        List<String> expResultListNational = new ArrayList();
        List<String> expResultListInternational = new ArrayList();
        
        sipAddressList.add("sip:14720988@172.18.22.200");
        sipAddressList.add("sip:038614720988@172.18.22.200");
        sipAddressList.add("sip:014324147208988@172.18.22.200:iser=phone");
        sipAddressList.add("sip:+38651952000@ocmpt.ims.mobitel.si;reason=unknown;privacy=off;counter=1");
        sipAddressList.add("sip:38651952000@ocmpt.ims.mobitel.si;reason=unknown;privacy=off;counter=1");
        sipAddressList.add("sip:51952000;phone-context=+386@ocmpt.ims.mobitel.si;reason=unknown;privacy=off;counter=1");
        sipAddressList.add("sip:anonymous@ocmpt.ims.mobitel.si;reason=unknown;privacy=off;counter=1");
        sipAddressList.add("sip:Anonymous@172.18.22.200");
        sipAddressList.add("sip:UNKNOWN@172.18.22.200");
        sipAddressList.add("sip:unavailable@172.18.22.200");
        
        expResultListNational.add("014720988");
        expResultListNational.add("014720988");
        expResultListNational.add("0014324147208988");
        expResultListNational.add("051952000");
        expResultListNational.add("051952000");
        expResultListNational.add("051952000");
        expResultListNational.add("anonymous");
        expResultListNational.add("anonymous");
        expResultListNational.add("anonymous");
        expResultListNational.add("anonymous");
        
        expResultListInternational.add("0038614720988");
        expResultListInternational.add("0038614720988");
        expResultListInternational.add("0014324147208988");
        expResultListInternational.add("0038651952000");
        expResultListInternational.add("0038651952000");
        expResultListInternational.add("0038651952000");
        expResultListInternational.add("anonymous");
        expResultListInternational.add("anonymous");
        expResultListInternational.add("anonymous");
        expResultListInternational.add("anonymous");
        
        System.out.println("Testind TelType.NATIONAL");
        for (int i = 0; i < sipAddressList.size(); i++) {
            System.out.println("Testind index: " + i);
            String sipAddress = sipAddressList.get(i);
            
            String expResult = expResultListNational.get(i);
            String result = SipAddressParser.getTel(sipAddress, TelType.NATIONAL);
            assertEquals(expResult, result);
        }
        System.out.println("Testind TelType.INTERNATIONAL");
        for (int i = 0; i < sipAddressList.size(); i++) {
            System.out.println("Testind index: " + i);
            String sipAddress = sipAddressList.get(i);
            
            String expResult = expResultListInternational.get(i);
            String result = SipAddressParser.getTel(sipAddress, TelType.INTERNATIONAL);
            assertEquals(expResult, result);
        }
    }
    
}
