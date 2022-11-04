package lk.directpay.task.entity;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String code;
    @Column(columnDefinition = "integer default 10")
    private int minLength;
    @Column(columnDefinition = "integer default 20")
    private int maxLength;
    @Column(columnDefinition = "integer default 0")
    private int state;

}
