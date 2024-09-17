package ru.kuchko.cool_app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.awt.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String username;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role;
    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private CityEntity city;
    @OneToOne
    @JoinColumn(name = "icon_id", referencedColumnName = "id")
    private ImageEntity icon;

    @ManyToMany
    @JoinTable(name = "likes",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"}),
    joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id")
    )
    private List<ProductEntity> likes;
}
