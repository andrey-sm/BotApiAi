package pro.smartum.botapiai.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Configuration
@EnableSwagger2
@Profile("dev")
public class SwaggerConfiguration {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("pro.smartum.botapiai.controllers"))
                .paths(PathSelectors.any())
                .build()
                .directModelSubstitute(LocalDateTime.class, Long.class)
                .directModelSubstitute(LocalDate.class, String.class)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("Smartum Bot API",
                "<br/>" + "<b>Backend URL:</b> <br/> " + "   <b>https://botapiai.herokuapp.com/</b>"
                        + "<br/><br/>" + "<b>Error codes description:</b> <br/> "
                        + "INTERNAL_SERVER(1, \"Server error. Please, try later\"), <br/>"
                        + "NOT_VALID_REQUEST_DATA(2, \"Not valid data\"), <br/>"
                        + "NOT_IMPLEMENTED_YET(3, \"Not implemented yet\"), <br/>"
                        + "CONVERSATION_NOT_EXISTS(4, \"Conversation not exists\"), <br/>"
                        + "NOT_VALID_MESSENGER(5, \"Not valid messenger\"), <br/>"
                        + "GET_SKYPE_TOKEN(6, \"Get skype access token exception\"); <br/>"

                , null, null, new Contact(null, null, null), null, null);
    }

    @Bean
    SecurityConfiguration security() {
        return new SecurityConfiguration(null, null, null, null, null, ApiKeyVehicle.HEADER, "token", ",");
    }
}
