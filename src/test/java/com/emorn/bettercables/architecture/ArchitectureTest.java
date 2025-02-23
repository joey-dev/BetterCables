package com.emorn.bettercables.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class ArchitectureTest {
    @Test
    public void coreShouldNotDependOnApi() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.emorn.bettercables");

        ArchRule rule = noClasses()
            .that().resideInAPackage("com.emorn.bettercables.core..")
            .should().dependOnClassesThat().resideInAPackage("com.emorn.bettercables.api..");

        rule.check(importedClasses);
    }

    @Test
    public void coreShouldNotDependOnMinecraft() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.emorn.bettercables");

        ArchRule rule = noClasses()
            .that().resideInAPackage("com.emorn.bettercables.core..")
            .should().dependOnClassesThat().resideInAPackage("net.minecraft..");

        rule.check(importedClasses);
    }

    @Test
    public void coreShouldNotDependOnForge() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.emorn.bettercables");

        ArchRule rule = noClasses()
            .that().resideInAPackage("com.emorn.bettercables.core..")
            .should().dependOnClassesThat().resideInAPackage("net.minecraftforge..");

        rule.check(importedClasses);
    }

    @Test
    public void contractShouldNotDependOnApi() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.emorn.bettercables");

        ArchRule rule = noClasses()
            .that().resideInAPackage("com.emorn.bettercables.contract..")
            .should().dependOnClassesThat().resideInAPackage("com.emorn.bettercables.api..");

        rule.check(importedClasses);
    }

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
