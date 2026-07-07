package com.aishwarya.Finbank.mapper;

import com.aishwarya.Finbank.dto.CoApplicantRequest;
import com.aishwarya.Finbank.model.CoApplicant;
import com.aishwarya.Finbank.model.EmploymentType;
import com.aishwarya.Finbank.repository.EmploymentTypeRepo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CoApplicantMapper {

    @Autowired
    protected EmploymentTypeRepo employmentTypeRepo;

    @Mapping(
            target = "employmentType",
            expression = "java(mapEmploymentType(coApplicantRequest.getEmploymentTypeId()))"
    )
    public abstract CoApplicant toEntity(CoApplicantRequest coApplicantRequest);

    public abstract List<CoApplicant> toEntities(List<CoApplicantRequest> requests);

    protected EmploymentType mapEmploymentType(Integer id) {
        return employmentTypeRepo.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("EmploymentType not found"));
    }
}
