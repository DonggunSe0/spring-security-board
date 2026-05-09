# Chapter 2. 프론트엔드와 백엔드 연결하기

## 1. 프론트와 백엔드는 서로 다른 서버다

현재 프로젝트는 프론트와 백엔드가 따로 실행된다.

```text
프론트엔드: http://localhost:3000
백엔드:     http://localhost:8080
```

Next.js는 화면을 담당하고, Spring Boot는 API 응답을 담당한다.

```text
Next.js 화면
 → Spring Boot API 호출
 → 응답을 받아 화면에 출력
```

---

## 2. `/api/v1/health`를 만든 이유

`/api/v1/health`는 백엔드 서버가 정상적으로 응답하는지 확인하기 위한 테스트 API다.

```text
GET /api/v1/health
→ OK
```

회원가입, 로그인, 게시글 기능을 만들기 전에 먼저 간단한 API로 연결 상태를 확인하면 좋다.

```text
서버 실행 확인
Controller 동작 확인
URL 매핑 확인
프론트-백엔드 연결 확인
```

---

## 3. Spring Security 때문에 401이 발생할 수 있다

처음 `/api/v1/health` 요청을 보냈을 때 `401 Unauthorized`가 발생했다.

이유는 Spring Security가 요청을 먼저 검사하고, 인증되지 않은 요청을 막았기 때문이다.

```text
요청
 → Spring Security 검사
 → 로그인 정보 없음
 → 401 Unauthorized
```

해결 방법은 `SecurityConfig`에서 `/api/v1/health`를 인증 없이 접근 가능하게 열어주는 것이다.

```text
/api/v1/health → permitAll
```

---

## 4. SecurityConfig의 역할

`SecurityConfig`는 Spring Security의 보안 규칙을 설정하는 파일이다.

여기서 다음과 같은 내용을 정한다.

```text
어떤 API는 누구나 접근 가능한지
어떤 API는 로그인해야 접근 가능한지
CSRF를 사용할지
기본 로그인 화면을 사용할지
HTTP Basic 인증을 사용할지
CORS를 허용할지
```

현재는 초기 개발 단계이므로 대부분의 요청을 열어두고, 나중에 JWT 인증을 붙이면서 점점 잠글 예정이다.

---

## 5. CSRF는 초기 개발 단계에서 잠시 꺼둔다

CSRF는 쿠키가 자동으로 전송되는 특성을 악용하는 공격이다.

하지만 지금은 회원가입, 로그인, CRUD 흐름을 먼저 익히는 단계이므로 CSRF 설정을 잠시 비활성화했다.

```text
초기 개발 단계
→ CSRF disable

JWT/Cookie 인증 이해 후
→ CSRF 개념 다시 정리

실무 수준
→ SameSite, Secure, HttpOnly, CSRF Token 고려
```

---

## 6. CORS 에러가 발생할 수 있다

프론트에서 백엔드 API를 호출했을 때 CORS 에러가 발생했다.

에러 내용은 대략 이런 의미였다.

```text
localhost:3000에서 localhost:8080으로 요청했는데,
백엔드가 이 출처를 허용하지 않아서 브라우저가 막았다.
```

브라우저는 포트가 다르면 다른 출처로 판단한다.

```text
http://localhost:3000
http://localhost:8080
```

둘은 포트가 다르기 때문에 서로 다른 Origin이다.

---

## 7. CORS는 백엔드에서 허용해줘야 한다

CORS 문제는 프론트 코드 문제가 아니라, 백엔드에서 허용 설정을 해줘야 해결된다.

현재는 Spring Security 설정 안에서 다음 내용을 추가했다.

```text
localhost:3000에서 오는 요청 허용
GET, POST, PUT, PATCH, DELETE, OPTIONS 허용
모든 요청 헤더 허용
쿠키 포함 요청 허용
```

즉, 백엔드가 프론트 개발 서버의 요청을 받아들이도록 설정했다.

---

## 8. `.env.local`은 프론트 환경 변수 파일이다

프론트에서 백엔드 주소를 직접 코드에 쓰지 않고 `.env.local`에 따로 저장했다.

```env
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080
```

