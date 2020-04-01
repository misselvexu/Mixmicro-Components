package xyz.vopen.mixmicro.components.enhance.dbm.db;

import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;

import java.util.List;

/**
 * Netezza database type information used to create the tables, etc..
 *
 * <p><b>NOTE:</b> This is the initial take on this database type. We hope to get access to an
 * external database for testing. Please contact us if you'd like to help with this class.
 *
 * @author Richard Kooijman
 */
public class NetezzaDatabaseType extends BaseDatabaseType {

  private static final String DATABASE_URL_PORTION = "netezza";
  private static final String DRIVER_CLASS_NAME = "org.netezza.Driver";
  private static final String DATABASE_NAME = "Netezza";

  @Override
  public boolean isDatabaseUrlThisType(String url, String dbTypePart) {
    return DATABASE_URL_PORTION.equals(dbTypePart);
  }

  @Override
  protected String[] getDriverClassNames() {
    return new String[] {DRIVER_CLASS_NAME};
  }

  @Override
  public String getDatabaseName() {
    return DATABASE_NAME;
  }

  @Override
  protected void appendByteType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
    sb.append("BYTEINT");
  }

  @Override
  protected void configureGeneratedIdSequence(
      StringBuilder sb,
      FieldType fieldType,
      List<String> statementsBefore,
      List<String> additionalArgs,
      List<String> queriesAfter) {
    String sequenceName = fieldType.getGeneratedIdSequence();
    // needs to match dropColumnArg()
    StringBuilder seqSb = new StringBuilder(64);
    seqSb.append("CREATE SEQUENCE ");
    // when it is created, it needs to be escaped specially
    appendEscapedEntityName(seqSb, sequenceName);
    statementsBefore.add(seqSb.toString());

    configureId(sb, fieldType, statementsBefore, additionalArgs, queriesAfter);
  }

  @Override
  public void dropColumnArg(
      FieldType fieldType, List<String> statementsBefore, List<String> statementsAfter) {
    if (fieldType.isGeneratedIdSequence()) {
      StringBuilder sb = new StringBuilder(64);
      sb.append("DROP SEQUENCE ");
      appendEscapedEntityName(sb, fieldType.getGeneratedIdSequence());
      statementsAfter.add(sb.toString());
    }
  }

  @Override
  public void appendEscapedEntityName(StringBuilder sb, String name) {
    sb.append('\"').append(name).append('\"');
  }

  @Override
  public boolean isIdSequenceNeeded() {
    return true;
  }

  @Override
  public void appendSelectNextValFromSequence(StringBuilder sb, String sequenceName) {
    sb.append("SELECT NEXT VALUE FOR ");
    // this is word and not entity unfortunately
    appendEscapedWord(sb, sequenceName);
  }
}
