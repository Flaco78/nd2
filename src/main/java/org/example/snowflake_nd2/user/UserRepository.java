package org.example.snowflake_nd2.user;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    // Čia gali pridėti papildomų užklausų metodų, jeigu reikia
}
