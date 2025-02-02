package com.example.demo5;

public interface CommonService {

    void updateVisitor(Visitor visitor, VisitorDto visitorDto);

    void updateCompany(Visitor visitor, VisitorDto visitorDto);

    void updateVisitorTest(Visitor visitor, VisitorTestDto visitorTestDto);

    void updateCompanyTest(Visitor visitor, VisitorTestDto visitorTestDto);
}
