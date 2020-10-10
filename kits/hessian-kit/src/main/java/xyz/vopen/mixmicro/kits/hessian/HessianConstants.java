package xyz.vopen.mixmicro.kits.hessian;

/**
 * {@link HessianConstants}
 *
 * <p>Class HessianConstants Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/9/27
 */
public class HessianConstants {

  /**
   * enable blacklist of serializer, default is true
   */
  public static final String SERIALIZE_BLACKLIST_ENABLE         = "serialize.blacklist.enable";
  /**
   * default value of SERIALIZE_BLACKLIST_ENABLE
   */
  public static final String DEFAULT_SERIALIZE_BLACKLIST_ENABLE = "true";
  /**
   * the custom blacklist file path of serializer, default is DEFAULT_SERIALIZE_BLACK_LIST
   *
   * @see #DEFAULT_SERIALIZE_BLACK_LIST
   */
  public static final String SERIALIZE_BLACKLIST_FILE           = "serialize.blacklist.file";
  /**
   * default value of SERIALIZE_BLACKLIST_FILE
   */
  public static final String DEFAULT_SERIALIZE_BLACK_LIST       = "security/serialize.blacklist";
  /**
   * whether to create ContextSerializeFactory of parent classloader, default is true
   *
   * @since 4.0.3
   */
  public final static String HESSIAN_PARENT_CONTEXT_CREATE      = "hessian.parent.context.create";
}
