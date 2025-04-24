package com.repsy.restapi.repository;

import com.repsy.restapi.model.Dependency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DependencyRepository extends JpaRepository<Dependency, Long> {
    Optional<Dependency> findDependencyByPackageNameAndVersion(String name, String version);
}
