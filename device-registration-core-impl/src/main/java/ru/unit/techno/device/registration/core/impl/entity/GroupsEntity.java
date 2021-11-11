package ru.unit.techno.device.registration.core.impl.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "groups")
public class GroupsEntity {
    @Id
    @Column(name = "group_id")
    private Long groupId;
    @Column(name = "address")
    private String address;
}
