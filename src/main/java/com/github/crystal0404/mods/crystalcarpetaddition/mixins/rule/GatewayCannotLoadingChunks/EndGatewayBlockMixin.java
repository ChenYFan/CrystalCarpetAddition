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

package com.github.crystal0404.mods.crystalcarpetaddition.mixins.rule.GatewayCannotLoadingChunks;

import com.github.crystal0404.mods.crystalcarpetaddition.CCASettings;
import net.minecraft.block.EndGatewayBlock;
import net.minecraft.world.TeleportTarget;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EndGatewayBlock.class)
public abstract class EndGatewayBlockMixin {
        @ModifyArg(method = "createTeleportTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/TeleportTarget;"
                        +
                        "<init>(Lnet/minecraft/server/world/ServerWorld;" +
                        "Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;" +
                        "FFLjava/util/Set;Lnet/minecraft/world/TeleportTarget$PostDimensionTransition;)V", ordinal = 1), index = 6)
        private TeleportTarget.PostDimensionTransition createTeleportTargetMixin(
                        TeleportTarget.PostDimensionTransition original) {

                // return CCASettings.GatewayCannotLoadingChunks ? TeleportTarget.NO_OP :
                // original;

                return entity -> {
                        if (entity != null) {
                                String EntityType = entity.getType().getName().getString();
                                String[] DisallowedEntityType = CCASettings.GatewayCannotLoadingChunks.split(",");
                                if (DisallowedEntityType.length > 0) {
                                        for (String type : DisallowedEntityType) {
                                                if (type.equalsIgnoreCase("None")
                                                                || type.equals("false")) {
                                                        original.onTransition(entity);
                                                        return;
                                                }
                                                if (type.equalsIgnoreCase(EntityType)
                                                                || type.equalsIgnoreCase("All")
                                                                || type.equals("true")) {
                                                        TeleportTarget.NO_OP.onTransition(entity);
                                                        return;
                                                }
                                        }
                                }
                        }
                        original.onTransition(entity);
                };
        }
}
