package com.kisters.repository;

import com.kisters.model.Resource;
import com.kisters.model.ResourceType;
import com.kisters.model.ResourceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findByType(ResourceType type);
    
    List<Resource> findByStatus(ResourceStatus status);
    
    @Query("SELECT r FROM Resource r WHERE " +
           "r.location.latitude BETWEEN :minLat AND :maxLat AND " +
           "r.location.longitude BETWEEN :minLon AND :maxLon")
    List<Resource> findResourcesInArea(
        @Param("minLat") String minLat,
        @Param("maxLat") String maxLat,
        @Param("minLon") String minLon,
        @Param("maxLon") String maxLon
    );

    @Query("SELECT r FROM Resource r WHERE " +
           "r.status = 'ACTIVE' AND " +
           "r.type = :resourceType")
    List<Resource> findActiveResourcesByType(@Param("resourceType") ResourceType resourceType);
}
