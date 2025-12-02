package chnu.edu.kn.rusnak.simple_web_app.request;

public record StudentUpdateRequest(
        String id,
        String firstname,
        String lastname,
        int age
) {}

