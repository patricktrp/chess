import styles from './Modal.module.css'
import { AiOutlineCopy } from 'react-icons/ai';
import { TbExternalLink } from 'react-icons/tb';

const InvitationModal = (props) => {
    const url = `${window.location.origin}/play/${props.game}`;

    const copyToClipboard = () => {
        navigator.clipboard.writeText(url);
    }

    return (
        <>
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                <div className={styles["modal"]}>
                    {url}
                    <AiOutlineCopy onClick={copyToClipboard} size={'3vh'} style={{ color: 'white', cursor: 'pointer' }} />
                    <a href={url} target='_blank' rel="noreferrer">
                        <TbExternalLink size={'3vh'} style={{ color: 'white', cursor: 'pointer' }} />
                    </a>

                </div>
            </div>
            <div className={styles["backdrop"]} />
        </>
    )
}

export default InvitationModal;