# 프로젝트 구조 정리

## 목표 구조

```text
spring-security-board/
 ├── backend/    # Spring Boot 백엔드
 └── frontend/   # Next.js 프론트엔드
```

## 왜 나눌까?

백엔드와 프론트엔드는 역할이 다르기 때문이다.

```text
backend
- API 처리
- DB 저장
- 로그인/JWT 처리
- 게시글/댓글 CRUD
- 권한 검사
```

```text
frontend
- 화면 구성
- 사용자 입력 처리
- 백엔드 API 호출
- 로그인 상태 표시
```

## 나누면 좋은 점

```text
1. 역할이 명확해진다.
2. 에러가 났을 때 원인을 찾기 쉽다.
3. 백엔드와 프론트 파일이 섞이지 않는다.
4. GitHub에 올렸을 때 구조가 깔끔하다.
```

## 실행 방식도 다르다

```bash
# 백엔드 실행
cd backend
./gradlew bootRun
```

```bash
# 프론트 실행
cd frontend
npm run dev
```

## 현재 프로젝트 정리 방법

현재 Spring Boot 프로젝트가 바로 `spring-security-board`에 있다면, `backend` 폴더로 옮긴다.

```bash
cd ~/devCourse

mv spring-security-board backend
mkdir spring-security-board
mv backend spring-security-board/
```

정리 후 구조:

```text
spring-security-board/
 └── backend/
```

나중에 프론트 생성:

```bash
cd ~/devCourse/spring-security-board
npx create-next-app@latest frontend
```

최종 구조:

```text
spring-security-board/
 ├── backend/
 └── frontend/
```

## 핵심 정리

```text
backend  = 데이터, API, 인증, 권한 처리
frontend = 화면, 사용자 입력, API 호출
```

---

# 14단계 최종 프로젝트 구조 설계

## 이 프로젝트의 최종 목표

이 프로젝트는 단순히 화면만 만드는 것이 아니라,  
로그인 기반 게시판 CRUD 흐름을 백엔드와 프론트엔드로 나누어 직접 구현하는 학습용 풀스택 프로젝트다.

최종적으로 구현할 기능은 다음과 같다.

```text
회원가입
로그인
로그아웃
현재 로그인 사용자 조회
게시글 목록 조회
게시글 상세 조회
게시글 작성
게시글 수정
게시글 삭제
댓글 목록 조회
댓글 작성
댓글 수정
댓글 삭제
작성자 권한 제어
예외 처리
입력값 검증
초기 데이터 생성
기본 테스트
```

---

## 최종 프로젝트 전체 구조

```text
spring-security-board/
 ├── backend/
 │   ├── src/main/java/com/example/springsecurityboard/
 │   │   ├── SpringSecurityBoardApplication.java
 │   │   │
 │   │   ├── domain/
 │   │   │   ├── member/
 │   │   │   ├── auth/
 │   │   │   ├── post/
 │   │   │   └── comment/
 │   │   │
 │   │   └── global/
 │   │       ├── security/
 │   │       ├── exception/
 │   │       ├── response/
 │   │       ├── config/
 │   │       └── health/
 │   │
 │   ├── src/main/resources/
 │   │   └── application.yml
 │   │
 │   ├── src/test/
 │   ├── build.gradle
 │   └── settings.gradle
 │
 └── frontend/
     ├── app/
     │   ├── page.tsx
     │   ├── login/
     │   ├── signup/
     │   └── posts/
     │       ├── page.tsx
     │       ├── new/
     │       └── [id]/
     │
     ├── components/
     ├── lib/
     ├── types/
     ├── package.json
     └── tsconfig.json
```

---

# 백엔드 설계 관점

## backend의 역할

백엔드는 화면 뒤에서 실제 데이터를 처리하는 서버다.

```text
API 요청 받기
DB 저장/조회/수정/삭제
회원가입 처리
로그인 검증
JWT 발급
현재 사용자 인증
작성자 권한 검사
예외 처리
입력값 검증
```

즉, 백엔드는 이 프로젝트의 핵심 로직을 담당한다.

---

## 백엔드 패키지 구조

```text
backend/src/main/java/com/example/springsecurityboard/
 ├── domain/
 └── global/
```

크게 `domain`과 `global`로 나눈다.

---

## domain

`domain`은 실제 서비스 기능을 담당하는 영역이다.

```text
domain/
 ├── member/
 ├── auth/
 ├── post/
 └── comment/
```

각 폴더는 하나의 기능 단위다.

```text
member  = 회원 정보
auth    = 로그인/로그아웃/JWT 인증
post    = 게시글
comment = 댓글
```

---

## member 구조

```text
domain/member/
 ├── entity/
 ├── repository/
 ├── service/
 ├── controller/
 └── dto/
```

역할:

```text
entity      = DB 테이블과 연결되는 회원 객체
repository  = 회원 DB 접근
service     = 회원 관련 비즈니스 로직
controller  = 회원 API 요청 처리
dto         = 요청/응답 데이터 객체
```

