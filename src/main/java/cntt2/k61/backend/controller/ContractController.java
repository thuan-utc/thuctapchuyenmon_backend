package cntt2.k61.backend.controller;

import cntt2.k61.backend.dto.ContractDto;
import cntt2.k61.backend.service.ContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contract")
public class ContractController {
    private final Logger log = LoggerFactory.getLogger(ContractController.class);
    private final ContractService contractService;

    @Autowired
    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping("/{userName}")
    public Page<ContractDto> getContractByCustomer(@RequestParam(defaultValue = "0") int pageNumber,
                                                   @RequestParam(defaultValue = "10") int pageSize,
                                                   @PathVariable String userName) {
        log.info("Getting all contract for customer {} from page {}, size {}", userName, pageNumber, pageSize);
        return contractService.getContractByCustomer(pageNumber, pageSize, userName);
    }

    @GetMapping("/{userName}/{packageId}")
    public String submitPackage(@PathVariable String userName, @PathVariable Long packageId) {
        return contractService.submitPackage(userName, packageId);
    }
}
