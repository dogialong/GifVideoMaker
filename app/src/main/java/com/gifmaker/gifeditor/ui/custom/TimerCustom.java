package com.gifmaker.gifeditor.ui.custom;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by macbook on 5/6/17.
 */

public abstract class TimerCustom {

    public interface TimerEvent{
        public void onTickTimer();
    }

    public static final int DURATION_INFINITY = -1;
    private volatile boolean isRunning = false;
    private long interval;
    private long elapsedTime;
    private long duration;
    private TimerEvent timerEvent;
    private ScheduledExecutorService execService = Executors
            .newSingleThreadScheduledExecutor();
    private Future<?> future = null;

    /**
     * Default constructor which sets the interval to 1000 ms (1s) and the
     * duration to {@link TimerCustom#DURATION_INFINITY}
     */
    public TimerCustom() {
        this(1000, -1);
    }

    /**
     * @param interval
     *            The time gap between each tick in millis.
     * @param duration
     *            The period in millis for which the timer should run. Set it to {@code Timer#DURATION_INFINITY} if the timer has to run indefinitely.
     */
    public TimerCustom(long interval, long duration) {
        this.interval = interval;
        this.duration = duration;
        this.elapsedTime = 0;
    }

    /**
     * Starts the timer. If the timer was already running, this call is ignored.
     */
    public void start() {
        if (isRunning)
            return;

        isRunning = true;
        future = execService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                onTick();
                if (timerEvent!=null)
                    timerEvent.onTickTimer();
                elapsedTime += TimerCustom.this.interval;
                if (duration > 0) {
                    if(elapsedTime >=duration){
                        onFinish();
                        future.cancel(false);
                    }
                }
            }
        }, 0, this.interval, TimeUnit.MILLISECONDS);
    }

    /**
     * Paused the timer. If the timer is not running, this call is ignored.
     */
    public void pause() {
        if(!isRunning) return;
        future.cancel(false);
        isRunning = false;
    }


    /**
     * Resumes the timer if it was paused, else starts the timer.
     */
    public void resume() {
        this.start();
    }


    /**
     *	This method is called periodically with the interval set as the delay between subsequent calls.
     */
    protected abstract void onTick();


    /**
     * This method is called once the timer has run for the specified duration. If the duration was set as infinity, then this method is never called.
     */
    protected abstract void onFinish();

    /**
     * Stops the timer. If the timer is not running, then this call does nothing.
     */
    public void cancel() {
        pause();
        this.elapsedTime = 0;
    }


    /**
     * @return the elapsed time (in millis) since the start of the timer.
     */
    public long getElapsedTime() {
        return this.elapsedTime;
    }

    /**
     * @return the time remaining (in millis) for the timer to stop. If the duration was set to {@code Timer#DURATION_INFINITY}, then -1 is returned.
     */
    public long getRemainingTime(){
        if(this.duration <0){
            return TimerCustom.DURATION_INFINITY;
        }
        else{
            return duration-elapsedTime;
        }
    }

    /**
     * @return true if the timer is currently running, and false otherwise.
     */
    public boolean isRunning() {
        return isRunning;
    }

    public TimerEvent getTimerEvent() {
        return timerEvent;
    }

    public void setTimerEvent(TimerEvent timerEvent) {
        this.timerEvent = timerEvent;
    }
}
