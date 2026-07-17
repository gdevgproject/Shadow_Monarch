package dev.umbra.core.combat;

import static org.junit.jupiter.api.Assertions.*;

import dev.umbra.core.impl.combat.CombatServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public final class CombatServiceTest {

    private CombatServiceImpl combatService;

    @BeforeEach
    public void setUp() {
        combatService = new CombatServiceImpl();
    }

    @Test
    public void testBaseDamageCalculation() {
        // STR = 10, WeaponBase = 1.0 (melee barehand)
        // Base = 1.0 + 10 * 1.2 = 13.0
        // level = 1, comboCount = 0 (comboMult = 1.0), PER = 10, targetArmor = 0 (no mitigation), forceCrit = false
        float dmg = combatService.calculateFormulaDamage(
            1,    // level
            10,   // strength
            10,   // perception
            0,    // comboCount
            1.0f, // weaponBase
            1.0f, // cooldownScale
            0.0f, // targetArmor
            false,// forceCrit
            false,// applyVariance
            0.0   // randomDoubleForVariance
        );

        assertEquals(13.0f, dmg, 0.001f);
    }

    @Test
    public void testComboMultiplier() {
        // STR = 10, WeaponBase = 1.0 -> base = 13.0
        // comboCount = 5 -> comboMult = 1.0 + 5 * 0.02 = 1.10
        // Expected damage = 13.0 * 1.10 = 14.3
        float dmg = combatService.calculateFormulaDamage(
            1, 10, 10, 5, 1.0f, 1.0f, 0.0f, false, false, 0.0
        );
        assertEquals(14.3f, dmg, 0.001f);

        // comboCount = 50 -> comboMult = 1.0 + 50 * 0.02 = 2.00, capped at 1.50
        // Expected damage = 13.0 * 1.50 = 19.5
        float dmgCap = combatService.calculateFormulaDamage(
            1, 10, 10, 50, 1.0f, 1.0f, 0.0f, false, false, 0.0
        );
        assertEquals(19.5f, dmgCap, 0.001f);
    }

    @Test
    public void testCriticalHitScaling() {
        // STR = 10, WeaponBase = 1.0 -> base = 13.0
        // PER = 10 -> critDamage = 1.50 + 10 * 0.005 = 1.55 (155%)
        // forceCrit = true
        // Expected damage = 13.0 * 1.55 = 20.15
        float dmgCrit = combatService.calculateFormulaDamage(
            1, 10, 10, 0, 1.0f, 1.0f, 0.0f, true, false, 0.0
        );
        assertEquals(20.15f, dmgCrit, 0.001f);

        // High PER = 300 (softcapped)
        // effPer = 100 + (300 - 100)^0.75 = 100 + 200^0.75 = 100 + 53 = 153
        // critDamage = 1.50 + 153 * 0.005 = 2.265 (226.5%)
        // Expected damage = 13.0 * 2.265 = 29.445
        float dmgHighCrit = combatService.calculateFormulaDamage(
            1, 10, 300, 0, 1.0f, 1.0f, 0.0f, true, false, 0.0
        );
        assertEquals(29.445f, dmgHighCrit, 0.001f);
    }

    @Test
    public void testArmorMitigation() {
        // STR = 10, WeaponBase = 1.0 -> base = 13.0
        // level = 1, targetArmor = 10.0
        // mitigation = 10 / (10 + 50 + 10 * 1) = 10 / 70 = 14.2857%
        // Expected damage = 13.0 * (1 - 0.142857) = 13.0 * 0.857143 = 11.1428
        float dmgArmor = combatService.calculateFormulaDamage(
            1, 10, 10, 0, 1.0f, 1.0f, 10.0f, false, false, 0.0
        );
        assertEquals(11.1428f, dmgArmor, 0.001f);

        // targetArmor = 10000.0 -> mitigation capped at 75%
        // Expected damage = 13.0 * (1 - 0.75) = 3.25
        float dmgArmorCap = combatService.calculateFormulaDamage(
            1, 10, 10, 0, 1.0f, 1.0f, 10000.0f, false, false, 0.0
        );
        assertEquals(3.25f, dmgArmorCap, 0.001f);
    }

    @Test
    public void testVarianceBounds() {
        // base = 13.0
        // randomDoubleForVariance = 0.0 -> variance = 0.95 -> 13.0 * 0.95 = 12.35
        float dmgMin = combatService.calculateFormulaDamage(
            1, 10, 10, 0, 1.0f, 1.0f, 0.0f, false, true, 0.0
        );
        assertEquals(12.35f, dmgMin, 0.001f);

        // randomDoubleForVariance = 1.0 -> variance = 1.05 -> 13.0 * 1.05 = 13.65
        // Expected damage = 13.0 * 1.05 = 13.65
        float dmgMax = combatService.calculateFormulaDamage(
            1, 10, 10, 0, 1.0f, 1.0f, 0.0f, false, true, 1.0
        );
        assertEquals(13.65f, dmgMax, 0.001f);
    }

    @Test
    public void testDodgeFormulaVectors() {
        // 0.25s + AGI * 0.001, rounded up to server ticks and capped at 0.4s.
        assertEquals(6, CombatServiceImpl.calculateDodgeIFrameTicksForAgility(10));
        assertEquals(8, CombatServiceImpl.calculateDodgeIFrameTicksForAgility(1000));
        assertEquals(7, CombatServiceImpl.calculateDodgeActionTicks());
        assertEquals(3.22, CombatServiceImpl.calculateDodgeUnobstructedDistance(), 0.0001);
        assertEquals(25.0, CombatServiceImpl.calculateDodgeFocusCost(84));
        assertEquals(37.5, CombatServiceImpl.calculateDodgeFocusCost(85));
        assertEquals(2.02, CombatServiceImpl.calculatePrecisionManaRestore(101.0), 0.0001);
        assertEquals(6.0, CombatServiceImpl.calculatePrecisionManaRestore(1000.0), 0.0001);
    }
}
