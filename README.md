# PatchNote - Kotlin Multiplatform Project

This is a Kotlin Multiplatform project targeting Android, iOS, Web, Desktop (JVM), and Server.

개발자들이 최신 기술 뉴스와 과거 공식 문서를 통합 관리하며, 역할과 기술 스택에 맞춤형 정보를 실시간으로 받을 수 있는 플랫폼입니다.

## 📁 프로젝트 구조

```
PatchNote/
├── composeApp/          # Compose Multiplatform 클라이언트
├── shared/              # 공통 데이터 모델 및 비즈니스 로직
├── server/              # Spring Boot 백엔드 서버
├── iosApp/              # iOS 진입점
├── webApp/              # React Web 애플리케이션
└── gradle/              # Gradle 설정 및 버전 관리
```

### `/composeApp` - Compose Multiplatform 클라이언트

Compose Multiplatform 애플리케이션에서 공유되는 코드입니다.

- **[commonMain](./composeApp/src/commonMain/kotlin)**: 모든 플랫폼에서 공통으로 사용되는 코드
- **[androidMain](./composeApp/src/androidMain/kotlin)**: Android 전용 코드
- **[iosMain](./composeApp/src/iosMain/kotlin)**: iOS 전용 코드 (예: CoreCrypto 사용)
- **[jvmMain](./composeApp/src/jvmMain/kotlin)**: Desktop (JVM) 전용 코드
- **[wasmJsMain](./composeApp/src/wasmJsMain/kotlin)**: Web (WASM) 전용 코드

### `/iosApp` - iOS 애플리케이션

iOS 애플리케이션의 진입점입니다. Compose Multiplatform으로 UI를 공유하더라도 이 진입점이 필요하며, SwiftUI 코드도 여기에 추가합니다.

### `/server` - Spring Boot 백엔드 서버

**Spring Boot 3.5.7** 기반 REST API 서버입니다.

**기술 스택:**
- Spring Boot 3.5.7
- Kotlin 2.2.20
- Java 17
- H2 Database (개발), PostgreSQL (프로덕션)
- Spring Data JPA
- Spring Security
- Redis (캐싱/세션)
- Flyway (DB 마이그레이션)

**구조:**
```
server/
├── src/main/kotlin/org/patchnote/patchnote/
│   ├── ServerApplication.kt    # 메인 애플리케이션
│   ├── controller/             # REST API 컨트롤러
│   ├── domain/                 # JPA 엔티티
│   ├── repository/             # 데이터 접근 계층
│   ├── service/                # 비즈니스 로직
│   └── config/                 # Spring 설정
└── src/main/resources/
    ├── application.yml         # 애플리케이션 설정
    └── db/migration/           # Flyway 마이그레이션 스크립트
```

### `/shared` - 공통 모듈

모든 타겟(클라이언트와 서버)에서 공유되는 코드입니다.

- **[commonMain](./shared/src/commonMain/kotlin)**: 가장 중요한 폴더로, 공통 데이터 모델과 비즈니스 로직이 위치합니다
- 필요시 플랫폼별 폴더에 특화된 코드를 추가할 수 있습니다

**주요 기능:**
- 클라이언트와 서버 간 데이터 모델 공유
- Kotlin Serialization을 통한 일관된 직렬화
- 공통 비즈니스 로직 및 유틸리티

### `/webApp` - React Web 애플리케이션

React 기반 웹 애플리케이션입니다. [shared](./shared) 모듈에서 생성된 Kotlin/JS 라이브러리를 사용합니다.

---

## 🚀 빌드 및 실행

### 사전 요구사항

- **Java 17 이상** (권장: Eclipse Temurin 17 또는 Amazon Corretto 17)
- **Gradle 8.14.3** (Gradle Wrapper 포함)
- **Node.js** (Web 앱 실행 시)
- **Xcode** (iOS 앱 빌드 시, macOS 전용)

### Java 버전 확인

```bash
# 설치된 Java 버전 확인 (macOS)
/usr/libexec/java_home -V

# 현재 사용 중인 Java 버전
java -version
```

### Gradle 설정

프로젝트 루트의 `gradle.properties` 파일에서 Java 경로를 설정합니다:

```properties
org.gradle.java.home=/Users/YOUR_USERNAME/Library/Java/JavaVirtualMachines/temurin-17.0.16/Contents/Home
org.gradle.jvmargs=-Xmx4096M -Dfile.encoding=UTF-8
org.gradle.parallel=true
org.gradle.caching=true
```

---

## 📱 Android 애플리케이션

### 빌드

```bash
# macOS/Linux
./gradlew :composeApp:assembleDebug

# Windows
.\gradlew.bat :composeApp:assembleDebug
```

### 실행

Android Studio에서 `composeApp` 모듈을 선택하고 실행하거나, 연결된 디바이스에 설치합니다.

```bash
./gradlew :composeApp:installDebug
```

---

## 🖥️ Desktop (JVM) 애플리케이션

### 빌드 및 실행

```bash
# macOS/Linux
./gradlew :composeApp:run

# Windows
.\gradlew.bat :composeApp:run
```

### 패키지 생성

```bash
./gradlew :composeApp:packageDistributionForCurrentOS
```

---

## 🌐 Server (Spring Boot)

### 개발 모드 실행

```bash
# macOS/Linux
./gradlew :server:bootRun

# Windows
.\gradlew.bat :server:bootRun
```

서버가 시작되면 다음 주소로 접속할 수 있습니다:
- API: http://localhost:8080/api/hello
- Health Check: http://localhost:8080/actuator/health
- H2 Console: http://localhost:8080/h2-console

**H2 Console 접속 정보:**
- JDBC URL: `jdbc:h2:mem:patchnote`
- User Name: `sa`
- Password: (비워두기)

