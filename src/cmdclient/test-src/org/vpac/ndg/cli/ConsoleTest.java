package org.vpac.ndg.cli;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.vpac.ndg.cli.Client;

/**
 * Allows the Client to be run using regular command line arguments. stdout and
 * stderr are redirected to output streams, so their contents can be verified by
 * a test case.
 */
public class ConsoleTest {

	protected ByteArrayOutputStream output;
	protected ByteArrayOutputStream errput;
	PrintStream origStdErr;
	PrintStream origStdOut;
	protected Client client;
	protected int errcode;

	@Before
	public void setUp() {
		// Redirect stdout and stderr.
		origStdOut = System.out;
		origStdErr = System.err;
		output = new ByteArrayOutputStream();
		errput = new ByteArrayOutputStream();
		System.setOut(new PrintStream(output));
		System.setErr(new PrintStream(errput));

		// Capture error code.
		client = new Client(Paths.get(".")) {
			protected void exit(int err) {
				errcode = err;
			};
		};
	}

	@After
	public void tearDown() {
		// Redirect stdout and stderr.
		System.setOut(origStdOut);
		System.setErr(origStdErr);
		System.out.print(output);
		System.err.print(errput);

		try {
			output.close();
		} catch (IOException e) {
			System.err.println("Failed to close output stream.");
		}

		try {
			errput.close();
		} catch (IOException e) {
			System.err.println("Failed to close output stream.");
		}
	}

}
