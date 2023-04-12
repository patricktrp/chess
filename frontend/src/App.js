import { useCallback, useEffect, useState } from 'react';
import SockJS from 'sockjs-client';
import { over } from 'stompjs';
import styles from './App.module.css';
import Chat from './Chat';
import Chess from './Chess';
import Gamelog from './Gamelog';
import Navbar from "./Navbar";
import InvitationModal from './InvitationModal';

const INITIAL_GAME_STATE = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
let stompClient = null;
let game = null;
let AI_GAME = false;
let isCreator = true;

const App = () => {
  const [chatMessages, setChatMessages] = useState([]);
  const [gameHistory, setGameHistory] = useState([INITIAL_GAME_STATE]);
  const [color, setColor] = useState("white");
  const [isPlaying, setIsPlaying] = useState(false);
  const [whiteTimeLeft, setWhiteTimeLeft] = useState();
  const [blackTimeLeft, setBlackTimeLeft] = useState();
  const [isWhiteTurn, setIsWhiteTurn] = useState(true);
  const [showInvitationModal, setShowInvitationModal] = useState(false);

  const subscribeToGame = useCallback((isBotGame) => {
    const appendix = isBotGame ? "/ai" : ""
    stompClient.subscribe(`/games/${game}${appendix}`, onGameEventReceived);
    window.history.replaceState(null, "", `/play/${game}`);
    if (isBotGame) {
      AI_GAME = true;
    }
  }, []);

  const createWebSocketConnection = useCallback((isBotGame) => {
    const sock = new SockJS(`${process.env.REACT_APP_BACKEND_SERVER}/ws`);
    stompClient = over(sock);
    stompClient.connect({}, () => subscribeToGame(isBotGame), handleError);
  }, [subscribeToGame]);

  useEffect(() => {
    const path = window.location.pathname;
    if (path.length <= 1) return;
    const gameId = path.split("/")[2]
    game = gameId;
    isCreator = false;
    createWebSocketConnection(false);
  }, [createWebSocketConnection]);

  const createGame = async (isBotGame, color, time, increment) => {
    const res = await fetch(`${process.env.REACT_APP_BACKEND_SERVER}/create${isBotGame ? "-bot" : ""}-game?color=${color}&time=${time}&increment=${increment}`);
    const body = await res.json();
    game = body;
    createWebSocketConnection(isBotGame);
    setShowInvitationModal(true);
  }

  const handleError = () => {

  }

  const onGameEventReceived = (payload) => {
    const body = payload.body;
    const obj = JSON.parse(body);
    let gameState;
    switch (obj["type"]) {
      case "GAME_START":
        setIsPlaying(true);
        gameState = obj["gameState"]
        setGameHistory((oldHistory) => [...oldHistory, gameState["gameFen"]]);
        setWhiteTimeLeft(gameState["whiteTimeLeft"]);
        setBlackTimeLeft(gameState["blackTimeLeft"]);
        setIsWhiteTurn(gameState["isWhiteTurn"]);

        if (isCreator) {
          setColor(gameState["firstPlayerWhite"] ? "white" : "black")
        } else {
          setColor(gameState["firstPlayerWhite"] ? "black" : "white")
        }
        setShowInvitationModal(false);
        break;
      case "MESSAGE":
        setChatMessages((oldMessages) => [...oldMessages, { message: obj["message"], color: obj["sender"], id: payload["headers"]["message-id"] }]);
        break;
      case "MOVE":
        gameState = obj["gameState"]
        setGameHistory((oldHistory) => [...oldHistory, gameState["gameFen"]]);
        setWhiteTimeLeft(gameState["whiteTimeLeft"]);
        setBlackTimeLeft(gameState["blackTimeLeft"]);
        setIsWhiteTurn(gameState["isWhiteTurn"]);
        break;
      default:
        console.log("def");
    }
  }

  const sendChatMessageHandler = (message) => {
    if (stompClient !== null) {
      stompClient.send(`/app/${game}/chat`, {}, message);
    }
  }

  const moveHandler = (source, target, piece) => {
    if (!isPlaying) return;
    let currentMove = {
      from: source.toUpperCase(),
      to: target.toUpperCase()
    }
    let path = `/app/${game}/move`
    if (AI_GAME) {
      path += "/ai"
    }
    stompClient.send(path, {}, JSON.stringify(currentMove));
  }

  return (
    <>
      <Navbar onCreateGame={(isBotGame, color, time, increment) => createGame(isBotGame, color, time, increment)} isPlaying={isPlaying} />
      <main className={styles["main-layout"]}>
        <Chat onSendChatMessage={sendChatMessageHandler} chatMessages={chatMessages} />
        <Chess onPieceDrop={moveHandler} position={gameHistory[gameHistory.length - 1]} color={color || "white"} />
        <Gamelog isPlaying={isPlaying} color={color} whiteTimeLeft={whiteTimeLeft} blackTimeLeft={blackTimeLeft} isWhiteTurn={isWhiteTurn} setWhiteTime={setWhiteTimeLeft} setBlackTime={setBlackTimeLeft} />
      </main>
      {showInvitationModal && <InvitationModal game={game} />}
    </>
  );
}

export default App;
