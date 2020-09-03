package xyz.vopen.mixmicro.components.mongo.client.mapping.validation.classrules;

import xyz.vopen.mixmicro.components.mongo.client.annotations.Embedded;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedClass;
import xyz.vopen.mixmicro.components.mongo.client.mapping.Mapper;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ClassConstraint;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ConstraintViolation;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ConstraintViolation.Level;

import java.util.Set;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public class EmbeddedAndValue implements ClassConstraint {

  @Override
  public void check(final Mapper mapper, final MappedClass mc, final Set<ConstraintViolation> ve) {

    if (mc.getEmbeddedAnnotation() != null
        && !mc.getEmbeddedAnnotation().value().equals(Mapper.IGNORED_FIELDNAME)) {
      ve.add(
          new ConstraintViolation(
              Level.FATAL,
              mc,
              getClass(),
              "@"
                  + Embedded.class.getSimpleName()
                  + " classes cannot specify a fieldName value(); this is on applicable on fields"));
    }
  }
}
