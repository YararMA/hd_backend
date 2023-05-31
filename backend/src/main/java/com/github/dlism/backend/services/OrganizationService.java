package com.github.dlism.backend.services;

import com.github.dlism.backend.dto.OrganizationDto;
import com.github.dlism.backend.exceptions.DuplicateRecordException;
import com.github.dlism.backend.models.Organization;
import com.github.dlism.backend.models.User;
import com.github.dlism.backend.pojo.OrganizationPojo;
import com.github.dlism.backend.repositories.OrganizationRepository;
import com.github.dlism.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void create(OrganizationDto organizationDto, User user) throws DuplicateRecordException{

        //TODO использовать маппер
        Organization organization = new Organization();
        organization.setName(organizationDto.getName());
        organization.setDescription(organizationDto.getDescription());
        organization.setAuth(user);

        user.setOrganization(organization);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateRecordException("Организация уже существует!");
        }
    }

    public OrganizationDto searchOrganization(User user) {
        Optional<Organization> organization = organizationRepository.findByUserId(user.getId());

        return organization.map(o -> {
                    OrganizationDto dto = new OrganizationDto();
                    dto.setName(o.getName());
                    dto.setDescription(o.getDescription());
                    return dto;
                })
                .orElse(null);
    }

    public long count() {
        return organizationRepository.count();
    }

    public List<OrganizationPojo> getAllOrganizations() {
        return organizationRepository.getAll();
    }

    public List<OrganizationPojo> getAllActiveOrganizations() {
        return organizationRepository.getAllActive();
    }

    public void active(Long id) {

        Optional<Organization> organization = organizationRepository.findById(id);
        organization.map(o -> {
            o.setActive(!o.isActive());
            return o;
        });

        organization.ifPresent(o -> organizationRepository.save(o));
    }

    public Organization update(User user, OrganizationDto organizationDto) throws DuplicateRecordException{

        Organization updatedOrganization = organizationRepository
                                            .findByUserId(user.getId())
                                            .orElseThrow(() -> new IllegalArgumentException("Организация не найдена"));

        updatedOrganization.setName(organizationDto.getName());
        updatedOrganization.setDescription(organizationDto.getDescription());

        try {
            updatedOrganization = organizationRepository.save(updatedOrganization);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateRecordException("Организация уже существует!");
        }

        return updatedOrganization;
    }

    public Organization update(Long organizationId, OrganizationDto organizationDto){

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

        return organization;
    }

    public Organization getById(Long id) {
        return organizationRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Организация не найдена"));
    }
}
