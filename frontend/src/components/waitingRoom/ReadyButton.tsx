import { useSelector } from 'react-redux';
import { RootState } from '../../redux/store';
import { useEffect, useState } from 'react';
import axios from 'axios';

interface readyState {
  type: string;
  content: {
    playerId: string;
    ready: boolean;
    allReady: boolean;
  };
}

interface roomInfoProps {
  socket: WebSocket | null;
}

function ReadyButton({ socket }: roomInfoProps) {
  const API_LINK = import.meta.env.VITE_API_URL;
  const userId = useSelector((state: RootState) => state.user.id);
  const roomId = useSelector((state: RootState) => state.room.roomId);
  const hostId = useSelector((state: RootState) => state.room.HostId);
  const players = useSelector((state: RootState) => state.room.participants);
  const [isReady, setIsReady] = useState(false);
  const [isAllReady, setIsAllReady] = useState(false);

  useEffect(() => {
    if (!socket) return;

    if (hostId === userId) {
      // 방장은 알아서 ready가 true가 되게
      const hostReady = () => {
        const readyRequest = {
          type: 'READY',
        };
        socket.send(JSON.stringify(readyRequest));
        setIsReady(!isReady);
      };
      if (!isReady) {
        hostReady();
      }
    }

    // 서버로부터 준비 상태 변경 이벤트 수신
    socket.onmessage = (event) => {
      if (event.data.startsWith('{') && event.data.endsWith('}')) {
        try {
          const data = JSON.parse(event.data);
          if (data.type === 'READY') {
            // 응답에서 플레이어 준비 상태 업데이트
            if (data.content.playerId === userId) {
              setIsReady(data.content.ready);
              setIsAllReady(data.content.allReady);
              console.log('준비: ', data.content.ready);
              console.log('모두 준비: ', data.content.allReady);
            }
          }
        } catch (error) {
          console.error('JSON 파싱 오류:', error);
        }
        // 전체 준비 상태에 따라 필요한 추가 로직 작성 가능
      }
    };

    // 컴포넌트 언마운트 시 이벤트 리스너 제거
    return () => {
      socket.onmessage = null;
    };
  }, [socket, userId]);

  // 준비 버튼 클릭 시
  const handleChangeReady = () => {
    if (!socket) return;

    const readyRequest = {
      type: 'READY',
    };

    socket.send(JSON.stringify(readyRequest));
  };

  // 게임 시작 버튼 클릭 시
  const handleStartGame = async () => {
    if (!roomId || !players) return;

    try {
      const response = await axios.post(
        `${API_LINK}/room/start/${roomId}`,
        players,
      );
      console.log('게임 시작 요청 성공: ', response.data);
    } catch (error) {
      console.error('게임 시작 요청 중 오류 발생: ', error);
    }
  };

  return (
    <>
      {hostId === userId ? (
        !isAllReady ? (
          <button className="unready-button">
            <p>게임 시작</p>
          </button>
        ) : (
          <button onClick={handleStartGame} className="ready-button">
            <p>게임 시작</p>
          </button>
        )
      ) : !isReady ? (
        <button onClick={handleChangeReady} className="ready-button">
          <p>준 비!</p>
        </button>
      ) : (
        <button onClick={handleChangeReady} className="unready-button">
          <p>준비 완료</p>
        </button>
      )}
    </>
  );
}

export default ReadyButton;
