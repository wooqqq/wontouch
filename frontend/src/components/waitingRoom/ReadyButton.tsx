import { useSelector } from 'react-redux';
import { RootState } from '../../redux/store';
import { useState } from 'react';

interface readyState {
  type: string;
  content: {
    playerId: string;
    ready: boolean;
    allReady: boolean;
  };
}

function ReadyButton(socket: WebSocket | null) {
  const userId = useSelector((state: RootState) => state.user.id);
  const [ready, setReady] = useState('');

  const handleChangeReady = () => {
    if (!socket) return;

    const readyRequest = {
      type: 'READY',
    };

    socket.send(JSON.stringify(readyRequest));
  };

  return (
    <button onClick={handleChangeReady} className="ready-button">
      {!ready ? <p>준 비!</p> : <p>준비 완료</p>}
    </button>
  );
}

export default ReadyButton;
