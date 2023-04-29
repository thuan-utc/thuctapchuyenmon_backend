package cntt2.k61.backend.service;

import cntt2.k61.backend.repository.CustomerContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContractService {
    private final CustomerContractRepository contractRepository;
    @Autowired
    public ContractService(CustomerContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }
}
