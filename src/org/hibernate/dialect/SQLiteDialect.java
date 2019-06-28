package org.hibernate.dialect;

/*
 * The author disclaims copyright to this source code.  In place of
 * a legal notice, here is a blessing:
 *
 *    May you do good and not evil.
 *    May you find forgiveness for yourself and forgive others.
 *    May you share freely, never taking more than you give.
 *
 */

import org.hibernate.dialect.function.*;
import org.hibernate.type.StandardBasicTypes;

import java.sql.Types;

/**
 *
 * @author tmartinez
 */
public class SQLiteDialect extends Dialect {

    /**
     *
     */
    public SQLiteDialect() {
    registerColumnType(Types.BIT, "boolean");
    registerColumnType(Types.TINYINT, "tinyint");
    registerColumnType(Types.SMALLINT, "smallint");
    registerColumnType(Types.INTEGER, "integer");
    registerColumnType(Types.BIGINT, "bigint");
    registerColumnType(Types.FLOAT, "float");
    registerColumnType(Types.REAL, "real");
    registerColumnType(Types.DOUBLE, "double");
    registerColumnType(Types.NUMERIC, "numeric($p, $s)");
    registerColumnType(Types.DECIMAL, "decimal");
    registerColumnType(Types.CHAR, "char");
    registerColumnType(Types.VARCHAR, "varchar($l)");
    registerColumnType(Types.LONGVARCHAR, "longvarchar");
    registerColumnType(Types.DATE, "date");
    registerColumnType(Types.TIME, "time");
    registerColumnType(Types.TIMESTAMP, "datetime");
    registerColumnType(Types.BINARY, "blob");
    registerColumnType(Types.VARBINARY, "blob");
    registerColumnType(Types.LONGVARBINARY, "blob");
    registerColumnType(Types.BLOB, "blob");
    registerColumnType(Types.CLOB, "clob");
    registerColumnType(Types.BOOLEAN, "boolean");

    //registerFunction( "abs", new StandardSQLFunction("abs") );
    registerFunction( "concat", new VarArgsSQLFunction(StandardBasicTypes.STRING, "", "||", "") );
    //registerFunction( "length", new StandardSQLFunction("length", StandardBasicTypes.LONG) );
    //registerFunction( "lower", new StandardSQLFunction("lower") );
    registerFunction( "mod", new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "?1 % ?2" ) );
    registerFunction( "quote", new StandardSQLFunction("quote", StandardBasicTypes.STRING) );
    registerFunction( "random", new NoArgSQLFunction("random", StandardBasicTypes.INTEGER) );
    registerFunction( "round", new StandardSQLFunction("round") );
    registerFunction( "substr", new StandardSQLFunction("substr", StandardBasicTypes.STRING) );
    registerFunction( "substring", new SQLFunctionTemplate( StandardBasicTypes.STRING, "substr(?1, ?2, ?3)" ) );
    registerFunction( "trim", new AbstractAnsiTrimEmulationFunction() {
        protected SQLFunction resolveBothSpaceTrimFunction() {
          return new SQLFunctionTemplate(StandardBasicTypes.STRING, "trim(?1)");
        }

        protected SQLFunction resolveBothSpaceTrimFromFunction() {
          return new SQLFunctionTemplate(StandardBasicTypes.STRING, "trim(?2)");
        }

        protected SQLFunction resolveLeadingSpaceTrimFunction() {
          return new SQLFunctionTemplate(StandardBasicTypes.STRING, "ltrim(?1)");
        }

        protected SQLFunction resolveTrailingSpaceTrimFunction() {
          return new SQLFunctionTemplate(StandardBasicTypes.STRING, "rtrim(?1)");
        }

        protected SQLFunction resolveBothTrimFunction() {
          return new SQLFunctionTemplate(StandardBasicTypes.STRING, "trim(?1, ?2)");
        }

        protected SQLFunction resolveLeadingTrimFunction() {
          return new SQLFunctionTemplate(StandardBasicTypes.STRING, "ltrim(?1, ?2)");
        }

        protected SQLFunction resolveTrailingTrimFunction() {
          return new SQLFunctionTemplate(StandardBasicTypes.STRING, "rtrim(?1, ?2)");
        }
    } );
    //registerFunction( "upper", new StandardSQLFunction("upper") );
  }

    /**
     *
     * @return
     */
    public boolean supportsIdentityColumns() {
    return true;
  }

  /*
  public boolean supportsInsertSelectIdentity() {
    return true; // As specify in NHibernate dialect
  }
  */

    /**
     *
     * @return
     */
    

  public boolean hasDataTypeInIdentityColumn() {
    return false; // As specify in NHibernate dialect
  }

  /*
  public String appendIdentitySelectToInsert(String insertString) {
    return new StringBuffer(insertString.length()+30). // As specify in NHibernate dialect
      append(insertString).
      append("; ").append(getIdentitySelectString()).
      toString();
  }
  */

    /**
     *
     * @return
     */
    

  public String getIdentityColumnString() {
    // return "integer primary key autoincrement";
    return "integer";
  }

    /**
     *
     * @return
     */
    public String getIdentitySelectString() {
    return "select last_insert_rowid()";
  }

    /**
     *
     * @return
     */
    public boolean supportsLimit() {
    return true;
  }

    /**
     *
     * @return
     */
    public boolean bindLimitParametersInReverseOrder() {
    return true;
  }

    /**
     *
     * @param query
     * @param hasOffset
     * @return
     */
    protected String getLimitString(String query, boolean hasOffset) {
    return new StringBuffer(query.length()+20).
      append(query).
      append(hasOffset ? " limit ? offset ?" : " limit ?").
      toString();
  }

    /**
     *
     * @return
     */
    public boolean supportsTemporaryTables() {
    return true;
  }

    /**
     *
     * @return
     */
    public String getCreateTemporaryTableString() {
    return "create temporary table if not exists";
  }

    /**
     *
     * @return
     */
    public boolean dropTemporaryTableAfterUse() {
    return true; // TODO Validate
  }

    /**
     *
     * @return
     */
    public boolean supportsCurrentTimestampSelection() {
    return true;
  }

    /**
     *
     * @return
     */
    public boolean isCurrentTimestampSelectStringCallable() {
    return false;
  }

    /**
     *
     * @return
     */
    public String getCurrentTimestampSelectString() {
    return "select current_timestamp";
  }

    /**
     *
     * @return
     */
    public boolean supportsUnionAll() {
    return true;
  }

    /**
     *
     * @return
     */
    public boolean hasAlterTable() {
    return false; // As specify in NHibernate dialect
  }

    /**
     *
     * @return
     */
    public boolean dropConstraints() {
    return false;
  }

  public String getAddColumnString() {
    return "add column";
  }

    public String getForUpdateString() {
    return "";
  }

    /**
     *
     * @return
     */
    public boolean supportsOuterJoinForUpdate() {
    return false;
  }

    /**
     *
     * @return
     */
    public String getDropForeignKeyString() {
    throw new UnsupportedOperationException("No drop foreign key syntax supported by SQLiteDialect");
  }

    /**
     *
     * @param constraintName
     * @param foreignKey
     * @param referencedTable
     * @param primaryKey
     * @param referencesPrimaryKey
     * @return
     */
    public String getAddForeignKeyConstraintString(String constraintName,
      String[] foreignKey, String referencedTable, String[] primaryKey,
      boolean referencesPrimaryKey) {
    throw new UnsupportedOperationException("No add foreign key syntax supported by SQLiteDialect");
  }

    /**
     *
     * @param constraintName
     * @return
     */
    public String getAddPrimaryKeyConstraintString(String constraintName) {
    throw new UnsupportedOperationException("No add primary key syntax supported by SQLiteDialect");
  }

    /**
     *
     * @return
     */
    public boolean supportsIfExistsBeforeTableName() {
    return true;
  }

    /**
     *
     * @return
     */
    public boolean supportsCascadeDelete() {
    return true;
  }

  /* not case insensitive for unicode characters by default (ICU extension needed)
  public boolean supportsCaseInsensitiveLike() {
    return true;
  }
  */

    /**
     *
     * @return
     */
    

  public boolean supportsTupleDistinctCounts() {
    return false;
  }

    /**
     *
     * @return
     */
    public String getSelectGUIDString() {
    return "select hex(randomblob(16))";
  }
}