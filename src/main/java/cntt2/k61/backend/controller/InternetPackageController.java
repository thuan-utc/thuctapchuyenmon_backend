package cntt2.k61.backend.controller;

import cntt2.k61.backend.domain.InternetPackage;
import cntt2.k61.backend.service.InternetPackageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internet-packages")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})

public class InternetPackageController {
    private final Logger log = LoggerFactory.getLogger(InternetPackageController.class);
    private InternetPackageService internetPackageService;

    @Autowired
    public InternetPackageController(InternetPackageService internetPackageService) {
        this.internetPackageService = internetPackageService;
    }

    @GetMapping("/get-all-packages")
    public List<InternetPackage> getAllPackages() {
        log.info("Getting all packages");
        return internetPackageService.getAllPackages();
    }

}
