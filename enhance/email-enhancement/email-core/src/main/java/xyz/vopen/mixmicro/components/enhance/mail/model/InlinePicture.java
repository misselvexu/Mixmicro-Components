package xyz.vopen.mixmicro.components.enhance.mail.model;

import java.io.File;
import java.io.Serializable;

public interface InlinePicture extends Serializable {

  ImageType getImageType();

  File getFile();

  /**
   * Return the name used in the template with the path to the resource.
   * The name has to be replaced by a proper cid.
   *
   * @return the name used in the template, included any path to folders.
   */
  String getTemplateName();

}
