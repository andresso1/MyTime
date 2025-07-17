package com.MyTime.service;

import com.MyTime.entity.Company;
import com.MyTime.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyService companyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getByNameCompany_Found() {
        Company company = new Company("Test Company", "Address", "12345", "NIT");
        when(companyRepository.findByNameCompany("Test Company")).thenReturn(Optional.of(company));

        Optional<Company> foundCompany = companyService.getByNameCompany("Test Company");

        assertTrue(foundCompany.isPresent());
        assertEquals("Test Company", foundCompany.get().getNameCompany());
    }

    @Test
    void getByNameCompany_NotFound() {
        when(companyRepository.findByNameCompany("NonExistent Company")).thenReturn(Optional.empty());

        Optional<Company> foundCompany = companyService.getByNameCompany("NonExistent Company");

        assertFalse(foundCompany.isPresent());
    }

    @Test
    void save() {
        Company company = new Company("New Company", "Address", "12345", "NIT");
        companyService.save(company);

        verify(companyRepository, times(1)).save(company);
    }

    @Test
    void existsById_True() {
        when(companyRepository.existsById(1)).thenReturn(true);

        boolean exists = companyService.existsById(1);

        assertTrue(exists);
    }

    @Test
    void existsById_False() {
        when(companyRepository.existsById(1)).thenReturn(false);

        boolean exists = companyService.existsById(1);

        assertFalse(exists);
    }

    @Test
    void getOne_Found() {
        Company company = new Company("Test Company", "Address", "12345", "NIT");
        when(companyRepository.findById(1)).thenReturn(Optional.of(company));

        Optional<Company> foundCompany = companyService.getOne(1);

        assertTrue(foundCompany.isPresent());
        assertEquals("Test Company", foundCompany.get().getNameCompany());
    }

    @Test
    void getOne_NotFound() {
        when(companyRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Company> foundCompany = companyService.getOne(1);

        assertFalse(foundCompany.isPresent());
    }

    @Test
    void getByCompanyId() {
        Company company = new Company("Test Company", "Address", "12345", "NIT");
        when(companyRepository.getByCompanyId(1)).thenReturn(company);

        Company foundCompany = companyService.getByCompanyId(1);

        assertEquals("Test Company", foundCompany.getNameCompany());
    }
}
