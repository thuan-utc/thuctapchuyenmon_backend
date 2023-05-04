package cntt2.k61.backend.repository;

import cntt2.k61.backend.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllByCreatedDateBetween(Instant startDate, Instant endDate);
}
