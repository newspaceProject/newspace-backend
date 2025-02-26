# newspace-frontend


<br>

## 📍 프로젝트명: Newspace
<img src="https://github.com/user-attachments/assets/04d415b7-b379-4a0b-9aba-ff1d3609db85" width="300" />
<br>


## 👩‍💻 팀원
<table>
    <tr>
        <!-- 첫 번째 팀원 -->
        <td align="center" width="50%">
            <img src="https://avatars.githubusercontent.com/judymoody59" alt="Avatar" width="100px"/><br/>
            <a href="https://github.com/judymoody59">채민주</a>
            <br/>
            <img src="https://github-readme-stats.vercel.app/api?username=judymoody59&show_icons=true&theme=transparent" alt="Minju's GitHub stats" width="350px"/>
        </td>
        <!-- 두 번째 팀원 -->
        <td align="center" width="50%">
            <img src="https://avatars.githubusercontent.com/Y0ungse" alt="Avatar" width="100px"/><br/>
            <a href="https://github.com/hayong39">유영서</a>
            <br/>
            <img src="https://github-readme-stats.vercel.app/api?username=Y0ungse&show_icons=true&theme=transparent" alt="Yeongseo's GitHub stats" width="350px"/>
        </td>
    </tr>
</table>
<br/>

## 🛠️ 기술 스택

<img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=HTML5&logoColor=white"> <img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=CSS3&logoColor=white"> <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=JavaScript&logoColor=black"> <img src="https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=React&logoColor=black"> <img src="https://img.shields.io/badge/Vite-646CFF?style=for-the-badge&logo=Vite&logoColor=white"> <img src="https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=Figma&logoColor=white"> 

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

