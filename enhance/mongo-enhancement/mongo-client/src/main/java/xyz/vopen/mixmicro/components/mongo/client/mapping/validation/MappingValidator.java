package xyz.vopen.mixmicro.components.mongo.client.mapping.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.mixmicro.components.mongo.client.ObjectFactory;
import xyz.vopen.mixmicro.components.mongo.client.annotations.Embedded;
import xyz.vopen.mixmicro.components.mongo.client.annotations.Property;
import xyz.vopen.mixmicro.components.mongo.client.annotations.Reference;
import xyz.vopen.mixmicro.components.mongo.client.annotations.Serialized;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedClass;
import xyz.vopen.mixmicro.components.mongo.client.mapping.Mapper;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.classrules.DuplicatedAttributeNames;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.classrules.EmbeddedAndId;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.classrules.EmbeddedAndValue;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.classrules.EntityAndEmbed;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.classrules.EntityCannotBeMapOrIterable;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.classrules.MultipleId;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.classrules.MultipleVersions;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.classrules.NoId;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.fieldrules.ContradictingFieldAnnotation;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.fieldrules.LazyReferenceMissingDependencies;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.fieldrules.LazyReferenceOnArray;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.fieldrules.MapKeyDifferentFromString;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.fieldrules.MapNotSerializable;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.fieldrules.MisplacedProperty;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.fieldrules.ReferenceToUnidentifiable;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.fieldrules.VersionMisuse;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static java.util.Collections.sort;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public class MappingValidator {

  private static final Logger LOG = LoggerFactory.getLogger(MappingValidator.class);
  private ObjectFactory creator;

  /**
   * Creates a mapping validator
   *
   * @param objectFactory the object factory to be used when creating throw away instances to use in
   *     validation
   */
  public MappingValidator(final ObjectFactory objectFactory) {
    creator = objectFactory;
  }

  /**
   * Validates a MappedClass
   *
   * @param mappedClass the MappedClass to validate
   * @param mapper the Mapper to use for validation
   */
  @Deprecated
  public void validate(final Mapper mapper, final MappedClass mappedClass) {
    validate(mapper, singletonList(mappedClass));
  }

  /**
   * Validates a List of MappedClasses
   *
   * @param classes the MappedClasses to validate
   * @param mapper the Mapper to use for validation
   */
  public void validate(final Mapper mapper, final List<MappedClass> classes) {
    final Set<ConstraintViolation> ve =
        new TreeSet<ConstraintViolation>(
            new Comparator<ConstraintViolation>() {

              @Override
              public int compare(final ConstraintViolation o1, final ConstraintViolation o2) {
                return o1.getLevel().ordinal() > o2.getLevel().ordinal() ? -1 : 1;
              }
            });

    final List<ClassConstraint> rules = getConstraints();
    for (final MappedClass c : classes) {
      for (final ClassConstraint v : rules) {
        v.check(mapper, c, ve);
      }
    }

    if (!ve.isEmpty()) {
      final ConstraintViolation worst = ve.iterator().next();
      final ConstraintViolation.Level maxLevel = worst.getLevel();
      if (maxLevel.ordinal() >= ConstraintViolation.Level.FATAL.ordinal()) {
        throw new ConstraintViolationException(ve);
      }

      // sort by class to make it more readable
      final List<LogLine> l = new ArrayList<LogLine>();
      for (final ConstraintViolation v : ve) {
        l.add(new LogLine(v));
      }
      sort(l);

      for (final LogLine line : l) {
        line.log(LOG);
      }
    }
  }

  private List<ClassConstraint> getConstraints() {
    final List<ClassConstraint> constraints = new ArrayList<ClassConstraint>(32);

    // normally, i do this with scanning the classpath, but that´d bring
    // another dependency ;)

    // class-level
    constraints.add(new MultipleId());
    constraints.add(new MultipleVersions());
    constraints.add(new NoId());
    constraints.add(new EmbeddedAndId());
    constraints.add(new EntityAndEmbed());
    constraints.add(new EmbeddedAndValue());
    constraints.add(new EntityCannotBeMapOrIterable());
    constraints.add(new DuplicatedAttributeNames());
    // constraints.add(new ContainsEmbeddedWithId());
    // field-level
    constraints.add(new MisplacedProperty());
    constraints.add(new ReferenceToUnidentifiable());
    constraints.add(new LazyReferenceMissingDependencies());
    constraints.add(new LazyReferenceOnArray());
    constraints.add(new MapKeyDifferentFromString());
    constraints.add(new MapNotSerializable());
    constraints.add(new VersionMisuse(creator));
    //
    constraints.add(new ContradictingFieldAnnotation(Reference.class, Serialized.class));
    constraints.add(new ContradictingFieldAnnotation(Reference.class, Property.class));
    constraints.add(new ContradictingFieldAnnotation(Reference.class, Embedded.class));
    //
    constraints.add(new ContradictingFieldAnnotation(Embedded.class, Serialized.class));
    constraints.add(new ContradictingFieldAnnotation(Embedded.class, Property.class));
    //
    constraints.add(new ContradictingFieldAnnotation(Property.class, Serialized.class));

    return constraints;
  }

  static class LogLine implements Comparable<LogLine> {
    private final ConstraintViolation v;

    LogLine(final ConstraintViolation v) {
      this.v = v;
    }

    @Override
    public int compareTo(final LogLine o) {
      return v.getPrefix().compareTo(o.v.getPrefix());
    }

    @Override
    public int hashCode() {
      return v.hashCode();
    }

    @Override
    public boolean equals(final Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      final LogLine logLine = (LogLine) o;

      return v.equals(logLine.v);
    }

    void log(final Logger logger) {
      switch (v.getLevel()) {
        case SEVERE:
          logger.error(v.render());
          break;
        case WARNING:
          logger.warn(v.render());
          break;
        case INFO:
          logger.info(v.render());
          break;
        case MINOR:
          logger.debug(v.render());
          break;
        default:
          throw new IllegalStateException(
              format(
                  "Cannot log %s of Level %s",
                  ConstraintViolation.class.getSimpleName(), v.getLevel()));
      }
    }
  }
}
