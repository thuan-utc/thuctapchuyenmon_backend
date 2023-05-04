package cntt2.k61.backend.service;

import cntt2.k61.backend.domain.BillStatus;
import cntt2.k61.backend.domain.Payment;
import cntt2.k61.backend.repository.BillRepository;
import cntt2.k61.backend.repository.CustomerRepository;
import cntt2.k61.backend.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {
    private final CustomerRepository customerRepository;
    private final BillRepository billRepository;
    private final PaymentRepository paymentRepository;

    public AdminService(CustomerRepository customerRepository, BillRepository billRepository, PaymentRepository paymentRepository) {
        this.customerRepository = customerRepository;
        this.billRepository = billRepository;
        this.paymentRepository = paymentRepository;
    }

    public Map<String, String> getDashboardData() {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime startOfMonth = now.withDayOfMonth(1).with(LocalTime.MIN);
        ZonedDateTime endOfMonth = now.withDayOfMonth(now.getMonth().maxLength()).with(LocalTime.MAX);
        HashMap<String, String> response = new HashMap<>();
        long totalMoneyMonthly, totalCustomer, totalPaidBillMonthly, totalPendingBill;
        totalCustomer = customerRepository.count();
        List<Payment> payments = paymentRepository.findAllByCreatedDateBetween(startOfMonth.toInstant(), endOfMonth.toInstant());
        totalMoneyMonthly = payments.stream().mapToLong(Payment::getTotalMoney).sum();
        totalPaidBillMonthly = billRepository.findAllByStatusAndCreatedAtBetween(BillStatus.paid, startOfMonth.toInstant(), endOfMonth.toInstant()).size();
        totalPendingBill = billRepository.findAllByStatus(BillStatus.pending).size();
        response.put("totalMoneyMonthly", String.valueOf(totalMoneyMonthly));
        response.put("totalCustomer", String.valueOf(totalCustomer));
        response.put("totalPaidBillMonthly", String.valueOf(totalPaidBillMonthly));
        response.put("totalPendingBill", String.valueOf(totalPendingBill));
        return response;
    }
}
