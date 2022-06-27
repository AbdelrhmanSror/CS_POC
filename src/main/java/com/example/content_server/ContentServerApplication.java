package com.example.content_server;

import com.example.content_server.repository.WorkFlowPocRepository;
import com.example.content_server.service.*;
import com.example.content_server.utility.Utilities;
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
    public CommandLineRunner demo(WorkFlowPocRepository repository) {
        return (args -> {
            /**
             * <?xml version="1.0"?>
             *
             * -<employee>
             *
             * <الاسم>محمد ااحمد السيد</الاسم>
             *
             * <الرقم_القومى>29307935414896</الرقم_القومى>
             *
             * <العنوان>مدينه نصر</العنوان>
             *
             * </employee>
             */
            // fetch all customers
            log.info("Customers Saved with SAVe():");
            String idNumber = "26908241300412";
            log.info("Date From ID NUmber is :" + Utilities.getBirthDateUsingIdNumber("29307935414896"));


            log.info("ORDER ID= " + Utilities.getWorkFlowPocAttributes(repository.findAllWorkFlowWIthId(814140)));
            //new NodeService().createNode(new AuthenticationService().getToken("admin","Asset99a"), NodeType.FOLDER.getNodeTypeId(),"811398","xyztest");
            log.info("count= " + repository.getWorkFlowId());


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
