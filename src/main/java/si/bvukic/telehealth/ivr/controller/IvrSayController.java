/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.ivr.controller;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import si.bvukic.telehealth.core.model.MedicalDataType;

/**
 *
 * @author vukicb
 */
@Controller
public class IvrSayController {
    private static final Logger LOG = LoggerFactory.getLogger(IvrSayController.class);
    private static final String PROMPT_FILE_EXTENSION = ".wav";
    
    @RequestMapping(value = "/api/vxml/say/{type}", method = RequestMethod.GET)
    public String initSay(@PathVariable("type") String requestedType, 
            @RequestParam(name = "value", required = true) float value,
            @RequestParam(name = "subdialog", required = false, defaultValue = "true") boolean isSubdialog,
            @RequestParam(name = "bargein", required = false, defaultValue = "true") boolean bargein,
            Model model) throws IllegalAccessException {
        
        MedicalDataType type = MedicalDataType.BOOLEAN;
        try {
            type = MedicalDataType.valueOf(requestedType.toUpperCase());
        } catch (IllegalArgumentException ex) {
            LOG.error("Can't say value of type {}", requestedType);
            throw new IllegalArgumentException("Invalid type " + requestedType);
        }
        
        LOG.debug("Saying {} value of {}", type.toString().toLowerCase(), value);
        
        List<String> promptList = new ArrayList();
        
        switch (type) {
            case BOOLEAN:
                promptList.add("/boolean/" + (int) value + PROMPT_FILE_EXTENSION);
                break;
            case SCALE_ARGUMENT:
                promptList.add("/argument/" + (int) value + PROMPT_FILE_EXTENSION);
                break;
            case SCALE_CONDITION:
                promptList.add("/condition/" + (int) value + PROMPT_FILE_EXTENSION);
                break;
            case SCALE_QUANTITY:
                promptList.add("/quantity/" + (int) value + PROMPT_FILE_EXTENSION);
                break;
            case SCALE_FREQUENCY:
                promptList.add("/frequency/" + (int) value + PROMPT_FILE_EXTENSION);
                break;    
            case NUMERIC:
                String frac = Float.toString(value);
                frac =  frac.substring(frac.indexOf(".") + 1);
                LOG.debug("Numeric type; value={}, int={}, frac={}", value, (int) value, frac);
                
                promptList = generateIntPromptList("/numeric/", (int) value);
                if (Integer.parseInt(frac) > 0) {
                    promptList.add("/numeric/point" + PROMPT_FILE_EXTENSION);
                    for (int i = 0; i < frac.length(); i++ ) {
                        if (frac.charAt(i) == '0') {
                            promptList.add("/numeric/0" + PROMPT_FILE_EXTENSION);
                        } else {
                            promptList.addAll(generateIntPromptList("/numeric/", Integer.parseInt(frac)));
                            break;
                        }
                    }
                }
                break;
        }
        
        LOG.debug("Generated {}", promptList);
        
        model.addAttribute("promptList", promptList);
        model.addAttribute("isSubdialog", isSubdialog);
        model.addAttribute("bargein", bargein);
        
        return "vxml/say.xml";
    }
    
    
    @RequestMapping(value = "/api/vxml/say/{type}/help", method = RequestMethod.GET)
    public String help(@PathVariable("type") String requestedType, 
            @RequestParam(name = "bargein", required = false, defaultValue = "true") boolean bargein,
            @RequestParam(name = "subdialog", required = false, defaultValue = "true") boolean isSubdialog,
            Model model) 
            throws IllegalAccessException {
        
        MedicalDataType type = MedicalDataType.BOOLEAN;
        try {
            type = MedicalDataType.valueOf(requestedType.toUpperCase());
        } catch (IllegalArgumentException ex) {
            LOG.error("Can't say help of type {}", requestedType);
            throw new IllegalArgumentException("Invalid type " + requestedType);
        }    
        String prompt = "";
        switch (type) {
            case BOOLEAN:
                prompt = "/boolean/help" + PROMPT_FILE_EXTENSION;
                break;
            case SCALE_ARGUMENT:
                prompt = "/argument/help"  + PROMPT_FILE_EXTENSION;
                break;
            case SCALE_CONDITION:
                prompt = "/condition/help" + PROMPT_FILE_EXTENSION;
                break;
            case SCALE_QUANTITY:
                prompt = "/quantity/help" + PROMPT_FILE_EXTENSION;
                break;
            case SCALE_FREQUENCY:
                prompt = "/frequency/help" + PROMPT_FILE_EXTENSION;
                break;    
            case NUMERIC:
                prompt = "/numeric/help" + PROMPT_FILE_EXTENSION;
                break;
            default:
                throw new IllegalArgumentException("Invalid type " + requestedType);
        }
        model.addAttribute("bargin", bargein);
        model.addAttribute("isSubdialog", isSubdialog);
        model.addAttribute("prompt", prompt);
        
        return "vxml/help.xml";
        
    }
    
    /**
     * Generates a list of strings for number&digit prompt files. Prefix paramter is 
     * used to set desired string prefix. For example folder path or filename prefix.
     * After prefix a given number is added accourding to slovenian convention.
     * @param prefix
     * @param value
     * @return Orderd prompt list for saying slovenian numbers
     * @throws IllegalAccessException 
     */
    private List<String> generateIntPromptList(String prefix, int value) throws IllegalAccessException {
        List<String> list = new ArrayList();
        
        LOG.debug("Generating list for int value: {}", value);
        
        if (value >= 10000) {
            LOG.error("Provided value {} is not supported", value);
            throw new IllegalAccessException("Valuss from 10000 an higer are not supported!");
        }
        
        //If 0 add 0 and return
        if (value == 0) {
            list.add(prefix + value + PROMPT_FILE_EXTENSION);
            return list;
        }
        
        //Add 1000s
        if(value >= 1000 && value < 10000) {
            int digit = value / 1000;
            if (digit != 1) {
                list.add(prefix + digit + PROMPT_FILE_EXTENSION);
            }
            list.add(prefix + "1000" + PROMPT_FILE_EXTENSION);
            
            if (value % 1000 == 0) {
                //return the list if there are no more numbers left to add to the list
                return list;
            } else {
                value = value % 1000;
            }
        }
        
        //Add 100s
        if(value >= 100 && value < 1000) {
            int digit = value / 100;
            switch (digit) {
                case 1:
                    list.add(prefix + "100" + PROMPT_FILE_EXTENSION);
                    break;
                case 2:
                    list.add(prefix + "2e" + PROMPT_FILE_EXTENSION);
                    list.add(prefix + "100o" + PROMPT_FILE_EXTENSION);
                    break;
                default:
                    list.add(prefix + digit + PROMPT_FILE_EXTENSION);
                    list.add(prefix + "100o" + PROMPT_FILE_EXTENSION);
                    break;
            }
            
            
            if (value % 100 == 0) {
                //return the list if there are no more numbers left to add to the list
                return list;
            } else {
                value = value % 100;
            }
        }
        
        //Add 1-19 or 10s amd return
        if (value < 20 || value % 10 == 0) {
            list.add(prefix + value + PROMPT_FILE_EXTENSION);
            return list;
        }
        
        int d1 = value / 10; //first digit
        int d2 = value % 10; //second digit
        
        //In slovenian we say 2bd digit firt and than the 1st digit
        list.add(prefix + d2 + PROMPT_FILE_EXTENSION);
        list.add(prefix + "and" + PROMPT_FILE_EXTENSION);
        list.add(prefix + d1 + "0" + PROMPT_FILE_EXTENSION);
        
        return list;
    }
    
    
}
