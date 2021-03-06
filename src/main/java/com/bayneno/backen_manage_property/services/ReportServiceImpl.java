package com.bayneno.backen_manage_property.services;

import com.bayneno.backen_manage_property.utils.StringUtils;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService{

    private final ResourceLoader resourceLoader;

    public ReportServiceImpl(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public byte[] generateReport(String filePath, Map<String, Object> parametersFromController) throws IOException, JRException {
        JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(resourceLoader.getResource(filePath).getFile().getPath());
//        JasperReport jasperReport = JasperCompileManager.compileReport(resourceLoader.getResource(filePath).getInputStream());
        final Map<String, Object> reportParameters = new HashMap<>();
        parametersFromController.forEach((k,v) -> {
            reportParameters.put(StringUtils.camelToSnake(k).toUpperCase(), v);
        });
        reportParameters.put("REPORT_PATH",resourceLoader.getResource("classpath:jasper/").getFile().getPath());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, reportParameters, new JREmptyDataSource());
        JRPdfExporter exporter = new JRPdfExporter();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(
                new SimpleOutputStreamExporterOutput(byteArrayOutputStream));

        SimplePdfReportConfiguration reportConfig
                = new SimplePdfReportConfiguration();
        reportConfig.setSizePageToContent(true);
        reportConfig.setForceLineBreakPolicy(false);

        SimplePdfExporterConfiguration exportConfig
                = new SimplePdfExporterConfiguration();
        exportConfig.setMetadataAuthor("LEASE AGREEMENT");
        exportConfig.setEncrypted(false);
        exportConfig.setAllowedPermissionsHint("PRINTING");

        exporter.setConfiguration(reportConfig);
        exporter.setConfiguration(exportConfig);

        exporter.exportReport();
        return byteArrayOutputStream.toByteArray();
    }
}
