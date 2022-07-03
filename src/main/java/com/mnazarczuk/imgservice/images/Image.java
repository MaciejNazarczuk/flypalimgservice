package com.mnazarczuk.imgservice.images;

import javax.persistence.*;

@Entity
public class Image {
    @Id
    @GeneratedValue
    private Long id;
    @Lob
    @Column(
        columnDefinition ="BLOB(30M)"
    )
    private byte[] data;
    private String code;
    
    
    public Long getId() {
        return id;
    }
    
    public byte[] getData() {
        return data;
    }
    
    public String getCode() {
        return code;
    }
    
    public Image() {
    }
    
    public Image(byte[] data, String code) {
        this.data = data;
        this.code = code;
    }
}
