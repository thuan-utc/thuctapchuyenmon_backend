package cntt2.k61.backend.repository;

import cntt2.k61.backend.domain.Bill;
import cntt2.k61.backend.domain.BillStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    Page<Bill> findAllByStatus(BillStatus status, Pageable pageable);

    Page<Bill> findAllByStatusAndCustomerId(BillStatus paid, long customerId, Pageable pageable);

    List<Bill> findAllByStatusAndCreatedAtBetween(BillStatus paid, Instant toInstant, Instant toInstant1);

    List<Bill> findAllByStatus(BillStatus status);
}
