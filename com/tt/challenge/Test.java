package com.tt.challenge;

/**
 * Created by shannon on 11/21/15.
 */
public class Test {
    public static void main(String[] args) {
        Session.process("SET a 50");
        Session.process("BEGIN");
        Session.process("GET a");
        Session.process("SET a 60");
        Session.process("BEGIN");
        Session.process("UNSET a");
        Session.process("GET a");
        Session.process("ROLLBACK");
        Session.process("GET a");
        Session.process("COMMIT");
        Session.process("GET a");

        System.out.println(Session.getDb().get("a").equals(60));
    }
}
