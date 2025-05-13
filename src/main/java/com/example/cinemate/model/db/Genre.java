package com.example.cinemate.model.db;

import lombok.*;
import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "genre")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "img_url", nullable = false)
    private String imageUrl;

    @Column(name = "description")
    private String description;

    @Column(name = "tags")
    private String tags;
}
