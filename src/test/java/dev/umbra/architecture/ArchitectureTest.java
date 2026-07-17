package dev.umbra.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class ArchitectureTest {

    private final JavaClasses classes = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("dev.umbra");

    @Test
    public void testClientDoesNotAccessServerImpl() {
        noClasses().that().resideInAPackage("dev.umbra.client..")
            .should().dependOnClassesThat().resideInAPackage("dev.umbra.core.impl..")
            .check(classes);
    }

    @Test
    public void testServerSafetyNoClientDependencies() {
        noClasses().that().resideInAPackage("dev.umbra..")
            .and().resideOutsideOfPackage("dev.umbra.client..")
            .should().dependOnClassesThat().resideInAnyPackage("dev.umbra.client..", "net.minecraft.client..")
            .check(classes);
    }

    @Test
    public void testGameplayModulesDoNotDependOnCoreImplementationsDirectly() {
        noClasses().that().resideInAnyPackage(
            "dev.umbra.progression..",
            "dev.umbra.combat..",
            "dev.umbra.shadows..",
            "dev.umbra.ai..",
            "dev.umbra.dungeons..",
            "dev.umbra.world..",
            "dev.umbra.strata..",
            "dev.umbra.items..",
            "dev.umbra.economy.."
        )
        .should().dependOnClassesThat().resideInAPackage("dev.umbra.core.impl..")
        .allowEmptyShould(true)
        .check(classes);
    }
}
