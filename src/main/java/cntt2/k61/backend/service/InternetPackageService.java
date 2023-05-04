package cntt2.k61.backend.service;

import cntt2.k61.backend.domain.InternetPackage;
import cntt2.k61.backend.exception.BusinessException;
import cntt2.k61.backend.repository.InternetPackagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class InternetPackageService {
    private final InternetPackagesRepository internetPackagesRepository;

    @Autowired
    public InternetPackageService(InternetPackagesRepository internetPackagesRepository) {
        this.internetPackagesRepository = internetPackagesRepository;
    }

    public Page<InternetPackage> getAllPackages(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return  internetPackagesRepository.findAllByOrderByCreatedDateDesc(pageable);
    }

    public Page<InternetPackage> getAllPackagesForCustomer(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return  internetPackagesRepository.findAllByIsDeletedIsFalse(pageable);
    }

    public boolean toggleInternetPackageStatus(Long internetPackageId) {
        InternetPackage internetPackage = internetPackagesRepository.findById(internetPackageId).orElseThrow(() -> new BusinessException("Can not get internetpackage with id: " + internetPackageId));
        internetPackage.setDeleted(!internetPackage.isDeleted());
        internetPackagesRepository.save(internetPackage);
        return true;
    }

    public String save(InternetPackage internetPackage) {
        Optional<InternetPackage> existingInternetPackage = internetPackagesRepository.findAllByPackageName(internetPackage.getPackageName());
        if (existingInternetPackage.isPresent()) {
            return internetPackage.getPackageName() + " has already existed";
        }
        internetPackage.setCreatedDate(Instant.now());
        internetPackagesRepository.save(internetPackage);
        return "SUCCESS";
    }
}
