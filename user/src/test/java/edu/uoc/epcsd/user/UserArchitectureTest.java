package edu.uoc.epcsd.user;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class UserArchitectureTest {

    private static final String BASE_PACKAGE = "edu.uoc.epcsd.user";
    
    private final JavaClasses classes = new ClassFileImporter()
            .importPackages(BASE_PACKAGE);

    // verifiquem que el microservei user segueix l'arquitectura hexagonal
    @Test
    public void hexagonalArchitectureShouldBeRespected() {
        JavaClasses productionClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(BASE_PACKAGE);

        // Arquitectura hexagonal:
        // - Capa de domini és independent i no depèn de cap altra capa
        // - Capa d'aplicació depèn en domini però no en infraestructura
        // - Capa d'infraestructura depèn en domini i en aplicació
        ArchRule onionArchitectureRule = onionArchitecture()
                .domainModels(BASE_PACKAGE + ".domain..")
                .domainServices(BASE_PACKAGE + ".domain.service..")
                .applicationServices(BASE_PACKAGE + ".application..")
                .adapter("rest", BASE_PACKAGE + ".application.rest..")
                .adapter("jpa", BASE_PACKAGE + ".infrastructure.repository.jpa..")
                .adapter("external_rest", BASE_PACKAGE + ".infrastructure.repository.rest..");

        onionArchitectureRule.check(productionClasses);
    }

    // verifiquem que les intefícies que defineixen un servei tenen una implementació que acaba en ServiceImpl
    @Test
    public void domainServicesShouldEndWithServiceImpl() {
        ArchRule domainServiceNamingRule = classes()
                .that().resideInAPackage(BASE_PACKAGE + ".domain.service..")
                .and().areAnnotatedWith(Service.class)
                .should().haveSimpleNameEndingWith("ServiceImpl")
                .because("Domain service implementations should follow the naming convention *ServiceImpl");

        domainServiceNamingRule.check(classes);
    }
} 