package com.patricktreppmann.chess.game;

import com.patricktreppmann.chess.chess.error.IllegalMoveException;
import com.patricktreppmann.chess.chess.error.PlayerNotFoundException;
import com.patricktreppmann.chess.chess.events.GameStartEvent;
import com.patricktreppmann.chess.chess.events.MessageEvent;
import com.patricktreppmann.chess.chess.events.MoveEvent;
import com.patricktreppmann.chess.chess.game.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

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
    public UUID createTwoPlayerGame(@RequestParam("color") ColorWish color, @RequestParam("time") int time, @RequestParam("increment") int increment) {
        return gameService.createTwoPlayerGame(color, time, increment);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/create-bot-game")
    @ResponseBody
    public UUID createOnePlayerGame(@RequestParam ColorWish color) {
        return gameService.createOnePlayerGame(color);
    }

    @MessageMapping("/{gameId}/move")
    public void move(@DestinationVariable UUID gameId, @Payload Move move, SimpMessageHeaderAccessor accessor) {
        try {
            GameState gameState = gameService.move(gameId, accessor.getSessionId(), move);
            MoveEvent moveEvent = new MoveEvent(gameState);

            simpMessagingTemplate.convertAndSend("/games/"+gameId, moveEvent);
        } catch (IllegalMoveException e) {
            e.printStackTrace();
        }
    }

/*    @MessageMapping("/{gameId}/move/ai")
    public void moveAI(@DestinationVariable UUID gameId, @Payload Move move, SimpMessageHeaderAccessor accessor) {
        try {
            String gameState = gameService.move(gameId, accessor.getSessionId(), move);
            Event event = new Event(EventType.MOVE, gameState);
            simpMessagingTemplate.convertAndSend("/games/"+gameId+"/ai", event);
            System.out.println("/games/"+gameId+"/ai");

           // ASYNC START OF AI MOVE CALCULATION
           CompletableFuture.runAsync(() -> {
                try {
                    Move move2 = new Move(Square.A8, Square. A5);
                    String gameState2 = gameService.move(gameId, "ai", move2);
                    Event event2 = new Event(EventType.MOVE, gameState2);
                    simpMessagingTemplate.convertAndSend("/games/"+gameId+"/ai", event2);
                } catch (IllegalMoveException e) {
                    e.printStackTrace();
                }
            });
        } catch (IllegalMoveException e) {
            e.printStackTrace();
        }
    }*/

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
    @MessageMapping
    public void handleSessionConnectEvent(SessionConnectEvent event) {
    }

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) throws PlayerNotFoundException {
        GenericMessage message = (GenericMessage) event.getMessage();
        String simpDestination = (String) message.getHeaders().get("simpDestination");
        String[] splitDestination = simpDestination.split("/");
        String gameId = splitDestination[2];
        String simpSessionId = (String) message.getHeaders().get("simpSessionId");

        boolean gameStarts = gameService.playerJoined(gameId, simpSessionId);
        if (gameStarts) {
            GameState gameState = gameService.getGameState(gameId);
            simpMessagingTemplate.convertAndSend("/games/"+gameId, new GameStartEvent(gameState));
        }
    }

    @EventListener
    public void handleSessionUnsubscribeEvent(SessionUnsubscribeEvent event) {
        System.out.println("LEFT");
        GenericMessage message = (GenericMessage) event.getMessage();
        String simpDestination = (String) message.getHeaders().get("simpDestination");

        String gameId = simpDestination.split("/")[2];
        UUID gameUUID = UUID.fromString(gameId);

        String simpSessionId = (String) message.getHeaders().get("simpSessionId");
        gameService.playerLeft(gameUUID, simpSessionId);
    }
}
