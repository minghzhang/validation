package com.lewis.validation.validation.param;

import com.lewis.validation.validation.custom.Email;
import com.lewis.validation.validation.custom.EmailList;
import com.lewis.validation.validation.custom.RestrictiveInteger;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @date : 2021/8/11
 */
@Data
public class MyRequestParam {

    @NotNull
    private Long productId;

    @NotBlank
    private String productName;

    @Email
    private String email;

    @EmailList
    private List<String> emailList;

    @NotNull
    @RestrictiveInteger(applyEnum = Proportion.class)
    private Integer proportion;
}
