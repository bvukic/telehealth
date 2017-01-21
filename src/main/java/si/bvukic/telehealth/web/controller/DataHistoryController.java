/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.bvukic.telehealth.web.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import si.bvukic.telehealth.core.model.MedicalData;
import si.bvukic.telehealth.core.model.MedicalParameter;
import si.bvukic.telehealth.core.model.User;
import si.bvukic.telehealth.core.service.MedicalDataService;
import si.bvukic.telehealth.core.service.MedicalParameterService;
import si.bvukic.telehealth.core.service.UserService;
import si.bvukic.telehealth.web.formatter.DateFormatter;
import si.bvukic.telehealth.web.model.ChartData;
import si.bvukic.telehealth.web.model.DashboardChartPanel;
import si.bvukic.telehealth.web.model.DataHistoryAdd;
import si.bvukic.telehealth.web.model.DataHistorySearch;

/**
 *
 * @author bostjanvukic
 */
@Controller
@SessionAttributes("classActiveDataHistory")
public class DataHistoryController extends GenericChartController {

    private static final Logger LOG = LoggerFactory.getLogger(DataHistoryController.class);
    private final MedicalDataService medicalDataService;
    private final MedicalParameterService medicalParameterService;

    @Autowired
    public DataHistoryController(MedicalDataService medicalDataService,
            MedicalParameterService medicalParameterService, UserService userService) {
        super(userService);
        this.medicalDataService = medicalDataService;
        this.medicalParameterService = medicalParameterService;
    }

    @ModelAttribute("classActiveDataHistory")
    public String initClassActiveDataHistory() {
        return "active";
    }
    @ModelAttribute("allHours")
    public List<Integer> initAllHours() {
        List<Integer> hoursList = new ArrayList();
        for (int i = 0; i < 24; i++) {
            hoursList.add(i);
        }
        return hoursList;
    }
    
    @ModelAttribute("allMinutes")
    public List<Integer> initAllMinutes() {
        List<Integer> minutesList = new ArrayList();
        for (int i = 0; i < 60; i++) {
            minutesList.add(i);
        }
        return minutesList;
    }

    @PreAuthorize("hasRole('ROLE_PERMISSION_data_history')")
    @RequestMapping(value = "/data/history", method = RequestMethod.GET)
    public String initDataHistory(Model model, Locale locale, HttpServletRequest request) {

        User user = super.getAuthenticatedUser();
        DataHistorySearch search = new DataHistorySearch();
        if (user.getMedicalParameters().size() > 0) {
            search.setParameterId(user.getMedicalParameters().get(0).getId());
        }
        DateFormatter dateFormatter = new DateFormatter();
        String now = dateFormatter.print(new Date(), locale);
        search.setFrom(now);
        search.setTo(now);

        request.getSession().setAttribute("DataHistoryController_search", search);
        model.addAttribute("search", search);
        
        DataHistoryAdd add = new DataHistoryAdd(user.getMedicalParameters().get(0).getId());
        request.getSession().setAttribute("DataHistoryController_add", add);
        model.addAttribute("add", add);
        
        return "redirect:/data/history/result";
    }

    @PreAuthorize("hasRole('ROLE_PERMISSION_data_history')")
    @RequestMapping(value = "/data/history", method = RequestMethod.POST)
    public String updateDataHistorySearch(
            @ModelAttribute("search") DataHistorySearch search,
            HttpServletRequest request,
            Model model, Locale locale) {

        LOG.debug("Updated data history search. Details: {}", search);
        request.getSession().setAttribute("DataHistoryController_search", search);
        model.addAttribute("search", search);

        return "redirect:/data/history/result";
    }

