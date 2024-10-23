package com.ecommerce.library.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDto {

    @Size(min = 1,max = 10, message = "Họ... chỉ chứa từ 1-10 ký tự")
    private String firstName;

    @Size(min = 1,max = 10, message = "Tên chỉ chứa từ 1-10 ký tự")
    private String lastName;

    private String username;

    @Size(min = 3,max = 20, message = "Mật khẩu chỉ chứa từ 3-20 ký tự")
    private String password;

    private String repeatPassword;
}
