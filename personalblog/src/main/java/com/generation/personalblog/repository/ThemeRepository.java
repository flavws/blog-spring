package com.generation.personalblog.repository;

import com.generation.personalblog.model.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ThemeRepository extends JpaRepository<Theme, Long> {

    public List<Theme> findAllByDescriptionContainingIgnoreCase(@Param("description") String description);

}