    @PreAuthorize("hasRole('ROLE_PERMISSION_data_history')")
    @RequestMapping(value = "/data/history/result", method = RequestMethod.GET)
    public String loadDataHistory(
            HttpServletRequest request,
            Model model, Locale locale) {

        User user = super.getAuthenticatedUser();
        
        //Validate add data parameters
        DataHistoryAdd add = (DataHistoryAdd) request.getSession().getAttribute("DataHistoryController_add");
        model.addAttribute("add",add);
        if (add.getTimestamp().getTime() > new Date().getTime()) {
            model.addAttribute("errorAdd","Datum ne sme biti v prihodnosti.");
        }
        
        //Validate search parameter
        DataHistorySearch search = (DataHistorySearch) request.getSession()
                .getAttribute("DataHistoryController_search");

        model.addAttribute("search", search);

        DateFormatter dateFormatter = new DateFormatter();
        Date fromDate;
        try {
            fromDate = dateFormatter.parse(search.getFrom(), locale);
        } catch (ParseException ex) {
            LOG.warn("FromDate format error. Exception: {}", ex);
            model.addAttribute("errorMsg", "Napačna oblika datuma. Datum mora ustrezati vzorcu dd.mm.llll. Kliknite na polje in s koledarjem izbreite željen datum.");
            model.addAttribute("error", true);
            model.addAttribute("errorClass", "background-danger");
            return "dataHistory.html";
        }
        Date toDate;
        try {
            toDate = dateFormatter.parse(search.getTo(), locale);
            toDate.setHours(23);
            toDate.setMinutes(59);
            toDate.setSeconds(59);
            if (toDate.before(fromDate)) {
                model.addAttribute("errorMsg", "Datum Do ne sme biti manjši od datuma Od.");
                model.addAttribute("error", true);
                model.addAttribute("errorClass", "background-danger");
                return "dataHistory.html";
            }
        } catch (ParseException ex) {
            LOG.warn("FromDate format error. Exception: {}", ex);
            model.addAttribute("errorMsg", "Napačna oblika datuma. Datum mora ustrezati vzorcu dd.mm.llll. Kliknite na polje in s koledarjem izbreite željen datum.");
            model.addAttribute("error", true);
            model.addAttribute("errorClass", "background-danger");
            return "dataHistory.html";
        }

        MedicalParameter parameter = medicalParameterService.getMedicalParameterById(search.getParameterId());
        LOG.info("Data history search for user: {}, parameter.name: {}, startDate: {}, stopDate: {}",
                user.getUsername(), parameter.getName(), fromDate, toDate);
        List<MedicalData> dataList = this.medicalDataService.listMedicalData(user, parameter, fromDate, toDate);
        LOG.info("Data history liset size is: {} for user: {}, parameter.name: {}, startDate: {}, stopDate: {}",
                dataList.size(), user.getUsername(), parameter.getName(), fromDate, toDate);
        model.addAttribute("dataList", dataList);
        
        ChartData data = super.genChartData(dataList);
        DashboardChartPanel dcp = new DashboardChartPanel(parameter, data);
        model.addAttribute("panel", dcp);
        model.addAttribute("error", false);
        
        return "dataHistory.html";
    }

    @PreAuthorize("hasRole('ROLE_PERMISSION_data_history')")
    @RequestMapping("/data/history/remove/{id}")
    public String removeData(@PathVariable("id") Long id) {

        medicalDataService.removeMedicalData(id);
        return "redirect:/data/history/result";
    }

    @PreAuthorize("hasRole('ROLE_PERMISSION_data_history')")
    @RequestMapping(value = "/data/history/edit/{id}", method = RequestMethod.GET)
    public String loadEditData(@PathVariable("id") Long id, Model model) {

        MedicalData data = medicalDataService.getMedicalDataById(id);
        model.addAttribute("data", data);
        return "dataHistoryEdit.html";
    }

    @PreAuthorize("hasRole('ROLE_PERMISSION_data_history')")
    @RequestMapping(value = "/data/history/edit", method = RequestMethod.POST)
    public String editData(@ModelAttribute MedicalData data, Model model,
            HttpServletRequest request) {
        
        if (data.getId() == null) {
            
            DataHistoryAdd add = (DataHistoryAdd) request.getSession().getAttribute("DataHistoryController_add");
            User user = super.getAuthenticatedUser();
            
            data.setCreatedAt(new Date());
            data.setCreatedBy(user.getUsername());
            data.setDataValueDate(add.getTimestamp());
            data.setMedicalParameter(medicalParameterService.getMedicalParameterById(add.getParameterId()));
            data.setUser(user);
            
            if (!data.isValueValid()) {
                model.addAttribute("error", "Napaka: Vnešeni podatki niso veljavni!");
                model.addAttribute("data", data);
                return "dataHistoryEdit.html";
            }
        
            medicalDataService.saveMedicalData(data);
        } else {
            MedicalData storedData = medicalDataService.getMedicalDataById(data.getId());
            storedData.setDataValue(data.getDataValue());
            storedData.setUpdatedAt(new Date());
            storedData.setUpdatedBy(super.getAuthenticatedUser().getUsername());
            
            //Validate input dataValue
            if (!storedData.isValueValid()) {
                model.addAttribute("error", "Napaka: Vnešeni podatki niso veljavni!");
                model.addAttribute("data", storedData);
                return "dataHistoryEdit.html";
            }
        
            medicalDataService.updateMedicalData(storedData);
        }
        
        return "redirect:/data/history/result";
    }
    
    
    @PreAuthorize("hasRole('ROLE_PERMISSION_data_history')")
    @RequestMapping(value = "/data/history/add", method = RequestMethod.POST)
    public String addData(@ModelAttribute("add") DataHistoryAdd add,
            HttpServletRequest request,
            Model model, Locale locale) {
        
        request.getSession().setAttribute("DataHistoryController_add", add);
        LOG.debug("Adding data from history. Details: {}", add);
        
        if (add.getTimestamp().getTime() > new Date().getTime()) {
            LOG.debug("Invalid add timestamp {}", add.getTimestamp());
            return "redirect:/data/history/result";
        }
        
        User user = super.getAuthenticatedUser();
        MedicalData data = new MedicalData();
        data.setMedicalParameter(medicalParameterService.getMedicalParameterById(add.getParameterId()));
        data.setCreatedAt(new Date());
        data.setCreatedBy(user.getUsername());
        data.setDefaultValue();
        data.setDataValueDate(add.getTimestamp());
        data.setUser(user);
        
        model.addAttribute("data", data);
        return "dataHistoryEdit.html";
    }


}
