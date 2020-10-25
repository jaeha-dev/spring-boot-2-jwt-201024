# 1. JWT 인증 웹 애플리케이션  
    * 개발 환경: Windows 10 64 bit, IntelliJ IDEA  
    * 기술 스택: Spring Framework(Boot), JPA, Maria DB(10.4.12), Redis(3.2.100)  

    
# 2. 실행 흐름
```
* 계정 등록 시퀀스
```
![Alt text](./Join.png)
```
* 계정 로그인 및 리소스 요청 시퀀스
- Access(유효 상태), Refresh(유효 상태) -> 리소스 응답
- Access(만료 상태), Refresh(유효 상태) -> Access Token 재생성 및 리소스 응답
- Access(만료 상태), Refresh(만료 상태) -> 로그인 요청
```
![Alt text](./Login-Request.png)
```
* 계정 로그아웃 시퀀스
```
![Alt text](./Logout.png)
  
# 3. 패키지 구조  
```bash
*  
├── application  
│   ├── aop: 커스텀 예외 클래스, 공통 예외처리, 핸들러 클래스 등  
│   ├── config: 시큐리티, 메시지 국제화, 레디스 설정 클래스 등  
│   ├── security: JWT 필터 및 시큐리티 관련 클래스  
│   ├── utility: 메시지 국제화, 토큰/쿠키 등 유틸리티 클래스  
│   └── web: 프레젠테이션 계층  
│  
└── domain  
    ├── model: 엔터티 클래스
    ├── payload: 요청 & 응답 모델
    ├── repository: 데이터 액세스 계층
    └── service: 서비스 계층  
*  
├── i18n  
│   ├── error_messages.properties: 에러 메시지 소스  
│   ├── success_messages.properties: 성공 메시지 소스  
│   └── web_messages.properties: 웹 출력용 메시지 소스  
│  
│── static  
│   └── css:
│       └── i18n.css: i18n.html을 위한 스타일 시트  
│  
│── templates  
│   └── i18n.html: Thymeleaf를 활용한 메시지 국제화 활용 예제  
│  
│── application.yml: 애플리케이션 설정 정보  
└── data.sql: 계정 및 계정 권한 데이터 초기화
              (파일에 작성된 해싱된 비밀번호는 test-password)  
```  
  
  
  
# 4. URL, 접근 권한  
```
- /api/user/** 경로는 **Postman**을 사용하여 테스트한다.
- /web/i18n 경로는 **웹 브라우저**에서 접속한다.  
```  
| COMMAND | URL | ROLE |  
| --- | --- | --- |
| 계정 등록 | http://localhost:8000/api/user/join | EMPTY  |  
| 계정 로그인 | http://localhost:8000/api/user/login | EMPTY |  
| 계정 로그아웃 | http://localhost:8000/api/user/logout | AUTHENTICATED |  
| 접근 권한 검사 | http://localhost:8000/api/user/all | EMPTY |  
| 접근 권한 검사 | http://localhost:8000/api/user/authenticated | AUTHENTICATED |  
| 접근 권한 검사 | http://localhost:8000/api/user/user | ROLE_USER |  
| 접근 권한 검사 | http://localhost:8000/api/user/admin | ROLE_ADMIN |  
| 메시지 국제화 검사 | http://localhost:8000/web/i18n | EMPTY |  
| 미인증 경로 | http://localhost:8000/error/entry-point | EMPTY |  
| 비인가 경로 | http://localhost:8000/error/access-denied | EMPTY |  
  
  
  
# 5. Postman 테스트  

`Postman`에서 `POST`을 선택하고 `Body -> Raw -> JSON`를 선택한 후, 요청 값을 입력한다.  
`JWT`는 `Cookie`에 저장하므로 `Header`값에 토큰을 입력하지 않도록 한다.  

`username`은 **5 ~ 15자**로 제한하였다.   
`password`는 **10 ~ 20자**로 제한하였다.  

### [계정 등록] 요청 값
```
http://localhost:8000/api/user/join
```
```json  
{
    "username": "sample",
    "password": "test-password"
}
```  
  
### [계정 등록] 성공 시, 응답 값
```json  
{
    "requestUrl": "http://localhost:8000/api/user/join",
    "requestDate": "2020-10-24T15:22:27.887+00:00",
    "responseStatus": true,
    "responseCode": "2000",
    "responseMessage": "계정이 생성되었습니다.",
    "responseResource": {
        "createdAt": "2020-10-24T15:22:27.822846200Z",
        "updatedAt": "2020-10-24T15:22:27.822846200Z",
        "id": 3,
        "username": "sample",
        "password": "$2a$10$sY8H9.WB/r36qtDlD8vu7OUA56EbNzdFvHa29obaQtu55lcSYnw0C",
        "roles": [
            {
                "id": 1,
                "name": "ROLE_USER"
            }
        ]
    }
}
```  
  
