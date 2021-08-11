package com.lewis.validation.validation.controller;

import com.lewis.validation.validation.exceptionhandler.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : landon
 * @date : 2021/8/11
 */
@RestController
@RequestMapping("/i18")
public class I18TestController {

    @GetMapping("/testException")
    public String get(@RequestParam("exception") boolean exception) {
        if (exception) {
            throw new ResourceNotFoundException("resource_not_found");
        }
        return "ok";
    }
}
