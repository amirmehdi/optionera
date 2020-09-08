package com.gitlab.amirmehdi;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.gitlab.amirmehdi");

        noClasses()
            .that()
                .resideInAnyPackage("com.gitlab.amirmehdi.service..")
            .or()
                .resideInAnyPackage("com.gitlab.amirmehdi.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..com.gitlab.amirmehdi.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
