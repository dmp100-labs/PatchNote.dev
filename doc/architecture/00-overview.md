# Google 권장 아키텍처 다이어그램

PatchNote11 프로젝트의 Google Android App Architecture 기반 구조를 시각화한 PlantUML 다이어그램입니다.

## 다이어그램 목록

### 1. 전체 레이어 아키텍처
- **파일**: `architecture-layers.puml`
- **내용**: UI - Domain - Data 3 레이어 구조
- **핵심 개념**:
  - UI Layer: Composables + ViewModels + StateFlow
  - Domain Layer: Use Cases + Repository Interfaces + Models
  - Data Layer: Repository Implementations + Data Sources + DTOs

### 2. 데이터 플로우 - 읽기
- **파일**: `dataflow-read.puml`
- **내용**: 데이터 읽기 시퀀스 (GET)
- **흐름**:
  1. Screen → ViewModel → UseCase
  2. UseCase → Repository → DataSource
  3. Cache 확인 → API 호출 (필요시)
  4. DTO → Domain 변환
  5. StateFlow → UI 업데이트

### 3. 데이터 플로우 - 쓰기
- **파일**: `dataflow-write.puml`
- **내용**: 데이터 쓰기 시퀀스 (POST/PUT)
- **흐름**:
  1. User Input → ViewModel → UseCase
  2. Domain → DTO 변환
  3. API 호출 → 서버 저장
  4. 로컬 캐시 업데이트
  5. StateFlow → UI 업데이트

### 4. Shared 모듈 상세
- **파일**: `architecture-shared-detail.puml`
- **내용**: shared 모듈의 Domain + Data Layer 구조
- **주요 구성**:
  - domain/model: ReleaseNote, Project 등
  - domain/repository: Repository 인터페이스
  - domain/usecase: Use Cases
  - data/repository: Repository 구현
  - data/source: Remote + Local Data Sources
  - data/dto: 네트워크 DTOs
  - data/mapper: DTO ↔ Domain 변환

### 5. ComposeApp 모듈 상세
- **파일**: `architecture-composeapp-detail.puml`
- **내용**: composeApp 모듈의 UI Layer 구조
- **주요 구성**:
  - feature별 구조 (releasenote, project, auth)
  - Screen + ViewModel + UiState 패턴
  - 공통 컴포넌트 (Button, TextField 등)
  - Theme (Color, Typography)
  - Navigation

### 6. 모듈 간 의존성
- **파일**: `architecture-module-dependencies.puml`
- **내용**: 전체 모듈 간 의존성 관계
- **모듈**:
  - composeApp → shared (Use Cases)
  - iosApp → shared.framework
  - webApp → shared.js
  - server (독립적)
  - shared (Data Sources → Server API)

### 7. StateFlow 패턴
- **파일**: `architecture-stateflow-pattern.puml`
- **내용**: StateFlow 기반 상태 관리 시퀀스
- **패턴**:
  - MutableStateFlow (private)
  - StateFlow (public, read-only)
  - collectAsState()로 UI 구독
  - 단방향 데이터 플로우

### 8. 가이드 문서
- **파일**: `google-architecture-guide.md`
- **내용**: 전체 폴더 구조와 코드 예시

---

## 핵심 아키텍처 원칙

### 1. 레이어 분리
```
UI Layer (composeApp)
    ↓ depends on
Domain Layer (shared/domain)
    ↑ implements by
Data Layer (shared/data)
```

### 2. 단방향 데이터 플로우 (UDF)
```
User Action → ViewModel → UseCase → Repository → DataSource
     ↑                                                    ↓
     └────────── StateFlow ← Domain Model ←──────────────┘
```

### 3. 의존성 규칙
- UI Layer는 Domain Layer만 의존
- Domain Layer는 독립적 (순수 Kotlin)
- Data Layer는 Domain Layer 구현
- 상위 레이어는 하위 레이어를 모름

### 4. 책임 분리

#### UI Layer
- UI 렌더링 (Composables)
- 사용자 인터랙션 처리
- ViewModel을 통한 상태 관리