### [계정 등록] 실패 시, 응답 값 (중복된 계정 ID)
```json  
{
    "requestUrl": "http://localhost:8000/api/user/join",
    "requestDate": "2020-10-24T15:23:43.292+00:00",
    "responseStatus": false,
    "responseCode": "-2003",
    "responseMessage": "이미 사용 중인 계정 ID 입니다."
}
```  
  
### [계정 등록] 실패 시, 응답 값 (유효성 검사 실패)  
```json  
{
    "requestUrl": "http://localhost:8000/api/user/join",
    "requestDate": "2020-10-24T15:24:05.008+00:00",
    "responseStatus": false,
    "responseCode": "-0000",
    "responseMessage": "Failure",
    "responseResource": {
        "password": "계정 비밀번호 형식이 맞지 않습니다.",
        "username": "계정 ID 형식이 맞지 않습니다."
    }
}
```  

### [계정 로그인] 요청 값
```
http://localhost:8000/api/user/login
```
```json  
{
    "username": "sample",
    "password": "test-password"
}
```

### [계정 로그인] 성공 시, 응답 값
```json  
{
    "requestUrl": "http://localhost:8000/api/user/login",
    "requestDate": "2020-10-24T13:48:18.959+00:00",
    "responseStatus": true,
    "responseCode": "2001",
    "responseMessage": "계정 로그인에 성공하였습니다.",
    "responseResource": {
        "accessToken": {
            "name": "accessToken",
            "value": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjEiLCJpYXQiOjE2MDM1NDcyOTgsImV4cCI6MTYwMzU0NzMxM30.htcmPpau_1p_cK6bqROiPIyVgB3IYudqfVxSEF1KkGY",
            "version": 0,
            "comment": null,
            "domain": null,
            "maxAge": 3600,
            "path": "/",
            "secure": false,
            "httpOnly": true
        },
        "refreshToken": {
            "name": "refreshToken",
            "value": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjEiLCJpYXQiOjE2MDM1NDcyOTgsImV4cCI6MTYwMzU0NzM1OH0.uipgR7ilcQ1lrdyfXqG-sF3T0nzKyufLz0nrOTunKm8",
            "version": 0,
            "comment": null,
            "domain": null,
            "maxAge": 3600,
            "path": "/",
            "secure": false,
            "httpOnly": true
        }
    }
}
```
### [계정 로그인] 실패 시, 응답 값 (ID/Password 불일치)  
```json  
{
    "requestUrl": "http://localhost:8000/api/user/login",
    "requestDate": "2020-10-24T15:25:53.894+00:00",
    "responseStatus": false,
    "responseCode": "-2004",
    "responseMessage": "계정에 로그인 할 수 없습니다."
}
```

### [계정 로그인] 실패 시, 응답 값 (유효성 검사 실패)  
```json  
{
    "requestUrl": "http://localhost:8000/api/user/login",
    "requestDate": "2020-10-24T15:26:34.474+00:00",
    "responseStatus": false,
    "responseCode": "-0000",
    "responseMessage": "Failure",
    "responseResource": {
        "password": "계정 비밀번호 형식이 맞지 않습니다.",
        "username": "계정 ID 형식이 맞지 않습니다."
    }
}
```

### [계정 로그아웃] 요청 값  
```
http://localhost:8000/api/user/logout
```
```text
쿠키에 저장된 토큰을 사용하므로 요청 값은 없음.
```  

### [계정 로그아웃] 성공 시, 응답 값  
```json  
{
    "requestUrl": "http://localhost:8000/api/user/logout",
    "requestDate": "2020-10-24T15:29:50.057+00:00",
    "responseStatus": true,
    "responseCode": "2002",
    "responseMessage": "계정 로그아웃에 성공하였습니다.",
    "responseResource": {
        "accessToken": {
            "name": "accessToken",
            "value": "",
            "version": 0,
            "comment": null,
            "domain": null,
            "maxAge": 0,
            "path": "/",
            "secure": false,
            "httpOnly": true
        },
        "refreshToken": {
            "name": "refreshToken",
            "value": "",
            "version": 0,
            "comment": null,
            "domain": null,
            "maxAge": 0,
            "path": "/",
            "secure": false,
            "httpOnly": true
        }
    }
}
```
### [계정 로그아웃] 실패 시, 응답 값  
```json
{
    "requestUrl": "http://localhost:8000/api/user/logout",
    "requestDate": "2020-10-25T01:03:54.935+00:00",
    "responseStatus": false,
    "responseCode": "-2005",
    "responseMessage": "계정을 로그아웃 할 수 없습니다."
}
```
### [계정 로그아웃] 실패 시, 응답 값 ("/api/user/logout" 경로의 permitAll()을 해제했을 때)  
```json  
{
    "requestUrl": "http://localhost:8000/error/entry-point",
    "requestDate": "2020-10-24T15:30:21.458+00:00",
    "responseStatus": false,
    "responseCode": "-2000",
    "responseMessage": "해당 리소스에 접근하기 위한 인증이 필요합니다."
}
```
### [접근 권한 검사(All)] 요청 값  
```
http://localhost:8000/api/user/all
```
```text
요청 값 없음.
접근 권한을 지정하지 않았으므로 로그인 없이 실행함.
```  

