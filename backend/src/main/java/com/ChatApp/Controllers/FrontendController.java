package com.ChatApp.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrontendController {

    // This forwards any non-API, non-static route to index.html (for SPA routing)
    @RequestMapping(value = {
            "/"
    })
    public String forward() {
        return "forward:/index.html";
    }
}
