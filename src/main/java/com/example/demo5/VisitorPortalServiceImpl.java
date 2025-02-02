package com.example.demo5;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VisitorPortalServiceImpl implements VisitorPortalService {

    private final VisitorRepository visitorRepository;
    private final ReasonRepository reasonRepository;
    private final BadgeRepository badgeRepository;
    private final VisitorLogMapper visitorLogMapper;

    private final VisitorBackOfficeServiceImpl visitorBackOfficeService;

    private final VisitorLogRepository visitorLogRepository;

    private final CommonService commonService;

    @Autowired
    public VisitorPortalServiceImpl(VisitorRepository visitorRepository, ReasonRepository reasonRepository, BadgeRepository badgeRepository, VisitorLogMapper visitorLogMapper, VisitorLogRepository visitorLogRepository, VisitorBackOfficeServiceImpl visitorBackOfficeService, CommonService commonService) {
        this.visitorRepository = visitorRepository;
        this.reasonRepository = reasonRepository;
        this.badgeRepository = badgeRepository;
        this.visitorLogMapper = visitorLogMapper;
        this.visitorLogRepository = visitorLogRepository;
        this.visitorBackOfficeService = visitorBackOfficeService;
        this.commonService = commonService;
    }

    @Override
    public VisitorLogDtoPortal createExistingVisitor(VisitorLogDtoPortal visitorLogDtoPortal) {

        System.out.println(visitorLogDtoPortal);

        if (visitorLogDtoPortal.visitor() == null || visitorLogDtoPortal.visitor().getVisitorId() == null) {
            throw new IllegalArgumentException("Visitor or VisitorId cannot be null");
        }

        Visitor visitor = visitorRepository.findById(visitorLogDtoPortal.visitor().getVisitorId())
                .orElseThrow(() -> new ResourceNotFoundException("Visitor with ID " + visitorLogDtoPortal.visitor().getVisitorId() + " not found."));


        VisitorLog visitorLog = createVisitorLogEntity(visitor, visitorLogDtoPortal);

        return visitorLogMapper.convertToDtoPortal(visitorLogRepository.save(visitorLog));
    }

    @Transactional
    @Override
    public Visitor updateExistingVisitor(VisitorDto visitorDto) {
        Visitor existingVisitor = visitorRepository.findById(visitorDto.visitorId())
                .orElseThrow(() -> new ResourceNotFoundException("Visitor with ID " + visitorDto.visitorId() + " not found."));

        visitorBackOfficeService.updateVisitor(existingVisitor, visitorDto);
        visitorBackOfficeService.updateCompany(existingVisitor, visitorDto);

        return visitorRepository.save(existingVisitor);
    }

    private VisitorLog createVisitorLogEntity(Visitor visitor, VisitorLogDtoPortal visitorLogDtoPortal) {
        VisitorLog visitorLog = new VisitorLog();
        visitorLog.setVisitor(visitor);

        Optional.ofNullable(visitorLogDtoPortal.reason().getReasonId())
                .flatMap(reasonRepository::findById)
                .ifPresent(visitorLog::setReason);

        Optional.ofNullable(visitorLogDtoPortal.badge().getBadgeId())
                .flatMap(badgeRepository::findById)
                .ifPresent(visitorLog::setBadge);

        visitorLog.setClockIn(LocalDateTime.now());
        visitorLog.setOtherReason(visitorLogDtoPortal.otherReason());

        String encodedSignature = Base64.getEncoder().encodeToString(visitorLogDtoPortal.signature().getBytes());
        visitorLog.setSignature(encodedSignature);
        visitorLog.setAttendeeName(visitorLogDtoPortal.attendeeName());

        return visitorLog;
    }

    private VisitorLog createVisitorLogTest(Visitor visitor, VisitorTestDto testDto) {
        VisitorLog visitorLog = new VisitorLog();
        visitorLog.setVisitor(visitor);

        Optional.ofNullable(testDto.reasonId())
                .map(UUID::fromString) // Convert String to UUID
                .flatMap(reasonRepository::findById)
                .ifPresent(visitorLog::setReason);

        Optional.ofNullable(testDto.badgeId())
                .map(UUID::fromString)
                .flatMap(badgeRepository::findById)
                .ifPresent(visitorLog::setBadge);

        visitorLog.setClockIn(LocalDateTime.now());
        visitorLog.setOtherReason(testDto.otherReason());

        String encodedSignature = Base64.getEncoder().encodeToString(testDto.signature().getBytes());
        visitorLog.setSignature(encodedSignature);
        visitorLog.setAttendeeName(testDto.attendeeName());

        return visitorLog;

    }


    @Override
    public List<VisitorLogDtoPortal> getVisitorsByDetails(String firstName, String lastName, String companyName) {
        List<Visitor> visitors = visitorRepository.findByFirstNameAndLastNameContainsIgnoreCaseAndCompany_CompanyNameContainsIgnoreCase(firstName, lastName, companyName);

        if (visitors.isEmpty()) {
            return Collections.emptyList();
        }
        return visitors.stream()
                .map(visitor -> {
                   VisitorLog visitorLog = visitorLogRepository.findByVisitor(visitor);

                   if (visitorLog != null) {
                       return visitorLogMapper.convertToDtoPortal(visitorLog);
                   }
                   else {
                       return null;
                   }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    @Transactional
    @Override
    public VisitorLogDtoPortal updateVisitorAndCreateLog(VisitorDto visitorDto, VisitorLogDtoPortal visitorLogDtoPortal) {
        Visitor existingVisitor = visitorRepository.findById(visitorDto.visitorId()).orElseThrow(() -> new ResourceNotFoundException("Visitor with ID " + visitorDto.visitorId() + " not found."));
        commonService.updateVisitor(existingVisitor, visitorDto);
        commonService.updateCompany(existingVisitor, visitorDto);
        VisitorLog visitorLog = createVisitorLogEntity(existingVisitor, visitorLogDtoPortal);
        visitorLogRepository.save(visitorLog);

        return visitorLogMapper.convertToDtoPortal(visitorLog);
    }

    @Override
    public String getContactNumber(String firstName, String lastName) {
        return visitorRepository.findByFirstNameAndLastName(firstName, lastName).getContactNumber();
    }

    @Override
    public void testUpdateAndCreate(VisitorTestDto testDto) {
        Visitor existingVisitor = visitorRepository.findByFirstNameAndLastName(testDto.firstName(), testDto.lastName());

        if (existingVisitor == null) {
            throw new IllegalArgumentException("Visitor not found for name: " + testDto.firstName() + " " + testDto.lastName());
        }
        commonService.updateVisitorTest(existingVisitor, testDto);
        commonService.updateCompanyTest(existingVisitor, testDto);
        VisitorLog visitorLog = createVisitorLogTest(existingVisitor, testDto);
        visitorLogRepository.save(visitorLog);
    }

}
