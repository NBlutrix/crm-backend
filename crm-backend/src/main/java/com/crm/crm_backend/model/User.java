package com.crm.crm_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Entity  //kaže Springu "ova klasa = tabela u bazi". Hibernate će automatski napraviti tabelu users kad pokreneš aplikaciju.
@Table(name = "users")
@Data // ovo je Lombok anotacija koja automatski generiše getere, setere, toString, equals — ne moraš ih pisati ručno. Bez Lomboka bi imao 50+ linija koda više.
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id // @Id + @GeneratedValue — ovo je primarni ključ, baza sama generiše broj (1, 2, 3...).
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING) // kaže Hibernateu da u bazi čuva tekst "ADMIN" umesto broja 0 — daleko čitljivije.
    @Column(nullable = false)
    private Role role;
}
