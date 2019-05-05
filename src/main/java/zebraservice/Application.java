package zebraservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.ApiContextInitializer;

import javax.servlet.FilterRegistration;
import java.util.TimeZone;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(Application.class,args);
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
        return jacksonObjectMapperBuilder ->
                jacksonObjectMapperBuilder.timeZone(TimeZone.getDefault());
    }

    /*
    @Bean
    public FilterRegistrationBean corsFilterRegistration(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new CORSFilter());
        registrationBean.setName("CORS Filter");
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
    */

}
