package xyz.vopen.mixmicro.kits.redis.embedded;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import xyz.vopen.mixmicro.kits.redis.embedded.util.Architecture;
import xyz.vopen.mixmicro.kits.redis.embedded.util.JarUtil;
import xyz.vopen.mixmicro.kits.redis.embedded.util.OS;
import xyz.vopen.mixmicro.kits.redis.embedded.util.OsArchitecture;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * {@link RedisExecProvider}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class RedisExecProvider {

  private final Map<OsArchitecture, String> executables = Maps.newHashMap();

  public static RedisExecProvider defaultProvider() {
    return new RedisExecProvider();
  }

  private RedisExecProvider() {
    initExecutables();
  }

  private void initExecutables() {
    executables.put(OsArchitecture.WINDOWS_x86, "redis-server-2.8.19.exe");
    executables.put(OsArchitecture.WINDOWS_x86_64, "redis-server-2.8.19.exe");

    executables.put(OsArchitecture.UNIX_x86, "redis-server-2.8.19-32");
    executables.put(OsArchitecture.UNIX_x86_64, "redis-server-2.8.19");

    executables.put(OsArchitecture.MAC_OS_X_x86, "redis-server-2.8.19.app");
    executables.put(OsArchitecture.MAC_OS_X_x86_64, "redis-server-2.8.19.app");
  }

  public RedisExecProvider override(OS os, String executable) {
    Preconditions.checkNotNull(executable);
    for (Architecture arch : Architecture.values()) {
      override(os, arch, executable);
    }
    return this;
  }

  public RedisExecProvider override(OS os, Architecture arch, String executable) {
    Preconditions.checkNotNull(executable);
    executables.put(new OsArchitecture(os, arch), executable);
    return this;
  }

  public File get() throws IOException {
    OsArchitecture osArch = OsArchitecture.detect();
    String executablePath = executables.get(osArch);
    return fileExists(executablePath) ?
        new File(executablePath) :
        JarUtil.extractExecutableFromJar(executablePath);

  }

  private boolean fileExists(String executablePath) {
    return new File(executablePath).exists();
  }
}
