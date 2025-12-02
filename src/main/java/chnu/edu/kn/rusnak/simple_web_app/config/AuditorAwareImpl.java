package chnu.edu.kn.rusnak.simple_web_app.config;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        //return Optional.of("admin");
        return Optional.of(System.getProperty("user.name"));
    }
}
