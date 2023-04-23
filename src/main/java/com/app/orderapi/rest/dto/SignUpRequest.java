package com.app.orderapi.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignUpRequest {

    @Schema(example = "user3")
    @NotBlank
    private String username;

    @Schema(example = "user3")
    @NotBlank
    private String password;

    @Schema(example = "User3")
    @NotBlank
    private String name;

    @Schema(example = "user3@mycompany.com")
    @Email
    private String email;

    @Schema(example = "Mr. / Mrs. / Ms. / Dr. / Prof. / ...")
    @NotBlank
    private String title;

    @Schema(example = "0 / 20 / 30 / 40 / 50 / 60 / 70 / 80 / 90 / 100")
    private int age;
}
