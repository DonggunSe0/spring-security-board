# JWT 핵심 개념 정리

## 1. JWT란?

JWT는 로그인한 사용자를 확인하기 위해 서버가 발급하는 토큰이다.

```text
JWT = JSON Web Token
```

로그인에 성공하면 서버는 JWT를 만들어 브라우저에 전달한다.  
브라우저는 이후 요청마다 JWT를 함께 보내고, 서버는 JWT를 확인해서 현재 사용자를 판단한다.

```text
로그인 성공
→ 서버가 JWT 발급
→ 브라우저가 JWT 저장
→ 요청할 때 JWT 전송
→ 서버가 JWT 검증
→ 현재 사용자 확인
```

---

## 2. JWT를 사용하는 이유

JWT를 사용하면 서버가 매번 로그인 상태를 직접 저장하지 않아도 된다.

```text
세션 방식
- 서버가 로그인 상태를 저장한다.

JWT 방식
- 서버는 토큰을 검증해서 사용자를 확인한다.
```

이 프로젝트에서는 게시글 작성, 댓글 작성, 현재 사용자 조회 등에 JWT를 사용할 예정이다.

---

## 3. JWT 구조

JWT는 보통 3부분으로 구성된다.

```text
Header.Payload.Signature
```

각 부분의 역할은 다음과 같다.

```text
Header
- 토큰 타입
- 서명 알고리즘 정보

Payload
- 사용자 정보
- 예: memberId, email, role, 만료 시간

Signature
- 토큰이 위조되지 않았는지 확인하는 서명
```

---

## 4. JWT에 담을 수 있는 정보

이 프로젝트에서는 JWT에 이런 정보를 담을 수 있다.

```text
memberId
email
role
만료 시간
```

단, 민감한 정보는 절대 넣으면 안 된다.

```text
JWT에 넣으면 안 되는 것
- 비밀번호
- 주민번호
- 카드번호
- 민감한 개인정보
```

JWT는 암호화된 저장소가 아니라 검증 가능한 토큰이기 때문에, 중요한 개인정보를 넣으면 위험하다.

---

## 5. JWT 인증 흐름

```text
1. 사용자가 로그인 요청
2. 서버가 이메일과 비밀번호 확인
3. 로그인 성공 시 JWT 생성
4. 서버가 JWT를 브라우저에 전달
5. 브라우저가 JWT 저장
6. 이후 요청마다 JWT 전송
7. 서버가 JWT 검증
8. 서버가 현재 사용자 확인
```

---

## 6. 쿠키 + JWT 방식

이 프로젝트에서는 JWT를 쿠키에 저장하는 방식을 사용할 예정이다.

```text
Set-Cookie: accessToken=JWT값
```

이후 브라우저는 요청할 때 쿠키를 자동으로 함께 보낸다.

```text
GET /api/v1/members/me
Cookie: accessToken=JWT값
```

서버는 쿠키 안의 JWT를 확인해서 현재 로그인 사용자를 찾는다.

---

## 7. Authorization Header 방식과 Cookie 방식 차이

JWT를 보내는 대표적인 방법은 두 가지다.

```text
1. Authorization Header 방식
2. Cookie 방식
```

### Authorization Header 방식

```text
Authorization: Bearer JWT값
```

특징:

```text
- 프론트가 직접 토큰을 헤더에 넣어 보낸다.
- localStorage 등에 저장하는 경우가 많다.
- CSRF 위험은 상대적으로 낮다.
- XSS 공격에 주의해야 한다.
```

### Cookie 방식

```text
Cookie: accessToken=JWT값
```

특징:

```text
- 브라우저가 쿠키를 자동으로 보낸다.
- HttpOnly 설정을 사용할 수 있다.
- JavaScript에서 토큰을 직접 읽지 못하게 할 수 있다.
- CSRF 공격을 고려해야 한다.
```

---

## 8. HttpOnly Cookie

`HttpOnly`는 JavaScript에서 쿠키를 읽지 못하게 하는 설정이다.

```text
HttpOnly=true
```

이 설정을 사용하면 아래 코드로 쿠키를 읽을 수 없다.

```text
document.cookie
```

JWT를 쿠키에 저장할 때는 보안을 위해 `HttpOnly` 설정을 자주 사용한다.

```text
JWT + HttpOnly Cookie
→ JavaScript로 토큰 접근 불가
→ XSS로 토큰이 탈취될 위험 감소
```

---

## 9. JWT와 CSRF

쿠키 방식은 브라우저가 쿠키를 자동으로 전송한다.

이 특성 때문에 CSRF 공격을 고려해야 한다.

```text
쿠키 자동 전송
→ 원하지 않는 요청에도 쿠키가 함께 갈 수 있음
→ 서버가 정상 요청으로 착각할 수 있음
```

그래서 실무에서는 다음 설정들을 함께 고려한다.

```text
SameSite 쿠키 설정
Secure 쿠키 설정
HttpOnly 쿠키 설정
CSRF Token
CORS 제한
```

현재 학습 초기 단계에서는 회원가입, 로그인, CRUD 흐름을 먼저 익히기 위해 CSRF를 잠시 비활성화한다.

