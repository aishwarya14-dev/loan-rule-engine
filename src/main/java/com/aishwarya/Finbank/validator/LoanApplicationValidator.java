package com.aishwarya.Finbank.validator;

import com.aishwarya.Finbank.enums.ApplicationStatus;
import com.aishwarya.Finbank.exceptions.LoanApplicationException;
import com.aishwarya.Finbank.model.LoanApplication;
import com.aishwarya.Finbank.repository.LoanRepository;
import com.aishwarya.Finbank.service.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoanApplicationValidator {
    @Autowired
    private LoanRepository loanRepository;

    public void validateLonApplication(LoanApplication loanApplication){
        if(duplicateExists(loanApplication)){
            log.info("duplicate loan application detected for the user {}" , loanApplication.getApplicantName());
            throw new LoanApplicationException("duplicate loan application detected for the user" + loanApplication.getApplicantName());
        }
        else if(!checkEmail(loanApplication)){
            log.info("applicant email should be same as logged in user email {}" ,loanApplication.getApplicantEmail() );
            throw new LoanApplicationException("applicant email should be same as user logged in email" );
        }
        else if(!checkMobileNumber(loanApplication)){
            log.info("applicant mobile should be same as logged in user mobile {}",loanApplication.getApplicantContact());
            throw new LoanApplicationException("applicant contact should be same as user logged in phone number");
        }
    }

    private boolean duplicateExists(LoanApplication loanApplication){
       int userId = loanApplication.getUser().getId();
       Long loanTypeId = loanApplication.getLoanType().getId();
       LoanApplication application = loanRepository.findByUserIdAndLoanTypeIdAndStatus((long)userId,loanTypeId,ApplicationStatus.PENDING);
       if(application != null){
           return true;
       }
       return false;
    }

    private boolean checkEmail(LoanApplication loanApplication){
       String applicantEmail = loanApplication.getApplicantEmail();
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        CustomUserDetails currentUser =
                (CustomUserDetails) authentication.getPrincipal();
        return currentUser.getUser().getEmail().equals(applicantEmail);

    }

    private boolean checkMobileNumber(LoanApplication loanApplication){
        String applicantContact = loanApplication.getApplicantContact();
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        CustomUserDetails currentUser =
                (CustomUserDetails) authentication.getPrincipal();
        return currentUser.getUser().getPhone().equals(applicantContact);
    }

}
