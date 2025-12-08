# Google 권장 아키텍처 기반 프로젝트 구조

## 아키텍처 개요

Google의 Modern Android App Architecture를 KMP에 적용:
- **UI Layer**: UI 요소 (Composables) + State Holders (ViewModel)
- **Domain Layer**: Use Cases + Repository Interfaces
- **Data Layer**: Repository Implementations + Data Sources

## 레이어별 역할

### UI Layer
- 화면 렌더링 (Compose)
- 사용자 인터랙션 처리
- UI 상태 관리 (ViewModel + StateFlow)

### Domain Layer
- 비즈니스 로직 (Use Cases)
- 도메인 모델 (엔티티)
- Repository 인터페이스

### Data Layer
- 데이터 접근 (Repository 구현)
- 네트워크 / 로컬 데이터 소스
- DTO ↔ Domain 매핑

---

## 1. Shared 모듈 (Domain + Data Layer)

```
shared/src/commonMain/kotlin/org/patchnote/patchnote/

├── domain/                           # Domain Layer
│   │
│   ├── model/                        # Domain Models (비즈니스 엔티티)
│   │   ├── ReleaseNote.kt
│   │   │   - id: String
│   │   │   - title: String
│   │   │   - content: String
│   │   │   - version: String
│   │   │   - releaseDate: LocalDateTime
│   │   │   - projectId: String
│   │   │   - status: NoteStatus
│   │   │
│   │   ├── Project.kt
│   │   │   - id: String
│   │   │   - name: String
│   │   │   - description: String
│   │   │   - createdAt: LocalDateTime
│   │   │
│   │   ├── User.kt
│   │   │   - id: String
│   │   │   - email: String
│   │   │   - name: String
│   │   │
│   │   └── common/                   # Value Objects
│   │       ├── NoteStatus.kt         # enum: DRAFT, PUBLISHED, ARCHIVED
│   │       └── ProjectType.kt
│   │
│   ├── repository/                   # Repository Interfaces
│   │   ├── ReleaseNoteRepository.kt
│   │   │   + suspend fun getReleaseNotes(projectId: String): Result<List<ReleaseNote>>
│   │   │   + suspend fun getReleaseNoteById(id: String): Result<ReleaseNote>
│   │   │   + suspend fun createReleaseNote(note: ReleaseNote): Result<ReleaseNote>
│   │   │   + suspend fun updateReleaseNote(note: ReleaseNote): Result<ReleaseNote>
│   │   │   + suspend fun deleteReleaseNote(id: String): Result<Unit>
│   │   │   + fun observeReleaseNotes(projectId: String): Flow<List<ReleaseNote>>
│   │   │
│   │   ├── ProjectRepository.kt
│   │   └── UserRepository.kt
│   │
│   └── usecase/                      # Use Cases (Single Responsibility)
│       │
│       ├── releasenote/
│       │   ├── GetReleaseNotesUseCase.kt
│       │   │   + suspend operator fun invoke(projectId: String): Result<List<ReleaseNote>>
│       │   │
│       │   ├── GetReleaseNoteByIdUseCase.kt
│       │   ├── CreateReleaseNoteUseCase.kt
│       │   ├── UpdateReleaseNoteUseCase.kt
│       │   ├── DeleteReleaseNoteUseCase.kt
│       │   └── ObserveReleaseNotesUseCase.kt
│       │
│       └── project/
│           ├── GetProjectsUseCase.kt
│           ├── CreateProjectUseCase.kt
│           └── UpdateProjectUseCase.kt
│
├── data/                             # Data Layer
│   │
│   ├── repository/                   # Repository Implementations
│   │   ├── ReleaseNoteRepositoryImpl.kt
│   │   │   - remoteDataSource: ReleaseNoteRemoteDataSource
│   │   │   - localDataSource: ReleaseNoteLocalDataSource
│   │   │   - mapper: ReleaseNoteMapper
│   │   │
│   │   ├── ProjectRepositoryImpl.kt
│   │   └── UserRepositoryImpl.kt
│   │
│   ├── source/                       # Data Sources
│   │   │
│   │   ├── remote/                   # Network
│   │   │   ├── ReleaseNoteRemoteDataSource.kt
│   │   │   │   + suspend fun fetchReleaseNotes(projectId: String): List<ReleaseNoteDto>
│   │   │   │   + suspend fun createReleaseNote(dto: ReleaseNoteDto): ReleaseNoteDto
│   │   │   │
│   │   │   ├── ProjectRemoteDataSource.kt
│   │   │   └── api/
│   │   │       ├── ApiClient.kt      # expect/actual (Ktor)
│   │   │       └── ApiEndpoints.kt
│   │   │
│   │   └── local/                    # Cache/Database
│   │       ├── ReleaseNoteLocalDataSource.kt
│   │       │   + suspend fun saveReleaseNotes(notes: List<ReleaseNoteEntity>)
│   │       │   + suspend fun getReleaseNotes(projectId: String): List<ReleaseNoteEntity>
│   │       │   + fun observeReleaseNotes(projectId: String): Flow<List<ReleaseNoteEntity>>
│   │       │
│   │       ├── ProjectLocalDataSource.kt
│   │       └── database/
│   │           ├── Database.kt       # expect/actual (SQLDelight)
│   │           └── entity/
│   │               ├── ReleaseNoteEntity.kt
│   │               └── ProjectEntity.kt
│   │
│   ├── dto/                          # Network DTOs
│   │   ├── ReleaseNoteDto.kt        # @Serializable
│   │   ├── ProjectDto.kt
│   │   └── UserDto.kt
│   │
│   └── mapper/                       # DTO ↔ Domain 변환
│       ├── ReleaseNoteMapper.kt
│       │   + fun toDomain(dto: ReleaseNoteDto): ReleaseNote
│       │   + fun toDto(domain: ReleaseNote): ReleaseNoteDto
│       │   + fun toEntity(domain: ReleaseNote): ReleaseNoteEntity
│       │   + fun fromEntity(entity: ReleaseNoteEntity): ReleaseNote
│       │
│       └── ProjectMapper.kt
│
├── util/                             # 공통 유틸
│   ├── Result.kt                     # sealed class Result<T>
│   ├── NetworkResult.kt
│   └── DateTimeUtil.kt
│
└── di/                               # 의존성 주입 모듈 (expect/actual)
    ├── RepositoryModule.kt
    ├── UseCaseModule.kt
    └── DataSourceModule.kt
```

