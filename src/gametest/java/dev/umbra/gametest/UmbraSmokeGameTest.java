package dev.umbra.gametest;

import dev.umbra.UmbraMod;
import dev.umbra.core.contract.config.UmbraConfigService;
import dev.umbra.core.contract.content.UmbraContentRegistry;
import dev.umbra.core.contract.event.UmbraEventBus;
import dev.umbra.core.contract.registry.UmbraServiceRegistry;
import dev.umbra.core.contract.scheduler.TickScheduler;
import dev.umbra.core.contract.state.StateSaveService;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;

public class UmbraSmokeGameTest {

    @GameTest(structure = "fabric-gametest-api-v1:empty")
    public void testUmbraCoreBootstrap(GameTestHelper helper) {
        UmbraServiceRegistry registry = UmbraMod.getServiceRegistry();

        // Assert core services are present and active
        if (registry.locate(UmbraEventBus.class).isEmpty()) {
            throw new IllegalStateException("UmbraEventBus service is not registered!");
        }
        if (registry.locate(TickScheduler.class).isEmpty()) {
            throw new IllegalStateException("TickScheduler service is not registered!");
        }
        if (registry.locate(UmbraContentRegistry.class).isEmpty()) {
            throw new IllegalStateException("UmbraContentRegistry service is not registered!");
        }
        if (registry.locate(StateSaveService.class).isEmpty()) {
            throw new IllegalStateException("StateSaveService service is not registered!");
        }
        if (registry.locate(UmbraConfigService.class).isEmpty()) {
            throw new IllegalStateException("UmbraConfigService service is not registered!");
        }

        helper.succeed();
    }
}
