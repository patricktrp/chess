package com.patricktreppmann.chess.game;

import com.patricktreppmann.chess.chess.error.IllegalMoveException;
import com.patricktreppmann.chess.chess.error.PlayerNotFoundException;
import com.patricktreppmann.chess.chess.events.GameStartEvent;
import com.patricktreppmann.chess.chess.events.MessageEvent;
import com.patricktreppmann.chess.chess.events.MoveEvent;
import com.patricktreppmann.chess.chess.game.GameState;
import com.patricktreppmann.chess.chess.game.Move;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.UUID;

@Controller
public class GameController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final IGameService gameService;

    @Autowired
    public GameController(SimpMessagingTemplate simpMessagingTemplate, IGameService gameService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.gameService = gameService;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/create-game")
    @ResponseBody
    public ResponseEntity<UUID> createTwoPlayerGame(@RequestParam("color") ColorWish color, @RequestParam("time") int time, @RequestParam("increment") int increment) {
        if (time < 3 || time > 15 || increment < 0 || increment > 15) {
            return ResponseEntity.badRequest().build();
        }
        UUID gameId = gameService.createTwoPlayerGame(color, time, increment);
        return ResponseEntity.ok(gameId);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/create-bot-game")
    @ResponseBody
    public ResponseEntity<UUID> createOnePlayerGame(@RequestParam ColorWish color, @RequestParam("time") int time, @RequestParam("increment") int increment) {
        UUID gameId = gameService.createOnePlayerGame(color, time, increment);
        return ResponseEntity.ok(gameId);
    }

    @MessageMapping("/{gameId}/move")
    public void move(@DestinationVariable UUID gameId, @Payload Move move, SimpMessageHeaderAccessor accessor) {
        System.out.println(move);
        try {
            GameState gameState = gameService.move(gameId, accessor.getSessionId(), move);
            MoveEvent moveEvent = new MoveEvent(gameState);

            simpMessagingTemplate.convertAndSend("/games/"+gameId, moveEvent);
        } catch (IllegalMoveException e) {
            e.printStackTrace();
        }
    }

    @MessageMapping("/{gameId}/move/ai")
    public void moveAI(@DestinationVariable UUID gameId, @Payload Move move, SimpMessageHeaderAccessor accessor) {
        try {
            GameState gameState = gameService.move(gameId, accessor.getSessionId(), move);
            MoveEvent moveEvent = new MoveEvent(gameState);
            simpMessagingTemplate.convertAndSend("/games/"+gameId+"/ai", moveEvent);

            GameState gameState2 = gameService.aiMove(gameId);
            MoveEvent moveEvent2 = new MoveEvent(gameState2);
            simpMessagingTemplate.convertAndSend("/games/"+gameId+"/ai", moveEvent2);

        } catch (IllegalMoveException e) {
            e.printStackTrace();
        }
    }

    @MessageMapping("/{gameId}/chat")
    public void chat(@DestinationVariable UUID gameId, @Payload String message, SimpMessageHeaderAccessor accessor) {
        try {
            String playerColor = gameService.getPlayerColor(gameId, accessor.getSessionId());
            MessageEvent messageEvent = new MessageEvent(message, playerColor);
            simpMessagingTemplate.convertAndSend("/games/"+gameId, messageEvent);
        } catch (PlayerNotFoundException e) {
            e.printStackTrace();
        }
    }

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        GenericMessage message = (GenericMessage) event.getMessage();
        String simpDestination = (String) message.getHeaders().get("simpDestination");
        String[] splitDestination = simpDestination.split("/");
        String gameId = splitDestination[2];
        String simpSessionId = (String) message.getHeaders().get("simpSessionId");

        String destination = "/games/"+gameId;
        if (splitDestination[splitDestination.length-1].equals("ai")) {
            destination += "/ai";
        }

        UUID gameUUID = UUID.fromString(gameId);

        boolean gameStarts = gameService.playerJoined(gameUUID, simpSessionId);
        if (gameStarts) {
            GameState gameState = gameService.getGameState(gameUUID);
            simpMessagingTemplate.convertAndSend(destination, new GameStartEvent(gameState));
        }
    }

 /*   @EventListener
    public void handleSessionUnsubscribeEvent(SessionUnsubscribeEvent event) {

    }

    @EventListener
    public void handleSessionConnectEvent(SessionConnectEvent event) {
    }*/
}