---

## 2. ComposeApp 모듈 (UI Layer)

```
composeApp/src/commonMain/kotlin/org/patchnote/patchnote/

├── App.kt                            # Root Composable
│
├── feature/                          # Feature 모듈
│   │
│   ├── releasenote/                  # Release Note Feature
│   │   │
│   │   ├── list/                     # 목록 화면
│   │   │   ├── ReleaseNoteListScreen.kt
│   │   │   │   @Composable
│   │   │   │   fun ReleaseNoteListScreen(
│   │   │   │       viewModel: ReleaseNoteListViewModel,
│   │   │   │       onNoteClick: (String) -> Unit
│   │   │   │   )
│   │   │   │
│   │   │   ├── ReleaseNoteListViewModel.kt
│   │   │   │   - getReleaseNotesUseCase: GetReleaseNotesUseCase
│   │   │   │   - observeReleaseNotesUseCase: ObserveReleaseNotesUseCase
│   │   │   │   
│   │   │   │   + val uiState: StateFlow<ReleaseNoteListUiState>
│   │   │   │   + fun loadReleaseNotes(projectId: String)
│   │   │   │   + fun refreshReleaseNotes()
│   │   │   │   + fun deleteReleaseNote(id: String)
│   │   │   │
│   │   │   ├── ReleaseNoteListUiState.kt
│   │   │   │   sealed interface ReleaseNoteListUiState {
│   │   │   │       data object Loading : ReleaseNoteListUiState
│   │   │   │       data class Success(val notes: List<ReleaseNote>) : ReleaseNoteListUiState
│   │   │   │       data class Error(val message: String) : ReleaseNoteListUiState
│   │   │   │   }
│   │   │   │
│   │   │   └── component/
│   │   │       ├── ReleaseNoteListItem.kt
│   │   │       └── EmptyReleaseNoteList.kt
│   │   │
│   │   ├── detail/                   # 상세 화면
│   │   │   ├── ReleaseNoteDetailScreen.kt
│   │   │   ├── ReleaseNoteDetailViewModel.kt
│   │   │   ├── ReleaseNoteDetailUiState.kt
│   │   │   └── component/
│   │   │       └── NoteContentCard.kt
│   │   │
│   │   ├── create/                   # 생성 화면
│   │   │   ├── CreateReleaseNoteScreen.kt
│   │   │   ├── CreateReleaseNoteViewModel.kt
│   │   │   └── CreateReleaseNoteUiState.kt
│   │   │
│   │   └── navigation/               # Navigation
│   │       └── ReleaseNoteNavigation.kt
│   │
│   ├── project/                      # Project Feature
│   │   ├── list/
│   │   │   ├── ProjectListScreen.kt
│   │   │   ├── ProjectListViewModel.kt
│   │   │   └── ProjectListUiState.kt
│   │   │
│   │   ├── detail/
│   │   │   ├── ProjectDetailScreen.kt
│   │   │   └── ProjectDetailViewModel.kt
│   │   │
│   │   └── navigation/
│   │       └── ProjectNavigation.kt
│   │
│   └── auth/                         # Auth Feature
│       ├── login/
│       │   ├── LoginScreen.kt
│       │   ├── LoginViewModel.kt
│       │   └── LoginUiState.kt
│       │
│       └── navigation/
│           └── AuthNavigation.kt
│
├── ui/                               # 공통 UI 요소
│   │
│   ├── component/                    # 재사용 컴포넌트
│   │   ├── button/
│   │   │   ├── PrimaryButton.kt
│   │   │   └── SecondaryButton.kt
│   │   │
│   │   ├── input/
│   │   │   ├── CustomTextField.kt
│   │   │   └── SearchBar.kt
│   │   │
│   │   ├── card/
│   │   │   └── CustomCard.kt
│   │   │
│   │   ├── dialog/
│   │   │   ├── ConfirmDialog.kt
│   │   │   └── LoadingDialog.kt
│   │   │
│   │   └── loading/
│   │       └── LoadingIndicator.kt
│   │
│   ├── theme/                        # Material3 테마
│   │   ├── Color.kt
│   │   ├── Typography.kt
│   │   ├── Shape.kt
│   │   └── Theme.kt
│   │
│   └── navigation/                   # App Navigation
│       ├── AppNavigation.kt
│       ├── NavGraph.kt
│       └── NavRoute.kt               # sealed class
│
├── di/                               # DI (Koin)
│   ├── AppModule.kt
│   └── ViewModelModule.kt
│
└── util/                             # UI 유틸
    ├── UiText.kt                     # 다국어 지원
    └── Extension.kt
```

