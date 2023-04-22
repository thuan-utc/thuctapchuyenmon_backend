package cntt2.k61.backend.controller;

import cntt2.k61.backend.domain.Customer;
import cntt2.k61.backend.repository.BillRepository;
import cntt2.k61.backend.repository.CustomerContractRepository;
import cntt2.k61.backend.repository.CustomerRepository;
import cntt2.k61.backend.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private Logger log = LoggerFactory.getLogger(CustomerController.class);
    private CustomerRepository customerRepository;
    private BillRepository billRepository;
    private PaymentRepository paymentRepository;
    private CustomerContractRepository customerContractRepository;

    @Autowired
    public CustomerController(CustomerRepository customerRepository, BillRepository billRepository, PaymentRepository paymentRepository, CustomerContractRepository customerContractRepository) {
        this.customerRepository = customerRepository;
        this.billRepository = billRepository;
        this.paymentRepository = paymentRepository;
        this.customerContractRepository = customerContractRepository;
    }

    @RequestMapping("/test")
    @GetMapping
    public Customer test() {
        return customerRepository.findAll().get(0);
    }
}
