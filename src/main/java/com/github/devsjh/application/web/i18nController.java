package com.github.devsjh.application.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import static com.github.devsjh.application.utility.MessageSourceUtils.getMessage;

/**
 * @memo: 웹 컨트롤러 클래스.
 */
@Slf4j
@Controller
@RequestMapping("/web")
public class i18nController {

    @GetMapping("/i18n")
    public String i18n() {
        return "i18n";
    }
}