## LePl Team Project

-----------------

1. 요구사항 분석
    ![](img/step1.png)
2. 도메인 모델 분석
    ![](img/step2.png)
3. 엔티티 설계
    ![](img/step3.png)
4. 테이블 설계
    ![](img/step4.png)

    ![](img/table.png) 

    ![](img/table2.png)

------------------

### 06/01
* 연관관계 편의 메서드 추가
* MemberRepository, MemberService 개발
* Member Test 추가 

### 06/02
* ListsRepository, ListsService 개발
* Lists Test 추가 
* Task에 업무가 추가되면 Lists에 오늘의 일정이 추가되어야 함. 

Ex) Task: 공부하기 추가 --> Lists: 날짜와 해당 날짜의 업무 개수 추가! 