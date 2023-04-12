import { useCallback, useEffect, useRef } from 'react';
import { TbChessRookFilled } from 'react-icons/tb';
import styles from './Chat.module.css';
import EmojiPicker from 'emoji-picker-react';

const Chat = (props) => {
    const inputRef = useRef();
    const scrollableDiv = useRef(null);

    const handleEnterPress = useCallback(() => {
        const msg = inputRef.current.value;
        if (msg === "") return;
        props.onSendChatMessage(msg);
        inputRef.current.value = "";
    }, [props]);

    const handleKeyUp = useCallback((event) => {
        if (event.keyCode === 13) {
            handleEnterPress();
        }
    }, [handleEnterPress]);


    useEffect(() => {
        scrollableDiv.current.scrollTop = scrollableDiv.current.scrollHeight;
    }, [props.chatMessages]);

    useEffect(() => {
        document.addEventListener("keyup", handleKeyUp);
        return () => {
            document.removeEventListener("keyup", handleKeyUp);
        };
    }, [handleKeyUp]);

    return (
        <section className={styles.chat}>
            <div>
                <div className={styles["chat-header"]}>
                    <h3>Chat</h3>
                </div>
                <div className={styles["scrollable-div"]} ref={scrollableDiv} style={{ height: '67vh', overflowY: 'auto' }}>
                    {props.chatMessages.map((msg) => {
                        return (
                            <div key={msg.id} className={styles["chat-message"]}>
                                <div className={styles["chat-message-icon"]}>
                                    <TbChessRookFilled className={styles[msg.color]} />
                                </div>
                                <div>
                                    {msg.message}
                                </div>
                            </div>
                        )
                    })}
                </div>
            </div>
            <div className={styles["chat-input-section"]}>
                <input placeholder="send message.." ref={inputRef} className={styles["chat-input-section-input"]} type='text' />
                {/* <EmojiPicker /> */}
            </div>
        </section >
    )
}

export default Chat;