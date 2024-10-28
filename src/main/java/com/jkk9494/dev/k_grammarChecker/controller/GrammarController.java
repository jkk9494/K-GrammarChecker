package com.jkk9494.dev.k_grammarChecker.controller;

import com.jkk9494.dev.k_grammarChecker.dto.GrammarRequest;
import com.jkk9494.dev.k_grammarChecker.dto.GrammarResponse;
import com.jkk9494.dev.k_grammarChecker.service.GrammarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/grammar")
public class GrammarController {

    @Autowired
    private GrammarService grammarService;

    @PostMapping("/check")
    public GrammarResponse checkGrammar(@RequestBody GrammarRequest request) {
        return grammarService.checkGrammar(request);
    }


}