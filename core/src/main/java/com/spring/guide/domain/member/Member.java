package com.spring.guide.domain.member;

import com.spring.guide.model.Address;
import com.spring.guide.model.Email;
import com.spring.guide.model.Name;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email", nullable = false, unique = true, updatable = false, length = 50))
    private Email email;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "first", column = @Column(name = "first_name", nullable = false)),
            @AttributeOverride(name = "middle", column = @Column(name = "middle_name")),
            @AttributeOverride(name = "last", column = @Column(name = "last_name", nullable = false))
    })
    private Name name;

    @CreationTimestamp
    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(name = "update_at", nullable = false)
    private LocalDateTime updateAt;

    @Builder
    public Member(Email email, Name name) {
        this.email = email;
        this.name = name;
    }
}
