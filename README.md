# newspace-backend


<br>

## 📍 프로젝트명: Newspace
<img src="https://github.com/user-attachments/assets/04d415b7-b379-4a0b-9aba-ff1d3609db85" width="300" />
<br>


## 👩‍💻 팀원
<table>
    <tr>
        <!-- 첫 번째 팀원 -->
        <td align="center" width="33%">
            <img src="https://avatars.githubusercontent.com/u/151743721?v=4" alt="Avatar" width="100px"/><br/>
            <a href="https://github.com/95hyun">현민영</a>
            <br/>
            <img src="https://github-readme-stats.vercel.app/api?username=95hyun&show_icons=true&theme=transparent" alt="95hyun's GitHub stats" width="300px"/>
        </td>
        <!-- 두 번째 팀원 -->
        <td align="center" width="33%">
            <img src="https://avatars.githubusercontent.com/u/39462045?v=4" alt="Avatar" width="100px"/><br/>
            <a href="https://github.com/dhku">구동혁</a>
            <br/>
            <img src="https://github-readme-stats.vercel.app/api?username=dhku&show_icons=true&theme=transparent" alt="dhku's GitHub stats" width="300px"/>
        </td>
        <!-- 세 번째 팀원 -->
        <td align="center" width="33%">
            <img src="https://avatars.githubusercontent.com/u/124752866?v=4" alt="Avatar" width="100px"/><br/>
            <a href="https://github.com/minnnseokk">정민석</a>
            <br/>
            <img src="https://github-readme-stats.vercel.app/api?username=minnnseokk&show_icons=true&theme=transparent" alt="minnnseokk's GitHub stats" width="300px"/>
        </td>
    </tr>
</table>
<br/>

## 🛠️ 기술 스택

<img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white"> <img src="https://img.shields.io/badge/SpringSecurity-6DB33F?style=for-the-badge&logo=SpringSecurity&logoColor=white"> <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white"> <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=SpringAi&logoColor=white">
<img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=SpringCloudEureka&logoColor=white"> <img src="https://img.shields.io/badge/003545?style=for-the-badge&logo=MariaDB&logoColor=white"> <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white"> 

<br/>

## 📂 프로젝트 아키텍처

```
.
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── settings.gradle
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── lgcns
    │   │           └── newspacebackend
    │   │               ├── domain
    │   │               │   ├── news
    │   │               │   │   ├── controller
    │   │               │   │   │   ├── NewsAIController.java
    │   │               │   │   │   ├── NewsCategoryController.java
    │   │               │   │   │   └── NewsKeywordController.java
    │   │               │   │   ├── dto
    │   │               │   │   │   ├── NewsCategoryRequestDto.java
    │   │               │   │   │   ├── NewsCategoryResponseDto.java
    │   │               │   │   │   ├── NewsKeywordRequestDto.java
    │   │               │   │   │   ├── NewsKeywordResponseDto.java
    │   │               │   │   │   ├── NewsRequestDto.java
    │   │               │   │   │   └── NewsResponseDto.java
    │   │               │   │   ├── entity
    │   │               │   │   │   ├── NewsCategory.java
    │   │               │   │   │   └── NewsKeyword.java
    │   │               │   │   ├── repository
    │   │               │   │   │   ├── NewsCategoryRepository.java
    │   │               │   │   │   └── NewsKeywordRepository.java
    │   │               │   │   └── service
    │   │               │   │       ├── NewsAIService.java
    │   │               │   │       ├── NewsCategoryService.java
    │   │               │   │       └── NewsKeywordService.java
    │   │               │   ├── notice
    │   │               │   │   ├── controller
    │   │               │   │   │   └── NoticeController.java
    │   │               │   │   ├── dto
    │   │               │   │   │   ├── NoticeRequestDto.java
    │   │               │   │   │   └── NoticeResponseDto.java
    │   │               │   │   ├── entity
    │   │               │   │   │   └── Notice.java
    │   │               │   │   ├── repository
    │   │               │   │   │   └── NoticeRepository.java
    │   │               │   │   └── service
    │   │               │   │       └── NoticeService.java
    │   │               │   └── user
    │   │               │       ├── controller
    │   │               │       │   └── UserController.java
    │   │               │       ├── dto
    │   │               │       │   ├── LoginRequestDto.java
    │   │               │       │   ├── SignupRequestDto.java
    │   │               │       │   ├── UserInfoRequestDto.java
    │   │               │       │   └── UserInfoResponseDto.java
    │   │               │       ├── entity
    │   │               │       │   ├── User.java
    │   │               │       │   └── UserRole.java
    │   │               │       ├── repository
    │   │               │       │   └── UserRepository.java
    │   │               │       └── service
    │   │               │           └── UserService.java
    │   │               ├── global
    │   │               │   ├── config
    │   │               │   │   ├── ChatClientConfig.java
    │   │               │   │   ├── PasswordConfig.java
    │   │               │   │   └── RestTemplateConfig.java
    │   │               │   ├── entity
    │   │               │   │   └── TimeStamp.java
    │   │               │   ├── security
    │   │               │   │   ├── config
    │   │               │   │   │   ├── CorsConfig.java
    │   │               │   │   │   ├── SecurityConfig.java
    │   │               │   │   │   └── WebConfig.java
    │   │               │   │   ├── constant
    │   │               │   │   │   ├── GrantType.java
    │   │               │   │   │   └── TokenType.java
    │   │               │   │   ├── dto
    │   │               │   │   │   └── JwtTokenInfo.java
    │   │               │   │   ├── filter
    │   │               │   │   │   ├── JwtAuthenticationFilter.java
    │   │               │   │   │   └── JwtAuthorizationFilter.java
    │   │               │   │   ├── jwt
    │   │               │   │   │   ├── JwtTokenUtil.java
    │   │               │   │   │   └── JwtTokenUtilPractice.java
    │   │               │   │   ├── UserDetailsImpl.java
    │   │               │   │   ├── UserDetailsServiceImpl.java
    │   │               │   │   └── util
    │   │               │   │       └── FilterResponseUtil.java
    │   │               │   └── util
    │   │               │       └── FileUtil.java
    │   │               └── NewspaceBackendApplication.java
    │   └── resources
    │       └── static
    │           └── profile.png
    └── test
        └── java
            └── com
                └── lgcns
                    └── newspacebackend
                        └── NewspaceBackendApplicationTests.java
```
<br/>

