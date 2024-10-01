import { useEffect, useState } from 'react';
import Modal from '../components/common/Modal';
import FriendInvite from '../components/waitingRoom/FriendInvite';
import MapInfo from '../components/waitingRoom/MapInfo';
import ReadyButton from '../components/waitingRoom/ReadyButton';
import RoomChat from '../components/waitingRoom/RoomChat';
import RoomHowTo from '../components/waitingRoom/RoomHowTo';
import RoomTitle from '../components/waitingRoom/RoomTitle';
import RoomUserList from '../components/waitingRoom/RoomUserList';
import { useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../redux/store';
import axios from 'axios';
import { setToken } from '../redux/slices/authSlice';
import { jwtDecode } from 'jwt-decode';
import { setUserId } from '../redux/slices/userSlice';

interface DecodedToken {
  userId: number;
}

function WaitingRoom() {
  const API_LINK = import.meta.env.VITE_API_URL;
  const dispatch = useDispatch();

  const { roomId } = useParams();
  const playerId = useSelector((state: RootState) => state.user.id);
  const [roomName, setRoomName] = useState<string>('');
  const [roomHost, setRoomHost] = useState<number>(0);
  const [participants, setParticipants] = useState<string[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [socket, setSocket] = useState<WebSocket | null>(null);
  // 클라이언트 생성
  const token = localStorage.getItem('access_token');

  useEffect(() => {
    // 로컬스토리지에서 토큰 읽어오기
    if (token) {
      dispatch(setToken(token));
      const decodedToken = jwtDecode<DecodedToken>(token);
      dispatch(setUserId(decodedToken.userId));
    }

    // roomId가 없으면 실행 X
    if (!roomId) return;

    const fetchRoomData = async () => {
      try {
        console.log(roomId);
        const response = await axios.post(`${API_LINK}/room/join/${roomId}`, {
          playerId: playerId,
        });
        if (response.data && response.data.data) {
          // participants 배열에서 0을 필터링
          const filteredParticipants = response.data.data.participants.filter(
            (id: string) => id !== '0',
          );

          setParticipants(filteredParticipants);
          setRoomName(response.data.data.roomName);
          setRoomHost(response.data.data.HostId);
        } else {
          console.error('응답 데이터에 participants가 없습니다.');
        }
      } catch (error) {
        console.error('방 정보 가져오는 중 에러 발생: ', error);
      }
    };

    // 웹소켓 연결
    const socket = new WebSocket(
      `ws://localhost:8082/socket/ws/game/${roomId}?playerId=${playerId}`,
    );

    socket.onopen = () => {
      console.log(
        '웹소켓 연결 성공 - roomId: ' + roomId + ', playerId: ' + playerId,
      );
    };

    setSocket(socket);
    fetchRoomData();

    return () => {
      socket.close();
    };
  }, [roomId, playerId, dispatch]);

  const handleOpenModal = () => setIsModalOpen(true); // 모달 열기
  const handleCloseModal = () => setIsModalOpen(false); // 모달 닫기

  return (
    <div className="flex relative">
      {/* 왼쪽 섹션 (방제목/게임 참여자 리스트/채팅) */}
      <section className="w-2/3">
        <RoomTitle roomName={roomName} roomId={roomId} />
        {/* 게임 참여 대기자 리스트 */}
        <RoomUserList
          participants={participants}
          roomHost={roomHost}
          onOpen={handleOpenModal}
        />
        <RoomChat roomId={roomId} participants={participants} socket={socket} />
      </section>

      {/* 오른쪽 섹션 (맵/게임방법/준비버튼) */}
      <section className="w-1/3">
        <MapInfo />
        <RoomHowTo />
        <ReadyButton />
      </section>

      {isModalOpen && (
        <Modal>
          <FriendInvite onClose={handleCloseModal} />
        </Modal>
      )}
    </div>
  );
}

export default WaitingRoom;
