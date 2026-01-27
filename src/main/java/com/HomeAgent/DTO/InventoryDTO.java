package com.HomeAgent.DTO;

import com.HomeAgent.Model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Setter
@Getter
public class InventoryDTO {


    private int id;

    private String itemName;
    private String category;

    private LocalDate purchaseDate;
    private LocalDate expiryDate;

    private String userEmail;
}
