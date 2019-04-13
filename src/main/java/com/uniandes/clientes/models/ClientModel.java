package com.uniandes.clientes.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonProperty;

@DynamoDBTable(tableName = "clients")
public class ClientModel {

        private int id;
        private String name;
        private String phone;
        private String email;

        public ClientModel(int id, String name, String phone, String email) {
            super();
            this.id = id;
            this.name = name;
            this.phone = phone;
            this.email = email;
        }

    public ClientModel() {
        super();
    }
        @JsonProperty("id")
        @DynamoDBHashKey(attributeName = "id")
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @JsonProperty("name")
        @DynamoDBHashKey(attributeName = "name")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    @JsonProperty("phone")
    @DynamoDBHashKey(attributeName = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JsonProperty("email")
    @DynamoDBHashKey(attributeName = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}