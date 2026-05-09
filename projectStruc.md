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