package com.game.service;

import com.game.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface PlayerService {

    public Page<Player> getAllPlayers(Specification<Player> specification, Pageable pageable);

    public Player getPlayer (Long id);

    public Player savePlayer (Player player);

    public void deletePlayer (Long id);

    public Integer getPlayersCount (Specification<Player> specification);

    public Player updatePlayer (Player player);
}
