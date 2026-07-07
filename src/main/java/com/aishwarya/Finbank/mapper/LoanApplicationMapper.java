package com.aishwarya.Finbank.mapper;

import com.aishwarya.Finbank.model.LoanApplication;
import com.aishwarya.Finbank.dto.loanApplication.LoanApplicationRequestDto;
import com.aishwarya.Finbank.model.*;
import com.aishwarya.Finbank.repository.*;
import lombok.AllArgsConstructor;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {
                GuarantorMapper.class,
                CoApplicantMapper.class
        }
)
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
    @Autowired
    protected IndustryRepo industryRepo;
    @Autowired
    protected LoanPurposeRepo loanPurposeRepo;
    @Autowired
    protected PropertyTypeRepo propertyTypeRepo;

    @Mapping(target = "user", expression = "java(mapUser(dto.getUserId()))")
    @Mapping(target = "loanType", expression = "java(mapLoanType(dto.getLoanTypeId()))")
    @Mapping(target = "jobTitle", expression = "java(mapJobTitle(dto.getJobTitleId()))")
    @Mapping(target = "region", expression = "java(mapRegion(dto.getRegionId()))")
    @Mapping(target = "employmentType", expression = "java(mapEmploymentType(dto.getEmploymentTypeId()))")
    @Mapping(target = "industry", expression = "java(mapIndustry(dto.getIndustryId()))")
    @Mapping(target = "loanPurpose", expression = "java(mapLoanPurpose(dto.getLoanPurposeId()))")
    @Mapping(target = "propertyType", expression = "java(mapPropertyType(dto.getPropertyTypeId()))")
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

    protected Industry mapIndustry(Integer id) {
        return industryRepo.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Industry not found"));
    }

    protected LoanPurpose mapLoanPurpose(Integer id) {
        return loanPurposeRepo.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("LoanPurpose not found"));
    }

    protected PropertyType mapPropertyType(Integer id) {
        return propertyTypeRepo.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("PropertyType not found"));
    }

    @AfterMapping
    protected void linkChildren(
            @MappingTarget LoanApplication application) {

        if (application.getGuarantors() != null) {
            application.getGuarantors()
                    .forEach(g -> g.setLoanApplication(application));
        }

        if (application.getCoApplicants() != null) {
            application.getCoApplicants()
                    .forEach(c -> c.setLoanApplication(application));
        }
    }

}
