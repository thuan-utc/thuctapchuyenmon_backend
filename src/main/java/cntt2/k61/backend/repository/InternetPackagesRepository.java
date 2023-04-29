package cntt2.k61.backend.repository;

import cntt2.k61.backend.domain.InternetPackages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternetPackagesRepository extends JpaRepository<InternetPackages, Long> {
}
