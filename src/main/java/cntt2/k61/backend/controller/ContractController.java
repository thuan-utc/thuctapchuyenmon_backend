package cntt2.k61.backend.controller;

import cntt2.k61.backend.service.ContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContractController {
    private final Logger log = LoggerFactory.getLogger(ContractController.class);
    private final ContractService contractService;

    @Autowired
    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }
}
