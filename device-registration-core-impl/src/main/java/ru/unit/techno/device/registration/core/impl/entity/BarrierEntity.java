package ru.unit.techno.device.registration.core.impl.entity;

import lombok.Data;
import ru.unit.techno.device.registration.api.enums.DeviceType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "barriers")
@SequenceGenerator(name = "squd_barrier_id_seq", sequenceName = "squd_barrier_id_seq")
public class BarrierEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "squd_barrier_id_seq")
    private Long id;
    @Column(name = "device_id")
    private Long deviceId;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private DeviceType type;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private GroupsEntity group;
}