---

## 3. Server 모듈 (백엔드)

```
server/src/main/kotlin/org/patchnote/patchnote/

├── ServerApplication.kt
│
├── domain/                           # JPA Entities
│   ├── entity/
│   │   ├── ReleaseNoteEntity.kt
│   │   │   @Entity @Table(name = "release_notes")
│   │   │   - id: UUID
│   │   │   - title: String
│   │   │   - content: String
│   │   │   - version: String
│   │   │   - releaseDate: LocalDateTime
│   │   │   - projectId: UUID
│   │   │   - status: String
│   │   │
│   │   ├── ProjectEntity.kt
│   │   └── UserEntity.kt
│   │
│   └── type/
│       └── NoteStatus.kt
│
├── repository/                       # Spring Data JPA
│   ├── ReleaseNoteJpaRepository.kt
│   │   interface ReleaseNoteJpaRepository : JpaRepository<ReleaseNoteEntity, UUID> {
│   │       fun findByProjectId(projectId: UUID): List<ReleaseNoteEntity>
│   │       fun findByStatus(status: String): List<ReleaseNoteEntity>
│   │   }
│   │
│   ├── ProjectJpaRepository.kt
│   └── UserJpaRepository.kt
│
├── service/                          # Business Logic
│   ├── ReleaseNoteService.kt
│   │   @Service
│   │   - repository: ReleaseNoteJpaRepository
│   │   
│   │   + fun getReleaseNotes(projectId: UUID): List<ReleaseNoteEntity>
│   │   + fun createReleaseNote(note: ReleaseNoteEntity): ReleaseNoteEntity
│   │   + fun updateReleaseNote(id: UUID, note: ReleaseNoteEntity): ReleaseNoteEntity
│   │   + fun deleteReleaseNote(id: UUID)
│   │
│   ├── ProjectService.kt
│   └── UserService.kt
│
├── controller/                       # REST API
│   ├── ReleaseNoteController.kt
│   │   @RestController
│   │   @RequestMapping("/api/v1/release-notes")
│   │   
│   │   + GET    /api/v1/release-notes?projectId={id}
│   │   + GET    /api/v1/release-notes/{id}
│   │   + POST   /api/v1/release-notes
│   │   + PUT    /api/v1/release-notes/{id}
│   │   + DELETE /api/v1/release-notes/{id}
│   │
│   ├── ProjectController.kt
│   └── UserController.kt
│
├── dto/                              # Request/Response DTOs
│   ├── request/
│   │   ├── CreateReleaseNoteRequest.kt
│   │   └── UpdateReleaseNoteRequest.kt
│   │
│   └── response/
│       ├── ReleaseNoteResponse.kt
│       └── ProjectResponse.kt
│
├── mapper/                           # Entity ↔ DTO
│   ├── ReleaseNoteMapper.kt
│   └── ProjectMapper.kt
│
├── config/
│   ├── SecurityConfig.kt
│   ├── JpaConfig.kt
│   ├── RedisConfig.kt
│   └── CorsConfig.kt
│
├── security/
│   ├── JwtTokenProvider.kt
│   ├── JwtAuthenticationFilter.kt
│   └── UserDetailsServiceImpl.kt
│
└── exception/
    ├── GlobalExceptionHandler.kt
    ├── CustomException.kt
    └── ErrorResponse.kt
```

