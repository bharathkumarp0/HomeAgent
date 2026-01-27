package com.HomeAgent.DTO;

import com.HomeAgent.Model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DocumentDTO {
 private Long documentId;
 private String documentName;
 private String physicalLocation;
 private String userEmail;
}