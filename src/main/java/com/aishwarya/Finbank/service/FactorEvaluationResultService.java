package com.aishwarya.Finbank.service;

import com.aishwarya.Finbank.repository.FactorEvaluationResultRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FactorEvaluationResultService {

    @Autowired
    private FactorEvaluationResultRepo factorEvaluationResultRepo;


}
