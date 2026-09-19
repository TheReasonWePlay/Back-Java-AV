package com.javaAV.coivoiturage.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.io.ClassPathResource;

import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;

import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig {

	@Bean
	public ServletRegistrationBean<MessageDispatcherServlet>
	        messageDispatcherServlet(ApplicationContext context) {

	    MessageDispatcherServlet servlet =
	            new MessageDispatcherServlet();

	    servlet.setApplicationContext(context);
	    servlet.setTransformWsdlLocations(true);

	    ServletRegistrationBean<MessageDispatcherServlet>
	            registration =
	            new ServletRegistrationBean<>(
	                    servlet,
	                    "/services/*"
	            );

	    registration.setName("messageDispatcherServlet");

	    return registration;
	}

    @Bean(name = "priceVerification")
    public DefaultWsdl11Definition defaultWsdl11Definition(
            XsdSchema priceSchema) {

        DefaultWsdl11Definition wsdl =
                new DefaultWsdl11Definition();

        wsdl.setPortTypeName("PriceVerificationPort");
        wsdl.setLocationUri("/services");
        wsdl.setTargetNamespace(
                "http://cargo.com/soap/price"
        );

        wsdl.setSchema(priceSchema);

        return wsdl;
    }

    @Bean
    public XsdSchema priceSchema() {

        return new SimpleXsdSchema(
                new ClassPathResource(
                        "xsd/price-verification.xsd"
                )
        );
    }
}