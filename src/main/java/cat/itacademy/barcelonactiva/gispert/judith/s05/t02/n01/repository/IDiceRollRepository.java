package cat.itacademy.barcelonactiva.gispert.judith.s05.t02.n01.repository;

import cat.itacademy.barcelonactiva.gispert.judith.s05.t02.n01.domain.DiceRoll;
import cat.itacademy.barcelonactiva.gispert.judith.s05.t02.n01.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDiceRollRepository extends JpaRepository<DiceRoll, Integer> {

    List<DiceRoll> findByPlayer(Player player);
}
