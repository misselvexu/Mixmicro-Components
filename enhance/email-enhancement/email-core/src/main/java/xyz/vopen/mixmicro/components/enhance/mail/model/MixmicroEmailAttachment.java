package xyz.vopen.mixmicro.components.enhance.mail.model;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.Serializable;

public interface MixmicroEmailAttachment extends Serializable {

  String getAttachmentName();

  byte[] getAttachmentData();

  MediaType getContentType() throws IOException;

  default ByteArrayResource getInputStream() {
    return new ByteArrayResource(getAttachmentData());
  }

}