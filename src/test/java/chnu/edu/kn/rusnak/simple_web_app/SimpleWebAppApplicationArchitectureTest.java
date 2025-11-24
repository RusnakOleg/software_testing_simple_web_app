package chnu.edu.kn.rusnak.simple_web_app;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;


class SimpleWebAppApplicationArchitectureTest {

    private JavaClasses importedClasses;
    @BeforeEach
    void setUp() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_ARCHIVES)
                .importPackages("chnu.edu.kn.rusnak.simple_web_app");
        System.out.println("Imported Classes Count: " + importedClasses.size());
        importedClasses.forEach(c -> System.out.println("-> " + c.getName()));
    }

    // 1
    @Test void shouldFollowLayersArchitecture(){
        layeredArchitecture()
                .consideringAllDependencies()
                .layer("Controller").definedBy("..controller..")
                .layer("Service").definedBy("..service..")
                .layer("Repository").definedBy("..repository..")
                //
                .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
                .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller", "Service")
                .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service")
                //
                .check(importedClasses); }

    // 2
    @Test
    void servicesShouldNotDependOnControllerLevel(){
        noClasses()
                .that().resideInAPackage("..service..")
                .should()
                .accessClassesThat()
                .resideInAPackage("..controller..")
                .because("out of rules")
                .check(importedClasses);
    }

    // 3
    @Test
    void controllerClassesShouldBeNamedXController(){
        classes()
                .that().resideInAPackage("..controller..")
                .should()
                .haveSimpleNameEndingWith("Controller")
                .check(importedClasses);
    }

    // 4
    @Test
    void controllerCLassesShouldBeAnnotatedByRestController(){
        classes()
                .that().resideInAPackage("..controller..")
                .should()
                .beAnnotatedWith(RestController.class)
                .andShould()
                .beAnnotatedWith(RequestMapping.class)
                .check(importedClasses);
    }

    // 5
    @Test
    void shouldNotUseFieldAutowired(){
        noFields()
                .should()
                .beAnnotatedWith(Autowired.class)
                .check(importedClasses);
    }

    // 6
    @Test
    void serviceClassesShouldBeNamedXService() {
        classes()
                .that().resideInAPackage("..service..")
                .should().haveSimpleNameEndingWith("Service")
                .check(importedClasses);
    }

    // 7
    @Test
    void repositoryClassesShouldBeNamedXRepository() {
        classes()
                .that().resideInAPackage("..repository..")
                .should().haveSimpleNameEndingWith("Repository")
                .check(importedClasses);
    }

    // 8
    @Test
    void repositoryClassesShouldBeInterfaces() {
        classes()
                .that().resideInAPackage("..repository..")
                .should().beInterfaces()
                .check(importedClasses);
    }

    // 9
    @Test
    void controllersShouldNotDependOnRepositories() {
        noClasses()
                .that().resideInAPackage("..controller..")
                .should().accessClassesThat().resideInAPackage("..repository..")
                .check(importedClasses);
    }

    // 10
    @Test
    void servicesShouldAccessRepositoryLayer() {
        classes()
                .that().resideInAPackage("..service..")
                .should().accessClassesThat().resideInAPackage("..repository..")
                .check(importedClasses);
    }

    // 11
    @Test
    void controllersShouldAccessServiceLayer() {
        classes()
                .that().resideInAPackage("..controller..")
                .should().accessClassesThat().resideInAPackage("..service..")
                .check(importedClasses);
    }

    // 12
    @Test
    void controllersShouldNotContainBusinessLogic() {
        noClasses()
                .that().resideInAPackage("..controller..")
                .should().accessClassesThat().resideInAnyPackage("java.io..", "java.util..")
                .check(importedClasses);
    }

    // 13
    @Test
    void servicesShouldNotDependOnDTO() {
        noClasses()
                .that().resideInAPackage("..service..")
                .should().accessClassesThat().resideInAPackage("..dto..")
                .check(importedClasses);
    }

    // 14
    @Test
    void entitiesShouldNotDependOnServiceLayer() {
        noClasses()
                .that().resideInAPackage("..model..")
                .should().dependOnClassesThat().resideInAPackage("..service..")
                .check(importedClasses);
    }

    // 15
    @Test
    void noCyclicDependencies() {
        slices().matching("chnu.edu.kn.rusnak.simple_web_app.(*)..")
                .should().beFreeOfCycles()
                .check(importedClasses);
    }

    // 16
    @Test
    void serviceClassesShouldBeAnnotatedWithService() {
        classes()
                .that().resideInAPackage("..service..")
                .should().beAnnotatedWith(org.springframework.stereotype.Service.class)
                .check(importedClasses);
    }

    // 17
    @Test
    void repositoryClassesShouldBeAnnotatedWithRepository() {
        classes()
                .that().resideInAPackage("..repository..")
                .should().beAnnotatedWith(org.springframework.stereotype.Repository.class)
                .check(importedClasses);
    }

    // 18
    @Test
    void controllersShouldNotDependDirectlyOnEntities() {
        noClasses()
                .that().resideInAPackage("..controller..")
                .should().accessClassesThat().resideInAPackage("..entity..")
                .check(importedClasses);
    }

    // 19
    @Test
    void serviceShouldNotAccessWebLayer() {
        noClasses()
                .that().resideInAPackage("..service..")
                .should().dependOnClassesThat().resideInAPackage("..web..")
                .check(importedClasses);
    }

    // 20
    @Test
    void controllerShouldBePublic() {
        classes()
                .that().resideInAPackage("..controller..")
                .should().bePublic()
                .check(importedClasses);
    }
}