package tacos;

import java.util.Arrays;
import java.util.Collection;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Document(collection = "users")
@NoArgsConstructor // 1. 복잡한 설정을 지우고 일반 기본 생성자만 남겨둡니다.
public class User implements UserDetails {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    // 2. 중요: 모든 필드에서 'final' 키워드를 완전히 삭제합니다.
    private String username;
    private String password;
    private String fullname;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String phone;

    // 3. 회원가입(RegistrationForm)에서 인자 있는 생성자를 사용할 수 있도록 커스텀 생성자를 직접 추가합니다.
    public User(String username, String password, String fullname, String street,
                String city, String state, String zip, String phone) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phone = phone;
    }

    // --- UserDetails 필수 구현 메서드들 ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}