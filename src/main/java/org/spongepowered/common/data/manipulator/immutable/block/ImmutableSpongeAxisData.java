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
package org.spongepowered.common.data.manipulator.immutable.block;

import static com.google.common.base.Preconditions.checkNotNull;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableAxisData;
import org.spongepowered.api.data.manipulator.mutable.block.AxisData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.util.Axis;
import org.spongepowered.common.data.manipulator.immutable.common.AbstractImmutableSingleEnumData;
import org.spongepowered.common.data.manipulator.mutable.block.SpongeAxisData;
import org.spongepowered.common.data.value.immutable.ImmutableSpongeValue;

public class ImmutableSpongeAxisData extends AbstractImmutableSingleEnumData<Axis, ImmutableAxisData, AxisData> implements ImmutableAxisData {

    public ImmutableSpongeAxisData(Axis value) {
        super(ImmutableAxisData.class, checkNotNull(value), Keys.AXIS, SpongeAxisData.class);
    }

    @Override
    public ImmutableValue<Axis> axis() {
        return ImmutableSpongeValue.cachedOf(Keys.AXIS, Axis.X, this.value);
    }

    @Override
    public ImmutableValue<Axis> type() {
        return axis();
    }

    @Override
    protected ImmutableValue<?> getValueGetter() {
        return axis();
    }
}
