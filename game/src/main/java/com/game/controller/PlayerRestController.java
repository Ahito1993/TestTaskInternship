package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.exception.BadRequestException;
import com.game.exception.IdNotFoundException;
import com.game.exception.InvalidIdException;
import com.game.service.PlayerService;
import com.game.utils.PlayerSpecification;
import com.game.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/players")
public class PlayerRestController {

    @Autowired
    private PlayerService playerService;

    private Validator validator = new Validator();

    @GetMapping("")
    public List<Player> getAllPlayers(@RequestParam(value = "name", required = false) String name,
                                      @RequestParam(value = "title", required = false) String title,
                                      @RequestParam(value = "race", required = false) Race race,
                                      @RequestParam(value = "profession", required = false) Profession profession,
                                      @RequestParam(value = "after", required = false) Long after,
                                      @RequestParam(value = "before", required = false) Long before,
                                      @RequestParam(value = "banned", required = false) Boolean banned,
                                      @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                      @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                      @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                      @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                      @RequestParam(value = "order", required = false, defaultValue = "ID") PlayerOrder order,
                                      @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));

        PlayerSpecification playerSpecification = new PlayerSpecification();

        return playerService.getAllPlayers(Specification.where(playerSpecification.selectByName(name))
                .and(playerSpecification.selectByTitle(title))
                .and(playerSpecification.selectByRace(race))
                .and(playerSpecification.selectByProfession(profession))
                .and(playerSpecification.selectByBirthday(after, before))
                .and(playerSpecification.selectByBanned(banned))
                .and(playerSpecification.selectByExperience(minExperience, maxExperience))
                .and(playerSpecification.selectByLevel(minLevel, maxLevel)), pageable).getContent();
    }

    @GetMapping("/{id}")
    public Player getPlayer(@PathVariable("id") Long playerId) {
        if (!validator.isIdValid(playerId)) {
            throw new InvalidIdException();
        }

        Player player = this.playerService.getPlayer(playerId);
        if (player == null) {
            throw new IdNotFoundException();
        }

        return player;
    }

    @GetMapping("/count")
    public Integer getPlayersCount (@RequestParam(value = "name", required = false) String name,
                                    @RequestParam(value = "title", required = false) String title,
                                    @RequestParam(value = "race", required = false) Race race,
                                    @RequestParam(value = "profession", required = false) Profession profession,
                                    @RequestParam(value = "after", required = false) Long after,
                                    @RequestParam(value = "before", required = false) Long before,
                                    @RequestParam(value = "banned", required = false) Boolean banned,
                                    @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                    @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                    @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                    @RequestParam(value = "maxLevel", required = false) Integer maxLevel) {
        PlayerSpecification playerSpecification = new PlayerSpecification();

        return playerService.getPlayersCount(Specification.where(playerSpecification.selectByName(name))
                .and(playerSpecification.selectByTitle(title))
                .and(playerSpecification.selectByRace(race))
                .and(playerSpecification.selectByProfession(profession))
                .and(playerSpecification.selectByBirthday(after, before))
                .and(playerSpecification.selectByBanned(banned))
                .and(playerSpecification.selectByExperience(minExperience, maxExperience))
                .and(playerSpecification.selectByLevel(minLevel, maxLevel)));
    }

    @DeleteMapping("/{id}")
    public void deletePlayer (@PathVariable("id") Long playerId) {
        if (!validator.isIdValid(playerId)) {
            throw new InvalidIdException();
        }
        Player player = this.playerService.getPlayer(playerId);
        if (player == null) {
            throw new IdNotFoundException();
        }

        playerService.deletePlayer(playerId);
    }

    @PostMapping ("")
    public Player createNewPlayer (@RequestBody Player player) {
        if (player.getName() == null || player.getTitle() == null || player.getRace() == null
                || player.getProfession() == null || player.getBirthday() == null || player.getExperience() == null) {
            throw new BadRequestException();
        }

        validator.checkName(player.getName());
        validator.checkTitle(player.getTitle());
        validator.checkBirthday(player.getBirthday());
        validator.checkExperience(player.getExperience());

        if (player.getBanned() == null) {
            player.setBanned(false);
        }
        Integer level = validator.level(player.getExperience());
        Integer untilNextLevel = validator.untilNextLevel(level, player.getExperience());

        player.setLevel(level);
        player.setUntilNextLevel(untilNextLevel);

        return playerService.savePlayer(player);
    }

    @PostMapping ("/{id}")
    public Player updatePlayer (@PathVariable("id") Long playerId,@RequestBody Player updPlayer) {
        if (!validator.isIdValid(playerId)) {
            throw new InvalidIdException();
        }

        Player player = playerService.getPlayer(playerId);
        if (player == null) {
            throw new IdNotFoundException();
        }

        if (updPlayer.getName() != null) {
            validator.checkName(updPlayer.getName());
            player.setName(updPlayer.getName());
        }
        if (updPlayer.getTitle() != null) {
            validator.checkTitle(updPlayer.getTitle());
            player.setTitle(updPlayer.getTitle());
        }
        if (updPlayer.getRace() != null) {
            player.setRace(updPlayer.getRace());
        }
        if (updPlayer.getProfession() != null) {
            player.setProfession(updPlayer.getProfession());
        }
        if (updPlayer.getBirthday() != null) {
            validator.checkBirthday(updPlayer.getBirthday());
            player.setBirthday(updPlayer.getBirthday());
        }
        if (updPlayer.getBanned() != null) {
            player.setBanned(updPlayer.getBanned());
        }
        if (updPlayer.getExperience() != null) {
            validator.checkExperience(updPlayer.getExperience());
            player.setExperience(updPlayer.getExperience());

            Integer level = validator.level(player.getExperience());
            Integer untilNextLevel = validator.untilNextLevel(level, player.getExperience());

            player.setLevel(level);
            player.setUntilNextLevel(untilNextLevel);
        }
        return player;
    }
}
