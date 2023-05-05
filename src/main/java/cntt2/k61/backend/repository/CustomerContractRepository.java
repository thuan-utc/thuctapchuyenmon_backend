package cntt2.k61.backend.repository;

import cntt2.k61.backend.domain.CustomerContract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerContractRepository extends JpaRepository<CustomerContract, Long> {
    Optional<List<CustomerContract>> findAllByCustomerId(Long customerId);
    Page<CustomerContract> findByCustomerId(Long id, Pageable pageable);

    List<CustomerContract> findAllByIsDeletedIsFalseAndEndDateGreaterThan(Date endDate);

}
