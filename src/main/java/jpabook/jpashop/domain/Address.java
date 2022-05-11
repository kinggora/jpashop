package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.*;

@Embeddable
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address() {
    }

    //임베디드 타입은 생성자를 통해 초기화하고 setter를 만들지 않음으로써 변경 불가능한 클래스로 만든다.
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}