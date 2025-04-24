package com.repsy.restapi.repository;

import com.repsy.restapi.model.PackageMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PackageMetaRepository extends JpaRepository<PackageMeta, Long> {
    Optional<Object> findByNameAndVersion(String name, String version);
}
