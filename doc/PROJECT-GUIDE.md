# PatchNote11 프로젝트 가이드

개발자를 위한 통합 릴리즈 노트 - Kotlin Multiplatform 프로젝트

---

## 📚 목차

1. [프로젝트 개요](#프로젝트-개요)
2. [기술 스택](#기술-스택)
3. [프로젝트 구조](#프로젝트-구조)
4. [아키텍처](#아키텍처)
5. [모듈 상세](#모듈-상세)
6. [시작하기](#시작하기)
7. [문서 구조](#문서-구조)

---

## 프로젝트 개요

### 목표
개발자들이 여러 프로젝트의 릴리즈 노트를 한 곳에서 관리하고 공유할 수 있는 통합 플랫폼

### 지원 플랫폼
- 🤖 **Android** - Compose Multiplatform
- 🍎 **iOS** - SwiftUI + Shared Framework
- 🖥️ **Desktop** - Compose for Desktop
- 🌐 **Web** - React + TypeScript
- ⚙️ **Server** - Spring Boot

### 핵심 기능
- 릴리즈 노트 작성 및 관리
- 프로젝트별 릴리즈 히스토리
- 버전 관리 및 태깅
- 팀 협업 기능

---

## 기술 스택

### Shared (공통)
- **Kotlin Multiplatform** - 코드 공유
- **Kotlinx Serialization** - 직렬화
- **Ktor** - 네트워크 (예정)
- **SQLDelight** - 로컬 DB (예정)
- **Koin** - 의존성 주입 (예정)

### Frontend
- **Compose Multiplatform** - Android/Desktop UI
- **Material3** - 디자인 시스템
- **SwiftUI** - iOS UI
- **React 18** - Web UI
- **TypeScript** - Web 타입 안정성

### Backend
- **Spring Boot 3.x** - 프레임워크
- **Kotlin** - 언어
- **Spring Data JPA** - ORM
- **PostgreSQL** - 데이터베이스
- **Redis** - 캐시
- **Flyway** - 마이그레이션
- **Spring Security** - 인증/인가

---

## 프로젝트 구조

### 루트 구조
```
PatchNote11/
├── composeApp/          # Android & Desktop UI
├── server/              # Spring Boot 백엔드
├── shared/              # 공유 비즈니스 로직
├── iosApp/              # iOS 네이티브 앱
├── webApp/              # React 웹 앱
├── doc/                 # 📚 문서 (여기!)
└── gradle/              # Gradle 설정
```

### 모듈 의존성
```
┌─────────────────────────────────────┐
│           shared                    │
│  (Domain + Data Layer)              │
└──────┬──────────┬──────────┬────────┘
       │          │          │
   ┌───▼───┐  ┌──▼──┐   ┌───▼────┐
   │server │  │ iOS │   │compose │
   │       │  │ App │   │  App   │
   └───────┘  └─────┘   └────────┘
   
                        ┌────────┐
                        │ web    │
                        │ App    │
                        └────────┘
```

---

## 아키텍처

### Google 권장 아키텍처 (3 Layer)

이 프로젝트는 [Google의 Modern Android App Architecture](https://developer.android.com/topic/architecture)를 따릅니다.

```
┌─────────────────────────────────┐
│   UI Layer                      │
│   - Composables (화면)          │
│   - ViewModels (상태 관리)      │
│   - StateFlow (단방향 데이터)   │
└────────────┬────────────────────┘
             │ depends on
┌────────────▼────────────────────┐
│   Domain Layer                  │
│   - Use Cases (비즈니스 로직)   │
│   - Repository Interfaces       │
│   - Domain Models               │
└────────────┬────────────────────┘
             ▲ implements
┌────────────┴────────────────────┐
│   Data Layer                    │
│   - Repository Implementations  │
│   - Data Sources (Remote/Local)│
│   - DTOs & Mappers              │
└─────────────────────────────────┘
```

### 주요 원칙

1. **단방향 데이터 플로우 (UDF)**
   - User Action → ViewModel → UseCase → Repository → DataSource
   - DataSource → Domain Model → StateFlow → UI Update

2. **레이어 분리**
   - UI는 Domain만 의존
   - Domain은 독립적 (순수 Kotlin)
   - Data는 Domain 구현

3. **책임 분리**
   - UI Layer: 렌더링 & 인터랙션
   - Domain Layer: 비즈니스 로직
   - Data Layer: 데이터 접근

### 상세 다이어그램

#### 전체 레이어 구조
[아키텍처 레이어 다이어그램](architecture/01-layers.puml)

#### 데이터 플로우
- [읽기 플로우 (GET)](architecture/04-dataflow-read.puml)
- [쓰기 플로우 (POST/PUT)](architecture/05-dataflow-write.puml)

#### 상태 관리
- [StateFlow 패턴](architecture/06-stateflow.puml)

#### 모듈 의존성
- [전체 의존성 관계](architecture/07-dependencies.puml)

---

## 모듈 상세

### 1. Shared 모듈

**역할**: 모든 플랫폼에서 공유하는 비즈니스 로직

**구조**:
```
shared/src/commonMain/
├── domain/              # 비즈니스 로직
│   ├── model/          # Domain Models (ReleaseNote, Project)
│   ├── repository/     # Repository 인터페이스
│   └── usecase/        # Use Cases
│
└── data/               # 데이터 처리
    ├── repository/     # Repository 구현
    ├── source/         # Remote & Local Data Sources
    ├── dto/            # 네트워크 DTOs
    └── mapper/         # DTO ↔ Domain 변환
```

**다이어그램**: [Shared 모듈 상세](architecture/02-shared-module.puml) | [모듈 구조](modules/shared.puml)

**주요 클래스**:
- `ReleaseNote` - 릴리즈 노트 도메인 모델
- `ReleaseNoteRepository` - Repository 인터페이스
- `GetReleaseNotesUseCase` - 릴리즈 노트 조회 Use Case

---

### 2. ComposeApp 모듈

**역할**: Android & Desktop UI

**구조**:
```
composeApp/src/commonMain/
├── feature/            # Feature별 구성
│   ├── releasenote/
│   │   ├── list/      # Screen + ViewModel + UiState
│   │   ├── detail/
│   │   └── create/
│   └── project/
│
└── ui/
    ├── component/      # 공통 컴포넌트
    ├── theme/          # Material3 테마
    └── navigation/     # Navigation
```

**다이어그램**: [UI 모듈 상세](architecture/03-ui-module.puml) | [모듈 구조](modules/composeApp.puml)

**패턴**:
- Screen (Composable) + ViewModel + UiState
- StateFlow로 상태 관리
- Feature 단위 구성

---

### 3. Server 모듈

**역할**: Spring Boot 백엔드 API

**구조**:
```
server/src/main/
├── domain/             # JPA Entities
├── repository/         # Spring Data JPA
├── service/            # 비즈니스 로직
├── controller/         # REST API
├── config/             # 설정
└── security/           # 인증/인가
```

**다이어그램**: [모듈 구조](modules/server.puml)

**주요 기능**:
- REST API 제공
- PostgreSQL 영속성
- JWT 인증 (예정)
- Redis 캐싱

---

### 4. iOS App 모듈

**역할**: iOS 네이티브 앱

**구조**:
```
iosApp/
├── iosApp/            # SwiftUI Views
├── Feature/           # Feature별 Views
└── Service/           # Shared Framework 래핑
```

**다이어그램**: [모듈 구조](modules/iosApp.puml)

**통합**:
- shared 모듈을 Static Framework로 링크
- SwiftUI로 네이티브 UI

---

### 5. Web App 모듈

**역할**: React 기반 웹 앱

**구조**:
```
webApp/src/
├── features/          # Feature별 구성
├── components/        # 공통 컴포넌트
├── hooks/             # Custom Hooks
└── services/          # API 호출
```

**다이어그램**: [모듈 구조](modules/webApp.puml)

**통합**:
- shared.js (Kotlin/JS 컴파일 결과) 사용
- TypeScript 타입 정의 자동 생성

---

## 시작하기

### 필수 요구사항

- **JDK 17** 이상
- **Kotlin 2.x**
- **Gradle 8.x**
- **Android Studio** (Android 개발)
- **Xcode** (iOS 개발)
- **Node.js** (Web 개발)

### 프로젝트 클론

```bash
git clone https://github.com/your-username/PatchNote11.git
cd PatchNote11
```

### Android/Desktop 실행

```bash
# Android
./gradlew :composeApp:installDebug

# Desktop
./gradlew :composeApp:run
```

### iOS 실행

```bash
# Xcode로 열기
open iosApp/iosApp.xcodeproj
```

### Web 실행

```bash
cd webApp
npm install
npm run dev
```

### Server 실행

```bash
./gradlew :server:bootRun
```

---

## 문서 구조

### 📁 Doc 디렉토리 구성

```
doc/
├── architecture/              🏗️ 아키텍처 문서
│   ├── 00-overview.md        - 개요
│   ├── 01-layers.puml        - 3 레이어 구조
│   ├── 02-shared-module.puml - Shared 모듈 상세
│   ├── 03-ui-module.puml     - UI 모듈 상세
│   ├── 04-dataflow-read.puml - 읽기 플로우
│   ├── 05-dataflow-write.puml - 쓰기 플로우
│   ├── 06-stateflow.puml     - 상태 관리
│   ├── 07-dependencies.puml  - 의존성
│   └── folder-structure.md   - 폴더 구조 가이드
│
├── modules/                   📦 모듈별 구조
│   ├── README.md             - 모듈 가이드
│   ├── shared.puml
│   ├── composeApp.puml
│   ├── server.puml
│   ├── iosApp.puml
│   └── webApp.puml
│
├── images/                    📁 이미지
│   └── img.png
│
└── archive/                   🗄️ 이전 문서
    └── (레거시 파일들)
```

### 아키텍처 문서

#### 개요 및 가이드
- [아키텍처 개요](architecture/00-overview.md) - 전체 구조 설명
- [폴더 구조 가이드](architecture/folder-structure.md) - 상세 폴더 구조

#### PlantUML 다이어그램

**시스템 아키텍처**
- [01. 3 레이어 아키텍처](architecture/01-layers.puml)
- [02. Shared 모듈 상세](architecture/02-shared-module.puml)
- [03. UI 모듈 상세](architecture/03-ui-module.puml)
- [07. 모듈 의존성](architecture/07-dependencies.puml)

**데이터 플로우**
- [04. 데이터 읽기 플로우](architecture/04-dataflow-read.puml)
- [05. 데이터 쓰기 플로우](architecture/05-dataflow-write.puml)

**패턴**
- [06. StateFlow 상태 관리](architecture/06-stateflow.puml)

### 모듈 문서

- [모듈 가이드](modules/README.md) - 모듈 개요
- [Shared 모듈](modules/shared.puml)
- [ComposeApp 모듈](modules/composeApp.puml)
- [Server 모듈](modules/server.puml)
- [iOS App 모듈](modules/iosApp.puml)
- [Web App 모듈](modules/webApp.puml)

---

## 다이어그램 렌더링

### IntelliJ IDEA / Android Studio

1. **PlantUML 플러그인 설치**
   - Settings → Plugins → "PlantUML integration" 검색 및 설치

2. **다이어그램 보기**
   - `.puml` 파일 열기
   - 우클릭 → "Show PlantUML Diagram"

### VS Code

1. **PlantUML 확장 설치**
   - Extensions → "PlantUML" 검색 및 설치

2. **다이어그램 보기**
   - `.puml` 파일 열기
   - `Alt+D` 또는 우클릭 → "Preview Current Diagram"

### 온라인

http://www.plantuml.com/plantuml/uml/ 에서 코드 붙여넣기

### CLI

```bash
# PlantUML JAR 다운로드
wget https://github.com/plantuml/plantuml/releases/download/v1.2024.0/plantuml-1.2024.0.jar

# PNG 생성
java -jar plantuml.jar doc/architecture/*.puml

# SVG 생성
java -jar plantuml.jar -tsvg doc/architecture/*.puml
```

---

## 개발 워크플로우

### 새 기능 개발 순서

1. **Domain Layer** (shared/domain/)
   - Domain Model 정의
   - Repository Interface 정의
   - Use Case 작성

2. **Data Layer** (shared/data/)
   - Repository 구현
   - Data Source 작성
   - DTO & Mapper 작성

3. **UI Layer** (composeApp/)
   - ViewModel 작성
   - UiState 정의
   - Screen Composable 작성

4. **Backend** (server/)
   - Entity 정의
   - Repository 작성
   - Service & Controller 작성

### 예시: 릴리즈 노트 조회 기능

```kotlin
// 1. Domain Model (shared/domain/model/)
data class ReleaseNote(
    val id: String,
    val title: String,
    val content: String,
    val version: String
)

// 2. Repository Interface (shared/domain/repository/)
interface ReleaseNoteRepository {
    suspend fun getReleaseNotes(projectId: String): Result<List<ReleaseNote>>
}

// 3. Use Case (shared/domain/usecase/)
class GetReleaseNotesUseCase(
    private val repository: ReleaseNoteRepository
) {
    suspend operator fun invoke(projectId: String) = 
        repository.getReleaseNotes(projectId)
}

// 4. ViewModel (composeApp/feature/releasenote/)
class ReleaseNoteListViewModel(
    private val getReleaseNotesUseCase: GetReleaseNotesUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    fun loadReleaseNotes(projectId: String) {
        viewModelScope.launch {
            when (val result = getReleaseNotesUseCase(projectId)) {
                is Result.Success -> {
                    _uiState.value = UiState.Success(result.data)
                }
                is Result.Error -> {
                    _uiState.value = UiState.Error(result.message)
                }
            }
        }
    }
}

// 5. Screen (composeApp/feature/releasenote/)
@Composable
fun ReleaseNoteListScreen(
    viewModel: ReleaseNoteListViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    
    when (uiState) {
        is UiState.Loading -> LoadingView()
        is UiState.Success -> ContentView((uiState as UiState.Success).notes)
        is UiState.Error -> ErrorView((uiState as UiState.Error).message)
    }
}
```

---

## 참고 자료

### Google 공식 문서
- [Guide to app architecture](https://developer.android.com/topic/architecture)
- [ViewModel Overview](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [StateFlow and SharedFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow)
- [Repository Pattern](https://developer.android.com/topic/architecture/data-layer)

### Kotlin Multiplatform
- [Kotlin Multiplatform 공식 문서](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [KMP 샘플 프로젝트](https://github.com/kotlin/kmm-samples)

### Spring Boot
- [Spring Boot 공식 문서](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Security](https://spring.io/projects/spring-security)

---

## 다음 단계

### Phase 1: 기본 기능 구현
- [ ] Domain Models 정의
- [ ] Repository Interfaces 작성
- [ ] Use Cases 구현
- [ ] 기본 UI 화면 작성

### Phase 2: 네트워크 & 데이터베이스
- [ ] Ktor HttpClient 설정
- [ ] SQLDelight 설정
- [ ] Repository 구현
- [ ] 서버 API 엔드포인트 작성

### Phase 3: UI 고도화
- [ ] Material3 테마 적용
- [ ] Navigation 구현
- [ ] 애니메이션 추가
- [ ] 반응형 레이아웃

### Phase 4: 고급 기능
- [ ] 오프라인 지원
- [ ] 푸시 알림
- [ ] 다국어 지원
- [ ] 다크 모드

---

## 기여하기

프로젝트에 기여하고 싶으신가요?

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 라이선스

이 프로젝트는 [MIT License](../LICENSE) 하에 있습니다.

---

## 문의

프로젝트 관련 문의사항이 있으시면 이슈를 생성해주세요.

**Made with ❤️ using Kotlin Multiplatform**
