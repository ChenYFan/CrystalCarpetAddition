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

package com.github.crystal0404.mods.crystalcarpetaddition.mixins.rule.DisallowEntityCreateNeatherPortalFrame;

import com.github.crystal0404.mods.crystalcarpetaddition.CCASettings;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.dimension.NetherPortal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(NetherPortalBlock.class)
public abstract class NetherPortalBlockMixin {
    @Inject(
            method = "createTeleportTarget",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/dimension/NetherPortal;createDestinationPortal(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/border/WorldBorder;Lnet/minecraft/util/math/Direction$Axis;)Ljava/util/Optional;",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    private void preventPortalFrameCreation(
            ServerWorld world,
            Entity entity,
            BlockPos pos,
            CallbackInfoReturnable<TeleportTarget> cir
    ) {
        if (entity == null) {
            return;
        }

        String entityTypeName = entity.getType().getName().getString();
        String[] disallowedEntityTypes = CCASettings.DisallowEntityCreateNeatherPortalFrame.split(",");

        for (String type : disallowedEntityTypes) {
            String trimmedType = type.trim();
            if (trimmedType.equalsIgnoreCase("None") || trimmedType.equals("false")) {
                return;
            }
            if (trimmedType.equalsIgnoreCase(entityTypeName) || 
                trimmedType.equalsIgnoreCase("All") || 
                trimmedType.equals("true")) {
                // Return NO_OP to prevent teleportation instead of creating a new portal
                cir.setReturnValue(TeleportTarget.NO_OP);
                return;
            }
        }
    }
}
