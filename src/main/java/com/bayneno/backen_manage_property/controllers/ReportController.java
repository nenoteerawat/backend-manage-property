package com.bayneno.backen_manage_property.controllers;

import com.bayneno.backen_manage_property.properties.PdfDefaultParameters;
import com.bayneno.backen_manage_property.services.ReportService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @PostMapping(value = "/realEstateAgentAgreement", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody String realEstateAgentAgreement(@RequestBody Map<String, Object> parametersFromController) throws IOException, JRException {
        pdfDefaultParameters.getRealEstateAgentAgreement().forEach((key, value) -> {
            if(!parametersFromController.containsKey(key)){
                parametersFromController.put(key, value);
            }
        });
        return Base64.getEncoder().encodeToString(reportService.generateReport("classpath:jasper/realEstateAgentAgreement.jasper", parametersFromController));
    }

}
