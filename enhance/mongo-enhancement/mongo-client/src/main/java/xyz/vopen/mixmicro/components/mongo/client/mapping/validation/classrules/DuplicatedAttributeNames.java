package xyz.vopen.mixmicro.components.mongo.client.mapping.validation.classrules;

import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedClass;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedField;
import xyz.vopen.mixmicro.components.mongo.client.mapping.Mapper;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ClassConstraint;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ConstraintViolation;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ConstraintViolation.Level;

import java.util.HashSet;
import java.util.Set;

/** @author josephpachod */
public class DuplicatedAttributeNames implements ClassConstraint {

  @Override
  public void check(final Mapper mapper, final MappedClass mc, final Set<ConstraintViolation> ve) {
    final Set<String> foundNames = new HashSet<String>();
    for (final MappedField mappedField : mc.getPersistenceFields()) {
      for (final String name : mappedField.getLoadNames()) {
        if (!foundNames.add(name)) {
          ve.add(
              new ConstraintViolation(
                  Level.FATAL,
                  mc,
                  mappedField,
                  getClass(),
                  "Mapping to MongoDB field name '"
                      + name
                      + "' is duplicated; you cannot map different java fields to the same MongoDB field."));
        }
      }
    }
  }
}
