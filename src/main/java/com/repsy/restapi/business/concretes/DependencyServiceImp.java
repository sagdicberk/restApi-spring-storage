package com.repsy.restapi.business.concretes;

import com.repsy.restapi.business.abstracts.DependencyService;
import com.repsy.restapi.model.Dependency;
import com.repsy.restapi.repository.DependencyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DependencyServiceImp implements DependencyService {
    private final DependencyRepository dependencyRepository;

    public DependencyServiceImp(DependencyRepository dependencyRepository) {
        this.dependencyRepository = dependencyRepository;
    }


    @Override
    public void saveDependency(Dependency dependency) {
        if (isExist(dependency.getPackageName(),dependency.getVersion())){
            throw new RuntimeException("this " + dependency.getPackageName()+"|"+dependency.getVersion() + " already exists");
        }

        dependencyRepository.save(dependency);
    }

    @Override
    public void deleteDependency(Dependency dependency) {

    }

    @Override
    public List<Dependency> getAllDependencies() {
        return dependencyRepository.findAll();
    }

    @Override
    public Dependency getAllDependencies(String name, String version) {
        return dependencyRepository.findDependencyByPackageNameAndVersion(name,version).orElseThrow(() -> new RuntimeException("this " + name + " does not exist"));
    }

    @Override
    public Dependency getDependency(long id) {
        return dependencyRepository.findById(id).orElseThrow(() -> new RuntimeException("this " + id + " does not exist"));
    }

    @Override
    public boolean isExist(String name, String version) {
        return dependencyRepository.findDependencyByPackageNameAndVersion(name, version).isPresent();
    }
}
