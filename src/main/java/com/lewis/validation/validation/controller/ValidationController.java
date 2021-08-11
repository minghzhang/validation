package com.lewis.validation.validation.controller;

import com.lewis.validation.validation.param.MyRequestParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @date : 2021/8/11
 */
@RestController
@RequestMapping("/validation")
@Slf4j
public class ValidationController {

    @PostMapping("/test")
    public String test(@RequestBody @Valid MyRequestParam requestParam) {
        log.info("param:{}", requestParam);
        int i = 10/0;
        return "ok";
    }
}
