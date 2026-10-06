package com.jonahjayasingh.CRM.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboardMetrics(Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "ANONYMOUS";
        return ResponseEntity.ok(dashboardService.getDashboardMetrics(username));
    }
}
