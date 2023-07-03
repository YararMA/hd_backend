package com.github.dlism.backend.mappers;

import com.github.dlism.backend.dto.OrganizationDto;
import com.github.dlism.backend.models.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrganizationMapper {
    OrganizationMapper INSTANCE = Mappers.getMapper(OrganizationMapper.class);

    OrganizationDto entityToDto(Organization organization);

    Organization dtoToEntity(OrganizationDto organizationDto);

    List<OrganizationDto> entityToDto(List<Organization> organizations);
}
