package cntt2.k61.backend.service;

import cntt2.k61.backend.domain.Customer;
import cntt2.k61.backend.domain.CustomerContract;
import cntt2.k61.backend.domain.InternetPackage;
import cntt2.k61.backend.domain.User;
import cntt2.k61.backend.dto.ContractDto;
import cntt2.k61.backend.exception.BusinessException;
import cntt2.k61.backend.repository.CustomerContractRepository;
import cntt2.k61.backend.repository.CustomerRepository;
import cntt2.k61.backend.repository.InternetPackagesRepository;
import cntt2.k61.backend.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.annotation.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

@Service
public class ContractService {
    private final CustomerContractRepository contractRepository;
    private final CustomerRepository customerRepository;
    private final InternetPackagesRepository packagesRepository;
    private final UserRepository userRepository;

    @Autowired
    public ContractService(CustomerContractRepository contractRepository, CustomerRepository customerRepository, InternetPackagesRepository packagesRepository, UserRepository userRepository) {
        this.contractRepository = contractRepository;
        this.customerRepository = customerRepository;
        this.packagesRepository = packagesRepository;
        this.userRepository = userRepository;
    }

    public Page<ContractDto> getContractByCustomer(int pageNumber, int pageSize, String userName) {
        User user = userRepository.findByUserName(userName).orElseThrow(() -> new BusinessException("Can not get user by userName {}", userName));
        Customer customer = user.getCustomer();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<CustomerContract> customerContracts = contractRepository.findByCustomerId(customer.getId(), pageable);
        List<ContractDto> response = customerContracts.stream()
                .map(contract -> {
                    ContractDto contractDto = new ContractDto();
                    contractDto.setContractId(contract.getId());
                    contractDto.setCustomerId(customer.getId());
                    InternetPackage internetPackage = packagesRepository.findById(contract.getPackageId())
                            .orElse(null);
                    if (internetPackage != null) {
                        contractDto.setPackageId(internetPackage.getId());
                        contractDto.setCustomerName(customer.getName());
                        contractDto.setPackageName(internetPackage.getPackageName());
                        contractDto.setPackageName(internetPackage.getPackageName());
                        contractDto.setCreatedDate(contract.getCreatedAt());
                        contractDto.setStartDate(contract.getStartDate().toString());
                        contractDto.setEndDate(contract.getEndDate().toString());
                        contractDto.setPrice(internetPackage.getPrice());
                        return contractDto;
                    } else {
                        throw new BusinessException("Can not find package {}", String.valueOf(contract.getPackageId()));
                    }
                })
                .toList();
        return new PageImpl<>(response, pageable, customerContracts.getTotalElements());
    }

    public String submitPackage(String userName, Long packageId) {
        User user = userRepository.findByUserName(userName).orElseThrow(() -> new BusinessException("Can not get user by userName {}", userName));
        Customer customer = user.getCustomer();
        Optional<List<CustomerContract>> contractRegistered = contractRepository.findAllByCustomerId(customer.getId());
        Date currentDate = new Date();
        String result = "";
        if (contractRegistered.isPresent()) {
            result = contractRegistered.get().stream()
                    .filter(c -> Objects.equals(c.getPackageId(), packageId) && c.getEndDate().compareTo(currentDate) > 0)
                    .findFirst()
                    .map(c -> "You have registered this package, and endDate is " + c.getEndDate().toString())
                    .orElse(null);
        }
        if (StringUtils.isNotBlank(result)) {
            return result;
        }
        Optional<InternetPackage> wantRegister = packagesRepository.findByIdAndIsDeletedIsFalse(packageId);
        if (wantRegister.isPresent()) {
            CustomerContract contract = new CustomerContract();
            contract.setCustomerId(customer.getId());
            contract.setCustomer(customer);
            contract.setPackages(wantRegister.get());
            contract.setPackageId(wantRegister.get().getId());
            contract.setCreatedAt(Instant.now());
            contract.setStartDate(currentDate);
            Calendar endDate = Calendar.getInstance();
            endDate.setTime(currentDate);
            endDate.add(Calendar.DAY_OF_MONTH, 30);
            contract.setEndDate(endDate.getTime());
            contractRepository.save(contract);
            return "SUCCESS";
        } else {
            return "Can not find internet package you want";
        }
    }
}
