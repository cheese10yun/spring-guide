# Domain Guide
도메인 레이어는 모든 레이어에서 사용됩니다. 이 때문에 자기 자신의 책임을 충분히 다하지 않으면 많은 영역에서 그 책임을 넘겨받게 됩니다. 이번에는 이 내용을 중심으로 포스팅을 진행하겠습니다.


## 목차

- [Domain Guide](#domain-guide)
  - [목차](#%EB%AA%A9%EC%B0%A8)
  - [Member 클래스](#member-%ED%81%B4%EB%9E%98%EC%8A%A4)
  - [Lombok 잘쓰기](#lombok-%EC%9E%98%EC%93%B0%EA%B8%B0)
  - [실무에서 Lombok 사용법 요약](#%EC%8B%A4%EB%AC%B4%EC%97%90%EC%84%9C-lombok-%EC%82%AC%EC%9A%A9%EB%B2%95-%EC%9A%94%EC%95%BD)
  - [JPA 어노테이션](#jpa-%EC%96%B4%EB%85%B8%ED%85%8C%EC%9D%B4%EC%85%98)
  - [Embedded 적극 활용하기](#embedded-%EC%A0%81%EA%B7%B9-%ED%99%9C%EC%9A%A9%ED%95%98%EA%B8%B0)
  - [Rich Obejct](#rich-obejct)


## Member 클래스
```java
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"email", "name"})
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email", nullable = false, unique = true, updatable = false, length = 50))
    private Email email;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "referral_code", nullable = false, unique = true, updatable = false, length = 50))
    private ReferralCode referralCode;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "first", column = @Column(name = "first_name", nullable = false)),
            @AttributeOverride(name = "middle", column = @Column(name = "middle_name")),
            @AttributeOverride(name = "last", column = @Column(name = "last_name", nullable = false))
    })
    private Name name;

    @CreationTimestamp
    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(name = "update_at", nullable = false)
    private LocalDateTime updateAt;

    @Builder
    public Member(Email email, ReferralCode referralCode, Name name) {
        this.email = email;
        this.referralCode = referralCode;
        this.name = name;
    }

    public void updateProfile(final Name name) {
        this.name = name;
    }
}
```

## Lombok 잘쓰기 
> [실무에서 Lombok 사용법](https://github.com/cheese10yun/blog-sample/tree/master/lombok)에 자세한 설명이 있습니다.

## 실무에서 Lombok 사용법 요약
* `@NoArgsConstructor(access = AccessLevel.PROTECTED)` JPA에서는 프록시 객체가 필요하므로 기본 생성자 하나가 반드시 있어야 합니다. 이때 접근지시자는 `protected`면 됩니다. (낮은 접근지시자를 사용)
* `@Data`는 사용하지 말자, 너무 많은 것들을 해준다.
* `@Setter`는 사용하지 말자, 객체는 변경 포인트를 남발하지 말자.
* `@ToString` 무한 참조가 생길 수 있다. 조심하자. (개인적으로 `@ToString(of = {"")` 권장)
* 클래스 상단의 `@Builder` X, 생성자 위에 `@Builder` OK


Lombok이 자동으로 해주는 것들을 남용하다 보면 코드의 안전성이 낮아집니다. 특히 도메인 엔티티는 모든 레이어에서 사용되는 객체이니 특별히 신경을 더 많이 써야 합니다. **이 부분은 모든 객체에 해당되는 부분입니다.**

## JPA 어노테이션

* `@Table(name = "member")` : 테이블 네임은 반드시 명시합니다. 명시하지 않으면 기본적으로 클래스 네임을 참조하기 때문에 클래스 네임 변경 시 영향을 받게 됩니다.
* `@Column` : 컬럼 네임도 클래스 네임과 마찬가지로 반드시 지정합니다.
* `nullable`, `unique`, `updatable` 등의 기능을 적극 활용합니다. 이메일일 경우 `nullable`, `unique` 같은 속성을 반드시 추가합니다.
* `@CreationTimestamp`, `@UpdateTimestamp` 어노테이션을 이용하여 생성, 수정 시간을 쉽게 설정할 수 있습니다.

## Embedded 적극 활용하기
`Embedded` 어노테이션을 이용하여 도메인 객체의 책임을 나눌 수 있습니다. **앞서 언급했지만, 객체가 자기 자신의 책임을 다하지 않으면 그 책임은 자연스럽게 다른 객체에게 넘어가게 됩니다.**

`Name`, `Address` 객체들이 대표적인 `Embedded` 대상이 되는 객체들입니다. `Member` 객체에서 `Embedded`으로 해당 객체를 가지고 있지 않았다면 다음과 같이 작성됩니다.

```java
class Member {
    @NotEmpty @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "middle_name", length = 50)
    private String middleName;

    @NotEmpty @Column(name = "last_name", length = 50)
    private String lastName;

    @NotEmpty @Column(name = "county")
    private String county;

    @NotEmpty
    @Column(name = "state")
    private String state;

    @NotEmpty
    @Column(name = "city")
    private String city;

    @NotEmpty
    @Column(name = "zip_code")
    private String zipCode;
}
```

전체 이름, 전체 주소를 가져오기 위해서는 Member 객체에서 기능을 구현해야 합니다. 즉 Member의 책임이 늘어나는 것입니다. 그뿐만이 아닙니다. `Name`, `Address`는 많은 도메인 객체에서 사용되는 객체이므로 중복 코드의 증가됩니다. 아래 코드는 `Embedded`을 활용한 코드입니다.

```java
public class Name {
    @NotEmpty @Column(name = "first_name", length = 50)
    private String first;

    @Column(name = "middle_name", length = 50)
    private String middle;

    @NotEmpty @Column(name = "last_name", length = 50)
    private String last;
}

public class Address {
    @NotEmpty @Column(name = "county")
    private String county;

    @NotEmpty @Column(name = "state")
    private String state;

    @NotEmpty @Column(name = "city")
    private String city;

    @NotEmpty @Column(name = "zip_code")
    private String zipCode;
}

public class Member {
    @Embedded private Name name;
    @Embedded private Address address;
}
```
`Name`, `Address` 객체에서 본인의 책임을 충분히 해주고 있다면 `Member` 객체도 그 부분에 대해서는 책임이 줄어들게 됩니다. 

만약 주문이라는 객체가 있다면 `Name`, `Address` 객체를 그대로 사용하면 됩니다. `Embedded`의 장점을 정리하면 아래와 같습니다.

1. 데이터 응집력 증가
2. 중복 코드 방지
3. 책임의 분산
4. 테스트 코드 작성의 용이함

## Rich Obejct
저는 이 부분이 객체지향에서 가장 기본적이며 핵심적인 것이라고 생각합니다. JPA도 객체지향 프로그래밍을 돕는(패러다임 불일치를 해결해서) 도구라고 생각합니다.

**객체지향에서 중요한 것들이 많겠지만 그중에 하나가 객체 본인의 책임을 다하는 것입니다. 여러번 반복해서 언급하지만, 객체가 자기 자신의 책임을 다하지 않으면 그 책임은 다른 객체에게 넘어가게 됩니다.**

도메인 객체들에 기본적인 getter, setter 외에는 메서드를 작성하지 않는 경우가 있습니다. 이렇게 되면 객체 본인의 책임을 다하지 않으니 이런 책임들이 다른 객체에서 이루어지게 됩니다.

다음은 쿠폰 도메인 객체 코드입니다.

```java
public class Coupon {

    @Embedded
    private CouponCode code;

    @Column(name = "used", nullable = false)
    private boolean used;

    @Column(name = "discount", nullable = false)
    private double discount;

    @Column(name = "expiration_date", nullable = false, updatable = false)
    private LocalDate expirationDate;

    public boolean isExpiration() {
        return LocalDate.now().isAfter(expirationDate);
    }

    public void use() {
        verifyExpiration();
        verifyUsed();
        this.used = true;
    }

    private void verifyUsed() {
        if (used) throw new CouponAlreadyUseException();
    }

    private void verifyExpiration() {
        if (LocalDate.now().isAfter(getExpirationDate())) throw new CouponExpireException();
    }
}

```

쿠폰에 만료 여부, 쿠폰이 사용 가능 여부, 쿠폰의 사용 등의 메서드는 어느 객체에서 제공해야 할까요? 당연히도 쿠폰 객체 자신입니다.


> 출처 : 객체지향의 사실과 오해 (정말 정말 추천드리고 싶은 도서입니다.)
> 
> 객체는 충분히 '협력적'이어야 한다. 객체는 다른 객체의 요청에 충실히 귀 기울이고 다른 객체에게 적극적으로 도움을 요청할 정도로 열린 마음을 지녀야 한다. 객체는 다른 객체의 명령에 복종하는 것이 아니라 요청에 응답할 뿐이다. 어떤 방식으로 응답할지는 객체 스스로 판단하고 결장한다. 심지어 요청에 응할지 여부도 객체 스스로 결정할 수 있다.


단순하게 getter, settet 메서드만 제공한다면 이는 협력적인 관계가 아닙니다. 그저 복종하는 관계에 지나지 않습니다. 또 요청에 응답할지 자체도 객체 스스로가 결절할 수 있게 객체의 자율성을 보장해야 합니다. `use()` 메서드 요청이 오더라도 쿠폰 객체는 해당 요청이 알맞지 않다고 판단하면 그 요청을 무시하고 예외를 발생시킵니다. 이렇듯 객체의 자율성이 있어야 합니다.

더 쉽게 설명하면 쿠폰 사용 로직을 만들기 위해서 내가 객체의 세부적인 사항을 다 알고 있어야 합니다. 쿠폰 만료일, 만료 여부, 기타 등등 수많은 세부사항을 다 알고 검사를 하고 나서 비로소 `use()` 메서드를 호출하게 됩니다. 실제 프로덕션 코드에는 이 밖에 더 많은 조건이 있으니 더 어려워집니다.

지금까지 설명드린 대부분의 경우는 도메인 객체에 국학 되지 않습니다. 모든 객체에 적용되는 설명입니다. 도메인 객체는 모든 레이어에서 사용하는 아주 주요한 객체이므로 여기서부터 올바른 책임을 제공해 주고 있지 않으면 모든 곳에서 힘들어지기 때문에 도메인 객체 가이드에 작성했습니다.