package ru.unit.techno.device.registration.core.impl.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "groups")
public class GroupsEntity {
    @Id
    @Column(name = "group_id")
    private String groupId;
    @Column(name = "address")
    private String address;
    @Column(name = "tag_id")
    private String tagId;
}
