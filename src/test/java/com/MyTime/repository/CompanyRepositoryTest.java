package com.MyTime.repository;

import com.MyTime.entity.Company;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Company company1;
    private Company company2;

    @BeforeEach
    void setUp() {
        company1 = new Company("Company A", "Address A", "111", "NIT A");
        company2 = new Company("Company B", "Address B", "222", "NIT B");

        entityManager.persist(company1);
        entityManager.persist(company2);
        entityManager.flush();
    }

    @Test
    void findByNameCompany_Found() {
        Optional<Company> foundCompany = companyRepository.findByNameCompany("Company A");

        assertTrue(foundCompany.isPresent());
        assertEquals("Company A", foundCompany.get().getNameCompany());
    }

    @Test
    void findByNameCompany_NotFound() {
        Optional<Company> foundCompany = companyRepository.findByNameCompany("NonExistent Company");

        assertFalse(foundCompany.isPresent());
    }

    @Test
    void getByCompanyId_Found() {
        Company foundCompany = companyRepository.getByCompanyId(company1.getCompanyId());

        assertNotNull(foundCompany);
        assertEquals("Company A", foundCompany.getNameCompany());
    }

    @Test
    void getByCompanyId_NotFound() {
        Company foundCompany = companyRepository.getByCompanyId(999);

        assertNull(foundCompany);
    }

    @Test
    void saveCompany() {
        Company newCompany = new Company("Company C", "Address C", "333", "NIT C");
        Company savedCompany = companyRepository.save(newCompany);

        assertNotNull(savedCompany.getCompanyId());
        assertEquals("Company C", savedCompany.getNameCompany());

        Optional<Company> found = companyRepository.findById(savedCompany.getCompanyId());
        assertTrue(found.isPresent());
        assertEquals("Company C", found.get().getNameCompany());
    }

    @Test
    void findById() {
        Optional<Company> foundCompany = companyRepository.findById(company1.getCompanyId());

        assertTrue(foundCompany.isPresent());
        assertEquals("Company A", foundCompany.get().getNameCompany());
    }

    @Test
    void existsById_True() {
        boolean exists = companyRepository.existsById(company1.getCompanyId());
        assertTrue(exists);
    }

    @Test
    void existsById_False() {
        boolean exists = companyRepository.existsById(999);
        assertFalse(exists);
    }
}
