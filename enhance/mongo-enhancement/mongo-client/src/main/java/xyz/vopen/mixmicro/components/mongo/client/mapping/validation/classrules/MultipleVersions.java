package xyz.vopen.mixmicro.components.mongo.client.mapping.validation.classrules;

import xyz.vopen.mixmicro.components.mongo.client.annotations.Version;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedClass;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedField;
import xyz.vopen.mixmicro.components.mongo.client.mapping.Mapper;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ClassConstraint;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ConstraintViolation;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ConstraintViolation.Level;

import java.util.List;
import java.util.Set;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public class MultipleVersions implements ClassConstraint {

  @Override
  public void check(final Mapper mapper, final MappedClass mc, final Set<ConstraintViolation> ve) {
    final List<MappedField> versionFields = mc.getFieldsAnnotatedWith(Version.class);
    if (versionFields.size() > 1) {
      ve.add(
          new ConstraintViolation(
              Level.FATAL,
              mc,
              getClass(),
              "Multiple @"
                  + Version.class
                  + " annotations are not allowed. ("
                  + new FieldEnumString(versionFields)));
    }
  }
}
