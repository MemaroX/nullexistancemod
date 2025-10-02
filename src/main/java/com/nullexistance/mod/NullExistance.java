package com.nullexistance.mod;

import com.mojang.logging.LogUtils;
import com.nullexistance.mod.capabilities.CharacterCapability;
import com.nullexistance.mod.capabilities.CharacterCapabilityProvider;
import com.nullexistance.mod.capabilities.ICharacter;
import com.nullexistance.mod.client.gui.CharacterSelectionScreen;
import com.nullexistance.mod.client.keys.KeyBindings;
import com.nullexistance.mod.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.util.UUID;

@Mod(NullExistance.MODID)
public class NullExistance {
    public static final String MODID = "nullexistance";
    private static final Logger LOGGER = LogUtils.getLogger();
    @SuppressWarnings("deprecation")
    public static final ResourceLocation CHARACTER_CAPABILITY_RL = new ResourceLocation(MODID, "character");

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public NullExistance(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        modEventBus.addListener(this::commonSetup);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        PacketHandler.register();
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }

        @SubscribeEvent
        public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
            event.register(KeyBindings.OPEN_CHARACTER_SELECTION_KEY);
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (KeyBindings.OPEN_CHARACTER_SELECTION_KEY.consumeClick()) {
                Minecraft.getInstance().player.getCapability(CharacterCapability.CHARACTER_CAPABILITY).ifPresent(character -> {
                    if (character.getCharacter() == com.nullexistance.mod.capabilities.Character.NONE) {
                        Minecraft.getInstance().setScreen(new CharacterSelectionScreen());
                    }
                });
            }
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ModEvents {
        private static final UUID GIXO_IV_HEALTH_ID = UUID.fromString("a7c6e5a8-8a8e-4a6e-8a5c-6a78a8e4a6e8");

        @SubscribeEvent
        public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof Player) {
                event.addCapability(CHARACTER_CAPABILITY_RL, new CharacterCapabilityProvider());
            }
        }

        @SubscribeEvent
        public static void onPlayerClone(PlayerEvent.Clone event) {
            if (event.isWasDeath()) {
                event.getOriginal().getCapability(CharacterCapability.CHARACTER_CAPABILITY).ifPresent(oldStore -> {
                    event.getEntity().getCapability(CharacterCapability.CHARACTER_CAPABILITY).ifPresent(newStore -> {
                        newStore.setCharacter(oldStore.getCharacter());
                    });
                });
            }
        }

        @SubscribeEvent
        public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
            event.register(ICharacter.class);
        }

        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                Player player = event.player;
                player.getCapability(CharacterCapability.CHARACTER_CAPABILITY).ifPresent(character -> {
                    if (character.getCharacter() == com.nullexistance.mod.capabilities.Character.GIXO_IV) {
                        long time = player.level().getDayTime() % 24000;
                        if (time > 12000 && time < 24000) {
                            double timeFactor = (time - 12000) / 12000.0;
                            int strengthAmplifier = (int) (timeFactor * 4); // Max strength IV
                            int speedAmplifier = (int) (timeFactor * 2); // Max speed II
                            int jumpAmplifier = (int) (timeFactor * 2); // Max jump II

                            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 80, strengthAmplifier, false, false));
                            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 80, speedAmplifier, false, false));
                            player.addEffect(new MobEffectInstance(MobEffects.JUMP, 80, jumpAmplifier, false, false));
                            player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 80, 1, false, false));

                            AttributeModifier healthModifier = new AttributeModifier(GIXO_IV_HEALTH_ID, "gixo_iv_health", 10, AttributeModifier.Operation.ADDITION);
                            player.getAttribute(Attributes.MAX_HEALTH).removeModifier(GIXO_IV_HEALTH_ID);
                            player.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(healthModifier);


                        } else {
                            player.removeEffect(MobEffects.DAMAGE_BOOST);
                            player.removeEffect(MobEffects.MOVEMENT_SPEED);
                            player.removeEffect(MobEffects.JUMP);
                            player.removeEffect(MobEffects.SATURATION);
                            player.getAttribute(Attributes.MAX_HEALTH).removeModifier(GIXO_IV_HEALTH_ID);

                        }
                    } else {
                        player.removeEffect(MobEffects.DAMAGE_BOOST);
                        player.removeEffect(MobEffects.MOVEMENT_SPEED);
                        player.removeEffect(MobEffects.JUMP);
                        player.removeEffect(MobEffects.SATURATION);
                        player.getAttribute(Attributes.MAX_HEALTH).removeModifier(GIXO_IV_HEALTH_ID);

                    }
                });
            }
        }
    }
}
