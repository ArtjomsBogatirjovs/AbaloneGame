package com.example.abalonegame;

import com.example.abalonegame.db.entity.Player;
import com.example.abalonegame.db.repository.PlayerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class AbaloneGameApplication {

    public static void main(String[] args) {
        SpringApplication.run(AbaloneGameApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(PlayerRepository playerRepository) {
        return (args) -> {

//            save a couple of players
//            playerRepository.deleteAll();
//            playerRepository.save(new Player("User1", new BCryptPasswordEncoder().encode("1")));
//            playerRepository.save(new Player("User2", new BCryptPasswordEncoder().encode("1")));
        };
    }
}
