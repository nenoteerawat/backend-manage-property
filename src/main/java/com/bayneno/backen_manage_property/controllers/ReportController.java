package com.bayneno.backen_manage_property.controllers;

import com.bayneno.backen_manage_property.properties.PdfDefaultParameters;
import com.bayneno.backen_manage_property.services.ReportService;
import com.bayneno.backen_manage_property.utils.ZonedDateTimeUtil;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.chrono.ThaiBuddhistDate;
import java.util.Base64;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;
    private final PdfDefaultParameters pdfDefaultParameters;

    public ReportController(ReportService reportService, PdfDefaultParameters pdfDefaultParameters) {
        this.reportService = reportService;
        this.pdfDefaultParameters = pdfDefaultParameters;
    }

    @PostMapping(value = "/leaseAgreement", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody String leaseAgreement(@RequestBody Map<String, Object> parametersFromController) throws IOException, JRException {
        pdfDefaultParameters.getLeaseAgreement().forEach((key, value) -> {
            if(!parametersFromController.containsKey(key)){
                parametersFromController.put(key, value);
            }
        });
        return Base64.getEncoder().encodeToString(reportService.generateReport("classpath:jasper/leaseAgreement.jasper", parametersFromController));
    }

    @PostMapping(value = "/leaseAgreementEng", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody String leaseAgreementEng(@RequestBody Map<String, Object> parametersFromController) throws IOException, JRException {
        pdfDefaultParameters.getLeaseAgreement().forEach((key, value) -> {
            if(!parametersFromController.containsKey(key)){
                parametersFromController.put(key, value);
            }
        });
        return Base64.getEncoder().encodeToString(reportService.generateReport("classpath:jasper/leaseAgreement-eng.jasper", parametersFromController));
    }

    @PostMapping(value = "/realEstateAgentAgreement", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody String realEstateAgentAgreement(@RequestBody Map<String, Object> parametersFromController) throws IOException, JRException {
        pdfDefaultParameters.getRealEstateAgentAgreement().forEach((key, value) -> {
            if(!parametersFromController.containsKey(key)){
                parametersFromController.put(key, value);
            }
        });
        return Base64.getEncoder().encodeToString(reportService.generateReport("classpath:jasper/realEstateAgentAgreement.jasper", parametersFromController));
    }

    @PostMapping(value = "/sellAndPurchaseAgreement", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody String sellAndPurchaseAgreement(@RequestBody Map<String, Object> parametersFromController) throws IOException, JRException {
        pdfDefaultParameters.getSellAndPurchaseAgreementTh().forEach((key, value) -> {
            if(!parametersFromController.containsKey(key)){
                parametersFromController.put(key, value);
            }
        });
        return Base64.getEncoder().encodeToString(reportService.generateReport("classpath:jasper/sellAndPurchaseAgreement.jasper", parametersFromController));
    }

    @PostMapping(value = "/sellAndPurchaseAgreementEng", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody String sellAndPurchaseAgreementEng(@RequestBody Map<String, Object> parametersFromController) throws IOException, JRException {
        pdfDefaultParameters.getSellAndPurchaseAgreementEng().forEach((key, value) -> {
            if(!parametersFromController.containsKey(key)){
                parametersFromController.put(key, value);
            }
        });
        parametersFromController.put("YEAR_BUDDHIST", ZonedDateTimeUtil.buddhistYear());
        parametersFromController.put("YEAR_CHRISTIANITY", ZonedDateTimeUtil.christianityYear());
        return Base64.getEncoder().encodeToString(reportService.generateReport("classpath:jasper/sellAndPurchaseAgreement-eng.jasper", parametersFromController));
    }

    @PostMapping(value = "/coBrokerForm", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody String coBrokerForm(@RequestBody Map<String, Object> parametersFromController) throws IOException, JRException {
        pdfDefaultParameters.getCoBrokerFrom().forEach((key, value) -> {
            if(!parametersFromController.containsKey(key)){
                parametersFromController.put(key, value);
            }
        });
        ZonedDateTime dateRegistration = ZonedDateTimeUtil.stringToZonedDateTime((String) parametersFromController.get("dateRegistration"), ZonedDateTimeUtil.YYYYMMDDTHHMMSSSSSZ, ZonedDateTimeUtil.BANGKOK_ASIA_ZONE_ID);
        parametersFromController.put("dateRegistration",ZonedDateTimeUtil.zonedDateTimeToString(dateRegistration, ZonedDateTimeUtil.DDMMYY, ZonedDateTimeUtil.BANGKOK_ASIA_ZONE_ID));
        return Base64.getEncoder().encodeToString(reportService.generateReport("classpath:jasper/coBrokerForm.jasper", parametersFromController));
    }

    @PostMapping(value = "/exclusiveAgreement", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody String exclusiveAgreement(@RequestBody Map<String, Object> parametersFromController) throws IOException, JRException {
        pdfDefaultParameters.getExclusiveAgreement().forEach((key, value) -> {
            if(!parametersFromController.containsKey(key)){
                parametersFromController.put(key, value);
            }
        });
        return Base64.getEncoder().encodeToString(reportService.generateReport("classpath:jasper/exclusiveAgreement.jasper", parametersFromController));
    }

}
