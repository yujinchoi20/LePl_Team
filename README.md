## LePl Team Project

-----------------

#### Version

* org.springframework.boot: version '3.1.2'
* io.spring.dependency-management: version '1.1.0'
* java: version 17
* junit: version '4.13.2'

-----------------

#### 1. 요구사항 분석
![step1](https://github.com/LvUpPlanner/LePl_Spring/assets/105353163/15aa6cab-f295-4da3-9088-5f2cd0af6a94)
#### 2. 도메인 모델 분석
![step2](https://github.com/LvUpPlanner/LePl_Spring/assets/105353163/3f41bc83-575c-497d-b38d-60bb8f06b04e)
#### 3. 엔티티 설계
![step3](https://github.com/LvUpPlanner/LePl_Spring/assets/105353163/84bfbe6f-75cd-4a1b-af37-228e97e5076d)
#### 4. 테이블 설계
![step4](https://github.com/LvUpPlanner/LePl_Spring/assets/105353163/0b98c6b2-08e0-410d-af9f-a089840ff0fe)
![table](https://github.com/LvUpPlanner/LePl_Spring/assets/105353163/f6970d00-db12-4ac9-92c7-9c60f8fb5dca)
![table2](https://github.com/LvUpPlanner/LePl_Spring/assets/105353163/2c23ac57-fad1-41a9-a927-7d077861abf7)

------------------

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
