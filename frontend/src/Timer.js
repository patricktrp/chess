import { useEffect } from 'react';

const Timer = (props) => {
    const timeLeft = props.timeLeft;

    let minutes = Math.floor(timeLeft / 60);
    let remainingSeconds = timeLeft % 60;

    useEffect(() => {
        if (!props.running) return;
        console.log("HI");

        let interval = setInterval(() => {
            props.setTime(prevSeconds => prevSeconds - 1);
        }, 1000);

        return () => clearInterval(interval);
    }, [props.running, props.timeLeft]);

    return (
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '40px' }}>
            {`${minutes}:${remainingSeconds >= 10 ? remainingSeconds : "0" + remainingSeconds}`}
        </div>
    )
}

export default Timer;