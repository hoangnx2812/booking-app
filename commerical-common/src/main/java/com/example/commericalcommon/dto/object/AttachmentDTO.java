package com.example.commericalcommon.dto.object;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttachmentDTO {
     Long id;
     String mimeType;
     String fileName;
     String thumbnail;
     String filePathSm;
     String filePathLg;
     String filePathOriginal;
     String checksum;
     String size;
     String description;
     Long createdBy;
     LocalDateTime createdAt;
     Long updatedBy;
     LocalDateTime updatedAt;
     String status;
     Integer version;
     String folder;
     String source;
     Boolean isSyncVultr;
     Boolean isSyncVultrError;

}
