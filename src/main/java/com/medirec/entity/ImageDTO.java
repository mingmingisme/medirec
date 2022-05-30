package com.medirec.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ImageDTO {
    private Long userId;

    private String id;

    private Long patId;

    private String fileName;

    private Boolean sinogram;

    private String suffix;

    private Integer height;

    private Integer width;

    private String inputUrl;

    private String demoUrl;

    private String outputUrl;

    private LocalDateTime uploadTime;

    public ImageDTO() {

    }


    public ImageDTO(Image image) {
        this.userId = image.getUserId();
        this.id = String.valueOf(image.getId());
        this.patId = image.getPatId();
        this.fileName = image.getFileName();
        this.sinogram = image.getSinogram();
        this.suffix = image.getSuffix();
        this.height = image.getHeight();
        this.width = image.getWidth();
        this.inputUrl = image.getInputUrl();
        this.demoUrl = image.getDemoUrl();
        this.outputUrl = image.getOutputUrl();
        this.uploadTime = image.getUploadTime();
    }
}
