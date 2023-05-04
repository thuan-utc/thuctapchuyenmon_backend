package cntt2.k61.backend.service;

import cntt2.k61.backend.domain.InternetPackage;
import cntt2.k61.backend.repository.InternetPackagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InternetPackageService {
    private InternetPackagesRepository internetPackagesRepository;

    @Autowired
    public InternetPackageService(InternetPackagesRepository internetPackagesRepository) {
        this.internetPackagesRepository = internetPackagesRepository;
    }

    public List<InternetPackage> getAllPackages() {
        return  internetPackagesRepository.findAll();
    }
}
