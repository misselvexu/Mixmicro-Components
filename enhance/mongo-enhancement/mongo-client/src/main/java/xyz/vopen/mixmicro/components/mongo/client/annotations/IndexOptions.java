package xyz.vopen.mixmicro.components.mongo.client.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Defines the options to be used when declaring an index. */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
public @interface IndexOptions {
  /** @return if true, create the index in the background */
  boolean background() default false;

  /** @return if true, disables validation for the field name */
  boolean disableValidation() default false;

  /**
   * @return if true, tells the unique index to drop duplicates silently when creating; only the
   *     first will be kept
   * @deprecated Support for this has been removed from the server. This value is ignored.
   */
  @Deprecated
  boolean dropDups() default false;

  /** @return defines the time to live for documents in the collection */
  int expireAfterSeconds() default -1;

  /** @return The default language for the index. */
  String language() default "";

  /** @return The field to use to override the default language. */
  String languageOverride() default "";

  /**
   * @return The name of the index to create; default is to let the mongodb create a name (in the
   *     form of key1_1/-1_key2_1/-1...)
   */
  String name() default "";

  /** @return if true, create the index with the sparse option */
  boolean sparse() default false;

  /**
   * @return if true, creates the index as a unique value index; inserting duplicates values in this
   *     field will cause errors
   */
  boolean unique() default false;

  /**
   * @return the filter to be used for this index
   * @since 1.3
   */
  String partialFilter() default "";

  /**
   * @return the collation to be used for this index
   * @since 1.3
   */
  Collation collation() default @Collation(locale = "");
}
