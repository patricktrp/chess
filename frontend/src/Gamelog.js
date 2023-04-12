import styles from './Gamelog.module.css';
import Timer from './Timer';

const Gamelog = (props) => {
    const isWhite = props.color === "white"

    return (
        <>
            <section className={styles.gamelog}>
                {props.isPlaying && !isWhite && <Timer timeLeft={props.whiteTimeLeft} running={props.isWhiteTurn} setTime={props.setWhiteTime} />}
                {props.isPlaying && isWhite && <Timer timeLeft={props.blackTimeLeft} running={!props.isWhiteTurn} setTime={props.setBlackTime} />}
                {/* <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', height: '70vh', backgroundColor: 'rgb(31,31,31)', margin: '10px' }}>gamelog</div> */}
                {props.isPlaying && isWhite && <Timer timeLeft={props.whiteTimeLeft} running={props.isWhiteTurn} setTime={props.setWhiteTime} />}
                {props.isPlaying && !isWhite && <Timer timeLeft={props.blackTimeLeft} running={!props.isWhiteTurn} setTime={props.setBlackTime} />}
            </section>
        </>
    );
}

export default Gamelog;