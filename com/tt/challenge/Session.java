package com.tt.challenge;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by shannon on 11/21/15.
 */
public class Session {
    private final static Pattern SPLITTER = Pattern.compile(" ");

    private static Database db = new Database();
    public static Database getDb() {
        return db;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String cmd = scanner.nextLine();
            process(cmd);
        }
    }

    public static void process(String cmd) {
        CMD.process(cmd);
    }

    enum CMD {
        BEGIN {
            public void execute(String cmd) {
                db = new Database(db);
            }
        },
        END {
            public void execute(String cmd) {
                System.exit(0);
            }
        },
        COMMIT {
            public void execute(String cmd) {
                //merge all prev DBs into current DB
                db = db.commit();
            }
        },
        ROLLBACK {
            public void execute(String cmd) {
                db = db.rollback();
            }
        },
        UNSET {
            public void execute(String cmd) {
                String[] unsetParams = SPLITTER.split(cmd);
                db.unset(unsetParams[1]);

            }
        },
        NUMEQUALTO {
            public void execute(String cmd) {
                String[] numEqualParams = SPLITTER.split(cmd);
                Integer value = db.numEqualsTo(Integer.parseInt(numEqualParams[1]));
                System.out.println(value);
            }
        },
        GET {
            public void execute(String cmd) {
                String[] getParams = SPLITTER.split(cmd);
                Integer value = db.get(getParams[1]);
                System.out.println(value == null ? "NULL" : value);
            }
        },
        SET {
            public void execute(String cmd) {
                String[] setParams = SPLITTER.split(cmd);
                if (setParams.length != 3) {
                    System.out.println("Invalid Number of Params for SET");
                    return;
                }
                db.set(setParams[1], Integer.parseInt(setParams[2]));
            }
        };

        public static void process(String inputCmd) {
            String[] inputCmds = SPLITTER.split(inputCmd);
            CMD cmd = valueOf(inputCmds[0]);
            if (cmd != null) {
                cmd.execute(inputCmd);
            } else {
                System.out.println("Command not found. Valid commands are: BEGIN, END, COMMIT, ROLLBACK, SET, UNSET, GET, NUMEQUALTO");
            }
        }

        public abstract void execute(String cmd);
    }
}
