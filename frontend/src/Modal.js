import { useState } from 'react';
import { FaRandom } from 'react-icons/fa';
import { TbChessRookFilled } from 'react-icons/tb';
import styles from './Modal.module.css';

const Modal = (props) => {
    const [selectedDiv, setSelectedDiv] = useState(1);
    const [time, setTime] = useState(10);
    const [increment, setIncrement] = useState(0);

    const handle = (e) => {
        const index = parseInt(e.target.dataset.index);
        setSelectedDiv(index);
    }

    const handleGameStart = (color) => {
        props.onCreateGame(selectedDiv === 1 ? false : true, color, time, increment);
        props.onClose();
    }

    const updateTime = (e) => {
        setTime(e.target.value);
        console.log(time);
    }

    return (
        <>
            <div className={styles["modal"]}>
                <h3>Play against</h3>
                <div style={{ display: 'flex' }}>
                    <div onClick={handle} data-index={1} style={{ cursor: 'pointer', backgroundColor: selectedDiv === 1 ? 'salmon' : 'rgb(31,31,31)', margin: '10px', width: '7vw', height: '5vh', display: 'flex', justifyContent: 'center', alignItems: 'center', borderRadius: '10px' }}>a friend</div>
                    <div onClick={handle} data-index={2} style={{ cursor: 'pointer', backgroundColor: selectedDiv === 2 ? 'salmon' : 'rgb(31,31,31)', margin: '10px', width: '7vw', height: '5vh', display: 'flex', justifyContent: 'center', alignItems: 'center', borderRadius: '10px' }}>the AI</div>
                </div>
                <div>
                    <label>Time</label>
                    <input type="range" min="3" max="15" value={time} onChange={updateTime} />
                    <p>{time} minutes</p>
                    <label>Increment</label>
                    <input type="range" min="0" max="15" value={increment} onChange={(e) => setIncrement(e.target.value)} />
                    <p>{increment} seconds</p>
                </div>
                <h3>Choose a color</h3>
                <div style={{ display: 'flex' }}>
                    <div onClick={() => handleGameStart("WHITE")} style={{ margin: '10px', width: '60px', cursor: 'pointer', height: '60px', backgroundColor: 'rgb(100,100,100)', borderRadius: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                        <TbChessRookFilled style={{ color: 'white' }} size={'3.5vh'} />
                    </div>
                    <div onClick={() => handleGameStart("RANDOM")} style={{ margin: '10px', width: '60px', cursor: 'pointer', height: '60px', backgroundColor: 'rgb(100,100,100)', borderRadius: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                        <FaRandom style={{ color: '#aaa' }} size={'3.5vh'} />
                    </div>
                    <div onClick={() => handleGameStart("BLACK")} style={{ margin: '10px', width: '60px', cursor: 'pointer', height: '60px', backgroundColor: 'rgb(100,100,100)', borderRadius: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                        <TbChessRookFilled style={{ color: 'black' }} size={'3.5vh'} />
                    </div>
                </div>
            </div>
            <div className={styles["backdrop"]} onClick={props.onClose} />
        </>
    );
}

export default Modal;