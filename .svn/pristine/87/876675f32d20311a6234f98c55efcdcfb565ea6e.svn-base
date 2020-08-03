package com.hyva.restopos.util;

import org.apache.log4j.Logger;

import javax.print.DocPrintJob;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Event watcher for PrintJob.
 * <p>
 * This will notify the PrintJob if the process is complete already.
 *
 * <b>** Special thanks to {@code papertoolkit} **</b>
 *
 * @author Einar Lagera
 */
public class PrintJobWatcher {

    Logger logger = Logger.getLogger(PrintJobWatcher.class);
    
    private final AtomicBoolean done = new AtomicBoolean();

    public PrintJobWatcher(DocPrintJob job) {
        job.addPrintJobListener(new PrintJobAdapter() {
            @Override
            public void printJobCanceled(PrintJobEvent pje) {
                allDone();
            }

            @Override
            public void printJobCompleted(PrintJobEvent pje) {
                allDone();
            }

            @Override
            public void printJobFailed(PrintJobEvent pje) {
                allDone();
            }

            @Override
            public void printJobNoMoreEvents(PrintJobEvent pje) {
                allDone();
            }

            void allDone() {
                synchronized (PrintJobWatcher.this) {
                    done.set(true);
                    PrintJobWatcher.this.notify();
                }
            }
        });
    }

    /**
     * Wait for print job to be done.
     */
    public synchronized void waitForDone() {
        try {
            logger.info("Checking printer status...");
            while (!done.get()) {
                wait();
            }
            logger.info("Print Job Completed!");
        } catch (InterruptedException e) {
        	e.printStackTrace();
        }
    }
}