이렇게 하면 나중에 백엔드 주소가 바뀌어도 코드 전체를 수정하지 않고 환경 변수만 바꾸면 된다.

```text
개발 환경: http://localhost:8080
배포 환경: https://api.example.com
```

---

## 9. Next.js에서 브라우저가 읽을 환경 변수는 `NEXT_PUBLIC_`이 필요하다

Next.js에서 클라이언트 코드가 환경 변수를 읽으려면 이름이 `NEXT_PUBLIC_`으로 시작해야 한다.

```env
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080
```

이렇게 해야 `page.tsx` 같은 브라우저 코드에서 사용할 수 있다.

---

## 10. `lib/api.ts`는 API 호출 코드를 모아두는 파일이다

API 호출 코드를 페이지마다 직접 작성하면 중복이 많아진다.

그래서 `lib/api.ts`에 공통 API 요청 함수를 만들었다.

```text
page.tsx
 → apiGet("/api/v1/health")
 → lib/api.ts
 → fetch로 백엔드 호출
```

이렇게 하면 나중에 로그인, 게시글, 댓글 API를 만들 때도 재사용하기 좋다.

---

## 11. `.ts`와 `.tsx`의 차이

`api.ts`는 TypeScript 파일이다.

```text
.ts  = 일반 TypeScript 파일
.tsx = React JSX를 포함하는 TypeScript 파일
```

따라서 API 함수만 있는 파일은 `.ts`를 사용한다.

```text
lib/api.ts
```

React 화면 컴포넌트는 JSX를 사용하므로 `.tsx`를 사용한다.

```text
app/page.tsx
```

---

## 12. `page.tsx`에서는 백엔드 API를 호출해서 화면에 보여준다

기존 Next.js 기본 화면 코드를 지우고, 백엔드 연결 상태를 보여주는 화면으로 바꿨다.

흐름은 다음과 같다.

```text
localhost:3000 접속
 → page.tsx 실행
 → /api/v1/health 호출
 → 백엔드에서 OK 응답
 → 화면에 OK 표시
```

최종적으로 프론트 화면에서 `OK`가 보이면 연결 성공이다.

---

## 13. `use client`가 필요한 이유

Next.js App Router에서는 기본적으로 컴포넌트가 서버 컴포넌트다.

하지만 `useState`, `useEffect` 같은 React Hook을 사용하려면 클라이언트 컴포넌트여야 한다.

그래서 `page.tsx` 맨 위에 다음을 붙였다.

```text
"use client";
```

의미는 다음과 같다.

```text
이 컴포넌트는 브라우저에서 동작하는 클라이언트 컴포넌트다.
```

---

## 14. 환경 변수 수정 후에는 프론트 서버를 재시작해야 한다

`.env.local`을 새로 만들거나 수정했다면 Next.js 서버를 다시 실행해야 한다.

```bash
npm run dev
```

환경 변수는 서버 시작 시점에 읽히기 때문에, 재시작하지 않으면 변경 내용이 반영되지 않을 수 있다.

---

## 15. 이번 단계에서 최종적으로 완성된 흐름

```text
브라우저
 → localhost:3000 접속
 → Next.js page.tsx 실행
 → lib/api.ts의 apiGet 호출
 → http://localhost:8080/api/v1/health 요청
 → Spring Boot HealthController 실행
 → OK 응답
 → Next.js 화면에 OK 출력
```

---

## 핵심 정리

```text
프론트와 백엔드는 서로 다른 서버다.

Next.js는 화면을 담당하고,
Spring Boot는 API를 담당한다.

포트가 다르면 브라우저는 다른 출처로 보기 때문에 CORS 설정이 필요하다.

Spring Security가 요청을 막으면 401이 발생할 수 있다.

SecurityConfig에서 health API를 permitAll로 열어주고,
CORS에서 localhost:3000을 허용해야 프론트에서 백엔드 API를 호출할 수 있다.

.env.local은 백엔드 주소를 관리하는 환경 변수 파일이다.

lib/api.ts는 API 호출 코드를 모아두는 공통 파일이다.

프론트 화면에서 OK가 나오면 프론트-백엔드 연결 성공이다.
```