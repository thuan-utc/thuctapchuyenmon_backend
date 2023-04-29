package cntt2.k61.backend.service;

import cntt2.k61.backend.domain.Bill;
import cntt2.k61.backend.domain.BillStatus;
import cntt2.k61.backend.domain.Customer;
import cntt2.k61.backend.domain.User;
import cntt2.k61.backend.dto.BillDto;
import cntt2.k61.backend.repository.BillRepository;
import cntt2.k61.backend.repository.CustomerRepository;
import cntt2.k61.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillService {
    private final Logger log = LoggerFactory.getLogger(BillService.class);
    private final BillRepository billRepository;
    private final EmailService emailService;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @Autowired
    public BillService(BillRepository billRepository, EmailService emailService, CustomerRepository customerRepository, UserRepository userRepository) {
        this.billRepository = billRepository;
        this.emailService = emailService;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    public Page<BillDto> getPaidBill(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Bill> bills = billRepository.findAllByStatus(BillStatus.paid, pageable);
        List<BillDto> response =  bills.stream().map(b -> {
            BillDto billDto = new BillDto();
            billDto.setId(b.getId());
            billDto.setBillStatus(b.getStatus());
            billDto.setAmount(b.getAmount());
            billDto.setCustomerId(b.getCustomerId());
            billDto.setCustomerName(b.getCustomer().getName());
            billDto.setCreatedDate(b.getCreatedAt());
            billDto.setDueDate(b.getDueDate());
            return billDto;
        }).toList();
        return new PageImpl<>(response, pageable, bills.getTotalElements());
    }

    public Page<BillDto> getPendingBill(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Bill> bills = billRepository.findAllByStatus(BillStatus.pending, pageable);
        List<BillDto> response =  bills.stream().map(b -> {
            BillDto billDto = new BillDto();
            billDto.setId(b.getId());
            billDto.setBillStatus(b.getStatus());
            billDto.setAmount(b.getAmount());
            billDto.setCustomerId(b.getCustomerId());
            billDto.setCustomerName(b.getCustomer().getName());
            billDto.setCreatedDate(b.getCreatedAt());
            billDto.setDueDate(b.getDueDate());
            return billDto;
        }).toList();
        return new PageImpl<>(response, pageable, bills.getTotalElements());
    }

    public boolean sendRemindEmailTo(Long customerId, Long billId) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        Optional<Bill> billOpt = billRepository.findById(billId);
        if (customerOpt.isEmpty()) {
            log.error("Can not find customer with id {}", customerId);
        } else if (billOpt.isEmpty()){
            log.error("Can not find bill with id {}", billId);
        } else {
            Customer customer = customerOpt.get();
            Bill bill = billOpt.get();
            User user = userRepository.findByCustomerId(customer.getId());
            try {
                String content = String.format("Hi %s, you have a bill %d month, due date %s", customer.getName(), bill.getAmount(),
                        bill.getDueDate());
                emailService.sendEmail(user.getEmail(), "Internet Bill" ,content);
                return true;
            } catch (Exception e) {
                log.error("Can not send email to {}", user.getEmail());
            }
        }
        return false;
    }
}
