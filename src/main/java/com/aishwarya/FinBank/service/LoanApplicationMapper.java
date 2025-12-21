package com.aishwarya.FinBank.service;
import org.mapstruct.Mapper;

import com.aishwarya.FinBank.dto.loanApplication.LoanApplicationRequestDto;
import com.aishwarya.FinBank.model.LoanApplication;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface LoanApplicationMapper {
     LoanApplication toEntity(LoanApplicationRequestDto dto);
}
