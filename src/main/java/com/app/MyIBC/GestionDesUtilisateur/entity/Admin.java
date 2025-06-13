package com.app.MyIBC.GestionDesUtilisateur.entity;

import com.app.MyIBC.Authentification.entity.User;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Admin extends User {
}
