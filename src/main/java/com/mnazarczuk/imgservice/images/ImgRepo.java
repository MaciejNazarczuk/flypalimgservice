package com.mnazarczuk.imgservice.images;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImgRepo extends JpaRepository<Image, Long> {
    Optional<Image> findFirstByCode(String code);
}
