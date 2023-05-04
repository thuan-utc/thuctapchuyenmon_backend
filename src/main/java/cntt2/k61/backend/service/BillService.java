package cntt2.k61.backend.service;

import cntt2.k61.backend.domain.*;
import cntt2.k61.backend.dto.BillDto;
import cntt2.k61.backend.exception.BusinessException;
import cntt2.k61.backend.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class BillService {
    private final Logger log = LoggerFactory.getLogger(BillService.class);
    private final BillRepository billRepository;
    private final EmailService emailService;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final InternetPackagesRepository internetPackagesRepository;
    private final CustomerContractRepository contractRepository;

    @Autowired
    public BillService(BillRepository billRepository, EmailService emailService, CustomerRepository customerRepository, UserRepository userRepository, PaymentRepository paymentRepository, InternetPackagesRepository internetPackagesRepository, CustomerContractRepository contractRepository) {
        this.billRepository = billRepository;
        this.emailService = emailService;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.internetPackagesRepository = internetPackagesRepository;
        this.contractRepository = contractRepository;
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

    public boolean sendRemindEmailTo(String userName, Long billId) {
        User user = userRepository.findByUserName(userName).orElseThrow(() -> new BusinessException("Can not get user by userName {}", userName));
        Customer customer = user.getCustomer();
        Optional<Bill> billOpt = billRepository.findById(billId);
        if (customer == null) {
            log.error("Can not find customer with userName {}", userName);
        } else if (billOpt.isEmpty()){
            log.error("Can not find bill with id {}", billId);
        } else {
            Bill bill = billOpt.get();
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

    public Page<BillDto> getPaidBillByCustomer(int pageNumber, int pageSize, String userName) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        User user = userRepository.findByUserName(userName).orElseThrow(() -> new BusinessException("Can not get user by userName {}", userName));
        Customer customer = user.getCustomer();
        Page<Bill> bills = billRepository.findAllByStatusAndCustomerId(BillStatus.paid,customer.getId(), pageable);
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

    public Page<BillDto> getPendingBillByCustomer(int pageNumber, int pageSize, String userName) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        User user = userRepository.findByUserName(userName).orElseThrow(() -> new BusinessException("Can not get user by userName {}", userName));
        Customer customer = user.getCustomer();
        Page<Bill> bills = billRepository.findAllByStatusAndCustomerId(BillStatus.pending, customer.getId(), pageable);
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

    @Transactional
    public boolean payBill(Long billId) {
        try {
            Bill bill = billRepository.findById(billId).orElseThrow(() -> new BusinessException("Can not find bill with id "+billId));
            bill.setStatus(BillStatus.paid);
            bill = billRepository.save(bill);
            String customerId = bill.getCustomerId().toString();
            Customer customer = customerRepository.findById(bill.getCustomerId()).orElseThrow(() -> new BusinessException("Can not find customer with id "+customerId));
            CustomerContract contract = contractRepository.findById(bill.getContract().getId()).orElseThrow(() -> new BusinessException("Can not find customer_contract for customer"+customerId));
            Payment payment = new Payment();
            payment.setBill(bill);
            payment.setCustomer(customer);
            payment.setCustomerContract(contract);
            InternetPackage internetPackage = contract.getPackages();
            Long totalMoney = bill.getAmount() * internetPackage.getPrice();
            payment.setTotalMoney(totalMoney);
            // mock transactionId
            payment.setTransactionId("data_mock");
            paymentRepository.save(payment);
            return true;
        } catch (Exception e) {
            log.error("Can not pay bill {}", billId,e);
            return false;
        }
    }
}
