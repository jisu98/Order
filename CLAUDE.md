# 메뉴 주문 앱 프로젝트 컨벤션

## 작업 방식

- 한 번에 하나의 커밋 단위로만 작업한다
- 작업 전 반드시 범위를 먼저 설명하고 확인을 받는다
- 명시적으로 범위를 지정하지 않으면 임의로 다음 작업을 진행하지 않는다
- 작업이 완료되면 아래 형식으로 커밋 컨벤션이 맞는 커밋 메시지를 추천한다
- 작업 내용이 이 파일의 규칙에 위배되는 경우 즉시 아래 형식으로 경고한다

```
  ⚠️CLAUDE.md 위반: <위반한 규칙> ⚠️
  이유: <왜 위반인지>
  권장 방향: <어떻게 수정해야 하는지>
```

---

## 핵심 비즈니스 로직

```
// 메뉴 조회
GetMenusUseCase(category):
  menus = menuRepository.getMenus()
  if category == 전체: return menus
  return menus.filter { it.category == category }

// 장바구니 조회
GetCartUseCase():
  return cartRepository.getCart()  // Flow<Cart>

// 장바구니 추가
AddToCartUseCase(menu):
  if menu.isSoldOut: return  // 품절 메뉴는 추가 불가
  cartRepository.add(menu)

// 장바구니 수량 변경
UpdateCartItemUseCase(menuId, delta):
  newQty = cartRepository.getQuantity(menuId) + delta
  if newQty <= 0: cartRepository.remove(menuId)
  else: cartRepository.updateQuantity(menuId, newQty)

// 주문 완료
PlaceOrderUseCase():
  cartRepository.clear()
  // 화면 이동은 Presentation 레이어에서 처리
```

---

## 프로젝트 개요

카페나 음식점에서 손님이 메뉴를 보고 주문하는 앱

---

## 아키텍처

Clean Architecture + MVVM을 기반으로 한다
레이어 의존성 방향: Presentation → Domain ← Data

### 레이어 구조

```
presentation/   UI 레이어 — Composable Screen, ViewModel, UiState
domain/         비즈니스 레이어 — UseCase, Repository 인터페이스, Domain Model
data/           데이터 레이어 — RepositoryImpl, DTO
di/             Hilt 모듈
```

---

## 기술 스택

| 역할     | 라이브러리                  |
|--------|------------------------|
| DI     | Hilt                   |
| 비동기    | Coroutines + StateFlow |
| 네트워크   | Retrofit2 + OkHttp3    |
| JSON   | Gson                   |
| 이미지 로딩 | Coil                   |

---

## 기술 요구사항

* 로컬 더미 데이터
* 장바구니 상태는 두 화면에서 실시간 공유
* 품절 메뉴는 장바구니 추가 불가

---

## 화면 구성

### 메뉴 목록 화면

* 카테고리 탭 (전체 / 음료 / 푸드 / 디저트)
* 메뉴 목록 표시 (메뉴명, 가격, 품절 여부)
* 품절 메뉴는 선택 불가 표시
* 메뉴 클릭 시 장바구니에 추가
* 하단에 장바구니 요약 표시 (담은 수량, 총 금액, 주문하기 버튼)

### 주문 확인 화면

* 장바구니 목록 표시 (메뉴명, 수량, 금액)
* 수량 조절 버튼 (+ / -)
* 수량이 0이 되면 목록에서 제거
* 총 금액 표시
* 주문 완료 버튼 클릭 시 목록 초기화 후 이전 화면으로 이동

---

## 파일 구조

```
app/src/main/java/com/jisu98/order/
├── MainActivity.kt
├── OrderApplication.kt
├── di/
│   └── AppModule.kt
├── data/
│   ├── model/
│   │   └── MenuDto.kt
│   └── repository/
│       ├── MenuRepositoryImpl.kt
│       └── CartRepositoryImpl.kt
├── domain/
│   ├── model/
│   │   ├── Menu.kt
│   │   ├── Cart.kt
│   │   ├── CartItem.kt
│   │   └── Category.kt
│   ├── repository/
│   │   ├── MenuRepository.kt
│   │   └── CartRepository.kt
│   └── usecase/
│       ├── GetMenusUseCase.kt
│       ├── GetCartUseCase.kt
│       ├── AddToCartUseCase.kt
│       ├── UpdateCartItemUseCase.kt
│       └── PlaceOrderUseCase.kt
└── presentation/
    ├── menu/
    │   ├── MenuScreen.kt
    │   ├── MenuViewModel.kt
    │   └── MenuUiState.kt
    └── order/
        ├── OrderScreen.kt
        ├── OrderViewModel.kt
        └── OrderUiState.kt
```

---

## 레이어별 규칙

### Presentation

- `MainActivity`는 `@AndroidEntryPoint`, ViewModel은 `@HiltViewModel` 어노테이션 필수
- ViewModel은 UseCase만 주입받는다 (Repository 직접 참조 금지)
- UI 상태는 `StateFlow<UiState>`로 관리
- `UiState`는 sealed class로 `Loading`, `Success`, `Error` 정의

### Domain

- `android.*` import 절대 금지 — JVM 단독 테스트 가능해야 함
- Repository 인터페이스는 이 레이어에 위치 (의존성 역전 원칙)
- UseCase는 `operator fun invoke()`로 호출

### Data

- Repository 구현체는 Domain의 인터페이스를 구현
- DTO → Domain 모델 변환은 Repository 구현체 안에서 처리 (DTO가 Domain으로 노출되지 않도록)

---

## 커밋 컨벤션

형식: `<type>(<scope>): <subject>`

| type       | 용도                                |
|------------|-----------------------------------|
| `feat`     | 새 기능                              |
| `fix`      | 버그 수정                             |
| `chore`    | 빌드 설정, 의존성, 툴링 등 코드 변경 없는 작업      |
| `refactor` | 기능 변경 없는 코드 구조 개선                 |
| `style`    | 포맷팅, trailing comma 등 코드 의미 변경 없음 |
| `test`     | 테스트 추가/수정                         |
| `docs`     | 문서 수정                             |
| `perf`     | 성능 개선                             |

- subject는 소문자로 시작, 마침표 없음
- 현재형 동사로 작성 (`added` ❌ → `add` ✅)
- scope는 레이어나 기능 단위 (`search`, `bookmark`, `domain`, `di` 등)
- 한 커밋은 한 가지 변경만

---

## 코딩 컨벤션

- 언어: Kotlin
- 함수 하나는 한 가지 일만
- 새 기능 추가 시 기존 레이어 규칙을 반드시 준수
- 레이어 경계를 넘는 의존성 추가 시 이 파일 확인 후 진행
- companion object는 맨 아래로 빼기
- class, interface 등에서 첫 행 비우기 금지
- trailing comma 사용

---

## 테스트 - 시간 남을 경우 추가

### JaCoCo 설정

- 유닛 테스트 커버리지는 JaCoCo로 측정한다
- `app/build.gradle.kts`에 JaCoCo 플러그인 적용

### 테스트 대상

- UseCase 단위 테스트 필수
- Repository 구현체 단위 테스트 필수
- ViewModel 단위 테스트 필수
- 테스트 프레임워크: JUnit4 + MockK + kotlinx-coroutines-test

### 테스트 규칙

- 테스트 함수명: `given <state/condition> when <action> then <expected result>` 형식
- 각 테스트는 하나의 동작만 검증