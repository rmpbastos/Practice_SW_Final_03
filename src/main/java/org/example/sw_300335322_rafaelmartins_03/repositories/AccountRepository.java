package org.example.sw_300335322_rafaelmartins_03.repositories;

import org.example.sw_300335322_rafaelmartins_03.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByCustomerNumber(String customerNumber);

}
