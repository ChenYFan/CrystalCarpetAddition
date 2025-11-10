/*
 * This file is part of the Crystal Carpet Addition project, licensed under the
 * GNU General Public License v3.0
 *
 * Copyright (C) 2025  Crystal0404 and contributors
 *
 * Crystal Carpet Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Crystal Carpet Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Crystal Carpet Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.crystal0404.mods.crystalcarpetaddition.mixins.rule.OnlyPlayerCanCreateNeatherPortalFrame;

import com.github.crystal0404.mods.crystalcarpetaddition.CCASettings;

import net.minecraft.block.BlockState;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.border.WorldBorder;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(NetherPortalBlock.class)
public class NetherPortalBlockMixin {
    @Inject(method = "getOrCreateExitPortalTarget", at = @At("INVOKE"), cancellable = true)
    private void cca$onGetOrCreateExitPortalTargetInvoke(ServerWorld world, Entity entity, BlockPos pos,
            BlockPos scaledPos, boolean inNether, WorldBorder worldBorder, CallbackInfoReturnable<TeleportTarget> cir) {
        if (CCASettings.OnlyPlayerCanCreateNeatherPortalFrame) {
            Optional<BlockPos> optional = world.getPortalForcer().getPortalPos(scaledPos, inNether, worldBorder);
            if (!optional.isPresent() && !(entity instanceof PlayerEntity)) {
                cir.setReturnValue(null);
            }
        }
    }

}