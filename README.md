# 음악의 재.구.SoNG
<img src="resource/Logo.png"></td>

## 프로젝트 소개
**진행 기간** : 2024.10.14 ~ 2024.11.19  
**인원** : 5명 (인프라 1명, 백엔드 2명, 프론트 3명)

가수들이 꾸준히 리메이커곡을 발매하고 많은 리메이커곡이 원곡과는 비슷하면서 새로운 분위기로 사랑받고 있습니다. 가수뿐만 아니라 일반인들도 자신의 유튜브 채널에 커버곡을 올리는 경우가 많이 있습니다. 혹시 곡을 리메이커 하는 것에 관심이 있나요? 그런데 전문적인 음악지식과 편곡지식이 없어서 힘드신가요? 음원 재생산 플랫폼인 재구송이 쉽게 도와드립니다.

## 주요 기능
- 음원 분리
    - 음원을 업로드하면 세션별(보컬/각 악기)로 분리
- 세션별 음원 듣기
    - 각 세션을 선택할 수 있는 기능이 있어 듣고 싶은 세션만 선택해서 들을 수 있음
- 내 음원 추가
    - 다른 워크스페이스에 내가 추가하고 싶은 음원을 추가해서 함께 들을 수 있음
- 워크스페이스 포크 
    - 다른 사람의 음원을 듣고 추가하거나 수정하고 싶을 때 내 워크스페이스로 가져가 음원을 수정
- 시작 포인트 / 종료 포인트 설정
    - 듣고 싶은 구간을 설정
    - 시작 포인트와 종료 포인트 각각 설정
- 채팅
    - 플랫폼 내 다른 유저와 채팅
- 게임
    - 음감과 리듬감을 이용한 3가지 게임
- 크루
    - 크루에 가입해 다른 유저들과 소통
- 유저 정보 기반 유저 추천
    - 유저 정보(포지션, 장르, 지역)을 기반으로 유사한 유저 추천

## 서비스 화면

<table>
    <tr>
        <th style="text-align: center;">로그인 페이지</th>
        <th style="text-align: center;">메인 페이지</th>
    </tr>
    <tr>
        <td><img src="resource/screen/1.png"></td>
        <td><img src="resource/screen/2.png"></td>
    </tr>
    <tr>
        <th style="text-align: center;"> 음원 업로드 페이지</th>
        <th style="text-align: center;">워크스페이스 페이지</th>
    </tr>
    <tr>
        <td><img src="resource/screen/3.png"></td>
        <td><img src="resource/screen/4.png"></td>
    </tr>
    <tr>
        <th style="text-align: center;">피드 목록 페이지</th>
        <th style="text-align: center;">음원피드 목록 페이지</th>
    </tr>
    <tr>
        <td><img src="resource/screen/5.png"></td>
        <td><img src="resource/screen/6.png"></td>
    </tr>
    <tr>
        <th style="text-align: center;">게시물 상세 페이지</th>
        <th style="text-align: center;">게시물 생성 페이지</th>
    </tr>
    <tr>
        <td><img src="resource/screen/7.png"></td>
        <td><img src="resource/screen/8.png"></td>
    </tr>
    <tr>
        <th style="text-align: center;">마이페이지</th>
        <th style="text-align: center;">검색창</th>
    </tr>
    <tr>
        <td><img src="resource/screen/9.png"></td>
        <td><img src="resource/screen/10.png"></td>
    </tr>
        <tr>
        <th style="text-align: center;">채팅 목록</th>
        <th style="text-align: center;">채팅창</th>
    </tr>
    <tr>
        <td><img src="resource/screen/11.png"></td>
        <td><img src="resource/screen/12.png"></td>
    </tr>
        <tr>
        <th style="text-align: center;">게임 페이지</th>
        <th style="text-align: center;">게임 상세 페이지</th>
    </tr>
    <tr>
        <td><img src="resource/screen/13.png"></td>
        <td><img src="resource/screen/14.png"></td>
    </tr>
</table>



## 주요 기술 스택

