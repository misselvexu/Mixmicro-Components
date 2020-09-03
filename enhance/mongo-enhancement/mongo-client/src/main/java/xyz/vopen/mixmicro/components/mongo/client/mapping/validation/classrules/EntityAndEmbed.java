package xyz.vopen.mixmicro.components.mongo.client.mapping.validation.classrules;

import xyz.vopen.mixmicro.components.mongo.client.annotations.Embedded;
import xyz.vopen.mixmicro.components.mongo.client.annotations.Entity;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedClass;
import xyz.vopen.mixmicro.components.mongo.client.mapping.Mapper;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ClassConstraint;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ConstraintViolation;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ConstraintViolation.Level;

import java.util.Set;

import static java.lang.String.format;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public class EntityAndEmbed implements ClassConstraint {

  @Override
  public void check(final Mapper mapper, final MappedClass mc, final Set<ConstraintViolation> ve) {

    if (mc.getEntityAnnotation() != null && mc.getEmbeddedAnnotation() != null) {
      ve.add(
          new ConstraintViolation(
              Level.FATAL,
              mc,
              getClass(),
              format(
                  "Cannot have both @%s and @%s annotation at class level.",
                  Entity.class.getSimpleName(), Embedded.class.getSimpleName())));
    }
  }
}
