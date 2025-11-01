plugins {
    // Kotlin JVM 플러그인 추가 - 이것이 가장 중요합니다
    // 이 플러그인이 Kotlin 코드를 JVM 바이트코드로 컴파일합니다
    alias(libs.plugins.kotlinJvm)

    // Spring Boot 관련 플러그인들
    // 버전 카탈로그(libs.versions.toml)에서 버전 정보를 가져옵니다
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.kotlinSpring)
    alias(libs.plugins.kotlinJpa)
    alias(libs.plugins.kotlinSerialization)
}

group = "org.patchnote.patchnote"
version = "1.0.0"
description = "PatchNote Backend API Server"

// Java 17을 사용하도록 설정합니다
// Spring Boot 3.x는 Java 17 이상이 필수입니다
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

// Gradle 설정 블록입니다
// annotationProcessor로 처리된 결과를 컴파일 클래스패스에 포함시킵니다
// 이렇게 하면 IDE가 자동 생성된 코드를 인식할 수 있습니다
configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
    all {
        resolutionStrategy {
            force("org.apache.commons:commons-compress:1.27.1")
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // ===== 프로젝트 내부 의존성 =====
    // shared 모듈을 참조합니다
    // 이를 통해 클라이언트와 서버가 동일한 데이터 모델을 사용할 수 있습니다
    implementation(project(":shared"))

    // Logback은 Spring Boot에 이미 포함되어 있지만 명시적으로 추가했습니다
    // 로깅 프레임워크로 SLF4J의 구현체입니다
    implementation(libs.logback)

    // ===== Spring Boot Starters =====
    // Web: REST API 개발을 위한 Spring MVC와 내장 Tomcat 서버
    implementation("org.springframework.boot:spring-boot-starter-web")

    // JPA: 데이터베이스 접근을 위한 Hibernate ORM
    // Entity 클래스를 작성하면 자동으로 테이블이 매핑됩니다
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Security: JWT 토큰 기반 인증, 권한 관리 등을 처리합니다
    implementation("org.springframework.boot:spring-boot-starter-security")

    // Validation: @NotNull, @Size 같은 어노테이션으로 입력값을 검증합니다
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Actuator: 헬스체크, 메트릭 등 운영에 필요한 엔드포인트를 제공합니다
    // 프로덕션 환경에서 애플리케이션 상태를 모니터링할 때 필수입니다
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Redis Reactive: 캐싱이나 세션 저장소로 사용할 Redis 클라이언트
    // Reactive 버전은 논블로킹 IO로 성능이 더 좋습니다
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")

    // ===== Kotlin 지원 라이브러리 =====
    // Jackson Kotlin Module: JSON 직렬화/역직렬화 시 Kotlin의 특성을 지원합니다
    // 예를 들어 data class의 기본값, nullable 타입 등을 올바르게 처리합니다
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Kotlin Reflection: Spring이 런타임에 Kotlin 클래스 정보를 분석할 수 있게 합니다
    // @Component 스캔이나 의존성 주입 시 필요합니다
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // Kotlin Serialization: shared 모듈에서 사용하는 @Serializable과 동일한 방식
    // Jackson 대신 이것을 사용하면 클라이언트와 서버의 직렬화 방식이 일치합니다
    implementation(libs.kotlinx.serialization.json)

    // ===== Reactive 프로그래밍 지원 =====
    // Reactor Kotlin Extensions: 코루틴 스타일로 리액티브 코드를 작성할 수 있습니다
    // Redis나 WebFlux를 사용할 때 필요합니다
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

    // Coroutines Reactor: Kotlin 코루틴과 Project Reactor를 연결합니다
    // suspend 함수에서 Mono나 Flux를 반환할 수 있게 해줍니다
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // ===== 데이터베이스 =====
    // PostgreSQL JDBC 드라이버
    // 런타임에만 필요하므로 runtimeOnly로 선언합니다
    //runtimeOnly("org.postgresql:postgresql")

    // Flyway Core: 데이터베이스 스키마를 버전 관리하는 마이그레이션 도구
    implementation("org.flywaydb:flyway-core")

    // Flyway PostgreSQL 드라이버: PostgreSQL 전용 기능을 지원합니다
    // 이것이 없으면 Flyway가 PostgreSQL 데이터베이스를 인식하지 못합니다
    implementation("org.flywaydb:flyway-database-postgresql")

    // ===== 모니터링 =====
    // Prometheus: Actuator의 메트릭을 Prometheus 형식으로 노출합니다
    // Grafana 같은 모니터링 도구와 연동할 때 사용합니다
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    // ===== 개발 편의 도구 =====
    // DevTools: 코드 변경 시 자동으로 애플리케이션을 재시작합니다
    // 개발 중에만 사용되고 프로덕션 빌드에는 포함되지 않습니다
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Configuration Processor: application.yml의 속성에 대한 자동완성을 제공합니다
    // IDE에서 설정 파일을 작성할 때 매우 편리합니다
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // ===== 테스트 =====
    // Spring Boot Test: @SpringBootTest 같은 통합 테스트 지원
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Security Test: SecurityMockMvc 같은 보안 테스트 도구
    testImplementation("org.springframework.security:spring-security-test")

    // Kotlin Test: Kotlin 코드를 테스트하기 위한 JUnit5 통합
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    // Reactor Test: 리액티브 코드를 테스트하기 위한 StepVerifier 등
    testImplementation("io.projectreactor:reactor-test")

    // Coroutines Test: 코루틴 코드를 테스트하기 위한 runTest 등
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")

    // JUnit Platform Launcher: 테스트 실행 엔진
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // 테스트용 H2 데이터베이스
    implementation("com.h2database:h2")
}

// Kotlin 컴파일러 옵션
kotlin {
    compilerOptions {
        // JSR-305 어노테이션을 엄격하게 처리합니다
        // Spring의 @Nullable/@NonNull이 Kotlin의 타입 시스템과 통합됩니다
        // 예를 들어 @NonNull String은 Kotlin에서 String으로 인식됩니다
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

// JPA를 위한 allOpen 플러그인 설정
// JPA는 엔티티 클래스가 open이어야 프록시를 생성할 수 있는데
// Kotlin은 기본적으로 모든 클래스가 final이므로 자동으로 open으로 만듭니다
allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

// 테스트에 JUnit Platform 사용
// JUnit5를 사용하려면 이 설정이 필요합니다
tasks.withType<Test> {
    useJUnitPlatform()
}