package io.sillysillyman.deventer.entity;

import io.sillysillyman.deventer.entity.like.CommentLike;
import io.sillysillyman.deventer.entity.like.PostLike;
import io.sillysillyman.deventer.enums.UserLoginType;
import io.sillysillyman.deventer.enums.UserRole;
import io.sillysillyman.deventer.enums.UserStatus;
import io.sillysillyman.deventer.exception.AlreadyWithdrawnException;
import io.sillysillyman.deventer.exception.InvalidPasswordException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends Timestamped {

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<CommentLike> commentLikes = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    @Setter
    private String password;

    @Column(nullable = false, unique = true)
    @Setter
    private String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Setter
    private UserRole role;

    @Column(unique = true)
    private String refreshToken;

    @Column(nullable = false, unique = true)
    @Setter
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Setter
    private UserStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserLoginType loginType;

    private LocalDateTime deletedAt;

    public User(String username, String password, String nickname, UserRole role, String email,
        UserLoginType loginType) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
        this.status = UserStatus.ACTIVE;
        this.loginType = loginType;
        this.refreshToken = null;
    }

    public void validatePassword(PasswordEncoder passwordEncoder, String password) {
        if (!passwordEncoder.matches(password, this.password)) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }
    }

    public void saveRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void softWithdrawUser() {
        this.status = UserStatus.DELETED;
        this.deletedAt = LocalDateTime.now();
    }

    public void checkUserWithdrawn() {
        if (this.status == UserStatus.DELETED) {
            throw new AlreadyWithdrawnException("이미 탈퇴한 사용자입니다.");
        }
    }
}