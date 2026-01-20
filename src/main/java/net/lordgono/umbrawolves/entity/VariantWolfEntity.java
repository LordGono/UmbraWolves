package net.lordgono.umbrawolves.entity;

import net.lordgono.umbrawolves.config.WolfConfig;
import net.lordgono.umbrawolves.item.WolfEquipmentItem;
import net.lordgono.umbrawolves.registry.ModEntities;
import net.lordgono.umbrawolves.registry.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VariantWolfEntity extends Wolf {
    private static final EntityDataAccessor<Integer> DATA_VARIANT_ID =
        SynchedEntityData.defineId(VariantWolfEntity.class, EntityDataSerializers.INT);

    private static final String VARIANT_TAG = "WolfVariant";

    // Equipment inventory: HEAD, BODY, FEET (indices 0, 1, 2)
    private final SimpleContainer equipmentInventory = new SimpleContainer(3);

    public VariantWolfEntity(EntityType<? extends Wolf> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_VARIANT_ID, WolfVariant.PALE.ordinal());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt(VARIANT_TAG, this.getVariantOrdinal());

        // Save equipment
        for (WolfEquipmentSlot slot : WolfEquipmentSlot.values()) {
            ItemStack stack = getEquipment(slot);
            if (!stack.isEmpty()) {
                tag.put(slot.getNbtKey(), stack.save(new CompoundTag()));
            }
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains(VARIANT_TAG)) {
            this.setVariant(WolfVariant.byOrdinal(tag.getInt(VARIANT_TAG)).orElse(WolfVariant.PALE));
        }

        // Load equipment
        for (WolfEquipmentSlot slot : WolfEquipmentSlot.values()) {
            if (tag.contains(slot.getNbtKey())) {
                setEquipment(slot, ItemStack.of(tag.getCompound(slot.getNbtKey())));
            }
        }
    }

    public WolfVariant getWolfVariant() {
        return WolfVariant.byOrdinal(this.getVariantOrdinal()).orElse(WolfVariant.PALE);
    }

    public int getVariantOrdinal() {
        return this.entityData.get(DATA_VARIANT_ID);
    }

    public void setVariant(WolfVariant variant) {
        this.entityData.set(DATA_VARIANT_ID, variant.ordinal());
    }

    // === Equipment Methods ===

    public ItemStack getEquipment(WolfEquipmentSlot slot) {
        return equipmentInventory.getItem(slot.ordinal());
    }

    public void setEquipment(WolfEquipmentSlot slot, ItemStack stack) {
        equipmentInventory.setItem(slot.ordinal(), stack);
    }

    public boolean hasEquipment(WolfEquipmentSlot slot) {
        return !getEquipment(slot).isEmpty();
    }

    public int getTotalProtection() {
        int protection = 0;
        for (WolfEquipmentSlot slot : WolfEquipmentSlot.values()) {
            ItemStack stack = getEquipment(slot);
            if (stack.getItem() instanceof WolfEquipmentItem equipmentItem) {
                protection += equipmentItem.getProtection();
            }
        }
        return protection;
    }

    public boolean hasOxygenSupply() {
        // Check if wolf has space helmet that provides oxygen
        ItemStack headGear = getEquipment(WolfEquipmentSlot.HEAD);
        if (headGear.getItem() instanceof WolfEquipmentItem equipmentItem) {
            return equipmentItem.providesOxygen();
        }
        return false;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        // Only allow equipping on tamed wolves by their owner
        if (this.isTame() && this.isOwnedBy(player)) {
            // Check if player is holding wolf equipment
            if (itemStack.getItem() instanceof WolfEquipmentItem equipmentItem) {
                WolfEquipmentSlot slot = equipmentItem.getEquipmentSlot();

                // Check if slot is empty
                if (!hasEquipment(slot)) {
                    if (!this.level.isClientSide) {
                        setEquipment(slot, itemStack.copy());
                        if (!player.getAbilities().instabuild) {
                            itemStack.shrink(1);
                        }
                        this.playSound(SoundEvents.ARMOR_EQUIP_LEATHER, 1.0F, 1.0F);
                    }
                    return InteractionResult.sidedSuccess(this.level.isClientSide);
                }
            }

            // Shift-click to remove equipment
            if (player.isShiftKeyDown() && itemStack.isEmpty()) {
                for (WolfEquipmentSlot slot : WolfEquipmentSlot.values()) {
                    if (hasEquipment(slot)) {
                        if (!this.level.isClientSide) {
                            ItemStack removed = getEquipment(slot).copy();
                            setEquipment(slot, ItemStack.EMPTY);
                            player.getInventory().placeItemBackInInventory(removed);
                            this.playSound(SoundEvents.ARMOR_EQUIP_LEATHER, 1.0F, 0.8F);
                        }
                        return InteractionResult.sidedSuccess(this.level.isClientSide);
                    }
                }
            }
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        // Reduce damage based on armor protection
        int protection = getTotalProtection();
        if (protection > 0) {
            // Each protection point reduces damage by ~4% (similar to vanilla armor)
            float reduction = Math.min(0.8F, protection * 0.04F);
            amount *= (1.0F - reduction);

            // Damage equipment
            if (!this.level.isClientSide && source.getEntity() != null) {
                for (WolfEquipmentSlot slot : WolfEquipmentSlot.values()) {
                    ItemStack stack = getEquipment(slot);
                    if (!stack.isEmpty() && stack.isDamageableItem()) {
                        stack.hurtAndBreak(1, this, (wolf) -> {
                            this.playSound(SoundEvents.ITEM_BREAK, 1.0F, 1.0F);
                        });
                        if (stack.isEmpty()) {
                            setEquipment(slot, ItemStack.EMPTY);
                        }
                    }
                }
            }
        }
        return super.hurt(source, amount);
    }

    // Drop equipment on death
    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        for (WolfEquipmentSlot slot : WolfEquipmentSlot.values()) {
            ItemStack stack = getEquipment(slot);
            if (!stack.isEmpty()) {
                this.spawnAtLocation(stack);
                setEquipment(slot, ItemStack.EMPTY);
            }
        }
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                         MobSpawnType spawnType, @Nullable SpawnGroupData spawnData,
                                         @Nullable CompoundTag tag) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnData, tag);

        // Determine variant based on biome
        if (spawnType != MobSpawnType.SPAWN_EGG && spawnType != MobSpawnType.BUCKET) {
            Holder<Biome> biomeHolder = level.getBiome(this.blockPosition());
            WolfVariant variant = getVariantForBiome(biomeHolder);
            this.setVariant(variant);

            // Space wolves (Martian and Glacian) spawn with space helmet
            if (variant == WolfVariant.MARTIAN || variant == WolfVariant.GLACIAN) {
                this.setEquipment(WolfEquipmentSlot.HEAD,
                    new ItemStack(ModItems.WOLF_SPACE_HELMET.get()));
            }
        }

        return data;
    }

    public static WolfVariant getVariantForBiome(Holder<Biome> biomeHolder) {
        List<WolfVariant> matchingVariants = new ArrayList<>();

        // Check each variant's biome tag
        for (WolfVariant variant : WolfVariant.values()) {
            if (WolfConfig.isVariantEnabled(variant) && biomeHolder.is(variant.getBiomeTag())) {
                matchingVariants.add(variant);
            }
        }

        // If we found matching variants, pick one randomly
        if (!matchingVariants.isEmpty()) {
            return matchingVariants.get(new Random().nextInt(matchingVariants.size()));
        }

        // Default to PALE if no specific variant matches
        return WolfVariant.PALE;
    }

    @Override
    public Wolf getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        VariantWolfEntity baby = ModEntities.VARIANT_WOLF.get().create(level);
        if (baby != null && otherParent instanceof VariantWolfEntity otherWolf) {
            // Baby inherits variant from random parent
            if (this.random.nextBoolean()) {
                baby.setVariant(this.getWolfVariant());
            } else {
                baby.setVariant(otherWolf.getWolfVariant());
            }
        } else if (baby != null) {
            // If breeding with vanilla wolf, inherit from this wolf
            baby.setVariant(this.getWolfVariant());
        }
        return baby;
    }

    // Special abilities based on variant
    @Override
    public boolean fireImmune() {
        // Nether wolves are fire immune
        return this.getWolfVariant() == WolfVariant.NETHER || super.fireImmune();
    }
}
