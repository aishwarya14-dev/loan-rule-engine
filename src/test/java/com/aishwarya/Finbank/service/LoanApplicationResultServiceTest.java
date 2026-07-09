package com.aishwarya.Finbank.service;

import com.aishwarya.Finbank.enums.Decision;
import com.aishwarya.Finbank.metrics.RuleEngineMetrics;
import com.aishwarya.Finbank.model.*;
import com.aishwarya.Finbank.repository.LoanApplicationResultRepo;
import com.aishwarya.Finbank.repository.LoanRepository;
import com.aishwarya.Finbank.ruleengine.evaluation.RuleEvaluationHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanApplicationResultServiceTest {

    @InjectMocks
    private LoanApplicationResultService loanApplicationResultService;

    @Mock
    private LoanApplicationResultRepo loanApplicationResultRepo;

    @Mock
    private RuleEngineMetrics metrics;

    @Mock
    private LoanRepository loanRepository;

    @Test
    void shouldCalculateStaticScoreAndApproveLoan() {
        LoanApplication application = new LoanApplication();

        RuleResult rule1 = mock(RuleResult.class);
        RuleResult rule2 = mock(RuleResult.class);

        when(rule1.getRuleEvaluationScore()).thenReturn(0.50);
        when(rule2.getRuleEvaluationScore()).thenReturn(0.30);

        when(loanApplicationResultRepo.save(any(LoanApplicationResult.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(loanRepository.save(any(LoanApplication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        loanApplicationResultService.calculateAndSaveLoanApplicationResult(
                List.of(rule1, rule2),
                application,
                false
        );

        ArgumentCaptor<LoanApplicationResult> captor =
                ArgumentCaptor.forClass(LoanApplicationResult.class);

        verify(loanApplicationResultRepo).save(captor.capture());
        verify(metrics).incrementApproved();
        verify(loanApplicationResultRepo).save(any(LoanApplicationResult.class));
        verify(loanRepository).save(application);

        LoanApplicationResult saved = captor.getValue();
        assertEquals(0.80, saved.getFinalScore());
        assertEquals(Decision.APPROVE, saved.getDecision());
        assertEquals(application, saved.getApplication());

    }

    @Test
    void shouldCalculateDynamicScoreAndSaveResult() {
        LoanApplication loanApplication = new LoanApplication();

        RuleResult rule1 = mock(RuleResult.class);
        RuleResult rule2 = mock(RuleResult.class);

        LoanTypeFactorConfig loanTypeFactorConfig1 = mock(LoanTypeFactorConfig.class);
        LoanTypeFactorConfig loanTypeFactorConfig2 = mock(LoanTypeFactorConfig.class);

        when(rule1.getLoanTypeFactorConfig()).thenReturn(loanTypeFactorConfig1);
        when(rule2.getLoanTypeFactorConfig()).thenReturn(loanTypeFactorConfig2);

        ImportanceLevel level1 = mock(ImportanceLevel.class);
        ImportanceLevel level2 = mock(ImportanceLevel.class);

        when(loanTypeFactorConfig1.getImportanceLevel()).thenReturn(level1);
        when(loanTypeFactorConfig2.getImportanceLevel()).thenReturn(level2);

        when(level1.getWeight()).thenReturn(5);
        when(level2.getWeight()).thenReturn(3);

        Factor factor1 = new Factor();
        Factor factor2 = new Factor();

        when(loanTypeFactorConfig1.getFactor()).thenReturn(factor1);
        when(loanTypeFactorConfig2.getFactor()).thenReturn(factor2);

        when(rule1.getRuleEvaluationScore()).thenReturn(1.0);
        when(rule2.getRuleEvaluationScore()).thenReturn(0.5);

        when(loanApplicationResultRepo.save(any(LoanApplicationResult.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(loanRepository.save(any(LoanApplication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        LoanApplicationResult result =
                loanApplicationResultService.calculateAndSaveLoanApplicationResult(
                        List.of(rule1, rule2),
                        loanApplication,
                        true
                );

        assertEquals(0.8125, result.getFinalScore(), 0.0001);
        assertEquals(Decision.APPROVE, result.getDecision());

        verify(metrics).incrementApproved();
        verify(loanApplicationResultRepo).save(any(LoanApplicationResult.class));
        verify(loanRepository).save(loanApplication);

    }
}