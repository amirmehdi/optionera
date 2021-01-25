package com.gitlab.amirmehdi.web.rest;


import com.gitlab.amirmehdi.security.AuthoritiesConstants;
import com.gitlab.amirmehdi.service.BourseCodeService;
import com.gitlab.amirmehdi.service.dto.tadbir.DailyPortfolioResponse;
import com.gitlab.amirmehdi.service.dto.tadbir.OpenOrderResponse;
import com.gitlab.amirmehdi.service.dto.tadbir.RemainResponse;
import com.gitlab.amirmehdi.service.dto.tadbir.UserOpenInterestResponse;
import com.gitlab.amirmehdi.service.tadbir.TadbirService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/tadbir")
public class TadbirResource {

    private final TadbirService tadbirService;
    private final BourseCodeService bourseCodeService;

    public TadbirResource(TadbirService tadbirService, BourseCodeService bourseCodeService) {
        this.tadbirService = tadbirService;
        this.bourseCodeService = bourseCodeService;
    }

    @GetMapping(value = "portfo/{bourseCodeId}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<DailyPortfolioResponse> getDailyPortfolio(@PathVariable long bourseCodeId) {
        return ResponseEntity.ok(tadbirService.getDailyPortfolio(bourseCodeService.findOne(bourseCodeId).get()));
    }

    @GetMapping(value = "open-orders/{bourseCodeId}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<OpenOrderResponse> getOpenOrders(@PathVariable long bourseCodeId) {
        return ResponseEntity.ok(tadbirService.getOpenOrders(bourseCodeService.findOne(bourseCodeId).get()));
    }

    @GetMapping(value = "remain/{bourseCodeId}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<RemainResponse> getRemain(@PathVariable long bourseCodeId) {
        return ResponseEntity.ok(tadbirService.getRemain(bourseCodeService.findOne(bourseCodeId).get()));
    }

    @GetMapping(value = "open-interests/{bourseCodeId}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<UserOpenInterestResponse> getUserOpenInterest(@PathVariable long bourseCodeId) {
        return ResponseEntity.ok(tadbirService.getUserOpenInterest(bourseCodeService.findOne(bourseCodeId).get()));
    }

    @GetMapping(value = "today-orders/{bourseCodeId}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<OpenOrderResponse> getTodayOrder(@PathVariable long bourseCodeId) {
        return ResponseEntity.ok(tadbirService.getTodayOrder(bourseCodeService.findOne(bourseCodeId).get()));
    }
}
