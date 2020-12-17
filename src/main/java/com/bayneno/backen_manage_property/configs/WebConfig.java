package com.bayneno.backen_manage_property.configs;

import com.bayneno.backen_manage_property.properties.ActionLogCalendar;
import com.bayneno.backen_manage_property.properties.PdfDefaultParameters;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@EnableConfigurationProperties({ActionLogCalendar.class, PdfDefaultParameters.class})
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    @Bean
    public XmlMapper xmlMapper(){
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure( ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true );
        return xmlMapper;
    }
}
