import { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';

import { RootState } from '../redux/store';
import {
  setRoomId,
  setRoomName,
  setHostId,
  setParticipants,
} from '../redux/slices/roomSlice';

import Modal from '../components/common/Modal';
import FriendInvite from '../components/waitingRoom/FriendInvite';
import MapInfo from '../components/waitingRoom/MapInfo';
import ReadyButton from '../components/waitingRoom/ReadyButton';
import RoomChat from '../components/waitingRoom/RoomChat';
import RoomHowTo from '../components/waitingRoom/RoomHowTo';
import RoomTitle from '../components/waitingRoom/RoomTitle';
import RoomUserList from '../components/waitingRoom/RoomUserList';

interface Message {
  type: string;
  content: {
    type: string;
    message: string;
    playerId: string;
  };
}

function WaitingRoom() {
  const API_LINK = import.meta.env.VITE_API_URL;
  const dispatch = useDispatch();

  const { roomId: roomIdFromParams } = useParams();
  const roomId = useSelector((state: RootState) => state.room.roomId);
  const playerId = useSelector((state: RootState) => state.user.id);
  const roomName = useSelector((state: RootState) => state.room.roomName);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [messages, setMessages] = useState<Message[]>([]);
  const [socket, setSocket] = useState<WebSocket | null>(null);

  useEffect(() => {
    console.log(roomIdFromParams);
    if (roomIdFromParams) {
      dispatch(setRoomId(roomIdFromParams));
    }
  }, [roomIdFromParams, dispatch]);

  useEffect(() => {
    // roomId가 없으면 실행 X
    if (!roomId) return;

    // 웹소켓 생성
    const newSocket = new WebSocket(
      `ws://localhost:8082/socket/ws/game/${roomId}?playerId=${playerId}`,
    );

    // 웹소켓 연결
    newSocket.onopen = () => {
      console.log(
        '웹소켓 연결 성공 - roomId: ' + roomId + ', playerId: ' + playerId,
      );
      fetchRoomData();
    };

    // 웹소켓 연결 오류 시 재연결 시도
    newSocket.onerror = (error) => {
      newSocket.close();
      console.error('웹소켓 연결 오류: ', error);
      setTimeout(() => {
        setSocket(
          new WebSocket(
            `ws://localhost:8082/socket/ws/game/${roomId}?playerId=${playerId}`,
          ),
        );
      }, 2500);
    };

    setSocket(newSocket);

    // 소켓에서 메시지를 수신할 때
    newSocket.onmessage = (event) => {
      // 메시지가 JSON 형식인지 확인
      if (event.data.startsWith('{') && event.data.endsWith('}')) {
        try {
          const receivedMessage = JSON.parse(event.data);

          if (setMessages && receivedMessage.type === 'CHAT') {
            setMessages((prevMessages) => [...prevMessages, receivedMessage]);
          } else if (receivedMessage.type === 'NOTIFY') {
            console.log(event.data);
            setTimeout(() => {
              fetchRoomData();
            }, 1000);
          }
        } catch (error) {
          console.error('JSON 파싱 오류:', error);
        }
      }
    };

    // 게임방 정보 가져오기 (방 입장 API)
    const fetchRoomData = async () => {
      try {
        console.log('시작~~~~~~~~~~~~~~~~~~');
        const response = await axios.post(`${API_LINK}/room/join/${roomId}`, {
          playerId: playerId,
        });
        if (response.data && response.data.data) {
          // participants 배열에서 0을 필터링
          const filteredParticipants = response.data.data.participants.filter(
            (id: string) => id !== '0',
          );

          dispatch(setParticipants(filteredParticipants));
          dispatch(setRoomName(response.data.data.roomName));
          dispatch(setHostId(response.data.data.hostId));
          console.log(response);
          console.log('참가자: ', filteredParticipants);
        } else {
          console.error('응답 데이터에 participants가 없습니다.');
        }
      } catch (error) {
        console.error('방 정보 가져오는 중 에러 발생: ', error);
      }
    };

    // 방 퇴장 시 게임방 정보 새로 가져오기

    // 3초 뒤에 로딩 끝내기
    const delay = setTimeout(() => {
      setIsLoading(false);
    }, 3000);

    // 컴포넌트 언마운트 시 타이머 및 웹소켓 연결 정리
    return () => {
      clearTimeout(delay);
      if (socket) {
        socket.onclose = () => {
          console.log('웹소켓 연결 닫기');
        };
      }
    };
  }, [roomId, playerId]);

  const handleOpenModal = () => setIsModalOpen(true); // 모달 열기
  const handleCloseModal = () => setIsModalOpen(false); // 모달 닫기

  return (
    <div className="flex relative">
      {isLoading && (
        <Modal>
          <div className="text-center">
            <p className="white-title">방 들어가는 중...</p>
          </div>
        </Modal>
      )}

      {/* 왼쪽 섹션 (방제목/게임 참여자 리스트/채팅) */}
      <section className="w-2/3">
        <RoomTitle roomName={roomName} roomId={roomId} />
        {/* 게임 참여 대기자 리스트 */}
        <RoomUserList onOpen={handleOpenModal} socket={socket} />
        <RoomChat messages={messages} socket={socket} />
      </section>

      {/* 오른쪽 섹션 (맵/게임방법/준비버튼) */}
      <section className="w-1/3">
        <MapInfo />
        <RoomHowTo />
        <ReadyButton socket={socket} />
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
