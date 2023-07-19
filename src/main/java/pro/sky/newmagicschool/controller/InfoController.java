package pro.sky.newmagicschool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.newmagicschool.service.InfoService;


@RestController
public class InfoController {

    @Autowired
    InfoService infoService;


    @GetMapping("/getPort")
    public Integer getPort() {
        return infoService.getPort();
    }
}
