package xyz.vopen.mixmicro.components.enhance.mail.model.impl;

import xyz.vopen.mixmicro.components.enhance.mail.model.ImageType;
import xyz.vopen.mixmicro.components.enhance.mail.model.InlinePicture;
import lombok.*;

import java.io.File;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DefaultInlinePicture implements InlinePicture {

  private static final long serialVersionUID = 1040548679790587446L;

  @NonNull
  private ImageType imageType;

  @NonNull
  private File file;

  @NonNull
  private String templateName;

}