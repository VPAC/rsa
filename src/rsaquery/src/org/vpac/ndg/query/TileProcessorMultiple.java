package org.vpac.ndg.query;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vpac.ndg.query.iteration.CoordinatePair;
import org.vpac.ndg.query.iteration.RectangleStrider;
import org.vpac.ndg.query.math.BoxInt;
import org.vpac.ndg.query.math.VectorInt;
import org.vpac.ndg.query.math.VectorReal;
import org.vpac.ndg.query.sampling.Binding;

public class TileProcessorMultiple implements TileProcessor {

	final Logger log = LoggerFactory.getLogger(TileProcessorMultiple.class);

	BoxInt bounds;
	Collection<Binding> bindings;

	ExecutorService executor;
	int numThreads;
	volatile boolean running;
	AtomicInteger numRunning;
	volatile Exception abortException;

	public TileProcessorMultiple(int numThreads) {
		this.numThreads = numThreads;

		numRunning = new AtomicInteger(0);
		running = true;
		abortException = null;

		log.info("Starting {} threads.", numThreads);
		executor = Executors.newFixedThreadPool(numThreads);
	}

	class TileProcWorker implements Runnable {

		final Logger log = LoggerFactory.getLogger(TileProcWorker.class);

		int id;

		public TileProcWorker(int id) {
			this.id = id;
		}

		@Override
		public void run() {
			log.trace("Worker thread {} starting.", id);
			try {
				process();
			} finally {
				synchronized (numRunning) {
					numRunning.decrementAndGet();
					numRunning.notify();
				}
			}
			log.info("Worker {} finished.", id);
		}

		private void process() {
			VectorInt currentTileShape = bounds.getSize();

			RectangleStrider rect = new RectangleStrider(currentTileShape, numThreads, id);
			VectorReal offset = bounds.getMin().toReal().add(0.5);
			rect.setOrigin(offset);

			for (CoordinatePair coords : rect) {
				if (!running || abortException != null) {
					log.trace("Worker {} exiting early.", id);
					return;
				}

				log.trace("Thread {} applying filter for pixel {}.", id, coords.coordinates);

				try {
					for (Binding b : bindings)
						b.transfer(coords.coordinates, coords.imageIndex, id);
				} catch (Exception e) {
					log.error("Worker {} encountered an exception.", id);
					log.error("Details:", e);
					abortException = e;
					running = false;
					return;
				}
			}
		}
	}

	public void processTile() throws IOException {

		for (int i = 0; i < numThreads; i++) {
			TileProcWorker worker = new TileProcWorker(i);
			numRunning.incrementAndGet();
			try {
				executor.execute(worker);
			} catch (RuntimeException e) {
				numRunning.decrementAndGet();
				throw e;
			}
		}

		log.trace("Waiting for threads to finish processing.");
		synchronized (numRunning) {
			try {
				while (numRunning.get() > 0) {
					log.trace("numRunnning = {}", numRunning);
					numRunning.wait();
				}
				log.trace("numRunnning = {}", numRunning);
			} catch (InterruptedException e) {
				throw new IOException("Interrupted while waiting for pixels " +
						"to be generated.", e);
			}
		}
		log.trace("Finished processing tile.");
	}

	@Override
	public void setBounds(BoxInt bounds) {
		this.bounds = bounds;
	}

	@Override
	public void setBindings(Collection<Binding> bindings) {
		this.bindings = bindings;
	}

	@Override
	public void shutDown() {
		log.trace("Waiting for threads to exit.");
		running = false;
		executor.shutdownNow();
		try {
			executor.awaitTermination(1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			log.warn("Interrupted while waiting for thread to finish.", e);
		}
	}
}
