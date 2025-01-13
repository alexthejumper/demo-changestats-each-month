package com.example.demo5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/visitors")
public class MainController {

    private final VisitorBackOfficeService visitorBackOfficeService;

    @Autowired
    public MainController(VisitorBackOfficeService visitorBackOfficeService) {
        this.visitorBackOfficeService = visitorBackOfficeService;
    }

    @GetMapping("/visit-count")
    public ResponseEntity<List<VisitsCountDto>> getVisitsCountPerWeek() {
        List<VisitsCountDto> countWeeklyVisit = visitorBackOfficeService.getVisitsCountPerWeek();
        System.out.println(countWeeklyVisit.listIterator());
        return ResponseEntity.ok(countWeeklyVisit);
    }

    @GetMapping("/reason-count")
    public ResponseEntity<List<VisitorReasonCountDto>> getCountOfReasons() {
        List<VisitorReasonCountDto> countByReason = visitorBackOfficeService.getCountOfReasons();
        System.out.println(countByReason.listIterator());
        return ResponseEntity.ok(countByReason);
    }

//    @GetMapping("/visitor-status")
//    public ResponseEntity<List<VisitorCountDto>> getVisitorCountPerWeek() {
//        List<VisitorCountDto> visitorStatus = visitorBackOfficeService.getVisitorCountPerWeek();
//        return ResponseEntity.ok(visitorStatus);
//    }
}

