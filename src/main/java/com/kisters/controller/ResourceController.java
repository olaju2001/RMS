package com.kisters.controller;

import com.kisters.dto.ResourceDTO;
import com.kisters.model.ResourceType;
import com.kisters.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resources")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Resource Management", description = "Operations for managing renewable energy resources")
public class ResourceController {
    private final ResourceService resourceService;

    @PostMapping
    @Operation(summary = "Create a new resource", description = "Creates a new renewable energy resource")
    public ResponseEntity<ResourceDTO> createResource(@Valid @RequestBody ResourceDTO resourceDTO) {
        log.info("Creating new resource of type: {}", resourceDTO.getType());
        ResourceDTO created = resourceService.createResource(resourceDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get resource by ID", description = "Retrieves a resource by its ID")
    public ResponseEntity<ResourceDTO> getResource(@PathVariable Long id) {
        log.info("Fetching resource with id: {}", id);
        ResourceDTO resource = resourceService.getResource(id);
        return ResponseEntity.ok(resource);
    }

    @GetMapping
    @Operation(summary = "Get all resources", description = "Retrieves all resources with optional type filter")
    public ResponseEntity<List<ResourceDTO>> getAllResources(
            @RequestParam(required = false) ResourceType type) {
        log.info("Fetching all resources with type filter: {}", type);
        List<ResourceDTO> resources = type != null ?
                resourceService.findResourcesByType(type) :
                resourceService.getAllResources();
        return ResponseEntity.ok(resources);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update resource", description = "Updates an existing resource")
    public ResponseEntity<ResourceDTO> updateResource(
            @PathVariable Long id,
            @Valid @RequestBody ResourceDTO resourceDTO) {
        log.info("Updating resource with id: {}", id);
        ResourceDTO updated = resourceService.updateResource(id, resourceDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete resource", description = "Deletes an existing resource")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        log.info("Deleting resource with id: {}", id);
        resourceService.deleteResource(id);
        return ResponseEntity.noContent().build();
    }
}