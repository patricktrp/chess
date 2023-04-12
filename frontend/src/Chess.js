import { useEffect, useState } from "react";
import { Chessboard } from "react-chessboard";
import styles from './Chess.module.css';

const Chess = (props) => {
    const [height, setHeight] = useState(window.innerHeight);
    const [width, setWidth] = useState(window.innerWidth);

    const boardWidth = width > 768 ? height * 0.8 : width * 0.5;

    useEffect(() => {
        function handleResize() {
          setHeight(window.innerHeight);
          setWidth(window.innerWidth);
        }
        window.addEventListener('resize', handleResize);
        return () => {
          window.removeEventListener('resize', handleResize);
        };
      }, []); 
    
    return (
        <section className={styles.chess}>
            <Chessboard 
                boardWidth={boardWidth}
                onPieceDrop={props.onPieceDrop} 
                position={props.position} 
                boardOrientation={props.color} />
        </section>
    );
}

export default Chess;