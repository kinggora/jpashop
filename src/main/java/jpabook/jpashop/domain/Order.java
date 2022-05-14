package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders") //sql 예약어(ORDER)와 중복 방지를 위한 관습
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    //주문 다수:멤버 1. @XToOne default fetch=EAGER
    @ManyToOne(fetch = FetchType.LAZY)
    //연관관계의 주인. 외래키 매핑 -> 해당 객체 참조
    @JoinColumn(name = "member_id")
    private Member member; //주문회원

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) //주문 1:주문상품 다수
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) //주문 1:배송지 1
    @JoinColumn(name = "delivery_id")
    private Delivery delivery; //배송정보

    private LocalDateTime orderDate; //주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문상태 [ORDER, CANCEL]

    //== 생성 메서드 ==
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.ORDER);
        return order;
    }

    //== 비지니스 로직 ==
    /** 주문 취소 **/
    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가합니다.");
        }
        setStatus(OrderStatus.CANCEL);
        for(OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }

    //== 조회 로직 ==
    /** 전체 주문 가격 조회 **/
    public int getTotalPrice(){
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            //OrderItem을 뜯어서 "구매 가격*수량" 을 가져온다.
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }


    //== 연관관계 메서드 ==
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

}



