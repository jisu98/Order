# Order

카페나 음식점에서 손님이 메뉴를 보고 주문하는 Android 앱

---

## 아키텍처

Clean Architecture + MVVM 기반으로 구성되며, 레이어 의존성 방향은 다음과 같습니다.

```
Presentation → Domain ← Data
```

| 레이어 | 역할 |
|--------|------|
| `presentation/` | Composable Screen, ViewModel, UiState |
| `domain/` | UseCase, Repository 인터페이스, Domain Model |
| `data/` | RepositoryImpl, DTO |
| `di/` | Hilt 모듈 |

---

## 기술 스택

| 역할 | 라이브러리 |
|------|-----------|
| DI | Hilt |
| 비동기 | Coroutines + StateFlow |
| 네트워크 | Retrofit2 + OkHttp3 |
| JSON | Gson |
| 이미지 로딩 | Coil |
| 테스트 | JUnit4 + MockK + kotlinx-coroutines-test |
| 커버리지 | JaCoCo |

---

## 화면 구성

### 메뉴 목록 화면

- 카테고리 탭 (전체 / 음료 / 푸드 / 디저트)
- 메뉴 목록 표시 (메뉴명, 가격, 품절 여부)
- 품절 메뉴는 선택 불가 표시
- 메뉴 클릭 시 장바구니에 추가
- 하단에 장바구니 요약 표시 (담은 수량, 총 금액, 주문하기 버튼)

### 주문 확인 화면

- 장바구니 목록 표시 (메뉴명, 수량, 금액)
- 수량 조절 버튼 (+ / -)
- 수량이 0이 되면 목록에서 제거
- 총 금액 표시
- 주문 완료 버튼 클릭 시 목록 초기화 후 이전 화면으로 이동

---

## 빌드 및 실행

```bash
# 디버그 빌드
./gradlew assembleDebug

# 기기에 설치 및 실행
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.jisu98.order/.MainActivity
```

---

## 테스트

```bash
# 유닛 테스트 실행
./gradlew :app:testDebugUnitTest

# 커버리지 리포트 생성
./gradlew :app:jacocoTestReport
```

커버리지 리포트 경로:

```
app/build/reports/jacoco/jacocoTestReport/html/index.html
```

### 커버리지 대상

| 레이어 | 대상 |
|--------|------|
| Domain | UseCase 전체 |
| Data | Repository 구현체 전체 |
| Presentation | ViewModel 전체 |
