package com.emorn.bettercables.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class ArchitectureTest {


    @Test
    public void contractShouldNotDependOnMinecraft() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.emorn.bettercables");

        ArchRule rule = noClasses()
            .that().resideInAPackage("com.emorn.bettercables.contract..")
            .should().dependOnClassesThat().resideInAPackage("net.minecraft..");

        rule.check(importedClasses);
    }

    @Test
    public void contractShouldNotDependOnForge() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.emorn.bettercables");

        ArchRule rule = noClasses()
            .that().resideInAPackage("com.emorn.bettercables.contract..")
            .should().dependOnClassesThat().resideInAPackage("net.minecraftforge..");

        rule.check(importedClasses);
    }
}
