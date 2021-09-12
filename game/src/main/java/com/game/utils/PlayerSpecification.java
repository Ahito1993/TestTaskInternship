package com.game.utils;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class PlayerSpecification {

    public Specification<Player> selectByName(final String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null) return null;
            return criteriaBuilder.like(root.get("name"), "%" + name + "%");
        };
    }

    public Specification<Player> selectByTitle(final String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null) return null;
            return criteriaBuilder.like(root.get("title"), "%" + title + "%");
        };
    }

    public Specification<Player> selectByRace(final Race race) {
        return (root, query, criteriaBuilder) -> {
            if (race == null) return null;
            return criteriaBuilder.equal(root.get("race"), race);
        };
    }

    public Specification<Player> selectByProfession(final Profession profession) {
        return (root, query, criteriaBuilder) -> {
            if (profession == null) return null;
            return criteriaBuilder.equal(root.get("profession"), profession);
        };
    }

    public Specification<Player> selectByBanned(final Boolean banned) {
        return (root, query, criteriaBuilder) -> {
            if (banned == null) return null;
            return criteriaBuilder.equal(root.get("banned"), banned);
        };
    }

    public Specification<Player> selectByBirthday (final Long after, final Long before) {
        return (root, query, criteriaBuilder) -> {
            if (after == null && before == null) return null;
            if (after == null) return criteriaBuilder.lessThanOrEqualTo(root.get("birthday"), new Date(before));
            if (before == null) return criteriaBuilder.greaterThanOrEqualTo(root.get("birthday"), new Date(after));
            return criteriaBuilder.between(root.get("birthday"), new Date(after), new Date(before));
        };
    }

    public Specification<Player> selectByExperience(final Integer minExperience, final Integer maxExperience) {
        return (root, query, criteriaBuilder) -> {
            if (minExperience == null && maxExperience == null) return null;
            if (minExperience == null) return criteriaBuilder.lessThanOrEqualTo(root.get("experience"), maxExperience);
            if (maxExperience == null) return criteriaBuilder.greaterThanOrEqualTo(root.get("experience"), minExperience);
            return criteriaBuilder.between(root.get("experience"), minExperience, maxExperience);
        };
    }

    public Specification<Player> selectByLevel(final Integer minLevel, final Integer maxLevel) {
        return (root, query, criteriaBuilder) -> {
            if (minLevel == null && maxLevel == null) return null;
            if (minLevel == null) return criteriaBuilder.lessThanOrEqualTo(root.get("level"), maxLevel);
            if (maxLevel == null) return criteriaBuilder.greaterThanOrEqualTo(root.get("level"), minLevel);
            return criteriaBuilder.between(root.get("level"), minLevel, maxLevel);
        };
    }
}
