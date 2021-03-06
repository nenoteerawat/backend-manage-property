package com.bayneno.backen_manage_property.configs;

import com.bayneno.backen_manage_property.properties.ActionLogCalendar;
import com.bayneno.backen_manage_property.properties.DDPropertiesParameters;
import com.bayneno.backen_manage_property.properties.PdfDefaultParameters;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@EnableWebMvc
@EnableConfigurationProperties({ActionLogCalendar.class, PdfDefaultParameters.class, DDPropertiesParameters.class})
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        converters.add(new MappingJackson2HttpMessageConverter());
        converters.add(new FormHttpMessageConverter());
        converters.add(new ByteArrayHttpMessageConverter());
    }

    @Bean
    public XmlMapper xmlMapper(){
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure( ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true );
        return xmlMapper;
    }
}
