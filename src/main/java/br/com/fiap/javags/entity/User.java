package br.com.fiap.javags.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "USUARIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_USUARIO")
    @SequenceGenerator(name = "SQ_USUARIO", sequenceName = "SQ_USUARIO", allocationSize = 1)
    @Column(name = "ID_USUARIO")
    private Long id;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "EMAIL")
    private String email;

    private String avatar;
}
//    @Transient
//    private Map<String, Object> attributes;
//
//    public User(OAuth2User principal) {
//    }
//
//    @Override
//    public Map<String, Object> getAttributes() {
//        return attributes;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
//    }
//
//    @Override
//    public String getName() {
//        return String.valueOf(this.id);
//    }
//
//    public void setAttributes(Map<String, Object> attributes) {
//        this.attributes = attributes;
//    }
//
//    public static User fromOAuth2User(OAuth2User principal) {
//        return User.builder()
//                .name(principal.getAttribute("name"))
//                .email(principal.getAttribute("email"))
//                .avatar(principal.getAttribute("avatar_url"))
//                .attributes(principal.getAttributes())
//                .build();
//    }
//}