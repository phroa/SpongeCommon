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
package org.spongepowered.common.item;

import static org.spongepowered.api.data.DataQuery.of;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.item.FireworkEffect;
import org.spongepowered.api.item.FireworkShape;

import java.awt.Color;
import java.util.List;
import java.util.stream.Collectors;

public class SpongeFireworkMeta implements FireworkEffect {

    private final boolean flicker;
    private final boolean trails;
    private final ImmutableList<Color> colors;
    private final ImmutableList<Color> fades;
    private final FireworkShape shape;

    SpongeFireworkMeta(boolean flicker, boolean trails, Iterable<Color> colors, Iterable<Color> fades, FireworkShape shape) {
        this.flicker = flicker;
        this.trails = trails;
        this.colors = ImmutableList.copyOf(colors);
        this.fades = ImmutableList.copyOf(fades);
        this.shape = shape;
    }

    @Override
    public boolean flickers() {
        return this.flicker;
    }

    @Override
    public boolean hasTrail() {
        return this.trails;
    }

    @Override
    public List<Color> getColors() {
        return this.colors;
    }

    @Override
    public List<Color> getFadeColors() {
        return this.fades;
    }

    @Override
    public FireworkShape getShape() {
        return this.shape;
    }

    @Override
    public DataContainer toContainer() {
        List<Integer> colors = Lists.newArrayList();
        colors.addAll(this.colors.stream()
                .map(Color::getRGB)
                .collect(Collectors.toList()));
        List<Integer> fades = Lists.newArrayList();
        fades.addAll(this.fades.stream()
                .map(Color::getRGB)
                .collect(Collectors.toList()));
        return new MemoryDataContainer()
                .set(of("Type"), this.shape.getId())
                .set(of("Colors"), colors)
                .set(of("Fades"), fades)
                .set(of("Trails"), this.trails)
                .set(of("Flickers"), this.flicker);
    }
}
