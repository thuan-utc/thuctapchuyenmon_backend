package cntt2.k61.backend.controller;

import cntt2.k61.backend.repository.BillRepository;
import cntt2.k61.backend.repository.CustomerContractRepository;
import cntt2.k61.backend.repository.CustomerRepository;
import cntt2.k61.backend.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final Logger log = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerRepository customerRepository;
    private final BillRepository billRepository;
    private final PaymentRepository paymentRepository;
    private final CustomerContractRepository customerContractRepository;

    @Autowired
    public CustomerController(CustomerRepository customerRepository, BillRepository billRepository, PaymentRepository paymentRepository, CustomerContractRepository customerContractRepository) {
        this.customerRepository = customerRepository;
        this.billRepository = billRepository;
        this.paymentRepository = paymentRepository;
        this.customerContractRepository = customerContractRepository;
    }

}
