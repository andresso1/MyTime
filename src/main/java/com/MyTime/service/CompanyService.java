package com.MyTime.service;

import com.MyTime.entity.Company;
import com.MyTime.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CompanyService {
    @Autowired
    CompanyRepository companyRepository;

    public Optional<Company> getByNameCompany(String nameCompany){
        return companyRepository.findByNameCompany(nameCompany);
    }

    public void save(Company company){
        companyRepository.save(company);
    }

    public boolean existsById(int id) {
        return companyRepository.existsById(id);
    }

    public Optional<Company> getOne(Integer id) {
        return companyRepository.findById(id);
    }

    public Company getByCompanyId(Integer id) {
        return companyRepository.getByCompanyId(id);
    }

}
