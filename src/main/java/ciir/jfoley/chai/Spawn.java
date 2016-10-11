package ciir.jfoley.chai;

import ciir.jfoley.chai.io.IO;
import ciir.jfoley.chai.io.Streams;
import ciir.jfoley.chai.lang.Module;
import ciir.jfoley.chai.string.StrUtil;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * Interface to Java's lackluster Process API.
 * @author jfoley.
 */
public class Spawn extends Module {
	public static void doProcess(int expectedStatus, String... args) throws IOException, InterruptedException {
		Process exec = Runtime.getRuntime().exec(args);
		int status = exec.waitFor();
		Streams.copy(exec.getErrorStream(), System.err);
		Streams.copy(exec.getInputStream(), System.err);
		System.err.println("# status: " + status + " for: " + StrUtil.join(Arrays.asList(args), " "));
		if(status != expectedStatus) throw new RuntimeException();
	}

  public static void doProcess(String... args) throws IOException, InterruptedException {
    doProcess(0, args);
  }

	public static int doProcess(List<String> cmds, File stdout, File stderr) throws IOException, InterruptedException {
		ProcessBuilder pb = new ProcessBuilder(cmds);
		pb.redirectError(stderr);
		pb.redirectOutput(stdout);
		pb.directory(null);
		Process exec = pb.start();
		return exec.waitFor();
	}

	public static class AsyncCommand implements Closeable {
		private final Process process;
		File tmpOutput;
		File tmpError;

		public AsyncCommand(List<String> cmd) throws IOException {
			tmpOutput = File.createTempFile("async_cmd_", ".out");
			tmpError = File.createTempFile("async_cmd_", ".err");
			ProcessBuilder pb = new ProcessBuilder(cmd);
			pb.redirectError(tmpError);
			pb.redirectOutput(tmpOutput);
			pb.directory(null); // current directory
			this.process = pb.start();
		}

		public int waitForStatus() throws InterruptedException {
			return process.waitFor();
		}
		public boolean waitForStatus(int what) throws InterruptedException {
			return waitForStatus() == what;
		}

		public String getStandardError() throws IOException, InterruptedException {
			waitForStatus();
			return IO.slurp(tmpOutput);
		}
		public String getStandardOutput() throws IOException, InterruptedException {
			waitForStatus();
			return IO.slurp(tmpOutput);
		}

		public Future<Integer> futureStatus() {
			AsyncCommand that = this;
			return ForkJoinPool.commonPool().submit((Callable<Integer>) that::waitForStatus);
		}

		@Override
		public void close() throws IOException {
			boolean ed = tmpError.delete();
			boolean od = tmpOutput.delete();
			if(!ed) {
				throw new IOException("Couldn't delete stderr: "+tmpError);
			}
			if(!od) {
				throw new IOException("Couldn't delete stdout: "+tmpOutput);
			}
		}
	}

}
