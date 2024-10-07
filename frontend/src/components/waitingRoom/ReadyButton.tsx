import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../../redux/store';
import { useRef, useState } from 'react';
import axios from 'axios';
import Modal from '../common/Modal';
import { useNavigate } from 'react-router-dom';
import { setGameParticipants } from '../../redux/slices/roomSlice';

interface GameParticipant {
  userId: number;
  isReady: boolean;
  nickname: string;
  description: string;
  characterName: string;
  tierPoint: number;
  mileage: number;
}

interface roomInfoProps {
  socket: WebSocket | null;
  isAllReady: boolean;
}

function ReadyButton({ socket, isAllReady }: roomInfoProps) {
  const API_LINK = import.meta.env.VITE_API_URL;
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const userId = useSelector((state: RootState) => state.user.id);
  const roomId = useSelector((state: RootState) => state.room.roomId);
  const hostId = useSelector((state: RootState) => state.room.hostId);
  const gameParticipants = useSelector(
    (state: RootState) => state.room.gameParticipants,
  );
  const [isReady, setIsReady] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const readyStateRef = useRef(isReady); // useRef로 상태 참조
  readyStateRef.current = isReady; // 항상 최신 상태로 업데이트

  // 준비 버튼 클릭 시
  const handleChangeReady = () => {
    if (!socket) return;

    const readyRequest = {
      type: 'READY',
      content: { playerId: userId, ready: !isReady },
    };

    socket.send(JSON.stringify(readyRequest));
    setIsReady(!isReady);

    console.log('준비완료~!!');
    const readyParticipants = gameParticipants.map((participant) => {
      if (participant.userId === userId) {
        return { ...participant, isReady: !participant.isReady };
      }
      return participant; // 다른 참가자는 그대로 반환
    });
    // 새로 받아온 유저 정보가 기존 정보와 다른 경우에만 dispatch 실행
    if (
      JSON.stringify(readyParticipants) !== JSON.stringify(gameParticipants)
    ) {
      dispatch(setGameParticipants(readyParticipants));
    }
  };

  // 게임 시작 버튼 클릭 시
  const handleStartGame = async () => {
    if (!roomId || !gameParticipants || gameParticipants.length === 1) return;

    if (!isAllReady) {
      setIsModalOpen(true);
      return;
    }

    // participants에서 필요한 정보만 추출
    const gameStartParticipants = gameParticipants.map(
      (gameParticipant: GameParticipant) => ({
        id: Number(gameParticipant.userId),
        nickname: gameParticipant.nickname,
      }),
    );

    try {
      const response = await axios.post(
        `${API_LINK}/room/start/${roomId}`,
        gameStartParticipants,
      );
      console.log('게임시작! : ', gameStartParticipants);
      console.log('게임 시작 요청 성공: ', response.data);
      navigate(`/game/${roomId}`);
    } catch (error) {
      console.error('게임 시작 요청 중 오류 발생: ', error);
    }
  };

  const closeModal = () => {
    setIsModalOpen(false); // 모달 닫기
  };

  return (
    <>
      {hostId === userId ? (
        <button onClick={handleStartGame} className="ready-button">
          <p>게임 시작</p>
        </button>
      ) : isReady === false ? (
        <button onClick={handleChangeReady} className="ready-button">
          <p>준 비!</p>
        </button>
      ) : (
        <button onClick={handleChangeReady} className="unready-button">
          <p>준비 완료</p>
        </button>
      )}

      {/* 모달이 열릴 때만 렌더링 */}
      {isModalOpen && (
        <Modal>
          <div className="yellow-box p-10">
            <p className="mb-7">모두 준비 완료 상태에서만 시작 가능합니다.</p>
            <button onClick={closeModal}>OK</button>
          </div>
        </Modal>
      )}
    </>
  );
}

export default ReadyButton;
