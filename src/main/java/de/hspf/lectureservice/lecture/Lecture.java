package de.hspf.lectureservice.lecture;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Lecture {
    @Id
    @GeneratedValue(generator = "lecture_generator")
    @SequenceGenerator(
            name = "lecture_generator",
            sequenceName = "lecture_sequence",
            initialValue = 1000
    )
    private Long id;
    private String title;
    private int credits;
    private int sws;
    private String modulNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getSws() {
        return sws;
    }

    public void setSws(int sws) {
        this.sws = sws;
    }

    public String getModulNumber() {
        return modulNumber;
    }

    public void setModulNumber(String modulNumber) {
        this.modulNumber = modulNumber;
    }
}