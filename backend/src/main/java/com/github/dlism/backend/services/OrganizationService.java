package com.github.dlism.backend.services;

import com.github.dlism.backend.dto.OrganizationDto;
import com.github.dlism.backend.exceptions.DuplicateRecordException;
import com.github.dlism.backend.exceptions.OrganizationNotFoundException;
import com.github.dlism.backend.mappers.OrganizationMapper;
import com.github.dlism.backend.models.Organization;
import com.github.dlism.backend.models.User;
import com.github.dlism.backend.repositories.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    @Transactional
    public void create(OrganizationDto organizationDto, User user) throws DuplicateRecordException {
        try {
            Organization organization = OrganizationMapper.INSTANCE.dtoToEntity(organizationDto);
            organization.setAuth(user);
            organizationRepository.save(organization);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateRecordException("Организация уже существует!");
        }
    }

    public Optional<OrganizationDto> searchOrganization(User user) {
        Optional<Organization> organization = organizationRepository.findByAuth(user);

        return organization
                .map(OrganizationMapper.INSTANCE::entityToDto);

    }

    public long count() {
        return organizationRepository.count();
    }

    public Page<Organization> getAllOrganizations(Pageable page) {
        return organizationRepository.findAll(page);
    }

    public Page<Organization> getAllActiveOrganizations(Pageable pageable) {
        return organizationRepository.findAllByActive(pageable, true);
    }

    public void active(Long id) {

        Optional<Organization> organization = organizationRepository.findById(id);
        organization.map(o -> {
            o.setActive(!o.isActive());
            return o;
        });

        organization.ifPresent(organizationRepository::save);
    }

    public OrganizationDto update(User user, OrganizationDto organizationDto) throws DuplicateRecordException {

        Organization organization = organizationRepository
                .findByAuth(user)
                .orElseThrow(() -> new OrganizationNotFoundException("Организация не найдена"));

        organization.setName(organizationDto.getName());
        organization.setDescription(organizationDto.getDescription());
        organization.setParticipantsMaxCount(organizationDto.getParticipantsMaxCount());
        organization.setCountry(organizationDto.getCountry());
        organization.setRegion(organizationDto.getRegion());
        organization.setCity(organizationDto.getCity());
        organization.setAddress(organizationDto.getAddress());
        organization.setType(organizationDto.getType());

        try {
            return OrganizationMapper.INSTANCE.entityToDto(organizationRepository.save(organization));
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateRecordException("Организация уже существует!");
        }

    }

    public OrganizationDto update(Long organizationId, OrganizationDto organizationDto) {

        Organization organization =
                organizationRepository
                        .findById(organizationId)
                        .orElseThrow(() -> new IllegalArgumentException("Организация не найдена"));

        organization.setName(organizationDto.getName());
        organization.setDescription(organizationDto.getDescription());
        organization.setParticipantsMaxCount(organizationDto.getParticipantsMaxCount());
        organization.setType(organizationDto.getType());
        organization.setCountry(organizationDto.getCountry());
        organization.setRegion(organizationDto.getRegion());
        organization.setCity(organizationDto.getCity());
        organization.setAddress(organizationDto.getAddress());

        try {
            organization = organizationRepository.save(organization);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateRecordException("Организация уже существует!");
        }

        return OrganizationMapper.INSTANCE.entityToDto(organization);
    }

    public OrganizationDto getById(Long id) {
        return OrganizationMapper.INSTANCE.entityToDto(organizationRepository
                .findById(id)
                .orElseThrow(() -> new OrganizationNotFoundException("Организация не найдена")));
    }

    public boolean existsOrganizationsByAuth(User user) {
        return organizationRepository.existsOrganizationsByAuth(user);
    }
}
