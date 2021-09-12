package com.game.service;

import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService{

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public Page<Player> getAllPlayers(Specification<Player> specification, Pageable pageable) {
        return playerRepository.findAll(specification, pageable);
    }

    @Override
    public Player getPlayer(Long id) {

        Player player = null;

        Optional<Player> playerOpt = playerRepository.findById(id);
        if (playerOpt.isPresent()) {
            player = playerOpt.get();
        }
        return player;
    }

    @Override
    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    @Override
    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }


    @Override
    public Integer getPlayersCount(Specification<Player> specification) {
        return Math.toIntExact(playerRepository.count(specification));
    }

    @Override
    public Player updatePlayer(Player player) {
        return playerRepository.saveAndFlush(player);
    }

}
