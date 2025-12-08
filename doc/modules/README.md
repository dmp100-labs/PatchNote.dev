# PatchNote11 모듈 구조 다이어그램

PlantUML로 작성된 프로젝트 아키텍처 문서입니다.

## 다이어그램 목록

### 1. 전체 아키텍처
- **파일**: `init-architecture.puml`
- **내용**: 전체 시스템 개요, 모듈 간 의존성 관계
- **수정**: 순수 컴포넌트 다이어그램 스타일로 변경

### 2. Shared 모듈
- **파일**: `module-shared.puml`
- **내용**: Kotlin Multiplatform 공유 모듈 구조
- **주요 항목**:
  - expect/actual 패턴
  - 5개 플랫폼 소스셋 (commonMain, androidMain, iosMain, jvmMain, jsMain)
  - Platform, Greeting, Constants 클래스

### 3. ComposeApp 모듈
- **파일**: `module-composeApp.puml`
- **내용**: Compose Multiplatform UI 모듈
- **주요 항목**:
  - Material3 디자인 시스템
  - Android/Desktop 진입점
  - Compose 의존성

### 4. Server 모듈
- **파일**: `module-server.puml`
- **내용**: Spring Boot 백엔드 서버
- **주요 항목**:
  - Controller, Config 레이어
  - PostgreSQL, Redis, H2 연동
  - Spring Boot 스택

### 5. IosApp 모듈
- **파일**: `module-iosApp.puml`
- **내용**: iOS 네이티브 앱
- **주요 항목**:
  - SwiftUI 구조
  - Shared Framework 링크
  - Xcode 프로젝트

### 6. WebApp 모듈
- **파일**: `module-webApp.puml`
- **내용**: React + TypeScript SPA
- **주요 항목**:
  - React 컴포넌트 구조
  - Vite 빌드 설정
  - Kotlin/JS 통합

## 수정 내역 (2024-12-09)

모든 다이어그램을 순수 컴포넌트 다이어그램 스타일로 수정하여 PlantUML 에러를 해결했습니다:
- `skinparam componentStyle rectangle` 사용
- class 요소와 component 요소 혼용 제거
- 패키지 색상을 배경색 대신 컴포넌트 색상으로 변경
- 구문 에러 수정

## 렌더링 방법

### IntelliJ IDEA / Android Studio
1. **PlantUML 플러그인 설치**
   - Settings → Plugins → "PlantUML integration" 검색 및 설치
2. `.puml` 파일 열기
3. 우클릭 → "Show PlantUML Diagram" 또는 단축키 사용

### VS Code
1. **PlantUML 확장 설치**
   - Extensions → "PlantUML" 검색 및 설치
2. `.puml` 파일 열기
3. `Alt+D` 또는 우클릭 → "Preview Current Diagram"

### 온라인
http://www.plantuml.com/plantuml/uml/ 에서 코드 붙여넣기

### CLI (로컬 렌더링)
```bash
# PlantUML JAR 다운로드
wget https://github.com/plantuml/plantuml/releases/download/v1.2024.0/plantuml-1.2024.0.jar

# PNG 이미지 생성
java -jar plantuml.jar doc/init-architecture.puml

# 모든 다이어그램 생성
java -jar plantuml.jar doc/*.puml

# SVG 생성 (벡터 그래픽)
java -jar plantuml.jar -tsvg doc/*.puml

# PDF 생성
java -jar plantuml.jar -tpdf doc/*.puml
```

### Docker
```bash
# Docker로 PlantUML 실행
docker run -v $(pwd)/doc:/data plantuml/plantuml:latest -tpng /data/*.puml
```

## 색상 범례

- **보라색** (#7F52FF): Kotlin Multiplatform
- **파란색** (#4285F4): Compose Multiplatform
- **초록색** (#6DB33F): Spring Boot
- **청록색** (#61DAFB): React
- **밝은 파란색** (#147EFB): iOS
- **주황색** (#F88909): JVM/Desktop
- **노란색** (#F7DF1E): JavaScript

## 다이어그램 작성 규칙

1. **컴포넌트 다이어그램 스타일 사용**
   ```plantuml
   skinparam componentStyle rectangle
   ```

2. **패키지 색상은 배경색 지정**
   ```plantuml
   package "패키지명" #색상코드 {
   }
   ```

3. **관계 표현**
   - `-->` : 일반 의존성
   - `..>` : 점선 의존성
   - `-down->` : 방향 지정

4. **노트 추가**
   ```plantuml
   note right of 컴포넌트
     설명 내용
   end note
   ```

## 업데이트 가이드

다이어그램을 수정하려면:
1. 해당 `.puml` 파일 편집
2. PlantUML 구문 확인 (위 규칙 준수)
3. 미리보기로 결과 확인
4. 에러 없는지 검증
5. 커밋 및 푸시

## 참고 자료

- [PlantUML 공식 문서](https://plantuml.com/)
- [PlantUML 컴포넌트 다이어그램](https://plantuml.com/component-diagram)
- [PlantUML 클래스 다이어그램](https://plantuml.com/class-diagram)
- [PlantUML 색상 가이드](https://plantuml.com/color)