#### Domain Layer
- 비즈니스 로직 (Use Cases)
- 도메인 모델 정의
- Repository 인터페이스

#### Data Layer
- 데이터 접근 (Network, Database)
- Repository 구현
- 캐싱 전략

---

## Google 권장 패턴

### ViewModel + StateFlow
```kotlin
class ReleaseNoteListViewModel(
    private val getReleaseNotesUseCase: GetReleaseNotesUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    fun loadReleaseNotes(projectId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            
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
```

### UseCase 패턴
```kotlin
class GetReleaseNotesUseCase(
    private val repository: ReleaseNoteRepository
) {
    suspend operator fun invoke(projectId: String): Result<List<ReleaseNote>> {
        return repository.getReleaseNotes(projectId)
    }
}
```

### Repository 패턴
```kotlin
// Interface (Domain Layer)
interface ReleaseNoteRepository {
    suspend fun getReleaseNotes(projectId: String): Result<List<ReleaseNote>>
}

// Implementation (Data Layer)
class ReleaseNoteRepositoryImpl(
    private val remoteDataSource: ReleaseNoteRemoteDataSource,
    private val localDataSource: ReleaseNoteLocalDataSource,
    private val mapper: ReleaseNoteMapper
) : ReleaseNoteRepository {
    
    override suspend fun getReleaseNotes(projectId: String): Result<List<ReleaseNote>> {
        // Try cache first
        val cached = localDataSource.getReleaseNotes(projectId)
        if (cached.isNotEmpty()) {
            return Result.Success(cached.map { mapper.fromEntity(it) })
        }
        
        // Fetch from network
        return try {
            val dtos = remoteDataSource.fetchReleaseNotes(projectId)
            val notes = dtos.map { mapper.toDomain(it) }
            
            // Update cache
            localDataSource.saveReleaseNotes(notes.map { mapper.toEntity(it) })
            
            Result.Success(notes)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }
}
```

---

## 렌더링 방법

### IntelliJ IDEA / Android Studio
1. PlantUML 플러그인 설치
2. `.puml` 파일 열기
3. 우클릭 → "Show PlantUML Diagram"

### VS Code
1. PlantUML 확장 설치
2. `.puml` 파일 열기
3. `Alt+D` 또는 우클릭 → "Preview Current Diagram"

### CLI
```bash
# 모든 다이어그램 생성 (PNG)
java -jar plantuml.jar doc/architecture-*.puml

# SVG 생성
java -jar plantuml.jar -tsvg doc/architecture-*.puml
```

---

## 추천 학습 순서

1. **architecture-layers.puml** - 전체 레이어 이해
2. **dataflow-read.puml** - 읽기 플로우 이해
3. **dataflow-write.puml** - 쓰기 플로우 이해
4. **architecture-shared-detail.puml** - Domain + Data 구조
5. **architecture-composeapp-detail.puml** - UI 구조
6. **architecture-stateflow-pattern.puml** - 상태 관리 패턴
7. **architecture-module-dependencies.puml** - 전체 의존성

---

## 참고 자료

### Google 공식 문서
- [Guide to app architecture](https://developer.android.com/topic/architecture)
- [Jetpack Compose State](https://developer.android.com/jetpack/compose/state)
- [ViewModel Overview](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [Repository Pattern](https://developer.android.com/topic/architecture/data-layer)

### KMP 관련
- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)

### 추가 패턴
- [Clean Architecture (Uncle Bob)](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [MVI Pattern](https://www.youtube.com/watch?v=0VKprFON5xI)

---

## 다음 단계

1. **구현 시작**: google-architecture-guide.md 참고
2. **의존성 주입**: Koin 설정
3. **네트워크**: Ktor HttpClient 설정
4. **데이터베이스**: SQLDelight 설정
5. **테스트**: Unit Tests + UI Tests

이 아키텍처는 확장 가능하고 유지보수가 용이하며, Google이 권장하는 현대적인 Android 앱 개발 패턴을 따릅니다.
