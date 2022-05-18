package com.medirec.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * Image类
 *
 * @author zsm
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Image extends Model<Image> {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private Long id;

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

    public Image() {
        super();
    }

    public Image(Long userId, String fileName, Boolean sinogram, String suffix, String inputUrl, String demoUrl, String outputUrl, LocalDateTime uploadTime) {
        this.userId = userId;
        this.fileName = fileName;
        this.sinogram = sinogram;
        this.suffix = suffix;
        this.inputUrl = inputUrl;
        this.demoUrl = demoUrl;
        this.outputUrl = outputUrl;
        this.uploadTime = uploadTime;
    }

    @Override
    public String toString() {
        return "Image{" +
                "userId=" + userId +
                ", id=" + id +
                ", patId=" + patId +
                ", fileName='" + fileName + '\'' +
                ", sinogram=" + sinogram +
                ", suffix='" + suffix + '\'' +
                ", height=" + height +
                ", width=" + width +
                ", inputUrl='" + inputUrl + '\'' +
                ", demoUrl='" + demoUrl + '\'' +
                ", outputUrl='" + outputUrl + '\'' +
                ", uploadTime=" + uploadTime +
                '}';
    }

    @Override
    protected Serializable pkVal() {
        return this.userId;
    }

}
