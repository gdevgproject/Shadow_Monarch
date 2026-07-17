package dev.umbra.core;

import static org.junit.jupiter.api.Assertions.*;

import dev.umbra.core.contract.content.EnemyDefinition;
import dev.umbra.core.contract.content.ReferenceCard;
import dev.umbra.core.impl.content.ContentLoader;
import dev.umbra.core.impl.content.ContentRegistryImpl;
import dev.umbra.core.impl.content.JsonLocationTracker;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public final class ContentValidatorTest {

    private ContentRegistryImpl registry;
    private ContentLoader loader;

    @BeforeEach
    public void setUp() {
        registry = new ContentRegistryImpl();
        loader = new ContentLoader(registry);
    }

    private String readFixture(String filename) throws IOException {
        try (var stream = ContentValidatorTest.class.getResourceAsStream("/fixtures/" + filename)) {
            if (stream == null) {
                throw new FileNotFoundException("Fixture not found: " + filename);
            }
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    @Test
    public void testLoadValidEnemies() throws Exception {
        assertTrue(loader.loadEnemy("enemy_valid_1.json", readFixture("enemy_valid_1.json")));
        assertTrue(loader.loadEnemy("enemy_valid_2.json", readFixture("enemy_valid_2.json")));
        assertTrue(loader.loadEnemy("enemy_valid_3.json", readFixture("enemy_valid_3.json")));

        assertEquals(3, registry.getAllEnemies().size());

        var elf = registry.getEnemy("umbra:frost_elf_stalker").orElseThrow();
        assertEquals("frost", elf.faction());
        assertEquals("assassin", elf.role());
        assertEquals(90.0, elf.baseStats().hp());
        assertEquals(18, elf.baseStats().level());
        assertEquals(8.0, elf.scaling().hpPerLevel());
        assertEquals(-0.25, elf.resistances().get("fire"));
        assertEquals(2, elf.squad().preferredSize().size());
        assertEquals(2, elf.squad().preferredSize().get(0));
        assertEquals(4, elf.squad().preferredSize().get(1));
        assertEquals("elite", elf.arise().tier());
        assertEquals(40.0, elf.arise().authorityRequired());

        var boss = registry.getEnemy("umbra:shadow_hunter_boss").orElseThrow();
        assertEquals("shadow", boss.faction());
        assertEquals("boss", boss.role());
        assertEquals(5000.0, boss.baseStats().hp());
        assertEquals("boss", boss.arise().tier());

        var goblin = registry.getEnemy("umbra:goblin_scout").orElseThrow();
        assertEquals("goblin", goblin.faction());
        assertEquals("scout", goblin.role());
        assertEquals(40.0, goblin.baseStats().hp());
        assertEquals("common", goblin.arise().tier());
    }

    @Test
    public void testLoadValidReferenceCards() throws Exception {
        assertTrue(loader.loadReferenceCard("card_valid_1.json", readFixture("card_valid_1.json")));
        assertTrue(loader.loadReferenceCard("card_valid_2.json", readFixture("card_valid_2.json")));

        assertEquals(2, registry.getAllReferenceCards().size());

        var card1 = registry.getReferenceCard("UMBRA-RC-001").orElseThrow();
        assertEquals("Solo Leveling / Necromancer concept", card1.sourceContext());
        assertEquals("1.0", card1.shipPhase());

        var card2 = registry.getReferenceCard("UMBRA-RC-999").orElseThrow();
        assertEquals("Genshin World Rank", card2.sourceContext());
        assertEquals("P7+", card2.shipPhase());
    }

    @Test
    public void testLoadInvalidSyntaxEnemy() throws Exception {
        assertFalse(loader.loadEnemy("enemy_invalid_syntax.json", readFixture("enemy_invalid_syntax.json")));
        assertTrue(registry.getAllEnemies().isEmpty());
    }

    @Test
    public void testLoadInvalidCodecEnemy() throws Exception {
        assertFalse(loader.loadEnemy("enemy_invalid_codec.json", readFixture("enemy_invalid_codec.json")));
        assertTrue(registry.getAllEnemies().isEmpty());
    }

    @Test
    public void testLoadInvalidSemanticEnemy() throws Exception {
        assertFalse(loader.loadEnemy("enemy_invalid_semantic.json", readFixture("enemy_invalid_semantic.json")));
        assertTrue(registry.getAllEnemies().isEmpty());
    }

    @Test
    public void testLoadInvalidCodecCard() throws Exception {
        assertFalse(loader.loadReferenceCard("card_invalid_codec.json", readFixture("card_invalid_codec.json")));
        assertTrue(registry.getAllReferenceCards().isEmpty());
    }

    @Test
    public void testLoadInvalidSemanticCard() throws Exception {
        assertFalse(loader.loadReferenceCard("card_invalid_semantic.json", readFixture("card_invalid_semantic.json")));
        assertTrue(registry.getAllReferenceCards().isEmpty());
    }

    @Test
    public void testJsonLocationTracker() throws Exception {
        String json = readFixture("enemy_invalid_semantic.json");
        Map<String, Integer> lineMap = JsonLocationTracker.trackLines(json);

        assertEquals(2, lineMap.get("$.id"));
        assertEquals(6, lineMap.get("$.base_stats"));
        assertEquals(6, lineMap.get("$.base_stats.hp"));
        assertEquals(7, lineMap.get("$.scaling.hp_per_level"));
        assertEquals(8, lineMap.get("$.resistances.fire"));
        assertEquals(10, lineMap.get("$.squad.preferred_size[0]"));
        assertEquals(12, lineMap.get("$.arise.authority_required"));
    }
}
