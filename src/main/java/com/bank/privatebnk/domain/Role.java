package com.bank.privatebnk.domain;

import com.bank.privatebnk.domain.enumeration.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


@Table(name="tbl_role")
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length=30,nullable=false)
    private UserRole name;

    @Override
    public String toString() {
        return "Role [name=" + name + "]";
    }


}