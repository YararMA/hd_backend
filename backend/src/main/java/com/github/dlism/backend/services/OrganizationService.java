package com.github.dlism.backend.services;

import com.github.dlism.backend.dto.OrganizationDto;
import com.github.dlism.backend.models.Organization;
import com.github.dlism.backend.models.User;
import com.github.dlism.backend.pojo.OrganizationPojo;
import com.github.dlism.backend.repositories.OrganizationRepository;
import com.github.dlism.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserRepository userRepository;

    public boolean create(OrganizationDto organizationDto, User user) {
        //TODO использовать маппер
        Organization organization = new Organization();
        organization.setName(organizationDto.getName());
        organization.setDescription(organizationDto.getDescription());

        if (organizationRepository.existsByName(organization.getName())) {
            return false;
        }

        //TODO Уменьшит количество запросов
        Organization organizationFromDB = organizationRepository.save(organization);
        user.setOrganization(organizationFromDB);
        userRepository.save(user);

        return true;
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
        return organizationRepository.all();
    }

    public void active(Long id) {

        Optional<Organization> organization = organizationRepository.findById(id);
        organization.map(o -> {
            o.setActive(!o.isActive());
            return o;
        });

        organization.ifPresent(value -> organizationRepository.save(value));
    }

    public Organization update(User user, OrganizationDto organizationDto) {
        Optional<Organization> organization = organizationRepository.findByUserId(user.getId());

        Organization updatedOrganization = organization.orElseThrow(() -> new IllegalArgumentException("Organization not found"));

        updatedOrganization.setName(organizationDto.getName());
        updatedOrganization.setDescription(organizationDto.getDescription());

        try {
            updatedOrganization = organizationRepository.save(updatedOrganization);
        } catch (DataIntegrityViolationException e) {
            // Обработка исключения или выброс собственного исключения
            e.printStackTrace();
        }

        return updatedOrganization;
    }
}
