package com.selfimpr.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private RefreshService refreshService;

    @GetMapping(value = "/ttt")
    public void ttt() {
        refreshService.test();
    }

}
