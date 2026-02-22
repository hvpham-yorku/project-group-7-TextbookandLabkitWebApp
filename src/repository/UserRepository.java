package repository;

import domain.User;

public interface UserRepository {
    User findByEmail(String email);
}
