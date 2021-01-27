package com.buffalo.ewaha.repository;

import com.buffalo.ewaha.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IStationRepository extends JpaRepository<Station, Long> {

    Station findByLocalization(String localization);
}
