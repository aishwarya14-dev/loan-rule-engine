package com.aishwarya.FinBank.mapper;
import com.aishwarya.FinBank.dto.rules.RulesRequestDto;
import com.aishwarya.FinBank.model.LoanType;
import com.aishwarya.FinBank.repository.LoanTypeRepo;
import com.aishwarya.FinBank.ruleengine.model.DslRule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class DslRuleMapper {

        @Autowired
        private LoanTypeRepo loanTypeRepo;

        @Mapping(target = "loanType", expression = "java(mapLoanType(dto.getLoanTypeId()))")
        public abstract DslRule toEntity(RulesRequestDto dto);

        protected LoanType mapLoanType(Integer id) {
            return loanTypeRepo.findById(Long.valueOf(id))
                    .orElseThrow(() -> new RuntimeException("LoanType not found"));
        }
}