```text
초기 개발 단계
→ csrf disable

JWT/Cookie 인증 이해 후
→ CSRF 다시 정리

실무 수준
→ SameSite, Secure, HttpOnly, CSRF Token 고려
```

---

## 10. JWT 인증 필터가 필요한 이유

로그인 후 사용자가 요청할 때마다 서버는 JWT를 확인해야 한다.

예를 들어 다음 API들은 현재 사용자를 알아야 한다.

```text
GET /api/v1/members/me
POST /api/v1/posts
PATCH /api/v1/posts/{id}
DELETE /api/v1/comments/{id}
```

이때 매 Controller마다 JWT를 직접 검사하면 코드가 반복된다.

그래서 JWT 인증 필터를 만든다.

```text
요청
→ JWT 인증 필터
→ JWT 추출
→ JWT 검증
→ 현재 사용자 정보 저장
→ Controller 실행
```

---

## 11. SecurityContext란?

`SecurityContext`는 Spring Security가 현재 요청의 인증 정보를 저장하는 공간이다.

JWT 검증이 성공하면 현재 사용자 정보가 `SecurityContext`에 저장된다.

```text
JWT 검증 성공
→ 현재 사용자 정보 생성
→ SecurityContext에 저장
→ Controller나 Service에서 현재 사용자 사용 가능
```

예를 들어 게시글 작성 시 현재 사용자를 작성자로 저장할 수 있다.

---

## 12. JWT와 권한 제어

JWT는 현재 사용자가 누구인지 확인하는 데 사용된다.

하지만 로그인했다고 모든 작업을 할 수 있는 것은 아니다.

```text
인증
= 현재 사용자가 누구인지 확인

인가
= 현재 사용자가 이 작업을 할 권한이 있는지 확인
```

예를 들어 게시글 수정은 작성자만 가능해야 한다.

```text
JWT로 현재 사용자 확인
→ 게시글 작성자 확인
→ 현재 사용자와 작성자가 같으면 수정 허용
→ 다르면 403 Forbidden
```

---

## 13. 이 프로젝트에서 JWT 관련 파일 위치

JWT 관련 코드는 `global/security` 아래에 둘 예정이다.

```text
global/security/
 ├── SecurityConfig.java
 ├── jwt/
 │   ├── JwtProvider.java
 │   └── JwtAuthenticationFilter.java
 └── auth/
     └── CustomUserDetails.java
```

각 파일의 역할은 다음과 같다.

```text
SecurityConfig
- 어떤 API를 열고 막을지 설정
- JWT 인증 필터 등록

JwtProvider
- JWT 생성
- JWT 검증
- JWT에서 사용자 정보 추출

JwtAuthenticationFilter
- 요청에서 JWT 추출
- JWT 검증
- SecurityContext에 인증 정보 저장

CustomUserDetails
- Spring Security에서 사용할 사용자 정보
```

---

## 14. JWT가 적용된 최종 로그인 흐름

```text
1. 사용자가 로그인 요청
2. AuthController가 요청을 받음
3. AuthService가 이메일과 비밀번호 검증
4. JwtProvider가 JWT 생성
5. 서버가 JWT를 HttpOnly Cookie에 담아 응답
6. 브라우저가 쿠키 저장
7. 이후 요청마다 쿠키 자동 전송
8. JwtAuthenticationFilter가 쿠키에서 JWT 추출
9. JwtProvider가 JWT 검증
10. SecurityContext에 현재 사용자 저장
11. Controller에서 현재 사용자 기준으로 기능 처리
```

---

## 15. JWT가 적용된 게시글 작성 흐름

```text
사용자 게시글 작성 요청
→ 쿠키에 JWT 자동 포함
→ JwtAuthenticationFilter가 JWT 검증
→ 현재 사용자 확인
→ PostService에서 작성자를 현재 사용자로 설정
→ DB에 게시글 저장
```

---

## 16. JWT가 적용된 게시글 수정/삭제 흐름

```text
사용자 게시글 수정/삭제 요청
→ JWT로 현재 사용자 확인
→ 게시글 조회
→ 게시글 작성자와 현재 사용자 비교
→ 같으면 수정/삭제
→ 다르면 403 Forbidden
```

---

## 핵심 요약

```text
JWT
= 로그인한 사용자를 확인하기 위한 토큰

JWT 역할
= 현재 사용자가 누구인지 서버가 확인하게 해줌

JWT 저장 위치
= 이 프로젝트에서는 쿠키에 저장할 예정

JWT 생성/검증 담당
= JwtProvider

JWT 요청 검증 담당
= JwtAuthenticationFilter

현재 사용자 저장 위치
= SecurityContext

JWT와 인증
= 현재 사용자가 누구인지 확인

JWT와 인가
= 현재 사용자가 해당 작업을 할 권한이 있는지 확인

주의할 점
= JWT에 비밀번호 같은 민감 정보는 넣지 않는다.
= 쿠키 방식에서는 CSRF를 고려해야 한다.
= 실무에서는 HttpOnly, Secure, SameSite, CSRF Token을 함께 고려한다.
```