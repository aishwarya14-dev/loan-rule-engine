package com.aishwarya.Finbank.mapper;

import com.aishwarya.Finbank.model.LoanApplication;
import com.aishwarya.Finbank.dto.loanApplication.LoanApplicationRequestDto;
import com.aishwarya.Finbank.model.*;
import com.aishwarya.Finbank.repository.*;
import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class LoanApplicationMapper {

    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected LoanTypeRepo loanTypeRepo;
    @Autowired
    protected JobTitleRepo jobTitleRepo;
    @Autowired
    protected RegionRepo regionRepo;
    @Autowired
    protected EmploymentTypeRepo employmentTypeRepo;

    @Mapping(target = "user", expression = "java(mapUser(dto.getUserId()))")
    @Mapping(target = "loanType", expression = "java(mapLoanType(dto.getLoanTypeId()))")
    @Mapping(target = "jobTitle", expression = "java(mapJobTitle(dto.getJobTitleId()))")
    @Mapping(target = "region", expression = "java(mapRegion(dto.getRegionId()))")
    @Mapping(target = "employmentType", expression = "java(mapEmploymentType(dto.getEmploymentTypeId()))")
    public abstract LoanApplication  toEntity(LoanApplicationRequestDto dto);

    protected User mapUser(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    protected LoanType mapLoanType(Integer id) {
        return loanTypeRepo.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("LoanType not found"));
    }

    protected JobTitle mapJobTitle(Integer id) {
        return jobTitleRepo.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("JobTitle not found"));
    }

    protected Region mapRegion(Integer id) {
        return regionRepo.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Region not found"));
    }

    protected EmploymentType mapEmploymentType(Integer id) {
        return employmentTypeRepo.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("EmploymentType not found"));
    }

}
