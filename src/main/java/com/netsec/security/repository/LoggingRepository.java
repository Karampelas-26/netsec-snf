package com.netsec.security.repository;

import com.netsec.security.model.Logging;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author George Karampelas
 */
public interface LoggingRepository extends JpaRepository<Logging, Integer> {
    List<Logging> findAllByUsername(String username);
}
