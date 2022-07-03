package com.example.content_server;

import com.example.content_server.repository.ApprovedCustomerRepository;
import com.example.content_server.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class ContentServerApplication {
    private static final Logger log = LoggerFactory.getLogger(ContentServerApplication.class);


    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ContentServerApplication.class);
        Map<String, Object> map = new HashMap<>();

        map.put("server.port", "9090");
        application.setDefaultProperties(map);
        application.run(args);
    }


    @Bean
    public CommandLineRunner demo(ApprovedCustomerRepository approvedCustomerRepository) {
        return (args -> {
           /* Element element = XmlParser.getParsedXML();
            String name = element.getElementsByTagName("الاسم").item(0).getTextContent();
            String idNumber = element.getElementsByTagName("الرقم_القومى").item(0).getTextContent();
            String address = element.getElementsByTagName("العنوان").item(0).getTextContent();
            ApprovedOcrCustomer ocrCustomerFromXml = new ApprovedOcrCustomer();
            ocrCustomerFromXml.setCustomerName(name);
            ocrCustomerFromXml.setAddress(address);
            ocrCustomerFromXml.setIdNumber(Long.valueOf(idNumber));
            approvedCustomerRepository.save(ocrCustomerFromXml);
            // fetch all customers
            log.info("Customers Saved with SAVED"+approvedCustomerRepository.findById(29307935414896L).get());
*/

        });
    }




    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }


    @Bean
    public AuthenticationService authentication() {
        return new AuthenticationService();
    }

    @Bean
    public POCService pocService() {
        return new POCService();
    }

    @Bean
    public NodeService nodeRepository() {
        return new NodeService();
    }


    @Bean
    public CategoryService categoryRepository() {
        return new CategoryService();
    }

    @Bean
    public WorkflowService workflow() {
        return new WorkflowService();
    }


}
