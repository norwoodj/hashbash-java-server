package com.johnmalcolmnorwood.hashbash.repository;

import com.johnmalcolmnorwood.hashbash.model.RainbowTableSearch;
import com.johnmalcolmnorwood.hashbash.model.RainbowTableSearchStatus;
import com.johnmalcolmnorwood.hashbash.repository.model.RainbowTableSearchResults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Repository
public interface RainbowTableSearchRepository extends PagingAndSortingRepository<RainbowTableSearch, Long> {

    long countByRainbowTableId(short rainbowTableId);
    long countByRainbowTableIdAndStatusNot(short rainbowTableId, RainbowTableSearchStatus status);

    List<RainbowTableSearch> getAllByRainbowTableId(short rainbowTableId, Pageable pageable);
    List<RainbowTableSearch> getAllByRainbowTableIdAndStatusNot(
            short rainbowTableId,
            RainbowTableSearchStatus status,
            Pageable pageable
    );

    @Query(
            "SELECT new com.johnmalcolmnorwood.hashbash.repository.model.RainbowTableSearchResults(s.status, COUNT(s)) " +
            "FROM RainbowTableSearch s " +
            "WHERE s.rainbowTableId = :rainbowTableId AND s.status IN ('FOUND', 'NOT_FOUND') " +
            "GROUP BY s.status"
    )
    List<RainbowTableSearchResults> searchCountsByStatus(@Param("rainbowTableId") short rainbowTableId);

    @Query(
            "UPDATE RainbowTableSearch s " +
            "SET s.status = :status, s.searchStarted = :searchStarted " +
            "WHERE s.id = :id"
    )
    @Modifying
    @Transactional
    void updateStatusAndSearchStartedById(
            @Param("id") long id,
            @Param("status") RainbowTableSearchStatus status,
            @Param("searchStarted") Date searchStarted
    );

    @Query(
            "UPDATE RainbowTableSearch s " +
            "SET s.status = :status, s.password = :password, s.searchCompleted = :searchCompleted " +
            "WHERE s.id = :id"
    )
    @Modifying
    @Transactional
    void updateStatusAndPasswordSearchCompletedById(
            @Param("id") long id,
            @Param("status") RainbowTableSearchStatus status,
            @Param("password") String password,
            @Param("searchCompleted") Date searchCompleted
    );
}
