package com.bayneno.backen_manage_property.services;

import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.util.Map;

public interface ReportService {
    byte[] generateReport(String filePath, Map<String, Object> parameters) throws IOException, JRException;
}
