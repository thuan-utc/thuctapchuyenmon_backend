package cntt2.k61.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {
    private final Logger log = LoggerFactory.getLogger(ScheduleService.class);
//    private final
    @Scheduled(cron = "0 0 10 1 * ?")
    public void sendRemindEmailForNewBill() {
        log.info("Starting to send email remind bill for customer");

    }
//    @Scheduled(fixedRate = 1000)
//    public void testSchedule() {
//        log.info("test schedule");
//    }
}
