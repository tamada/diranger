/**
 * This package contains the directory traversing APIs for the clients.
 * The API gives the result of traversing as {@link java.util.stream.Stream Stream},
 * {@link java.util.Iterator Iterator} and {@link java.util.List List}.
 *
 * <h2>Simple use case</h2>
 * <pre><code>
 *     var ranger = new RangerBuilder().build();
 *     ranger.stream(Path.of("target/directory"),
 *             new Config.Builder()
 *                 .respectIgnoreFiles(true)
 *                 .skipSymlinks(true)
 *                 .skipHiddenFiles(true)).build())
 *         .forEach(System.out::println);
 * </code></pre>
 * <p>
 *     Above code equals the following code.
 * </p>
 * <pre><code>
 *     var ranger = new RangerBuilder().build();
 *     ranger.stream(Path.of("target/directory"), new Config())
 *         .forEach(System.out::println);
 * </code></pre>
 *
 * @author Haruaki TAMADA
 */
package jp.cafebabe.diranger;

