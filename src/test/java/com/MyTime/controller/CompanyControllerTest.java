package com.MyTime.controller;

import com.MyTime.dto.CompanyDto;
import com.MyTime.entity.Company;
import com.MyTime.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CompanyControllerTest {

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private CompanyController companyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getById() {
        Company company = new Company();
        company.setCompanyId(1);

        when(companyService.existsById(1)).thenReturn(true);
        when(companyService.getOne(1)).thenReturn(Optional.of(company));

        ResponseEntity<Company> response = companyController.getById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void update() {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setNameCompany("Test Company");
        companyDto.setNit("123456789");

        Company company = new Company();
        company.setCompanyId(1);

        when(companyService.existsById(1)).thenReturn(true);
        when(companyService.getOne(1)).thenReturn(Optional.of(company));

        ResponseEntity<?> response = companyController.update(1, companyDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
