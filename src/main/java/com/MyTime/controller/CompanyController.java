package com.MyTime.controller;

import com.MyTime.entity.Company;
import com.MyTime.dto.CompanyDto;
import com.MyTime.dto.Message;
import com.MyTime.service.CompanyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
@CrossOrigin(origins = "*")
public class CompanyController {
    @Autowired
    CompanyService companyService;

    @GetMapping("/detail/{id}")
    public ResponseEntity<Company> getById(@PathVariable("id") Integer id) {
        if (!companyService.existsById(id))
            return new ResponseEntity(new Message("not exist"), HttpStatus.NOT_FOUND);
        Company company = companyService.getOne(id).get();

        System.out.println("busco la compania" +  "->" + company);
        return new ResponseEntity(company, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody CompanyDto companyDto) {
        if (!companyService.existsById(id))
            return new ResponseEntity(new Message("Company not exits"), HttpStatus.NOT_FOUND);
        if (StringUtils.isBlank(companyDto.getNameCompany()))
            return new ResponseEntity(new Message("el Name is required"), HttpStatus.BAD_REQUEST);
        if (StringUtils.isBlank(companyDto.getNit()))
            return new ResponseEntity(new Message("The id company is required"), HttpStatus.BAD_REQUEST);

        Company company = companyService.getOne(id).get();
        company.setNameCompany(companyDto.getNameCompany());
        company.setNit(companyDto.getNit());
        company.setAddress(companyDto.getAddress());
        company.setPhone(companyDto.getPhone());

        companyService.save(company);
        return new ResponseEntity(new Message("Company updated"), HttpStatus.OK);
    }
}
