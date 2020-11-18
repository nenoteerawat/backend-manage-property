package com.bayneno.backen_manage_property.controllers;

import com.bayneno.backen_manage_property.services.ReportService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(value = "/leaseAgreement", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] leaseAgreement() throws IOException, JRException {
        return reportService.generateReport("classpath:jasper/leaseAgreement.jrxml", new HashMap<>());
    }

}
