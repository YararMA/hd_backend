package com.github.dlism.backend.services;

import com.github.dlism.backend.dto.OrganizationDto;
import com.github.dlism.backend.exceptions.DuplicateRecordException;
import com.github.dlism.backend.exceptions.OrganizationNotFoundException;
import com.github.dlism.backend.mappers.OrganizationMapper;
import com.github.dlism.backend.models.Organization;
import com.github.dlism.backend.models.User;
import com.github.dlism.backend.pojo.OrganizationPojo;
import com.github.dlism.backend.repositories.OrganizationRepository;
import com.github.dlism.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void create(OrganizationDto organizationDto, User user) throws DuplicateRecordException{

        Organization organization = OrganizationMapper.INSTANCE.dtoToEntity(organizationDto);
        organization.setAuth(user);
        user.setOrganization(organization);

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateRecordException("Организация уже существует!");
        }
    }

    public Optional<OrganizationDto> searchOrganization(User user) {
        Optional<Organization> organization = organizationRepository.findByUserId(user.getId());

        return organization
                .map(OrganizationMapper.INSTANCE::entityToDto);
    }

    public long count() {
        return organizationRepository.count();
    }

    public List<OrganizationPojo> getAllOrganizations() {
        return organizationRepository.getAll();
    }

    public List<OrganizationPojo> getAllActiveOrganizations() {
        List<Map<String, Object>> results = organizationRepository.getAllActiveWithSubscribers();

        List<OrganizationPojo> organizations = new ArrayList<>();
        for (Map<String, Object> result : results) {
            OrganizationPojo organization = new OrganizationPojo();
            organization.setId((Long) result.get("id"));
            organization.setName((String) result.get("name"));
            organization.setDescription((String) result.get("description"));
            organization.setActive((boolean) result.get("active"));
            organization.setSubscribers((Long) result.get("subscribers"));
            organizations.add(organization);
        }

        return organizations;
    }

    public void active(Long id) {

        Optional<Organization> organization = organizationRepository.findById(id);
        organization.map(o -> {
            o.setActive(!o.isActive());
            return o;
        });

        organization.ifPresent(o -> organizationRepository.save(o));
    }

    public OrganizationDto update(User user, OrganizationDto organizationDto) throws DuplicateRecordException{

        Organization organization = organizationRepository
                                            .findByUserId(user.getId())
                                            .orElseThrow(() -> new IllegalArgumentException("Организация не найдена"));

        organization.setName(organizationDto.getName());
        organization.setDescription(organizationDto.getDescription());

        try {
            organization = organizationRepository.save(organization);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateRecordException("Организация уже существует!");
        }

        return OrganizationMapper.INSTANCE.entityToDto(organization);
    }

    public OrganizationDto update(Long organizationId, OrganizationDto organizationDto){

        Organization organization =
                organizationRepository
                        .findById(organizationId)
                        .orElseThrow(()->new IllegalArgumentException("Организация не найдена"));

        organization.setName(organizationDto.getName());
        organization.setDescription(organizationDto.getDescription());

        try {
            organization = organizationRepository.save(organization);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateRecordException("Организация уже существует!");
        }

        return OrganizationMapper.INSTANCE.entityToDto(organization);
    }

    public Organization getById(Long id) {
        return organizationRepository
                .findById(id)
                .orElseThrow(() -> new OrganizationNotFoundException("Организация не найдена"));
    }
}