### 테스트 실행

```bash
./gradlew :server:test
```

### 프로덕션 JAR 빌드

```bash
./gradlew :server:bootJar
```

생성된 JAR 파일 실행:

```bash
java -jar server/build/libs/server-1.0.0.jar
```

### 프로덕션 배포

환경 변수로 설정을 덮어쓸 수 있습니다:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/patchnote
export SPRING_DATASOURCE_USERNAME=patchnote
export SPRING_DATASOURCE_PASSWORD=your_password
export SPRING_JPA_HIBERNATE_DDL_AUTO=validate

java -jar server/build/libs/server-1.0.0.jar
```

---

## 🌐 Web 애플리케이션

### 방법 1: Compose Multiplatform Web (WASM)

Gradle을 통해 Web 앱을 빌드하고 실행합니다 (권장):

```bash
# 개발 서버 실행
./gradlew :composeApp:wasmJsBrowserDevelopmentRun

# 프로덕션 빌드
./gradlew :composeApp:wasmJsBrowserDistribution
```

### 방법 2: React Web (webApp 디렉토리 사용 시)

별도의 React 앱을 사용하는 경우:

1. **Shared 모듈 빌드:**
   ```bash
   # macOS/Linux
   ./gradlew :shared:jsProductionLibraryCompileSync

   # Windows
   .\gradlew.bat :shared:jsProductionLibraryCompileSync
   ```

2. **Web 애플리케이션 실행:**
   ```bash
   cd webApp
   npm install
   npm run dev
   ```

**참고:** 프로젝트 루트에서 직접 `npm install`을 실행하지 마세요. Gradle이 JavaScript 의존성을 자동으로 관리합니다.

---

## 📱 iOS 애플리케이션

### Xcode에서 실행

1. Xcode에서 `/iosApp` 디렉토리를 엽니다
2. 시뮬레이터 또는 실제 디바이스를 선택합니다
3. Run 버튼을 클릭합니다

### 터미널에서 빌드

```bash
./gradlew :composeApp:iosSimulatorArm64MainBinaryFramework
```

---

## 🧹 Gradle 캐시 정리

빌드 문제가 발생하거나 의존성 충돌이 있을 때 캐시를 정리할 수 있습니다.

### 전체 캐시 정리

```bash
# Gradle 데몬 중지
./gradlew --stop

# 전역 Gradle 캐시 삭제
rm -rf ~/.gradle/caches

# 프로젝트 로컬 캐시 삭제
cd /Users/seong-gyuhyeon/Downloads/PatchNote11
rm -rf .gradle

# 재빌드
./gradlew clean build
```

### 선택적 정리

```bash
# 프로젝트 빌드 산출물만 정리
./gradlew clean

# 특정 모듈만 정리
./gradlew :server:clean
```

---

## 🔧 문제 해결

### 일반적인 문제

**1. Java 버전 불일치**

```bash
# Java 버전 확인
java -version
./gradlew --version

# gradle.properties에서 올바른 Java 경로 설정
```

**2. bootJar 태스크 실패 (Apache Commons Compress 오류)**

`server/build.gradle.kts`의 `configurations` 블록 확인:

```kotlin
configurations {
    all {
        resolutionStrategy {
            force("org.apache.commons:commons-compress:1.27.1")
        }
    }
}
```

**3. npm 관련 오류**

프로젝트 루트에서 직접 `npm install`을 실행하지 마세요. Gradle 태스크를 사용하세요:

```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

**4. 테스트 실패 (데이터베이스 연결)**

`server/src/test/resources/application.yml`에 H2 설정이 있는지 확인하세요.

### 상세 로그 확인

```bash
# 정보 수준 로그
./gradlew :server:build --info

# 디버그 수준 로그
./gradlew :server:build --debug
```

### 의존성 확인

```bash
# 전체 의존성 트리 확인
./gradlew :server:dependencies

# 특정 configuration 확인
./gradlew :server:dependencies --configuration runtimeClasspath
```

---

## 📚 더 알아보기

- [Kotlin Multiplatform 공식 문서](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Spring Boot 공식 문서](https://spring.io/projects/spring-boot)
- [프로젝트 통합 가이드](./KMP-SpringBoot-Integration-Complete-Guide-Final.md)

---

## 🤝 기여하기

1. 이 저장소를 Fork 합니다
2. Feature 브랜치를 생성합니다 (`git checkout -b feature/AmazingFeature`)
3. 변경사항을 커밋합니다 (`git commit -m 'Add some AmazingFeature'`)
4. 브랜치에 Push 합니다 (`git push origin feature/AmazingFeature`)
5. Pull Request를 생성합니다

---

## 📝 라이선스

이 프로젝트는 MIT 라이선스를 따릅니다.

---

## 👥 개발팀

- **백엔드**: Spring Boot + Kotlin
- **프론트엔드**: Compose Multiplatform
- **공통 모듈**: Kotlin Serialization

---

## 🎯 주요 기능

- ✅ 크로스 플랫폼 지원 (Android, iOS, Web, Desktop)
- ✅ Spring Boot 기반 REST API
- ✅ 공통 데이터 모델 공유
- ✅ H2/PostgreSQL 데이터베이스
- ✅ Redis 캐싱
- ✅ JWT 인증 (예정)
- ✅ Flyway 마이그레이션
- ✅ Actuator 모니터링

---

## 📊 프로젝트 상태

- [x] 프로젝트 초기 설정
- [x] Spring Boot 통합
- [x] 기본 REST API
- [x] Security 설정
- [x] 테스트 환경 구성
- [ ] JWT 인증 구현
- [ ] 데이터베이스 엔티티 설계
- [ ] 클라이언트-서버 연동
- [ ] CI/CD 파이프라인

---

**Happy Coding! 🚀**# PatchNote.dev
