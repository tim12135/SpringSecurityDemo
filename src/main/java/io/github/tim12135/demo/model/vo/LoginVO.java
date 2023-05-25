package io.github.tim12135.demo.model.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 登录vo
 *
 * @author tim12135
 * @since 2023/5/4
 */
@Getter
@Setter
public class LoginVO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
