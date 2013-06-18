package org.vpac.ndg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.vpac.ndg.application.ApplicationShutdownHook;
import org.vpac.ndg.common.StringUtils;
import org.vpac.ndg.configuration.NdgConfigManager;
import org.vpac.ndg.rasterservices.ProcessException;

public class CommandUtil {

	final private Logger log = LoggerFactory.getLogger(CommandUtil.class);

	/**
	 * Adjust the command to suit the environment.
	 * @param command The command to run.
	 * @return The adjusted command.
	 */
	public List<String> prepareCommand(List<String> command) {
		ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();
		NdgConfigManager ndgConfigManager = (NdgConfigManager) appContext.getBean("ndgConfigManager");

		String loc = ndgConfigManager.getConfig().getGdalLocation();
		if (loc == null)
			throw new IllegalStateException("GDAL location is not configured.");

		Path gdalBinDir = Paths.get(loc, "bin");
		Path gdalCmdPath = gdalBinDir.resolve(command.get(0));

		// Patch in path to GDAL, if appropriate.
		ArrayList<String> cmd = new ArrayList<String>();
		cmd.add(gdalCmdPath.toString());

		log.trace("cma [0] = {}", command.get(0));

		// Remove windows-style backslashes.
		for (int i = 1; i < command.size(); ++i) {
			log.trace("cma [{}] = {}", i, command.get(i));
			String s = command.get(i).replace('\\', '/');
			cmd.add(s);
		}

		String commandStr = StringUtils.join(cmd, " ");
		log.debug("Attempting to run: " + commandStr);

		return cmd;
	}

	/**
	 * Starts the task. The arguments will be modified as required by the
	 * environment. E.g. file paths will be converted to suit the OS. Also, some
	 * arguments may be added depending on the state of member variables.
	 * @param command
	 *            The base arguments to the command. The first item must be the
	 *            name of the program.
	 * @throws ProcessException
	 *             If the command has a non-zero exit value.
	 * @throws IOException
	 *             If the command could not be communicated with.
	 * @throws InterruptedException
	 *             If this thread was interrupted while waiting for the command
	 *             to finish.
	 */
	public void start(List<String> command) throws ProcessException,
			InterruptedException, IOException {

		int exitValue;
		try {
			command = prepareCommand(command);
		} catch (IllegalStateException e) {
			throw new IOException(e);
		}

		ProcessBuilder pb = new ProcessBuilder(command);
		Process process = pb.start();

		try {
			ApplicationShutdownHook sh = new ApplicationShutdownHook(process);
			Runtime.getRuntime().addShutdownHook(sh);
			try {
				consumeOutput(process.getInputStream());
				consumeErrorOutput(process.getErrorStream());
			} finally {
				try {
					Runtime.getRuntime().removeShutdownHook(sh);
				} catch (IllegalStateException e) {
					log.debug("Tried to remove shutdown hook while shutting " +
							"down. Hook may still execute.");
				}
			}
		} finally {
			log.trace("Waiting for command to finish...");
			exitValue = process.waitFor();
		}

		if (!isSuccess(exitValue)) {
			log.debug("Command exited with code " + exitValue);
			throw new ProcessException(exitValue);
		}
	}

	/**
	 * Determines whether a process completed successfully. Override this if the
	 * command you are running uses non-zero return codes to indicate success.
	 * 
	 * @param exitValue
	 *            The code returned by the command.
	 * @return true if the command was successful.
	 */
	protected boolean isSuccess(int exitValue) {
		return exitValue == 0;
	}

	private void consumeOutput(InputStream stdout) throws IOException {
		// This consumes one character at a time, because that's how the GDAL
		// progress meter is written.
		BufferedReader brCleanUp = new BufferedReader(new InputStreamReader(stdout));
		try {
			char[] cbuf = new char[1];
			boolean started = false;
			int prog = 0;
			while (brCleanUp.read(cbuf) != -1) {
				started = true;
				if (started && prog < 100) {
					while (brCleanUp.read(cbuf) != -1) {
						if (log.isTraceEnabled())
							System.out.print(cbuf);
						if (cbuf[0] != '.') {
							break;
						}
					}

					while (brCleanUp.read(cbuf) != -1) {
						if (log.isTraceEnabled())
							System.out.print(cbuf);
						if (cbuf[0] == '.') {
							break;
						}
					}

					prog += 10;
				} else {
					if (log.isTraceEnabled())
						System.out.print(cbuf);
				}
			}
		} finally {
			try {
				brCleanUp.close();
			} catch (IOException e) {
				log.debug("Error closing output stream.");
			}
		}
	}

	private void consumeErrorOutput(InputStream stderr) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(stderr));
		try {
			while (br.ready()) {
				String line = br.readLine();
				if (!log.isErrorEnabled())
					continue;
				log.warn(line);
			}

		} finally {
			try {
				br.close();
			} catch (IOException e) {
				log.debug("Error closing output stream.");
			}
		}
	}
}
