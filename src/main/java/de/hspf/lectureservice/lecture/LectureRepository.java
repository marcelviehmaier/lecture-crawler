package de.hspf.lectureservice.lecture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {
    @Modifying
    @Transactional
    @Query("delete from Lecture l")
    void deleteAllRows();
    boolean existsByModulNumber(String modulNumber);
}