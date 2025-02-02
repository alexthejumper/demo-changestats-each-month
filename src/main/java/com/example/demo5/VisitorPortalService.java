package com.example.demo5;

import jakarta.validation.Valid;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

public interface VisitorPortalService {

    VisitorLogDtoPortal createExistingVisitor(@NotNull @Valid VisitorLogDtoPortal visitorLogDtoPortal);

    Visitor updateExistingVisitor(VisitorDto visitorDto);

    List<VisitorLogDtoPortal> getVisitorsByDetails(String firstName, String lastName, String companyName);

    VisitorLogDtoPortal updateVisitorAndCreateLog(VisitorDto visitorDto, VisitorLogDtoPortal visitorLogDtoPortal);

    String getContactNumber(String firsName, String lastName);

    void testUpdateAndCreate(VisitorTestDto testDto);

}
