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
package org.spongepowered.common.world.extent;

import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.util.DiscreteTransform3;
import org.spongepowered.api.world.extent.MutableBlockVolume;
import org.spongepowered.api.world.extent.UnmodifiableBlockVolume;

public class MutableBlockViewTransform extends AbstractBlockViewTransform<MutableBlockVolume> implements MutableBlockVolume {

    public MutableBlockViewTransform(MutableBlockVolume volume, DiscreteTransform3 transform) {
        super(volume, transform);
    }

    @Override
    public void setBlockType(Vector3i position, BlockType type) {
        setBlockType(position.getX(), position.getY(), position.getZ(), type);
    }

    @Override
    public void setBlockType(int x, int y, int z, BlockType type) {
        setBlock(x, y, z, type.getDefaultState());
    }

    @Override
    public void setBlock(Vector3i position, BlockState block) {
        setBlock(position.getX(), position.getY(), position.getZ(), block);
    }

    @Override
    public void setBlock(int x, int y, int z, BlockState block) {
        this.volume.setBlock(this.inverseTransform.transformX(x, y, z), this.inverseTransform.transformY(x, y, z),
                this.inverseTransform.transformZ(x, y, z), block);
    }

    @Override
    public MutableBlockVolume getBlockView(Vector3i newMin, Vector3i newMax) {
        return new MutableBlockViewDownsize(this.volume, this.inverseTransform.transform(newMin), this.inverseTransform.transform(newMax))
            .getBlockView(this.transform);
    }

    @Override
    public MutableBlockVolume getBlockView(DiscreteTransform3 transform) {
        return new MutableBlockViewTransform(this.volume, this.transform.withTransformation(transform));
    }

    @Override
    public MutableBlockVolume getRelativeBlockView() {
        return getBlockView(DiscreteTransform3.fromTranslation(this.min.negate()));
    }

    @Override
    public UnmodifiableBlockVolume getUnmodifiableBlockView() {
        return new UnmodifiableBlockVolumeWrapper(this);
    }
}
