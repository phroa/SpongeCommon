/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.common.scoreboard;

import com.google.common.collect.Maps;
import net.minecraft.scoreboard.ScoreObjective;
import org.spongepowered.api.scoreboard.Score;
import org.spongepowered.api.scoreboard.objective.Objective;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.common.interfaces.IMixinScore;
import org.spongepowered.common.interfaces.IMixinScoreObjective;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class SpongeScore implements Score {

    private Text name;
    private int score;

    private UUID uuid = UUID.randomUUID();

    private Map<ScoreObjective, net.minecraft.scoreboard.Score> scores = new HashMap<>();

    public SpongeScore(Text name) {
        this.name = name;
    }

    @Override
    public Text getName() {
        return this.name;
    }

    @Override
    public int getScore() {
        return this.score;
    }

    public net.minecraft.scoreboard.Score getScore(ScoreObjective objective) {
        return this.scores.get(objective);
    }

    @Override
    public void setScore(int score) {
        this.score = score;
        this.updateScore();
    }

    private void updateScore() {
        for (net.minecraft.scoreboard.Score score : this.scores.values()) {
            score.setScorePoints(this.score);
        }
    }

    @Override
    public Set<Objective> getObjectives() {
        Set<Objective> objectives = this.scores.keySet().stream()
                .map(objective -> ((IMixinScoreObjective) objective).getSpongeObjective())
                .collect(Collectors.toSet());
        return objectives;
    }

    public void addToObjective(Objective objective) {
        ((SpongeObjective) objective).getObjectives().forEach(this::addToScoreObjective);
    }

    @SuppressWarnings({"deprecation", "unchecked"})
    public void addToScoreObjective(ScoreObjective scoreObjective) {
        String name = Texts.legacy().to(this.name);

        net.minecraft.scoreboard.Score score = new net.minecraft.scoreboard.Score(scoreObjective.theScoreboard, scoreObjective, name);
        ((IMixinScore) score).setSpongeCreated();
        ((IMixinScore) score).setSpongeScore(this);
        score.setScorePoints(this.score);
        this.scores.put(scoreObjective, score);

        Map<ScoreObjective, net.minecraft.scoreboard.Score> scoreMap = (Map) scoreObjective.theScoreboard.entitiesScoreObjectives.get(name);
        if (scoreMap == null) {
            scoreMap = Maps.newHashMap();
        }

        if (scoreMap.containsKey(scoreObjective) && ((IMixinScore) scoreMap.get(scoreObjective)).spongeCreated()) {
            throw new IllegalArgumentException("A score already exists with the name " + this.name);
        }

        scoreObjective.theScoreboard.entitiesScoreObjectives.put(name, scoreMap);

        scoreMap.put(scoreObjective, score);
    }

    public void removeFromObjective(Objective objective) {
        ((SpongeObjective) objective).getObjectives().forEach(this::removeFromScoreObjective);
    }

    @SuppressWarnings("deprecation")
    public void removeFromScoreObjective(ScoreObjective objective) {
        objective.theScoreboard.removeObjectiveFromEntity(Texts.legacy().to(this.name), objective);
    }

    public UUID getUuid() {
        return this.uuid;
    }
}
