package chnu.edu.kn.rusnak.simple_web_app.request;

public record StudentCreateRequest(
        String firstname,
        String lastname,
        int age
) {}
