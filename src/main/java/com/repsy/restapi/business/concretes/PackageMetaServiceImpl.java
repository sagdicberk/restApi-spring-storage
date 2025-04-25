package com.repsy.restapi.business.concretes;

import com.repsy.restapi.business.abstracts.PackageMetaService;
import com.repsy.restapi.dto.MetaDto;
import com.repsy.restapi.model.PackageMeta;
import com.repsy.restapi.repository.PackageMetaRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PackageMetaServiceImpl implements PackageMetaService {

    private final PackageMetaRepository packageMetaRepository;

    public PackageMetaServiceImpl(PackageMetaRepository packageMetaRepository) {
        this.packageMetaRepository = packageMetaRepository;
    }

    @Override
    public void saveMeta(MetaDto metaDto) {
        try {
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

            packageMetaRepository.save(packageMeta);
        } catch (IllegalArgumentException e) {
            // Handle invalid argument for existing package
            throw new RuntimeException(e.getMessage());  // You can add more logging here if needed
        } catch (DataAccessException e) {
            // Handle database access exceptions (e.g., connection issues)
            throw new RuntimeException("Error accessing the database while saving package metadata: " + e.getMessage(), e);
        } catch (Exception e) {
            // Handle any other general exceptions
            throw new RuntimeException("An unexpected error occurred while saving package metadata: " + e.getMessage(), e);
        }
    }

}
