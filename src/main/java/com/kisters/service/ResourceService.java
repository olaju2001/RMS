package com.kisters.service;

import com.kisters.dto.ResourceDTO;
import com.kisters.exception.ResourceNotFoundException;
import com.kisters.model.Resource;
import com.kisters.model.ResourceType;
import com.kisters.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ResourceService {
    private final ResourceRepository resourceRepository;
    private final ModelMapper modelMapper;

    public ResourceDTO createResource(ResourceDTO resourceDTO) {
        log.info("Creating new resource of type: {}", resourceDTO.getType());
        Resource resource = modelMapper.map(resourceDTO, Resource.class);
        Resource savedResource = resourceRepository.save(resource);
        return modelMapper.map(savedResource, ResourceDTO.class);
    }

    public ResourceDTO getResource(Long id) {
        log.info("Fetching resource with id: {}", id);
        Resource resource = resourceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));
        return modelMapper.map(resource, ResourceDTO.class);
    }

    public List<ResourceDTO> getAllResources() {
        log.info("Fetching all resources");
        return resourceRepository.findAll().stream()
            .map(resource -> modelMapper.map(resource, ResourceDTO.class))
            .collect(Collectors.toList());
    }

    public ResourceDTO updateResource(Long id, ResourceDTO resourceDTO) {
        log.info("Updating resource with id: {}", id);
        Resource existingResource = resourceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));
        
        modelMapper.map(resourceDTO, existingResource);
        Resource updatedResource = resourceRepository.save(existingResource);
        return modelMapper.map(updatedResource, ResourceDTO.class);
    }

    public void deleteResource(Long id) {
        log.info("Deleting resource with id: {}", id);
        if (!resourceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resource not found with id: " + id);
        }
        resourceRepository.deleteById(id);
    }

    public List<ResourceDTO> findResourcesByType(ResourceType type) {
        log.info("Finding resources by type: {}", type);
        return resourceRepository.findByType(type).stream()
            .map(resource -> modelMapper.map(resource, ResourceDTO.class))
            .collect(Collectors.toList());
    }

    public List<ResourceDTO> findResourcesInArea(String minLat, String maxLat, String minLon, String maxLon) {
        log.info("Finding resources in area: ({}, {}) to ({}, {})", minLat, minLon, maxLat, maxLon);
        return resourceRepository.findResourcesInArea(minLat, maxLat, minLon, maxLon).stream()
            .map(resource -> modelMapper.map(resource, ResourceDTO.class))
            .collect(Collectors.toList());
    }
}
