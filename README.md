# Inter Ticket
![image](https://github.com/dev-hooon/interpark/assets/75837025/3d0c41d9-b11e-4406-a83c-26ad9da08c89)
> **인터파크 티켓 서비스 클론코딩**
> <br>프로젝트 기간 : 2023/12/11~2024/01/16

## 프로젝트 목적 🎯

> - 인터파크 티켓 서비스에서 겪을 수 있는 **기술적 문제를 경험하고 해결하는 과정을 경험하는데 초점을 맞춘다**
> - 지식공유와 코드리뷰를 적극적으로 하며 **협업 능력을 기른다**

## 팀 소개 👨‍👨‍👧‍👦
| SM & Developer | Developer | Developer |
| :--------------:|:---------:|:---------:|
| [남은찬](https://github.com/EunChanNam) | [박주한](https://github.com/ParkJuhan94) | [이수진](https://github.com/ssssujini99) |
| <p align="center"><img src="https://avatars.githubusercontent.com/u/75837025?v=4" width="100"></p> | <p align="center"><img src="https://avatars.githubusercontent.com/u/81701212?v=4" width="100"></p> | <p align="center"><img src="https://avatars.githubusercontent.com/u/71487608?v=4" width="100"></p> |

## 프로젝트 문서 📝

### 전체 문서 🧾

[노션 프로젝트 문서](https://eunchan.notion.site/4a865e0a60ed42e0883d0f643e03ab02)

### API 문서 🚀

[노션 API 문서](https://eunchan.notion.site/API-4bf18f0aadc642cabad474140edbb049)

### 블로그 🗂️

[Redis 를 활용한 티켓 예매 동시성 제어와 성능 최적화 경험기](https://velog.io/@namhm23/Redis-%EB%A5%BC-%ED%99%9C%EC%9A%A9%ED%95%9C-%ED%8B%B0%EC%BC%93-%EC%98%88%EB%A7%A4-%EB%8F%99%EC%8B%9C%EC%84%B1-%EC%A0%9C%EC%96%B4%EC%99%80-%EC%84%B1%EB%8A%A5-%EC%B5%9C%EC%A0%81%ED%99%94%EB%B6%84%EC%82%B0%EB%9D%BD-X)

[ShedLock 을 통한 스케줄러 서버 failover 구현](https://velog.io/@namhm23/ShedLock-%EC%9D%84-%ED%86%B5%ED%95%9C-%EC%8A%A4%EC%BC%80%EC%A4%84%EB%9F%AC-%EC%84%9C%EB%B2%84-failover)

[Redis 캐싱을 통한 공연 랭킹기능 구현](https://velog.io/@namhm23/Redis-%EC%BA%90%EC%8B%B1%EC%9D%84-%ED%86%B5%ED%95%9C-%EA%B3%B5%EC%97%B0-%EB%9E%AD%ED%82%B9%EA%B8%B0%EB%8A%A5-%EA%B5%AC%ED%98%84) 

[LocalDateTime.now() 직접호출 트러블 슈팅](https://velog.io/@namhm23/LocalDateTime.now-%EC%93%B0%EC%A7%80%EB%A7%90%EC%9E%90-%EC%A7%81%EC%A0%91%ED%98%B8%EC%B6%9C-%ED%95%98%EC%A7%80%EB%A7%90%EC%9E%90-5aiwi1bw)

[ShedLock 사용 시 스케줄러 통합테스트 트러블 슈팅](https://velog.io/@namhm23/ShedLock-%EC%82%AC%EC%9A%A9-%EC%8B%9C-%EC%8A%A4%EC%BC%80%EC%A4%84%EB%9F%AC-%ED%86%B5%ED%95%A9%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%A3%BC%EC%9D%98%EC%A0%90)

## 기술 스택 🛠️

<img src="https://github.com/dev-hooon/interpark/assets/75837025/65149067-ce8c-47e6-b918-1922b83f9bd4">


## ERD 📊
<img src="https://github.com/dev-hooon/interpark/assets/75837025/36179d22-6d1f-44c9-af04-670de1ed0661">

## 모듈 구성 ⚙️
### api 모듈
> 애플리케이션 End Point 모듈로 `API Controller`, `Interceptor`, `Argument Resolver`, `ExceptionHandler` 로 구성
### core 모듈
> 핵심 로직 모듈로 `Entity`, `Repository`, `Bussiness Logic`, `Service Logic` 로 구성
### scheduler 모듈
> 별도의 스케줄러 작업을 수행하는 모듈로 `scheduler` 로 구성
  
