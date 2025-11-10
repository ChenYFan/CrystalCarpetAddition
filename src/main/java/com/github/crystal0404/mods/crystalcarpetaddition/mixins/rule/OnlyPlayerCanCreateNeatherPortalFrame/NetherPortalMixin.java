/*
 * This file is part of the Crystal Carpet Addition project, licensed under the
 * GNU General Public License v3.0
 *
 * Copyright (C) 2024  Crystal0404 and contributors
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
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.dimension.NetherPortal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NetherPortal.class)
public abstract class NetherPortalMixin {
    @Unique
    private static final ThreadLocal<Entity> cca$teleportingEntity = new ThreadLocal<>();

    @Inject(
            method = "createTeleportTarget",
            at = @At("HEAD")
    )
    private void onCreateTeleportTargetHead(
            ServerWorld world,
            Entity entity,
            BlockPos pos,
            BlockPos scaledPos,
            boolean inNether,
            Direction.Axis portalAxis,
            CallbackInfoReturnable<TeleportTarget> cir
    ) {
        if (CCASettings.OnlyPlayerCanCreateNeatherPortalFrame) {
            cca$teleportingEntity.set(entity);
        }
    }

    @Inject(
            method = "createTeleportTarget",
            at = @At("RETURN")
    )
    private void onCreateTeleportTargetReturn(
            ServerWorld world,
            Entity entity,
            BlockPos pos,
            BlockPos scaledPos,
            boolean inNether,
            Direction.Axis portalAxis,
            CallbackInfoReturnable<TeleportTarget> cir
    ) {
        if (CCASettings.OnlyPlayerCanCreateNeatherPortalFrame) {
            cca$teleportingEntity.remove();
        }
    }

    @Inject(
            method = "findOrCreate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/dimension/NetherPortal;createPortal(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction$Axis;)Lnet/minecraft/world/dimension/NetherPortal$Target;"),
            cancellable = true
    )
    private void onBeforeCreatePortal(
            ServerWorld world,
            BlockPos pos,
            boolean destIsNether,
            Direction.Axis portalAxis,
            CallbackInfoReturnable<NetherPortal.Target> cir
    ) {
        if (CCASettings.OnlyPlayerCanCreateNeatherPortalFrame) {
            Entity entity = cca$teleportingEntity.get();
            if (entity != null && !(entity instanceof PlayerEntity)) {
                // Non-player entity trying to create a portal - cancel by returning null
                cir.setReturnValue(null);
            }
        }
    }
}
