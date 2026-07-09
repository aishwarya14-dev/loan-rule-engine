package com.aishwarya.Finbank.mapper;
import com.aishwarya.Finbank.dto.GuarantorRequest;
import com.aishwarya.Finbank.model.EmploymentType;
import com.aishwarya.Finbank.model.Guarantor;
import com.aishwarya.Finbank.repository.EmploymentTypeRepo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class GuarantorMapper {
    @Autowired
    protected EmploymentTypeRepo employmentTypeRepo;

    @Mapping(
            target = "employmentType",
            expression = "java(mapEmploymentType(guarantorRequest.getEmploymentTypeId()))"
    )
    public abstract Guarantor toEntity(GuarantorRequest guarantorRequest);

    public abstract List<Guarantor> toEntities(List<GuarantorRequest> requests);

    protected EmploymentType mapEmploymentType(Integer id) {
        return employmentTypeRepo.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("EmploymentType not found"));
    }
}
