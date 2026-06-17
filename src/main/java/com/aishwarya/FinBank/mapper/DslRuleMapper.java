package com.aishwarya.Finbank.mapper;

import com.aishwarya.Finbank.dto.rules.RulesRequestDto;

import com.aishwarya.Finbank.model.DslRule;
import com.aishwarya.Finbank.model.LoanType;
import com.aishwarya.Finbank.repository.LoanTypeRepo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class DslRuleMapper {

    @Autowired
    private LoanTypeRepo loanTypeRepo;

    @Mapping(target = "loanType", expression = "java(mapLoanType(dto.getLoanTypeId()))")
    public abstract DslRule toEntity(RulesRequestDto dto);

    protected LoanType mapLoanType(Long id) {
        return loanTypeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("LoanType not found"));
    }
}
