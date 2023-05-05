package cntt2.k61.backend.service;

import cntt2.k61.backend.domain.Bill;
import cntt2.k61.backend.domain.BillStatus;
import cntt2.k61.backend.domain.Customer;
import cntt2.k61.backend.domain.CustomerContract;
import cntt2.k61.backend.repository.BillRepository;
import cntt2.k61.backend.repository.CustomerContractRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ScheduleService {
    private final Logger log = LoggerFactory.getLogger(ScheduleService.class);
    private final BillRepository billRepository;
    private final CustomerContractRepository customerContractRepository;

    public ScheduleService(BillRepository billRepository, CustomerContractRepository customerContractRepository) {
        this.billRepository = billRepository;
        this.customerContractRepository = customerContractRepository;
    }

    @Scheduled(cron = "0 0 10 * * *")
    public void sendRemindEmailForNewBill() {
        log.info("Starting to send email remind bill for customer");
        Date today = new Date();
        List<CustomerContract> customerContractList = customerContractRepository.findAllByIsDeletedIsFalseAndEndDateGreaterThan(today);
        for (CustomerContract customerContract : customerContractList) {
            Date startDate = customerContract.getStartDate();
            if (ChronoUnit.DAYS.between(today.toInstant(), startDate.toInstant()) % 30 == 0) {
                Bill bill = new Bill();
                Customer customer = customerContract.getCustomer();
                bill.setCustomer(customer);
                bill.setStatus(BillStatus.pending);
                bill.setAmount(1L);
                bill.setContract(customerContract);
                bill.setCreatedAt(Instant.now());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(today);
                calendar.add(Calendar.DATE, 15);
                Date plus15Days = calendar.getTime();
                bill.setDueDate(plus15Days);
                billRepository.save(bill);
            }
        }
    }
//    @Scheduled(fixedRate = 1000)
//    public void testSchedule() {
//        log.info("test schedule");
//    }
}
