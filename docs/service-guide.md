# Service Guide

## 서비스 레이어란 ?

![](https://image.slidesharecdn.com/random-151127092631-lva1-app6892/95/-60-638.jpg?cb=1448755823)
> 이미지 출저 [애플리케이션 아키텍처와 객체지향](https://www.slideshare.net/baejjae93/ss-55571345)

우리는 Member라는 객체로 회원가입(객체 생성), 프로필 수정(객체 수정) 모든 행위가 가능하지만 그것을 영속화 시켜야 하기 때문에 별도의 레이어가 필요하고 이것을 서비스 레이어라고 합니다. 서비스 레이어에서는 대표적으로 데이터베이스에 대한 트랜잭션을 관리합니다. 

서비스 영역은 도메인의 핵심 비즈니스 코드를 담당하는 영역이 아니라 인프라스트럭처(데이터베이스) 영역과 도메인 영역을 연결해주는 매개체 역할이라고 생각합니다.

**다시 한번 강조하지만 Member 객체에 대한 제어는 Member 스스로 제어해야 합니다.**



## 서비스의 적절한 책임의 크기 부여하기

책임이란 것은 외부 객체의 요청에 대한 응답이라고 생각합니다. **이러한 책임들이 모여 역할이 되고 역할은 대체 가능성을 의미합니다.** 그렇기 때문에 대체가 가능할 정도의 적절한 크기를 가져야 합니다. 이 부분은 아래의 예제로 천천히 설명드리겠습니다.

### 행위 기반으로 네이밍 하기

서비스의 책임의 크기를 잘 부여하는 방법 중에 가장 쉬운 방법이라고 생각합니다. 행위 기반으로 서비스를 만드는 것입니다.

`MemberService`라는 네이밍은 많이 사용하지만 정말 좋지 않은 패턴이라고 생각합니다. 우선 해당 클래스의 책임이 분명하지 않아서 모든 로직들이 `MemberService`으로 모이게 될 것입니다. 그 결과 외부 객체에서는 `MemberSerivce` 객체를 의존하게 됩니다. findById 메서드 하나를 사용하고 싶어도 `MemberSerivce`를 주입받아야 합니다. `MemberSerivce` 구현도 본인이 모든 구현을 하려고 하니 메서드의 라인 수도 방대해집니다. 테스트 코드 작성하기도 더욱 어렵게 만들어집니다.

Member에 대한 조회 전용 서비스 객체인 `MemberFindService`으로 네이밍을 하면 자연스럽게 객체의 책임이 부여됩니다. **객체를 행위 기반으로 바라보고 행위 기반으로 네이밍을 주어 자연스럽게 책임을 부여하는 것이 좋습니다.**

### 역할은 대체 가능성을 의미

> 책임이란 것은 외부 객체의 요청에 대한 응답이라고 생각합니다. **이러한 책임들이 모여 역할이 되고 역할은 대체 가능성을 의미합니다.** 

위에서 언급한 말을 매우 과격하게 표현하면 아래와 같습니다.

> 메서드(책임)란 것은 외부 객체의 호출에 대한 응답이고, 이러한 메서드(책임)들이 모여 클래스(역할)가되고 클래스(역할)는 인터페이스(대체 가능성)을 의미합니다.



[Service, ServiceImpl 구조에 대한 고찰](https://github.com/cheese10yun/blog-sample/tree/master/service)에 대해서 포스팅 한 내용을 다시 한번 설명드리겠습니다. 


#### 책임의 크기가 적절해야하는 이유

```java
public interface MemberService {

    Member findById(MemberId id);

    Member findByEmail(Email email);

    void changePassword(PasswordDto.ChangeRequest dto);

    Member updateName(MemberId id, Name name);
}
```
위 같은 Service, ServiceImpl 구조는 스프링 예제에서 많이 사용되는 예제입니다. 위 객체의 책임은 크게 member 조회, 수정입니다. 이 책임이 모여 클래스가 됩니다.(여기서는 MemberServiceImpl) 이 클래스(역할)는 대체 가능성을 의미합니다. **그런데 저 인터페이스가 대체가 될까요?**


findById, findByEmail, changePassword, updateName의 세부 구현이 모두 다른 구현제가 있을까요? 일반적으로는 저 모든 메서드를 세부 구현이 다르게 대체하는 구현체는 2개 이상 갖기 힘듭니다. 이렇듯 객체의 책임이 너무 많으면 대체성을 갖지 못하고 SOLID 또 한 준수할 수가 없습니다.

책임에 대한 자세한 내용은 [Service, ServiceImpl 구조에 대한 고찰](https://github.com/cheese10yun/blog-sample/tree/master/service), [단일 책임의 원칙: Single Responsibility Principle](https://github.com/cheese10yun/spring-SOLID/blob/master/docs/SRP.md)
를 참조해주세요

물론 1개의 세부 구현체만 갖더라도 인프라스트럭처 영역 같은 경우에는 인터페이스로 바라보는 것이 좋습니다. 그 외에도 다양한 이유로 인터페이스로 바라보게 하는 것이 클래래스 간의 강결합을 줄일 수 있는 효과가 있습니다. 제가 말하고 싶은 것은 그렇게 인터페이스로 두더라도 **올바른 책임의 크기에 의해서(대체 가능한 범위) 인터페이스를 나눠야 한다는 것입니다.**


### 서비스의 적절한 크기는 대체 가능성을 염두 하는 것

우선 행위 기반으로 서비스의 네이밍을 하면 자연스럽게 해당 행위에 대해서 책임이 할당됩니다. 이렇게 행위 기반으로 책임을 할당하면 자연스럽게 대체 가능성을 갖게 될 수 있습니다.

물론 이것만으로 올바르게 객체지향 설계를 할 수 있는 것은 아니지만 최소한의 객체지향 프로그래밍을 할 수 있는 작은 시발점이 될 수 있다고 생각합니다.

## SignUp Sample Code


```java
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReferralCode {

    @Column(name = "referral_code", length = 50)
    private String value;

    private ReferralCode(String value) {
        this.value = value;
    }

    public static ReferralCode of(final String value) {
        return new ReferralCode(value);
    }

    public static ReferralCode generateCode() {
        return new ReferralCode(RandomString.make(10));
    }
}

@Service
@Transactional
@RequiredArgsConstructor
public class MemberSignUpService { // (1)

    private final MemberRepository memberRepository;

    public Member doSignUp(final SignUpRequest dto) {

        if (memberRepository.existsByEmail(dto.getEmail())) { //(2)
            throw new EmailDuplicateException(dto.getEmail());
        }

        final ReferralCode referralCode = generateUniqueReferralCode();
        return memberRepository.save(dto.toEntity(referralCode));
    }

    private ReferralCode generateUniqueReferralCode() { //(3)
        ReferralCode referralCode;
        do {
            referralCode = ReferralCode.generateCode(); //(4)
        } while (memberRepository.existsByReferralCode(referralCode)); // (5)

        return referralCode;
    }

}
```
1. MemberSignUpService 네이밍을 통해서 행위 기반의 책임을 부여
2. Email의 존재 여부는 데이터베이스에 있음으로 존재 여부는 memberRepository를 사용
3. 유니크한 referralCode를 생성을 위한 메서드
4. **ReferralCode에 대한 생성은 ReferralCode 객체가 관리**
5. 해당 코드가 존재하는지는 데이터베이스에 있음으로 존재 여부는 memberRepository를 사용


ReferralCode에 대한 생성 비즈니스 로직은 ReferralCode 객체가 스스로 제어하고 있습니다. 이것이 데이터베이스에 중복 여부 검사를 서비스 레이어에서 진행합니다.
