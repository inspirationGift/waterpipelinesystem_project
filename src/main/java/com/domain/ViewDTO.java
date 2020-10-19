package com.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ViewDTO {
    int id;
    boolean exist;
    Integer length;

    @Override
    public String toString() {
        return "{#" + id + ", route exists: " + exist + ", length: " + (!exist ? "" : length) + "}";
    }

}
