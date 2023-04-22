package cntt2.k61.backend.repository;

import cntt2.k61.backend.domain.Packages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackagesRepository extends JpaRepository<Packages, Long> {
}
