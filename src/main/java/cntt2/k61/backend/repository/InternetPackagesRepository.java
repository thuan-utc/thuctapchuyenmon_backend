package cntt2.k61.backend.repository;

import cntt2.k61.backend.domain.InternetPackage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InternetPackagesRepository extends JpaRepository<InternetPackage, Long> {
    Optional<InternetPackage> findByIdAndIsDeletedIsFalse(Long packageId);

    Page<InternetPackage> findAllByIsDeletedIsFalse(Pageable pageable);

    Page<InternetPackage> findAllByOrderByCreatedDateDesc(Pageable pageable);

    Optional<InternetPackage> findAllByPackageName(String packageName);
}
