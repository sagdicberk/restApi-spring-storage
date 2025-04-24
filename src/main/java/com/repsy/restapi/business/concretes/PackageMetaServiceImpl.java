package com.repsy.restapi.business.concretes;

import com.repsy.restapi.business.abstracts.DependencyService;
import com.repsy.restapi.business.abstracts.PackageMetaService;
import com.repsy.restapi.dto.MetaDto;
import com.repsy.restapi.model.Dependency;
import com.repsy.restapi.model.PackageMeta;
import com.repsy.restapi.repository.PackageMetaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PackageMetaServiceImpl implements PackageMetaService {

    private final PackageMetaRepository packageMetaRepository;
    // private final DependencyService dependencyService;

    public PackageMetaServiceImpl(PackageMetaRepository packageMetaRepository ){ //}, DependencyService dependencyService) {
        this.packageMetaRepository = packageMetaRepository;
        // this.dependencyService = dependencyService;
    }

    @Override
    public PackageMeta saveMeta(MetaDto metaDto) {
        boolean exists = packageMetaRepository
                .findByNameAndVersion(metaDto.getName(), metaDto.getVersion())
                .isPresent();

        if (exists) {
            throw new IllegalArgumentException("This package already exists: " +
                    metaDto.getName() + " - " + metaDto.getVersion());
        }

        PackageMeta packageMeta = new PackageMeta();
        packageMeta.setName(metaDto.getName());
        packageMeta.setVersion(metaDto.getVersion());
        packageMeta.setAuthor(metaDto.getAuthor());



        return packageMetaRepository.save(packageMeta);
    }

    @Override
    public List<PackageMeta> getAll() {
        return packageMetaRepository.findAll();
    }

    @Override
    public Optional<PackageMeta> getById(Long id) {
        return packageMetaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        packageMetaRepository.deleteById(id);
    }
}
