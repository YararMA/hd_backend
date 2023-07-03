package com.github.dlism.backend.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_activation_code")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserActivationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
}
