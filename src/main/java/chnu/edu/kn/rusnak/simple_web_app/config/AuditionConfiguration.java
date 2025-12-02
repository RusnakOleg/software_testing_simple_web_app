package chnu.edu.kn.rusnak.simple_web_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@Configuration
public class AuditionConfiguration {

    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAwareImpl();
    }


}