### [접근 권한 검사(All)] 성공 시, 응답 값  
```json  
{
    "requestUrl": "http://localhost:8000/api/user/all",
    "requestDate": "2020-10-24T15:57:10.693+00:00",
    "responseStatus": true,
    "responseCode": "1000",
    "responseMessage": "요청이 성공적으로 처리되었습니다.",
    "responseResource": "Public Content"
}
```

### [접근 권한 검사(Authenticated)] 요청 값  
```
http://localhost:8000/api/user/authenticated
```
```text
쿠키에 저장된 토큰을 사용하므로 요청 값은 없음.
```  

### [접근 권한 검사(Authenticated)] 성공 시, 응답 값  
```json  
{
    "requestUrl": "http://localhost:8000/api/user/authenticated",
    "requestDate": "2020-10-24T16:01:25.131+00:00",
    "responseStatus": true,
    "responseCode": "1000",
    "responseMessage": "요청이 성공적으로 처리되었습니다.",
    "responseResource": "Authenticated Content"
}
```

### [접근 권한 검사(Authenticated)] 실패 시, 응답 값 (비로그인)  
```json  
{
    "requestUrl": "http://localhost:8000/error/entry-point",
    "requestDate": "2020-10-24T16:00:38.743+00:00",
    "responseStatus": false,
    "responseCode": "-2000",
    "responseMessage": "해당 리소스에 접근하기 위한 인증이 필요합니다."
}
```

### [접근 권한 검사(User)] 요청 값  
```
http://localhost:8000/api/user/user
```
```text
쿠키에 저장된 토큰을 사용하므로 요청 값은 없음.
```  

### [접근 권한 검사(User)] 성공 시, 응답 값  
```json  
{
    "requestUrl": "http://localhost:8000/api/user/user",
    "requestDate": "2020-10-24T16:01:40.975+00:00",
    "responseStatus": true,
    "responseCode": "1000",
    "responseMessage": "요청이 성공적으로 처리되었습니다.",
    "responseResource": "User Content"
}
```

### [접근 권한 검사(User)] 실패 시, 응답 값 (비로그인)  
```json  
{
    "requestUrl": "http://localhost:8000/error/entry-point",
    "requestDate": "2020-10-24T16:02:17.053+00:00",
    "responseStatus": false,
    "responseCode": "-2000",
    "responseMessage": "해당 리소스에 접근하기 위한 인증이 필요합니다."
}
```

### [접근 권한 검사(Admin)] 요청 값  
```
http://localhost:8000/api/user/admin
```
```text
쿠키에 저장된 토큰을 사용하므로 요청 값은 없음.
```  

### [접근 권한 검사(Admin)] 성공 시, 응답 값  
```json  
{
    "requestUrl": "http://localhost:8000/api/user/admin",
    "requestDate": "2020-10-24T16:04:06.019+00:00",
    "responseStatus": true,
    "responseCode": "1000",
    "responseMessage": "요청이 성공적으로 처리되었습니다.",
    "responseResource": "Admin Content"
}
```

### [접근 권한 검사(Admin)] 실패 시, 응답 값 (비로그인)  
```json  
{
    "requestUrl": "http://localhost:8000/error/entry-point",
    "requestDate": "2020-10-24T16:03:11.069+00:00",
    "responseStatus": false,
    "responseCode": "-2000",
    "responseMessage": "해당 리소스에 접근하기 위한 인증이 필요합니다."
}
```

### [접근 권한 검사(Admin)] 실패 시, 응답 값 (ROLE_USER 권한으로 시도했을 때)  
```json  
{
    "requestUrl": "http://localhost:8000/api/user/admin",
    "requestDate": "2020-10-24T16:02:37.009+00:00",
    "responseStatus": false,
    "responseCode": "-2001",
    "responseMessage": "해당 리소스에 대한 접근 권한이 없습니다."
}
```

### [메시지 국제화 검사] Korean  
```
http://localhost:8000/web/i18n?lang=ko
```
![Alt text](./i18n-ko.png)

### [메시지 국제화 검사] English  
```
http://localhost:8000/web/i18n?lang=en
```
![Alt text](./i18n-en.png)



# 6. 참고 문서   
    [JWT 예제] https://velog.io/@ehdrms2034/Spring-Boot-Security-JWT-JPAMysql를-이용한-Restful-Login-사용하기-1
  