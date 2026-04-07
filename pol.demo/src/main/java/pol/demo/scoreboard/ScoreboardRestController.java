package pol.demo.scoreboard;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@RestController
@RequestMapping("/api/scoreboard")
@CrossOrigin(origins = "*")
public class ScoreboardRestController {

    private final ScoreboardService service;

    public ScoreboardRestController(ScoreboardService service) {
        this.service = service;
    }

    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() {
        return service.subscribe();
    }

    @GetMapping("/state")
    public GameState getState() {
        return service.getState();
    }

    @PostMapping("/goal/{team}")
    public ResponseEntity<Void> goal(@PathVariable String team) {
        service.goal(team);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/score/{team}/{value}")
    public ResponseEntity<Void> setScore(@PathVariable String team, @PathVariable int value) {
        service.setScore(team, value);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/clock/start")
    public ResponseEntity<Void> startClock() {
        service.startClock();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/clock/stop")
    public ResponseEntity<Void> stopClock() {
        service.stopClock();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/clock/reset")
    public ResponseEntity<Void> resetClock() {
        service.resetClock();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/clock/set/{seconds}")
    public ResponseEntity<Void> setClockSeconds(@PathVariable int seconds) {
        service.setClockSeconds(seconds);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/period/{period}")
    public ResponseEntity<Void> setPeriod(@PathVariable int period) {
        service.setPeriod(period);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/penalty/{team}")
    public ResponseEntity<Void> addPenalty(@PathVariable String team, @RequestBody Map<String, Object> body) {
        String playerNumber = (String) body.getOrDefault("playerNumber", "??");
        int minutes = (int) body.getOrDefault("minutes", 2);
        service.addPenalty(team, playerNumber, minutes);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/penalty/{team}/{index}")
    public ResponseEntity<Void> removePenalty(@PathVariable String team, @PathVariable int index) {
        service.removePenalty(team, index);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/teams")
    public ResponseEntity<Void> setTeams(@RequestBody Map<String, String> body) {
        service.setTeams(body.get("home"), body.get("away"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/tournament")
    public ResponseEntity<Void> setTournamentName(@RequestBody Map<String, String> body) {
        service.setTournamentName(body.get("name"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> resetGame() {
        service.resetGame();
        return ResponseEntity.ok().build();
    }
}
