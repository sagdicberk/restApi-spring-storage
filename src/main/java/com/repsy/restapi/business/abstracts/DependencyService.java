package com.repsy.restapi.business.abstracts;

import com.repsy.restapi.model.Dependency;

import java.util.List;

public interface DependencyService {
    void saveDependency(Dependency dependency);

    void deleteDependency(Dependency dependency);

    List<Dependency> getAllDependencies();

    Dependency getAllDependencies(String name, String version);
    Dependency getDependency(long id);

    boolean isExist(String name, String version);
}
