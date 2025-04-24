package com.repsy.restapi.business.abstracts;

import com.repsy.restapi.dto.MetaDto;
import com.repsy.restapi.model.PackageMeta;

import java.util.List;
import java.util.Optional;

public interface PackageMetaService {
    PackageMeta saveMeta(MetaDto metaDto);
    List<PackageMeta> getAll();
    Optional<PackageMeta> getById(Long id);
    void delete(Long id);
}
