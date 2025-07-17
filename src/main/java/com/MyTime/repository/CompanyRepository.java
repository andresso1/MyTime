package com.MyTime.repository;


import com.MyTime.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Optional<Company> findByNameCompany(String nameCompany);
    Company getByCompanyId(Integer id);

}
