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
package org.spongepowered.common.data.processor.data.entity;

import static org.spongepowered.common.data.util.DataUtil.getData;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataTransactionBuilder;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.immutable.entity.ImmutableHealthData;
import org.spongepowered.api.data.manipulator.mutable.entity.HealthData;
import org.spongepowered.common.data.manipulator.mutable.entity.SpongeHealthData;
import org.spongepowered.common.data.processor.common.AbstractEntityDataProcessor;

import java.util.Map;
import java.util.Optional;

public class HealthDataProcessor extends AbstractEntityDataProcessor<EntityLivingBase, HealthData, ImmutableHealthData> {

    public HealthDataProcessor() {
        super(EntityLivingBase.class);
    }

    @Override
    protected HealthData createManipulator() {
        return new SpongeHealthData(20, 20);
    }

    @Override
    protected boolean doesDataExist(EntityLivingBase entity) {
        return true;
    }

    @Override
    protected boolean set(EntityLivingBase entity, Map<Key<?>, Object> keyValues) {
        entity.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(((Double) keyValues.get(Keys.MAX_HEALTH)).floatValue());
        entity.setHealth(((Double) keyValues.get(Keys.HEALTH)).floatValue());
        return true;
    }

    @Override
    protected Map<Key<?>, ?> getValues(EntityLivingBase entity) {
        final double health = entity.getHealth();
        final double maxHealth = entity.getMaxHealth();
        return ImmutableMap.<Key<?>, Object>of(Keys.HEALTH, health,
                                               Keys.MAX_HEALTH, maxHealth);
    }

    @Override
    public Optional<HealthData> fill(DataContainer container, HealthData healthData) {
        healthData.set(Keys.MAX_HEALTH, getData(container, Keys.MAX_HEALTH));
        healthData.set(Keys.HEALTH, getData(container, Keys.HEALTH));
        return Optional.of(healthData);
    }

    @Override
    public DataTransactionResult remove(DataHolder dataHolder) {
        return DataTransactionBuilder.builder().result(DataTransactionResult.Type.FAILURE).build();
    }

}