---

## 데이터 흐름

### 읽기 (Read)
```
[UI] ReleaseNoteListScreen
  ↓ collect
[ViewModel] uiState: StateFlow<UiState>
  ↓ invoke
[UseCase] GetReleaseNotesUseCase
  ↓ call
[Repository Interface] ReleaseNoteRepository
  ↓ implement
[Repository Impl] ReleaseNoteRepositoryImpl
  ↓ fetch
[RemoteDataSource] → API → Server
  ↓ map
[DTO] → [Domain Model]
  ↓ emit
[StateFlow] → UI 업데이트
```

### 쓰기 (Write)
```
[UI] CreateReleaseNoteScreen (사용자 입력)
  ↓ call
[ViewModel] createNote(note)
  ↓ invoke
[UseCase] CreateReleaseNoteUseCase
  ↓ call
[Repository] createReleaseNote(note)
  ↓ map & send
[RemoteDataSource] → API → Server
  ↓ persist
[Database] → ReleaseNoteEntity 저장
  ↓ response
[DTO] → [Domain Model]
  ↓ update
[LocalCache] 업데이트
  ↓ emit
[StateFlow] → UI 업데이트
```

---

## 핵심 원칙

1. **단방향 데이터 플로우 (UDF)**
   - UI는 ViewModel의 StateFlow를 관찰
   - 사용자 이벤트는 ViewModel 메서드 호출
   - ViewModel은 UseCase 호출, StateFlow 업데이트

2. **레이어 간 의존성**
   - UI → Domain ← Data
   - Domain은 독립적 (플랫폼 무관)
   - Data는 Domain 인터페이스 구현

3. **UseCase의 역할**
   - 단일 비즈니스 로직 캡슐화
   - Repository 조합
   - 재사용 가능한 단위

4. **Repository 패턴**
   - 데이터 소스 추상화
   - Remote + Local 통합
   - 캐싱 전략

5. **ViewModel**
   - UI 상태 관리
   - 비즈니스 로직 호출 (UseCase)
   - Lifecycle 인식

---

## 의존성 주입 (Koin 예시)

```kotlin
// shared/di/RepositoryModule.kt
val repositoryModule = module {
    single<ReleaseNoteRepository> {
        ReleaseNoteRepositoryImpl(
            remoteDataSource = get(),
            localDataSource = get(),
            mapper = get()
        )
    }
}

val useCaseModule = module {
    factory { GetReleaseNotesUseCase(get()) }
    factory { CreateReleaseNoteUseCase(get()) }
}

// composeApp/di/ViewModelModule.kt
val viewModelModule = module {
    viewModel { 
        ReleaseNoteListViewModel(
            getReleaseNotesUseCase = get(),
            observeReleaseNotesUseCase = get()
        )
    }
}
```

이 구조는 Google의 권장 아키텍처를 KMP에 완벽히 적용한 형태입니다.
