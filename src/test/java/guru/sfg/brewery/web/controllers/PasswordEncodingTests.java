package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.DigestUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordEncodingTests {

    private static final String PASSWORD = "password";

    @Test
    void testBcrypt10() {
        PasswordEncoder bCrypt15 = new BCryptPasswordEncoder(10);
        System.out.println("bCrypt.encode(\"tiger\") = " + bCrypt15.encode("tiger"));
    }

    @Test
    @Disabled("Neemt teveel tijd")
    void testBcrypt15() {
        PasswordEncoder bCrypt15 = new BCryptPasswordEncoder(15);
        System.out.println("bCrypt.encode(\"tiger\") = " + bCrypt15.encode("tiger"));
    }

    @Test
    @Disabled("Neemt teveel tijd")
    void testBcrypt() {
        PasswordEncoder bCrypt = new BCryptPasswordEncoder();

        System.out.println("bCrypt.encode(PASSWORD) = " + bCrypt.encode(PASSWORD));
        System.out.println("bCrypt.encode(PASSWORD) = " + bCrypt.encode(PASSWORD));
        // uses random salt, thus different values
        //        bCrypt.encode(PASSWORD) = $2a$10$V8xGzt4rC9iUcda.KEFSW.6zbcISgEc0H45AI5.c24C7JuD7QAqYS
        //        bCrypt.encode(PASSWORD) = $2a$10$Uhb6KK5lZItsGuRPFPZeSeTqYdyZsmaB4X4KYBWBSe5/ZuAYf4ScW

        PasswordEncoder bCrypt2 = new BCryptPasswordEncoder(16);
        System.out.println("bCrypt2.encode(PASSWORD) = " + bCrypt2.encode(PASSWORD));
        // veel trager! En dat is positief in het kader van brute force attacks
        //        bCrypt2.encode(PASSWORD) = $2a$16$eI/yzV5S.OorJIz5LfxhvOZH/V8/FnvmCN.Hd.D811SiRSr4XEOja

        System.out.println("bCrypt.encode(\"tiger\") = " + bCrypt.encode("tiger"));
        System.out.println("bCrypt.encode(\"guru\") = " + bCrypt.encode("guru"));
    }

    @Test
    void testSha256() {
        PasswordEncoder sha256 = new StandardPasswordEncoder();
        System.out.println("sha256.encode(PASSWORD) = " + sha256.encode(PASSWORD));
        System.out.println("sha256.encode(PASSWORD) = " + sha256.encode(PASSWORD));
        // uses random salt, thus different values
        //        sha256.encode(PASSWORD) = eb3844170bde36c38f7716c6166f825dcc4afbe123fc5d86aa1a1e2dfae893dde983eaf1c7f861f2
        //        sha256.encode(PASSWORD) = 9c63ec3f195faa266da804208a4b8f7d9899733026a6f1b1ee4b8827c48e96bb33e34afe5976c53e

        String encodedPassword = sha256.encode(PASSWORD);
        assertTrue(sha256.matches(PASSWORD, encodedPassword));

        System.out.println("sha256.encode(\"tiger\") = " + sha256.encode("tiger"));
    }

    @Test
    void testLdap() {
        PasswordEncoder ldap = new LdapShaPasswordEncoder();
        System.out.println("ldap.encode(PASSWORD) = " + ldap.encode(PASSWORD));
        System.out.println("ldap.encode(PASSWORD) = " + ldap.encode(PASSWORD));
        // uses random salt, thus different values
        //        ldap.encode(PASSWORD) = {SSHA}QH5XxoGFhGTDq7D3g1HhYHoaI+9hn6XQhOff1w==
        //        ldap.encode(PASSWORD) = {SSHA}gtROkz5HASt3qxhsvIUdUc7UYn/yDz37eqd+WQ==

        String encodedPassword = ldap.encode(PASSWORD);
        assertTrue(ldap.matches(PASSWORD, encodedPassword));
    }

    @Test
    void testNoOp() {
        PasswordEncoder noOp = NoOpPasswordEncoder.getInstance();

        System.out.println("noOp.encode(PASSWORD) = " + noOp.encode(PASSWORD));
    }

    @Test
    void hashingExampleMD5() {
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes())); // altijd 5f4dcc3b5aa765d61d8327deb882cf99

        String salted = PASSWORD + "ThisIsMySALTVALUE";
        System.out.println(DigestUtils.md5DigestAsHex(salted.getBytes())); // altijd b177e1b7fab112477a6b1754caa9ef06
    }
}
