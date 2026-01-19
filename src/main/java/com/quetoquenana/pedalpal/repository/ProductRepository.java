package com.quetoquenana.pedalpal.repository;

import com.quetoquenana.pedalpal.model.data.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query("select p from Product p join p.status s where s.code in :codes")
    Page<Product> findByStatusCodes(Pageable page, @Param("codes") List<String> codes);
}
