package com.thaddev.villagercontrols.content.items;

import com.thaddev.villagercontrols.VillagerControls;
import com.thaddev.villagercontrols.core.Utils;
import com.thaddev.villagercontrols.core.inits.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.IForgeShearable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.thaddev.villagercontrols.core.Utils.component;

public class ControlRing extends Item {
    public ControlRing(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isValidRepairItem(ItemStack pStack, ItemStack pRepairCandidate) {
        return pRepairCandidate.is(Items.EMERALD);
    }

    public static void setTargetID(ItemStack stack, Villager villager) {
        if (stack.is(ItemInit.CONTROL_RING.get())) {
            CompoundTag compoundtag = stack.getOrCreateTag();
            compoundtag.putInt("Target", (villager != null ? villager.getId() : 0));
        }
    }


    public static int getTargetID(ItemStack stack) {
        CompoundTag compoundtag = stack.getTag();
        if (compoundtag != null) {
            return compoundtag.getInt("Target");
        } else {
            return 0;
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        int id;
        if ((id = getTargetID(pStack)) != -1 && pLevel != null) {
            //get Villager from stored UUID
            Villager villager = (Villager) pLevel.getEntity(id);

            if (villager != null) {
                CompoundTag compoundtag = pStack.getTag();
                pTooltipComponents.add(component(Integer.toString(compoundtag.getInt("Target"))));
                pTooltipComponents.add(
                    component(Utils.fromNoTag("(%$green)Linked to ["))
                        .append(villager.getName())
                        .append(component(Utils.fromNoTag("(%$green)]")))
                );
            }
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return getTargetID(pStack) != 0;
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity entity, InteractionHand hand) {
        if (stack.is(ItemInit.CONTROL_RING.get()) && entity instanceof Villager villager && hand == InteractionHand.MAIN_HAND) {
            ControlRing.setTargetID(playerIn.getInventory().getSelected(), villager);
            stack.hurtAndBreak(1, playerIn, (player) -> {
                player.broadcastBreakEvent(hand);
            });
            playerIn.getLevel().playSound(null, playerIn.getOnPos(), SoundEvents.COMPOSTER_EMPTY, SoundSource.MASTER, 1.0f, 1.0f);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pUsedHand == InteractionHand.MAIN_HAND && pPlayer.getInventory().getSelected().is(ItemInit.CONTROL_RING.get()) && ControlRing.getTargetID(pPlayer.getInventory().getSelected()) != 0) {
            ControlRing.setTargetID(pPlayer.getInventory().getSelected(), null);

            HitResult hr;
            if ((hr = pPlayer.pick(50, 0, false)).getType() == HitResult.Type.BLOCK) {
                Villager marker = new Villager(EntityType.VILLAGER, pLevel);
                marker.setPos(hr.getLocation());
                pLevel.addFreshEntity(marker);
            }

            //play sound
            if (!pLevel.isClientSide()) {
                pLevel.playSound(null, pPlayer.getOnPos(), SoundEvents.COMPOSTER_EMPTY, SoundSource.MASTER, 1.0f, 1.0f);
            }
            return InteractionResultHolder.success(pPlayer.getInventory().getSelected());
        }
        return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
    }
}
