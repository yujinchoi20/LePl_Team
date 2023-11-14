## LePl Team Project

-----------------

#### Version

* org.springframework.boot: version '3.1.2'
* io.spring.dependency-management: version '1.1.0'
* java: version 17
* junit: version '4.13.2'

-----------------

#### 1. 요구사항 분석
![image](https://github.com/yujinchoi20/LePl_Team/assets/105353163/d0567ab8-1f33-4d64-8711-274eb21c083a)

#### 2. 도메인 모델 분석
![image](https://github.com/yujinchoi20/LePl_Team/assets/105353163/a351fe5e-1b13-4752-8914-30d945059f06)

#### 3. 엔티티 설계
![image](https://github.com/yujinchoi20/LePl_Team/assets/105353163/4353fc00-6314-4a44-ba04-22818ecfeab2)

#### 4. 테이블 설계
![image](https://github.com/yujinchoi20/LePl_Team/assets/105353163/73cd0ba8-b791-4162-bfef-bb1a76853dbf)
------------------

### 11/14

__[Item}__
* __Item Domain__: id, type, name, price, purchase_quantity, start_time, end_time
* __Item Repository__: save(), findOne(), findByName(), findAll(), updatePurcharse(), remove()
* __Item Service__: save(), findOne(), findByName(), findAll(), updatePurcharse(), remove()
* __Item ApiController__: "api/v1/item"
    * findItem(): GetMapping, itemId로 아이템 조회, "/find/id/{itemId}"
    * findItemByName(): GetMapping, itemName으로 아이템 조회, "/find/name/{itemName}"
    * findItems(): 아이템 전체 조회, "/all"

__[Character]__
* __Character Domain__: Long money 필드 추가
* __Character Repo/Service__: updateCoint(Long money) 추가, 아이템 구매 시 화폐 차감, CharacterItemApiController에서 사용됨

__[Character_Item]__
* __Character_Item Repo/Service__: updateStatus(Long CharacterId, int status) 추가, 아이템 착용 여부 변경, status == 1 아이템 착용/status == 0 아이템 미착용
* __Character_Item ApiController__: "api/v1/characterItem"
    * buyItem(): PostMapping, 아이템 구매 버튼, "/buy/{itemId}"
    * putItem(): GetMapping, 아이템 착용 버튼. "/put/{itemId}/{status}"

### 11/06

__Item 테이블 추가__
* Item
* ItemRepository
* ItemService

__[아이템 구매 시나리오]__
1. Item 테이블에 관리자가 아이템을 추가함(관리자가 상점에 아이템을 등록한거라고 생각하면 됨)
2. 사용자가 아이템 구매를 원하면 아이템 가격을 확인하여 사용자가 구매할 수 있는 아이템인지 확인함
3. 아이템 구매 조건을 만족한다면 CharacterItem 테이블에 아이템 정보를 업데이트 함
4. 아이템 구매에 사용된 경험치를 차감하여 Exp 테이블에 사용자 현재 경험치를 업데이트 함

ExpRespository, ExpService에 updateBuyItem() 메서드 추가!

__[테스트 코드 작성 후 실행]__
![image](https://github.com/yujinchoi20/LePl_Team/assets/105353163/0d428030-9d25-40d4-b12c-f1954830f3cb)

현재 경험치가 10이고, 모자 가격이 10이기 때문에 구매조건 만족함! 

![image](https://github.com/yujinchoi20/LePl_Team/assets/105353163/09ab5e69-47e9-444d-bcdf-ef62a0e8eff4)

구매 후 현재 경험치는 0으로 업데이트 됨.

### 10/15

경험치 체계 및 타이머 체계 확립

* 경험치 수식: '=INT((A2-1)^1.2)+10' -> 일단 수식은 이렇게 가지만 변경될 가능성 있음!
* 타이머 체계: 타이머 기능은 있지만 타이머 객체 자체는 삭제함

    -> Task: remainTime(잔여 시간)
  
    -> Lists: timerAllUseTime(총 사용시간/누적 시간), curTime(현재 시간)

* 타이머 사용 여부
    -> 타이머를 사용하지 않을 경우, 체크 박스를 통해 일정 수행 여부를 체크함
  
    -> 타이머를 사용하는 경우, 잔여 시간이 00:00:00 되면 일정을 완료한 것으로 간주함

* 잔여 시간
    -> 사용자가 일정을 계획할 때, 일정에 소요되는 시간을 지정함

    -> 일정을 계획한 직후에는 (잔여 시간 == 사용자가 설정한 일정 소요 시간)

    -> Ex) 운동하기 일정을 10:00 ~ 12:00 으로 2시간 설정하면 잔여 시간은 2시간으로 초기화 됨, 1시간 운동 후 종료 버튼을 누르면 잔여 시간은 1시간으로 업데이트 됨, 잔여 시간이 0시간이 되면 계획한 일정을 완료했다고 간주함.
    -> 잔여 시간이 남았다고해서 남은 시간 만큼 의무적으로 일정을 수행할 필요는 없음

* 총 사용시간/누적시간: 하루 동안 일정 수행에 사용한 타이머 시간을 표시 -> 오로지 서비스 목적이지 경험치 계산에는 필요 없음
* 현재 시간: 경험치 계산에 사용하고 남은 시간!
    -> 이 시간 개념을 사용하는 이유는 사용자의 경험치 획득에 도움이 되고자 함

    -> 만약 현재 시간 저장 없이, 앞의 일정은 50분 수행으로 타이머 경험치를 얻지 못하고, 뒤의 일정에서 1시간 10분으로 1시간에 해당하는 타이머 경험치만 얻게 될 경우 일정 수행시간은 2시간이지만 실제로 1시간에 해당하는 타이머 경험치만 획득하게 됨

    -> 위의 상황을 방지하기 위해 현재 시간에 타이머 시간을 저장해두고, 1시간 단위로 타이머 경험치를 부여하도록 수정함

==> Service, Api 부분에서 세부적으로 수정해야되는 사항임! (수정 예정)

### 06/01
* 연관관계 편의 메서드
* Member 레포지토리, 서비스 개발
* Member Test 진행

### 06/02
* ListsRepository, ListsService 개발
* Lists Test 추가 
* Task에 업무가 추가되면 Lists에 오늘의 일정이 추가되어야 함. 

Ex) Task: 공부하기 추가 --> Lists: 날짜와 해당 날짜의 업무 개수 추가! 

### 06/03
* TaskRepository 개발 
* TaskRepositoryTest 진행 
* 업무 추가시 일정의 count 개수 증가 구현

### 06/05
* 업무 삭제 기능 추가 
* 삭제시 delete query 사용 -> 오류 발생 

![deleteError](https://github.com/LvUpPlanner/LePl_Spring/assets/105353163/cb2f9628-28de-4dbf-a500-f26dc51e9154)

delete 쿼리문은 반환 값이 없음 => createQuery() 의 매개변수중에 Task.class는 반환된 결과를 해당 엔티티 클래스(Task) 형식으로 자동 매핑해주는 역할을 한다. 

하지만 반환 값이 없는 쿼리에 반환 결과 매핑 역할을 해주는 매개변수를 사용해서 오류가 발생했던 것이다. 

Task.class 를 지우고 실행 해보니 원하는 방식으로 test가 진행되었다. 

### 06/06
* TaskService 개발 및 테스트 코드 작성 

### 06/07
* TaskStatusRepository, TaskStatusService 생성

------

### 09/17
* Character 기능 개발 진행중
* 회원가입시 유저의 캐릭터도 생성, 로그인시 유저의 캐릭터 불러오기
* 캐릭터의 레벨도 같이 가져오기

### 09/23
* 캐릭터 생성 테스트 코드 완성하기 -> 예상 시나리오: 캐릭터 생성과 동시에 경험치 0, 레벨 1, 친구(팔로잉) 0으로 세팅
* 친구 기능 -> 팔로우/팔로잉 기능으로 수정하기
* 알림 테이블, 엔티티 만들기

![image](https://github.com/yujinchoi20/LePl_Team/assets/105353163/0834a3c2-2b7e-4e14-8d73-a5b73338b471)

### 09/25
* 캐릭터 생성 테스트 완료!
* 친구 기능 -> 팔로우/팔로잉 기능으로 변경 완료!

![image](https://github.com/yujinchoi20/LePl_Team/assets/105353163/77f00680-bff2-4d5b-a93d-e5ed9cc61fc8)

-> 테스트 결과 (Rollback)

![image](https://github.com/yujinchoi20/LePl_Team/assets/105353163/5e3ff17c-d250-4ecf-b1c3-14bf156fd5ea)

-> H2 DB

* 영속성 전이(cascade)

@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)

@JoinColumn(name = "exp_id") // FK

private Exp exp;

-> FK 영속성 전이 추가!

PK 데이터가 추가되기 전에 FK 데이터가 추가되는 것을 막아줌. 

### 09/26

* Exp 테스트 코드 -> 하루 최대 경험치가 넘어가면 레벨업 안됨, 레벨업 필요 경험치 수식 변경((level - 1) ^ 2) * 2)
* Notification Entity, Repository, Service 추가

###### Service/Character/ExpService.java

-> 매일 경험치를 리셋하는 updatePoint() 메서드 

-> @Scheduled(cron = ), 쿼츠 크론 사용(0 0 0 1/1 * ? *): 매일 오전 12시 마다

* Notification 테스트 코드 추가 예정

### 10/10

* Notification 수정
* MemberApiController 중복회원 추가
* TimerApiContoller 추가 

-> 테스트 코드 작성 예정
