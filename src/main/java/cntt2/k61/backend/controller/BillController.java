package cntt2.k61.backend.controller;

import cntt2.k61.backend.dto.BillDto;
import cntt2.k61.backend.service.BillService;
import cntt2.k61.backend.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bill")
public class BillController {
    private final Logger log = LoggerFactory.getLogger(BillController.class);
    private final BillService billService;

    @Autowired
    public BillController(BillService billService, EmailService emailService) {
        this.billService = billService;
    }

    @GetMapping("/get-paid-bill")
    public Page<BillDto> getPaidBill(@RequestParam(defaultValue = "0") int pageNumber,
                                     @RequestParam(defaultValue = "10") int pageSize){
        log.info("getting bills from page {}", pageNumber);
        return billService.getPaidBill(pageNumber, pageSize);
    }

    @GetMapping("/get-pending-bill")
    public Page<BillDto> getPendingBill(@RequestParam(defaultValue = "0") int pageNumber,
                                     @RequestParam(defaultValue = "10") int pageSize){
        log.info("getting bills from page {}", pageNumber);
        return billService.getPendingBill(pageNumber, pageSize);
    }

    @GetMapping("/{customerId}/{billId}/send-remind-email")
    public String sendRemindEmailTo(@PathVariable Long customerId, @PathVariable Long billId) {
        if (billService.sendRemindEmailTo(customerId, billId)) {
            return "SUCCESS";
        } else {
            return "FAILED";
        }
    }

    @GetMapping("{billId}/pay")
    public String payBill(@PathVariable Long billId) {
        if (billService.payBill(billId)) {
            return "SUCCESS";
        } else {
            return "FAILED";
        }
    }

    @GetMapping("{userName}/get-paid-bill")
    public Page<BillDto> getPaidBillByCustomer(@RequestParam(defaultValue = "0") int pageNumber,
                                               @RequestParam(defaultValue = "10") int pageSize,
                                               @PathVariable String userName){
        log.info("getting bills from page {}", pageNumber);
        return billService.getPaidBillByCustomer(pageNumber, pageSize, userName);
    }

    @GetMapping("{userName}/get-pending-bill")
    public Page<BillDto> getPendingBillByCustomer(@RequestParam(defaultValue = "0") int pageNumber,
                                                  @RequestParam(defaultValue = "10") int pageSize,
                                                  @PathVariable String userName){
        log.info("getting bills from page {}", pageNumber);
        return billService.getPendingBillByCustomer(pageNumber, pageSize, userName);
    }
}
