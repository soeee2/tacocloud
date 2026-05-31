package tacos.data;

import org.springframework.data.repository.CrudRepository;
import tacos.User;

// 두 번째 제네릭 매개변수를 Long에서 String으로 변경합니다.
public interface UserRepository extends CrudRepository<User, String> {
    User findByUsername(String username);
}