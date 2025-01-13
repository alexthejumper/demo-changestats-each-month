package com.example.demo5;

import org.springframework.stereotype.Service;

import java.util.List;

public interface VisitorBackOfficeService {

    List<VisitsCountDto> getVisitsCountPerWeek();

    List<VisitorReasonCountDto> getCountOfReasons();

}
