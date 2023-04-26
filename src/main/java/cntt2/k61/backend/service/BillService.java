package cntt2.k61.backend.service;

import cntt2.k61.backend.domain.Bill;
import cntt2.k61.backend.domain.BillStatus;
import cntt2.k61.backend.dto.BillDto;
import cntt2.k61.backend.repository.BillRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillService {
    private final Logger log = LoggerFactory.getLogger(BillService.class);
    private final BillRepository billRepository;

    @Autowired
    public BillService(BillRepository billRepository) {
        this.billRepository = billRepository;
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
}
