package pol.demo.scoreboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ScoreboardService {

    private final GameState state = new GameState();
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final ObjectMapper mapper = new ObjectMapper();

    private static final int PERIOD_DURATION = 20 * 60;
    private static final int OT_DURATION = 5 * 60;

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(0L); // no timeout
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));
        // Send current state immediately to new subscriber
        sendToEmitter(emitter, state);
        return emitter;
    }

    @Scheduled(fixedRate = 1000)
    public void tick() {
        if (!state.isClockRunning()) return;

        if (state.getPeriod() != 5 && state.getClockSeconds() > 0) {
            state.setClockSeconds(state.getClockSeconds() - 1);
            if (state.getClockSeconds() == 0) {
                state.setClockRunning(false);
            }
        }

        tickPenalties(state.getHomePenalties());
        tickPenalties(state.getAwayPenalties());

        broadcastState();
    }

    private void tickPenalties(List<Penalty> penalties) {
        Iterator<Penalty> it = penalties.iterator();
        while (it.hasNext()) {
            Penalty p = it.next();
            if (p.getRemainingSeconds() > 0) {
                p.setRemainingSeconds(p.getRemainingSeconds() - 1);
            }
            if (p.getRemainingSeconds() <= 0) {
                it.remove();
            }
        }
    }

    public void broadcastState() {
        List<SseEmitter> dead = new ArrayList<>();
        for (SseEmitter emitter : emitters) {
            if (!sendToEmitter(emitter, state)) {
                dead.add(emitter);
            }
        }
        emitters.removeAll(dead);
    }

    private boolean sendToEmitter(SseEmitter emitter, GameState s) {
        try {
            String json = mapper.writeValueAsString(s);
            emitter.send(SseEmitter.event().name("state").data(json));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public GameState getState() { return state; }

    public void goal(String team) {
        if ("home".equals(team)) {
            state.setHomeScore(state.getHomeScore() + 1);
            if (!state.getAwayPenalties().isEmpty()) state.getAwayPenalties().remove(0);
        } else {
            state.setAwayScore(state.getAwayScore() + 1);
            if (!state.getHomePenalties().isEmpty()) state.getHomePenalties().remove(0);
        }
        broadcastState();
    }

    public void setScore(String team, int score) {
        if ("home".equals(team)) state.setHomeScore(Math.max(0, score));
        else state.setAwayScore(Math.max(0, score));
        broadcastState();
    }

    public void startClock() { state.setClockRunning(true); broadcastState(); }
    public void stopClock() { state.setClockRunning(false); broadcastState(); }

    public void resetClock() {
        state.setClockRunning(false);
        state.setClockSeconds(periodDuration(state.getPeriod()));
        broadcastState();
    }

    public void setClockSeconds(int seconds) {
        state.setClockSeconds(Math.max(0, seconds));
        broadcastState();
    }

    public void setPeriod(int period) {
        state.setClockRunning(false);
        state.setPeriod(period);
        state.setClockSeconds(periodDuration(period));
        broadcastState();
    }

    public void addPenalty(String team, String playerNumber, int minutes) {
        Penalty p = new Penalty(playerNumber, minutes * 60);
        if ("home".equals(team)) state.getHomePenalties().add(p);
        else state.getAwayPenalties().add(p);
        broadcastState();
    }

    public void removePenalty(String team, int index) {
        List<Penalty> penalties = "home".equals(team) ? state.getHomePenalties() : state.getAwayPenalties();
        if (index >= 0 && index < penalties.size()) penalties.remove(index);
        broadcastState();
    }

    public void setTeams(String home, String away) {
        if (home != null && !home.isBlank()) state.setHomeTeam(home.trim().toUpperCase());
        if (away != null && !away.isBlank()) state.setAwayTeam(away.trim().toUpperCase());
        broadcastState();
    }

    public void setTournamentName(String name) {
        if (name != null) state.setTournamentName(name.trim().toUpperCase());
        broadcastState();
    }

    public void resetGame() {
        state.setHomeScore(0);
        state.setAwayScore(0);
        state.setPeriod(1);
        state.setClockSeconds(PERIOD_DURATION);
        state.setClockRunning(false);
        state.setHomePenalties(new ArrayList<>());
        state.setAwayPenalties(new ArrayList<>());
        broadcastState();
    }

    private int periodDuration(int period) {
        if (period == 4) return OT_DURATION;
        if (period == 5) return 0;
        return PERIOD_DURATION;
    }
}
