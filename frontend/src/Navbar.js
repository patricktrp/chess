import { useState } from 'react';
import { FaChess, FaGithubSquare, FaLinkedin } from 'react-icons/fa';
import Modal from './Modal';
import styles from './Navbar.module.css';

const Navbar = (props) => {
    const [showModal, setShowModal] = useState(false);

    const closeModalHandler = () => {
        setShowModal(false);
    }

    const showModalHandler = () => {
        setShowModal(true);
    }

    return (
        <nav className={styles.navbar}>
            <div className={styles.navbaritems}>
                <a href="http://localhost:3000"><FaChess style={{ color: 'white' }} size="4vh" /></a>
                {!props.isPlaying && <button onClick={showModalHandler} className={styles.button}>Play</button>}
                <div>
                    <a href="https://www.github.com/patricktrp" target="_blank" rel="noreferrer"><FaGithubSquare style={{ color: 'white' }} size="4vh" /></a>
                    <a href="https://www.linkedin.com/in/patrick-treppmann/" target="_blank" rel="noreferrer"><FaLinkedin style={{ color: 'white' }} size="4vh" /></a>
                </div>
            </div>
            {showModal && <Modal onClose={closeModalHandler} onCreateGame={props.onCreateGame} />}
        </nav>
    );
}

export default Navbar;