---

## auth 구조

```text
domain/auth/
 ├── controller/
 ├── service/
 └── dto/
```

역할:

```text
로그인
로그아웃
JWT 발급
쿠키 생성/삭제
```

`auth`는 회원 정보를 직접 저장하기보다는,  
로그인과 인증 흐름을 담당한다.

---

## post 구조

```text
domain/post/
 ├── entity/
 ├── repository/
 ├── service/
 ├── controller/
 └── dto/
```

역할:

```text
게시글 목록 조회
게시글 상세 조회
게시글 작성
게시글 수정
게시글 삭제
작성자 권한 확인
```

게시글은 작성자인 `Member`와 연결된다.

```text
Member 1명 → Post 여러 개 작성 가능
Post 1개 → 작성자 Member 1명
```

---

## comment 구조

```text
domain/comment/
 ├── entity/
 ├── repository/
 ├── service/
 ├── controller/
 └── dto/
```

역할:

```text
댓글 목록 조회
댓글 작성
댓글 수정
댓글 삭제
댓글 작성자 권한 확인
```

댓글은 게시글과 회원 둘 다 연결된다.

```text
Post 1개 → Comment 여러 개
Member 1명 → Comment 여러 개
```

---

## global

`global`은 특정 기능 하나에 속하지 않고, 프로젝트 전체에서 공통으로 사용하는 영역이다.

```text
global/
 ├── security/
 ├── exception/
 ├── response/
 ├── config/
 └── health/
```

역할:

```text
security  = Spring Security, JWT, 인증 필터
exception = 전역 예외 처리
response  = 공통 응답 형식
config    = CORS, 설정 클래스
health    = 서버 상태 확인 API
```

---

# 프론트엔드 설계 관점

## frontend의 역할

프론트엔드는 사용자가 직접 보는 화면을 담당한다.

```text
회원가입 화면
로그인 화면
게시글 목록 화면
게시글 상세 화면
게시글 작성/수정 화면
댓글 입력/수정 화면
로그인 상태 표시
백엔드 API 호출
```

즉, 프론트엔드는 사용자의 입력을 받고, 백엔드 API를 호출한 뒤 결과를 화면에 보여준다.

---

## 프론트 기본 구조

```text
frontend/
 ├── app/
 ├── components/
 ├── lib/
 └── types/
```

---

## app

`app`은 Next.js App Router에서 페이지를 담당한다.

```text
app/
 ├── page.tsx
 ├── login/page.tsx
 ├── signup/page.tsx
 ├── posts/page.tsx
 ├── posts/new/page.tsx
 └── posts/[id]/page.tsx
```

역할:

```text
/               = 메인 페이지
/login          = 로그인 페이지
/signup         = 회원가입 페이지
/posts          = 게시글 목록 페이지
/posts/new      = 게시글 작성 페이지
/posts/[id]     = 게시글 상세 페이지
```

---

## components

`components`는 여러 페이지에서 재사용할 UI를 모아두는 곳이다.

```text
components/
 ├── Header.tsx
 ├── PostList.tsx
 ├── PostForm.tsx
 ├── CommentList.tsx
 └── CommentForm.tsx
```

예를 들어 `Header`는 여러 페이지에서 공통으로 사용할 수 있다.

```text
로그인 상태 표시
로그아웃 버튼
게시글 목록 이동
```

---

## lib

`lib`는 API 호출 함수나 공통 유틸을 모아두는 곳이다.

```text
lib/
 ├── api.ts
 ├── authApi.ts
 ├── postApi.ts
 └── commentApi.ts
```

역할:

```text
api.ts      = 공통 fetch 함수
authApi.ts  = 로그인/로그아웃/현재 사용자 조회 API
postApi.ts  = 게시글 API
commentApi.ts = 댓글 API
```

API 호출 코드를 한 곳에 모아두면, 페이지 코드가 깔끔해지고 유지보수하기 좋아진다.

---

## types

`types`는 백엔드 응답 데이터 타입을 정의하는 곳이다.

```text
types/
 ├── member.ts
 ├── post.ts
 └── comment.ts
```

예를 들어 게시글 타입은 다음과 같은 형태가 될 수 있다.

```text
Post
- id
- title
- content
- authorNickname
- createdAt
- updatedAt
```

타입을 분리하면 프론트에서 API 응답을 다룰 때 실수를 줄일 수 있다.

---

# 최종 요청 흐름

## 회원가입 흐름

```text
사용자
 → Next.js 회원가입 화면
 → Spring Boot 회원가입 API
 → 입력값 검증
 → 비밀번호 암호화
 → Member 저장
 → 회원가입 성공 응답
```

---

## 로그인 흐름

```text
사용자
 → Next.js 로그인 화면
 → Spring Boot 로그인 API
 → 이메일/비밀번호 검증
 → JWT 생성
 → 쿠키에 JWT 저장
 → 로그인 성공 응답
```

---