### 프론트엔드
![Typescript](https://img.shields.io/badge/TypeScript-007ACC?style=for-the-badge&logo=typescript&logoColor=white)
![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)
![Zustand](https://img.shields.io/badge/Zustand-2962FF?style=for-the-badge)
![Chakra UI](https://img.shields.io/badge/Chakra--UI-319795?style=for-the-badge&logo=chakra-ui&logoColor=white)
![Axios](https://img.shields.io/badge/axios-671ddf?&style=for-the-badge&logo=axios&logoColor=white)
![npm](https://img.shields.io/badge/npm-CB3837?style=for-the-badge&logo=npm&logoColor=white)
![vite](https://img.shields.io/badge/Vite-B73BFE?style=for-the-badge&logo=vite&logoColor=FFD62E)

### 백엔드
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Neo4j](https://img.shields.io/badge/Neo4j-018bff?style=for-the-badge&logo=neo4j&logoColor=white)
![WebFlux](https://img.shields.io/badge/WebFlux-76D04B?style=for-the-badge&logoColor=white)


### DB
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)

### Infra
![Jenkins](https://img.shields.io/badge/Jenkins-49728B?style=for-the-badge&logo=jenkins&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white)

### Tools
![GitLab](https://img.shields.io/badge/gitlab-%23181717.svg?style=for-the-badge&logo=gitlab&logoColor=white)
![Jira](https://img.shields.io/badge/jira-%230A0FFF.svg?style=for-the-badge&logo=jira&logoColor=white)
![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)

## 산출물

### 아키텍처
<img src="resource/structure.png"></td>

### ERD
<img src="resource/erd.png"></td>

## 팀원
<table style="width: 100%; border-collapse: collapse;">
    <thead>
        <tr>
            <th style="text-align: center;">이름</th>
            <th style="text-align: center;">역할</th>
            <th style="text-align: center;">세부사항</th>
        </tr>
    </thead>
    <body>
        <tr>
            <td style="text-align: center; vertical-align: middle;">박수빈</td>
            <td style="text-align: center; vertical-align: middle;">팀장<br>프론트엔드</td>
            <td>
                -로그인/회원가입
                <br>&nbsp;&nbsp;&nbsp;&nbsp;-이메일 인증을 통한 회원가입
                <br>-커뮤니티
                <br>&nbsp;&nbsp;&nbsp;&nbsp;-메인 페이지
                <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-유저 정보 기반의 다른 유저 추천
                <br>&nbsp;&nbsp;&nbsp;&nbsp;-유저 개인 및 크루 피드
                <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-글 생성 및 수정 페이지, 글 삭제
                <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-댓글 및 대댓글 생성, 수정, 삭제
                <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-PUBLIC/PRIVATE 필터링 적용
                <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-Zustand를 이용해 유저 및 크루 헤더 정보 변경
                <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-피드장에게만 글쓰기 기능 부여
                <br>-크루</br>
                <br>&nbsp;&nbsp;&nbsp;&nbsp;- 크루 생성 및 수정 페이지
                <br>&nbsp;&nbsp;&nbsp;&nbsp;- 크루장에 다른 크루원에게 피드장/채팅장 권한 부여
                <br>&nbsp;&nbsp;&nbsp;&nbsp;- 크루장은 크루 가입을 신청한 유저를 승인 혹은 거절
                <br>-팔로우</br>
                <br>&nbsp;&nbsp;&nbsp;&nbsp;-다른 유저를 팔로우하는 기능 구현
                <br>&nbsp;&nbsp;&nbsp;&nbsp;-팔로우/팔로워 목록 생성
                <br>-Chakra UI를 활용한 컴포넌트 디자인</br>
                <br>&nbsp;&nbsp;&nbsp;&nbsp;-커뮤니티
            </td>
        </tr>    
        <tr>
            <td style="text-align: center; vertical-align: middle;">권혜경</td>
            <td style="text-align: center; vertical-align: middle;">백엔드</td>
            <td>-유저
                <br>&nbsp;&nbsp;&nbsp;&nbsp;-로그인/회원가입
                <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-이메일 인증을 통한 회원가입
                <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-이름 길이 제한
                <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-text 정보는 json으로 받고 프로필 사진 이미지는 파일로 받는다?(수정)
                <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-액세스 토큰 재발급
                <br>-크루
                <br>&nbsp;&nbsp;&nbsp;&nbsp;-크루 관련 API
                <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-생성 및 수정, 탈퇴
                <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-크루 가입 신청
                <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-크루 가입 신청 승인, 거절
                <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-피드장, 채팅장 권한 부여
                <br>-커뮤니티
                <br>&nbsp;&nbsp;&nbsp;&nbsp;-커뮤니티 관련 API
                <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-피드 생성 및 수정, 삭제
                <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-피드 리스트 조회
                <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-게시물 상세페이지 조회
                <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-댓글 및 대댓글 생성, 수정, 삭제
                <br>-채팅
                <br>&nbsp;&nbsp;&nbsp;&nbsp;-WebFlux과 ReactiveMongoDB를 활용해 실시간 채팅 구현
            </td>
        </tr>    
        <tr>
            <td style="text-align: center; vertical-align: middle;">구현우</td>
            <td style="text-align: center; vertical-align: middle;">프론트엔드</td>
            <td>-게임
            <br>&nbsp;&nbsp;&nbsp;&nbsp;-tone.js를 활용해 음감 게임 생성
            <br>&nbsp;&nbsp;&nbsp;&nbsp;-게임 구현 어떻게?
            <br>-채팅
            <br>&nbsp;&nbsp;&nbsp;&nbsp;-채팅 받기? 실시간 채팅 구현
            <br>&nbsp;&nbsp;&nbsp;&nbsp;-채팅방 목록
            <br>-검색
            <br>&nbsp;&nbsp;&nbsp;&nbsp;-반응형을 활용한 검색 컴포넌트 실행
            <br>&nbsp;&nbsp;&nbsp;&nbsp;-검색어를 입력하면 실시간으로 일치하는 유저 출력
            <br>-Chakra UI를 활용한 컴포넌트 디자인
            <br>&nbsp;&nbsp;&nbsp;&nbsp;-채팅, 게임, 커뮤니티, 검색, 네비바
            <br>-Jira 관리</td>
        </tr>    
        <tr>
            <td style="text-align: center; vertical-align: middle;">김송희</td>
            <td style="text-align: center; vertical-align: middle;">프론트엔드<br>백엔드</td>
            <td>-워크스페이스
                <br>&nbsp;&nbsp;&nbsp;&nbsp;-wavesurfer.js를 이용해 음원 파형 시각화
                <br>&nbsp;&nbsp;&nbsp;&nbsp;-react-rnd를 활용해 음원 수정
                <br>&nbsp;&nbsp;&nbsp;&nbsp;-zustand와 wavesurfer.js, react-rnd를 조합해 프로젝트 전반적인 상태관리 및 동기화
                <br>-팔로우
                <br>&nbsp;&nbsp;&nbsp;&nbsp;-팔로우 관련 API
                <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-팔로우/언팔로우
                <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-팔로잉, 팔로워 목록
                <br>-Chakra UI를 활용한 컴포넌트 디자인</br>
                <br>&nbsp;&nbsp;&nbsp;&nbsp;-워크스페이스, 음원 업로드
            </td>
        </tr>    
        <tr>
            <td style="text-align: center; vertical-align: middle;">송도언</td>
            <td style="text-align: center; vertical-align: middle;">인프라<br>백엔드</td>
            <td>-서버 구축
                <br>-워크스페이스
                <br>&nbsp;&nbsp;&nbsp;&nbsp;-음원 분리 AI(이름 뭐더랑)을 이용해 세션별 분리
                <br>&nbsp;&nbsp;&nbsp;&nbsp;-워크스페이스 관련 API
                <br>&nbsp;&nbsp;&nbsp;&nbsp;-워크스페이스 관련 API
                <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-워크스페이스 생성 및 삭제
                <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-세션 추가
                <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-워크스페이스 목록
                <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-워크스페이스 상세 페이지
                <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-다른 유저 워크스페이스 포크
                <br>-추천 알고리즘
                <br>&nbsp;&nbsp;&nbsp;&nbsp;-그래프DB를 활용한 유저 추천
            </td>
        </tr> 
    </body>   
</table>

