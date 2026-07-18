package com.husseinrubaie.delivery.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Public landing page for the platform. Owns {@code GET /}.
 */
@Controller
public class HomeController {

    /** The happy-path order lifecycle, shown on the landing page. */
    private static final List<String> LIFECYCLE_STAGES = List.of(
            "Created", "Packaged", "Sent", "Driver", "Received", "Money in", "Complete");

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("stages", LIFECYCLE_STAGES);
        return "index";
    }
}
