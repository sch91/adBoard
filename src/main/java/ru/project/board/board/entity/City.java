package ru.project.board.board.entity;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false, length = 16)
    private UUID id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "city")
    private List<Ad> adList;

    public City() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ad> getAdList() {
        return adList;
    }

    public void setAdList(List<Ad> adList) {
        this.adList = adList;
    }
}
