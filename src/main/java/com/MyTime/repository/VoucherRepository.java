package com.MyTime.repository;


import com.MyTime.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    Optional<Voucher> findByVoucherId(long id);

    List<Voucher> getByCompanyIdAndStatusIn(long id, List<String> status);

}