## 현재 사용자 조회 흐름

```text
브라우저
 → /api/v1/members/me 요청
 → 쿠키에 담긴 JWT 전송
 → Spring Security 인증 필터
 → 현재 사용자 식별
 → 사용자 정보 응답
```

---

## 게시글 작성 흐름

```text
사용자
 → 게시글 작성 화면
 → 게시글 작성 API 요청
 → JWT로 현재 사용자 확인
 → 작성자를 현재 사용자로 지정
 → Post 저장
 → 작성 성공 응답
```

---

## 게시글 수정/삭제 흐름

```text
사용자
 → 게시글 수정/삭제 요청
 → JWT로 현재 사용자 확인
 → 게시글 작성자와 현재 사용자 비교
 → 같으면 수정/삭제
 → 다르면 403 Forbidden
```

---

## 댓글 작성 흐름

```text
사용자
 → 댓글 작성 요청
 → JWT로 현재 사용자 확인
 → 게시글 조회
 → Comment 저장
 → 댓글 목록 갱신
```

---

# 설계적으로 중요한 점

## 1. Controller, Service, Repository를 나누는 이유

```text
Controller  = 요청과 응답 담당
Service     = 비즈니스 로직 담당
Repository  = DB 접근 담당
```

이렇게 나누면 각 클래스의 책임이 명확해진다.

좋은 점:

```text
코드 읽기 쉬움
테스트하기 쉬움
기능 수정 시 영향 범위가 줄어듦
유지보수하기 좋음
```

---

## 2. Entity와 DTO를 나누는 이유

```text
Entity = DB와 연결되는 내부 객체
DTO    = 외부 요청/응답에 사용하는 객체
```

엔티티를 그대로 API 응답으로 내보내면 구조가 쉽게 깨질 수 있다.

그래서 요청과 응답은 DTO로 따로 관리한다.

```text
요청 DTO → Controller → Service → Entity 저장
Entity 조회 → Response DTO 변환 → 응답
```

---

## 3. 인증과 권한을 나누는 이유

```text
인증 = 너 누구야?
인가 = 너 이 작업 할 권한 있어?
```

예시:

```text
로그인 여부 확인 = 인증
작성자만 수정/삭제 가능 = 인가
```

로그인했다고 모든 글을 수정할 수 있으면 안 된다.  
그래서 작성자 권한 검사가 반드시 필요하다.

---

## 4. 공통 응답/예외 구조를 만드는 이유

API마다 응답 형식이 다르면 프론트에서 처리하기 어렵다.

그래서 응답 형식을 통일한다.

```text
성공 응답
- success
- message
- data

실패 응답
- success
- message
- errorCode
```

이렇게 하면 프론트에서 성공/실패 처리를 일관되게 할 수 있다.

---

## 5. Health API를 먼저 만든 이유

`/api/v1/health`는 서버 연결 확인용 API다.

```text
GET /api/v1/health
→ OK
```

복잡한 기능을 만들기 전에 먼저 서버와 프론트 연결이 되는지 확인하기 위해 만든다.

이 API가 성공하면 다음을 확인할 수 있다.

```text
Spring Boot 실행 정상
Controller 동작 정상
Security 설정 정상
CORS 설정 정상
Next.js에서 백엔드 호출 정상
```

---

# 14단계 로드맵

```text
1. 프로젝트 골격 만들기
2. 개발 환경 연결
3. DB와 JPA 세팅
4. 공통 응답/예외 구조 만들기
5. 회원 엔티티 설계
6. 회원가입 구현
7. 인증 방식 설계
8. 로그인/로그아웃 구현
9. 인증 필터와 현재 사용자 조회 구현
10. 게시글 엔티티와 게시글 CRUD API 구현
11. 게시글 권한 제어 추가
12. 댓글 엔티티와 댓글 CRUD API 구현
13. 프론트 UI 구현 및 API 연동
14. 초기 데이터, 테스트, 마무리 점검
```

---

# 현재 단계

현재까지 완료한 단계는 다음과 같다.

```text
1단계. 프로젝트 골격 만들기 완료
2단계. 개발 환경 연결 완료
```

현재 완성된 흐름:

```text
Next.js localhost:3000
 → Spring Boot localhost:8080/api/v1/health
 → OK 응답
 → 프론트 화면에 OK 표시
```

---

# 최종 핵심 정리

```text
이 프로젝트는 backend와 frontend를 분리한 로그인 기반 게시판 풀스택 앱이다.

backend는 API, DB, 인증, 권한, 예외 처리를 담당한다.
frontend는 화면, 사용자 입력, API 호출, 로그인 상태 표시를 담당한다.

최종적으로 회원가입, 로그인, JWT 인증, 게시글 CRUD, 댓글 CRUD, 작성자 권한 제어까지 구현한다.

설계의 핵심은 역할 분리다.

backend/frontend 분리
domain/global 분리
controller/service/repository 분리
entity/dto 분리
인증/인가 분리
```