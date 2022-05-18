package jpabook.jpashop.web;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookForm {

    //for update
    private Long id;

    //Item
    private String name;
    private int price;
    private int stockQuantity;

    //Book
    private String author;
    private String isbn;

}
