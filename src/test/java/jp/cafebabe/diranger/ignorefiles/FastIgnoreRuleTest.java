package jp.cafebabe.diranger.ignorefiles;

import org.eclipse.jgit.ignore.FastIgnoreRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This test cases are the confirmations of how to use FastIgnoreRules in JGit.
 */
public class FastIgnoreRuleTest {
    /**
     * Check wild card.
     */
    @Test
    public void testFastIgnoreRule() throws Exception {
        FastIgnoreRule rule = new FastIgnoreRule("*.sh");

        assertTrue(rule.isMatch("test1.sh", false));
        assertTrue(rule.isMatch("test2.sh", true));
        assertFalse(rule.isMatch("test3.exe", true));
        assertFalse(rule.isMatch("test3.exe", false));
    }

    /**
     * Ignore only directory which name was build.
     */
    @Test
    public void testFastIgnoreRuleForDir() throws Exception {
        var rule = new FastIgnoreRule("build/");
        assertTrue(rule.isMatch("build", true));
        assertFalse(rule.isMatch("build", false));
        assertTrue(rule.isMatch("./build", true));
        assertFalse(rule.isMatch("./build", false));
    }
}
