package cntt2.k61.backend.controller;

import cntt2.k61.backend.dto.BillDto;
import cntt2.k61.backend.service.BillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bill")
public class BillController {
    private final Logger log = LoggerFactory.getLogger(BillController.class);
    private final BillService billService;

    @Autowired
    public BillController(BillService billService) {
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
}