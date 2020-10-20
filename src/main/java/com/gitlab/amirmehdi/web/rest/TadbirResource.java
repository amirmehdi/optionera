package com.gitlab.amirmehdi.web.rest;


import com.gitlab.amirmehdi.domain.Order;
import com.gitlab.amirmehdi.security.AuthoritiesConstants;
import com.gitlab.amirmehdi.service.TadbirService;
import com.gitlab.amirmehdi.service.dto.tadbir.DailyPortfolioResponse;
import com.gitlab.amirmehdi.service.dto.tadbir.OpenOrderResponse;
import com.gitlab.amirmehdi.service.dto.tadbir.RemainResponse;
import com.gitlab.amirmehdi.service.dto.tadbir.UserOpenInterestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/tadbir")
public class TadbirResource {

    private final TadbirService tadbirService;

    public TadbirResource(TadbirService tadbirService) {
        this.tadbirService = tadbirService;
    }

    @PostMapping(value = "orders")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<String> getDailyPortfolio(@RequestBody Order order) {
        return ResponseEntity.ok(tadbirService.sendOrder(order));
    }

    @GetMapping(value = "portfo")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<DailyPortfolioResponse> getDailyPortfolio() {
        return ResponseEntity.ok(tadbirService.getDailyPortfolio());
    }

    @GetMapping(value = "open-orders")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<OpenOrderResponse> getOpenOrders() {
        return ResponseEntity.ok(tadbirService.getOpenOrders());
    }

    @GetMapping(value = "remain")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<RemainResponse> getRemain() {
        return ResponseEntity.ok(tadbirService.getRemain());
    }

    @GetMapping(value = "open-interests")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<UserOpenInterestResponse> getUserOpenInterest() {
        return ResponseEntity.ok(tadbirService.getUserOpenInterest());
    }

    @GetMapping(value = "today-orders")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<OpenOrderResponse> getTodayOrder() {
        return ResponseEntity.ok(tadbirService.getTodayOrder());
    }
}
